package com.bestbenefits.takoyaki.controller;

import com.bestbenefits.takoyaki.config.annotation.NeedAuthentication;
import com.bestbenefits.takoyaki.config.annotation.Session;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.SessionConst;
import com.bestbenefits.takoyaki.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parties/{party-id}")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @NeedAuthentication
    @PostMapping("/bookmark")
    public ApiResponse<?> addBookmark(@Session(attribute = SessionConst.ID) Long id,
                                         @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.addBookmark(id, partyId);
        return ApiResponseCreator.success(HttpStatus.CREATED);
    }

    @NeedAuthentication
    @DeleteMapping("/bookmark")
    public ApiResponse<?> deleteBookmark(@Session(attribute = SessionConst.ID) Long id,
                                            @PathVariable(name = "party-id") Long partyId) {
        bookmarkService.deleteBookmark(id, partyId);
        return ApiResponseCreator.success();
    }
}