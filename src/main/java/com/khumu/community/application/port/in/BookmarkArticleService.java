package com.khumu.community.application.port.in;

import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.BookmarkArticle;
import com.khumu.community.application.entity.LikeArticle;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.out.repository.BookmarkArticleRepository;
import com.khumu.community.application.port.out.repository.LikeArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class BookmarkArticleService {
    private final BookmarkArticleRepository bookmarkArticleRepository;

    @Transactional
    // 좋아요 했으면 true
    // 좋아요를 취소했으면 false
    public Boolean toggle(User requestUser, Integer articleId) {
        List<BookmarkArticle> bookmarks = bookmarkArticleRepository.findAllByUserAndArticle(requestUser.getUsername(), Article.builder().id(articleId).build());

        if (bookmarks.isEmpty()) {
            bookmarkArticleRepository.save(BookmarkArticle.builder()
                    .user(requestUser.getUsername())
                    .article(Article.builder().id(articleId).build())
                    .build());

            return true;
        }

        bookmarkArticleRepository.deleteAll(bookmarks);

        return false;
    }
}
