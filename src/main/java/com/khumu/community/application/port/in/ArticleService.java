package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.DetailedArticleDto;
import com.khumu.community.application.dto.input.CreateArticleInput;
import com.khumu.community.application.dto.input.IsAuthorInput;
import com.khumu.community.application.dto.input.UpdateArticleInput;
import com.khumu.community.application.entity.*;
import com.khumu.community.application.exception.ForbiddenException;
import com.khumu.community.application.port.out.messaging.MessagePublisher;
import com.khumu.community.application.port.out.repository.*;
import com.khumu.community.common.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final ArticleAdditionalDataInjector articleAdditionalDataInjector;
    private final ArticleRepository articleRepository;
    private final FollowBoardRepository followBoardRepository;
    private final BlockUserRepository blockUserRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final BookmarkArticleRepository bookmarkArticleRepository;
    private final CommentRepository commentRepository;

    private final MessagePublisher messagePublisher;

    private final List<Status> viewableArticleStatuses = List.of(Status.EXISTS);

    @Transactional
    public ArticleDto write(User requestUser, CreateArticleInput input) {
        Article tmp = Article.builder()
                .author(requestUser)
                .board(Board.builder().name(input.getBoard()).build())
                .title(input.getTitle())
                .content(input.getContent())
                .isHot(false)
                .images(input.getImages())
                .kind(input.getKind())
                .build();

        Article article = articleRepository.save(tmp);

        messagePublisher.publish("article", "create", articleMapper.toEventDto(article));

        return articleAdditionalDataInjector.inject(articleMapper.toDto(article), requestUser);
    }

    // 피드에 표시될 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesForFeed(User requestUser, Pageable pageable) {
        List<Board> followingBoards = followBoardRepository.findAllByUser(requestUser.getUsername(), Pageable.unpaged()).map(FollowBoard::getBoard).toList();
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByBoardInAndAuthor_UsernameNotInAndStatusIn(
                followingBoards,
                blockedUsernames,
                viewableArticleStatuses,
                pageable
        );

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 내가 작성한 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesIWrote(User requestUser, Pageable pageable) {
        Page<Article> articles = articleRepository.findAllByAuthorAndStatusIn(requestUser, viewableArticleStatuses, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 내가 좋아요한 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesILiked(User requestUser, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<LikeArticle> likes = likeArticleRepository.findAllByUserAndArticle_Author_UsernameNotIn(requestUser.getUsername(), blockedUsernames, pageable);
        Page<Article> articles = likes.map(LikeArticle::getArticle);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 내가 북마크한 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesIBookmarked(User requestUser, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<BookmarkArticle> bookmarks = bookmarkArticleRepository.findAllByUserAndArticle_Author_UsernameNotIn(requestUser.getUsername(), blockedUsernames, pageable);
        Page<Article> articles = bookmarks.map(BookmarkArticle::getArticle);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }
    
    // 내가 댓글 단 게시글들 조회
    // Page 정보를 올바르게 전달하기는 힘들어서 List로 전달
    @Transactional
    public List<ArticleDto> listArticlesICommented(User requestUser, String authorizationString, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        List<Integer> articleIds = commentRepository.findAllArticlesUserCommented(authorizationString, pageable);
        List<Article> articles = new ArrayList<>(articleRepository.findAllByIdInAndAuthor_UsernameNotInAndStatusIn(articleIds, blockedUsernames, viewableArticleStatuses, Pageable.unpaged()).getContent());
        // comment 서비스에서 조회해 온 인덱스 대로 정렬한다.
        // 인덱스에 따라 오름차순 정렬
        articles.sort((Article a, Article b) -> {
            if (articleIds.indexOf(a.getId()) < articleIds.indexOf(b.getId())) return -1;

            return 1;
        });

        return articleAdditionalDataInjector.inject(articles.stream().map(articleMapper::toDto).collect(Collectors.toList()), requestUser);
    }

    // 내가 댓글 단 게시글들 조회
    // Page 정보를 올바르게 전달하기는 힘들어서 List로 전달
    @Transactional
    public Page<ArticleDto> listHotArticles(User requestUser, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByIsHotAndAuthor_UsernameNotInAndStatusIn(true, blockedUsernames, viewableArticleStatuses, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 특정 게시판의 게시글들 조회
    @Transactional
    public Page<ArticleDto> listArticlesByBoard(User requestUser, String board, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), Pageable.unpaged()).getContent();
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByBoardInAndAuthor_UsernameNotInAndStatusIn(List.of(Board.builder().name(board).build()), blockedUsernames, viewableArticleStatuses, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 특정 게시글의 상세 조회
    @Transactional
    public DetailedArticleDto getArticle(User requestUser, Integer id) {
        Article article = articleRepository.findById(id).get();

        return articleAdditionalDataInjector.inject(articleMapper.toDetailedDto(article), requestUser);
    }
    
    @Transactional
    // TODO: 기존에는 Patch(부분 수정) 방식이었는데 이거 어떻게 대응해줄 것인가? => front에서도 Patch하는 걸로하자.
    // 일단은 null일 수 없는 값들이기때문에 null이면 수정 안하기로.
    public ArticleDto update(User requestUser, Integer id, UpdateArticleInput input) {

        Article article = articleRepository.findById(id).get();
        if (input.getBoard() != null) {
            article.setBoard(Board.builder().name(input.getBoard()).build());
        }

        if (input.getTitle() != null) {
            article.setTitle(input.getTitle());
        }
        if (input.getContent() != null) {
            article.setContent(input.getContent());
        }
        if (input.getImages() != null) {
            article.setImages(input.getImages());
        }
        if (input.getKind() != null) {
            article.setKind(input.getKind());
        }

        return articleAdditionalDataInjector.inject(articleMapper.toDto(article), requestUser);
    }

    @Transactional
    public void delete(User requestUser, Integer id) {
        Article article = articleRepository.findById(id).get();
        try {
            if (!article.getAuthor().getUsername().equals(requestUser.getUsername())) {
                throw new ForbiddenException("해당 게시글의 작성자가 아닙니다.");
            }
        } catch (NullPointerException e) {
            throw new ForbiddenException("해당 게시글의 작성자가 아닙니다.");
        }
        articleRepository.delete(article);
    }

    // TODO: 지금은 comment 서비스가 author을 직접 찍어서 확인을 시도하지만
    // 이렇게 되면 보안상 취약하다.
    // comment 측에서 requestUser를 올바르게 넣어주는 게 베스트!
    public boolean isAuthor(User requestUser, Integer articleId, IsAuthorInput input) {
        Article article = articleRepository.findById(articleId).orElse(null);
        if (article == null) {
            log.error("존재하지 않는 게시글 id입니다. id=" + article);
            return false;
        }
        if (!input.getAuthor().equals(article.getAuthor().getUsername())) {
            return false;
        }

        return true;
    }

}
