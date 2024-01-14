package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.DTO.client.request.CommentReqDTO;
import com.bestbenefits.takoyaki.config.annotation.DontCareAuthentication;
import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/parties/{party-id}")
public class CommentController {
    private final CommentService commentService;

    @DontCareAuthentication
    @GetMapping("/comment") //로그인 필요 X
    public ApiResponse<?> getComment(@PathVariable(name = "party-id") Long partyId) {
        Map<String, Integer> meta = new HashMap<>();
        Map<String, List<?>> data = new HashMap<>();

        List<?> commentList = commentService.getComments(partyId);

        meta.put("count", commentList.size());
        data.put("comment_list", commentList);
        return ApiResponseCreator.success(meta, data);
    }

    @NeedAuthentication
    @PostMapping("/comment")
    public ApiResponse<?> addComment(@Session(attribute = SessionConst.ID) Long id,
                                     @PathVariable(name = "party-id") Long partyId,
                                     @RequestBody @Valid CommentReqDTO dto) {
        return ApiResponseCreator.success(commentService.createComment(id, partyId, dto), HttpStatus.CREATED);
    }
}
