package com.khumu.community.infra.controller;

import com.khumu.community.application.dto.BlockUserDto;
import com.khumu.community.application.dto.ReportDto;
import com.khumu.community.application.dto.input.CreateBlockUserInput;
import com.khumu.community.application.entity.User;
import com.khumu.community.application.port.in.BlockService;
import com.khumu.community.application.port.in.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BlockController {
    final private BlockService blockService;

    @PostMapping(value = "/api/community/v1/blocks")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<BlockUserDto> blockUser(@AuthenticationPrincipal User user, @RequestBody CreateBlockUserInput body) {
        return DefaultResponse.<BlockUserDto>builder()
                .data(blockService.blockUser(user, body))
                .build();
    }

    @GetMapping(value = "/api/community/v1/blocks")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.CREATED)
    public DefaultResponse<List<BlockUserDto>> listMyBlocks(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort="createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return DefaultResponse.<List<BlockUserDto>>builder()
                .data(blockService.listMyBlocks(user, pageable).getContent())
                .build();
    }

    @DeleteMapping(value = "/api/community/v1/blocks/{id}")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public DefaultResponse<Void> unblockUser(@AuthenticationPrincipal User user, @PathVariable Long id) {
        blockService.unblockUser(user, id);
        return DefaultResponse.<Void>builder()
                .data(null)
                .build();
    }
}
