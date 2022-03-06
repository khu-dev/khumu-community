package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.ArticleDto;
import com.khumu.community.application.dto.input.CreateArticleRequest;
import com.khumu.community.application.dto.input.IsAuthorInput;
import com.khumu.community.application.dto.input.UpdateArticleRequest;
import com.khumu.community.application.dto.output.IsAuthorOutput;
import com.khumu.community.application.entity.*;
import com.khumu.community.application.exception.ForbiddenException;
import com.khumu.community.application.port.out.messaging.MessagePublisher;
import com.khumu.community.application.port.out.repository.*;
import com.khumu.community.common.mapper.ArticleMapper;
import com.khumu.community.infra.messaging.SnsPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Transactional
    public ArticleDto write(User requestUser, CreateArticleRequest input) {
        Article tmp = Article.builder()
                .author(requestUser)
                .board(Board.builder().name(input.getBoard()).build())
                .title(input.getTitle())
                .content(input.getContent())
                .isHot(false)
                .newImages(input.getImages())
                .kind(input.getKind())
                .build();

        Article article = articleRepository.save(tmp);

        // TODO: 게시글 생성 이벤트를 SNS에 발행하기!
        messagePublisher.publish("article", "create", articleMapper.toEventDto(article));

        return articleAdditionalDataInjector.inject(articleMapper.toDto(article), requestUser);
    }

    // 피드에 표시될 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesForFeed(User requestUser, Pageable pageable) {
        List<Board> followingBoards = followBoardRepository.findAllByUser(requestUser.getUsername(), Pageable.unpaged()).map(FollowBoard::getBoard).toList();
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByBoardInAndAuthor_UsernameNotIn(
                followingBoards,
                blockedUsernames,
                pageable
        );

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 내가 작성한 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesIWrote(User requestUser, Pageable pageable) {
        Page<Article> articles = articleRepository.findAllByAuthor(requestUser, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 내가 좋아요한 게시글 조회
    @Transactional
    public Page<ArticleDto> listArticlesILiked(User requestUser, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
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
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
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
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        List<Integer> articleIds = commentRepository.findAllArticlesUserCommented(authorizationString, pageable);
        List<Article> articles = new ArrayList<>(articleRepository.findAllByIdInAndAuthor_UsernameNotIn(articleIds, blockedUsernames, Pageable.unpaged()).getContent());
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
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByIsHotAndAuthor_UsernameNotIn(true, blockedUsernames, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }

    // 특정 게시판의 게시글들 조회
    @Transactional
    public Page<ArticleDto> listArticlesByBoard(User requestUser, String board, Pageable pageable) {
        List<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername());
        List<String> blockedUsernames = blocks.stream().map(BlockUser::getBlockee).collect(Collectors.toList());

        // list가 empty이면 JPQL에서 in query가 제대로 동작되지 못함.
        // 따라서 non-null인 dummy를 한 칸 넣어준다.
        if (blockedUsernames.isEmpty()) blockedUsernames.add("");

        Page<Article> articles = articleRepository.findAllByBoardInAndAuthor_UsernameNotIn(List.of(Board.builder().name(board).build()), blockedUsernames, pageable);

        return articleAdditionalDataInjector.inject(articles.map(articleMapper::toDto), requestUser);
    }
    
    @Transactional
    // TODO: 기존에는 Patch(부분 수정) 방식이었는데 이거 어떻게 대응해줄 것인가?
    // 일단은 null일 수 없는 값들이기때문에 null이면 수정 안하기로.
    public ArticleDto update(User requestUser, Integer id, UpdateArticleRequest input) {

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
            article.setNewImages(input.getImages());
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

    // TODO: 지금은 comment 서비스가 author을 직접 찍어서 보내지만
    // 이렇게 되면 보안상 취약하다.
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
