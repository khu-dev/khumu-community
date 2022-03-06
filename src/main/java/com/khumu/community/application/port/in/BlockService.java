package com.khumu.community.application.port.in;

import com.khumu.community.application.dto.BlockUserDto;
import com.khumu.community.application.dto.SimpleUserDto;
import com.khumu.community.application.dto.input.CreateBlockUserInput;
import com.khumu.community.application.entity.Article;
import com.khumu.community.application.entity.BlockUser;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.exception.ForbiddenException;
import com.khumu.community.application.port.out.repository.ArticleRepository;
import com.khumu.community.application.port.out.repository.BlockUserRepository;
import com.khumu.community.application.port.out.repository.UserRepository;
import com.khumu.community.common.mapper.BlockUserMapper;
import com.khumu.community.common.mapper.UserMapper;
import com.khumu.community.common.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class BlockService {
    private final BlockUserRepository blockUserRepository;
    private final UserMapper userMapper;
    private final BlockUserMapper blockUserMapper;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public BlockUserDto blockUser(User requestUser, CreateBlockUserInput input) {
        Article article = articleRepository.findById(input.getArticle()).get();
        if (requestUser.getUsername().equals(article.getAuthor().getUsername())) {
            throw new ForbiddenException("본인의 게시글을 바탕으로 작성자(본인)를 차단할 수 없습니다.");
        }

        BlockUser block = blockUserRepository.save(BlockUser.builder()
                .blocker(requestUser.getUsername())
                .blockee(article.getAuthor().getUsername())
                .isBlockeeAnonymous(article.getKind().equals("anonymous"))
                .reason("게시글 \"" + Util.getShortString(article.getTitle(), 16) + "\" 의 작성자를 차단했습니다.")
                .build());


        return map(block);
    }

    public Page<BlockUserDto> listMyBlocks(User requestUser, Pageable pageable) {
        Page<BlockUser> blocks = blockUserRepository.findAllByBlocker(requestUser.getUsername(), pageable);
        return blocks.map(this::map);
    }

    public void unblockUser(User requestUser, Long blockId) {
        BlockUser block = blockUserRepository.findById(blockId).get();
        // 본인이 신고자가 아닌 경우
        if (!requestUser.getUsername().equals(block.getBlocker())) {
            throw new ForbiddenException("본인의 차단 내역만 해제할 수 있습니다.");
        }
        
        blockUserRepository.delete(block);
    }

    private BlockUserDto map(BlockUser block) {
        BlockUserDto dto = blockUserMapper.toDto(block);
        if (dto.getIsBlockeeAnonymous()) {
            dto.setBlockee(SimpleUserDto.builder().username("anonymous").nickname("익명").status("anonymous").build());
        } else{
            User user = userRepository.findById(block.getBlockee()).get();
            userMapper.toSimpleDto(user);
        }

        return dto;
    }


}
