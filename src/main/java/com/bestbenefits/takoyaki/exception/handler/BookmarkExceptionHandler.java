package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.config.properties.BookmarkConst;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.bookmark.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookmarkExceptionHandler {
    @ExceptionHandler(AlreadyBookmarkedException.class)
    public ApiResponse<?> handleAlreadyBookmarkedException() {
        return ApiResponseCreator.fail(
                ExceptionCode.ALREADY_BOOKMARKED,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SelfBookmarkNotAllowedException.class)
    public ApiResponse<?> handleSelfBookmarkNotAllowedException() {
        return ApiResponseCreator.fail(
                ExceptionCode.SELF_BOOKMARK_NOT_ALLOWED,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookmarkCountExceedException.class)
    public ApiResponse<?> handleBookmarkCountExceedException() {
        return ApiResponseCreator.fail(
                ExceptionCode.BOOKMARK_COUNT_EXCEED,
                String.valueOf(BookmarkConst.MAX_BOOKMARK_NUMBER_PER_USER),
                true,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotBookmarkedException.class)
    public ApiResponse<?> handleNotBookmarkedException() {
        return ApiResponseCreator.fail(
                ExceptionCode.NOT_BOOKMARKED,
                HttpStatus.NOT_FOUND);
    }

}
