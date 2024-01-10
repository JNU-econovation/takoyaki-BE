package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ResponseEntityCreator;
import com.bestbenefits.takoyaki.config.properties.BookmarkConst;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.bestbenefits.takoyaki.exception.bookmark.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookmarkExceptionHandler {
    @ExceptionHandler(AlreadyBookmarkedException.class)
    public ResponseEntity<?> handleAlreadyBookmarkedException() {
        return ResponseEntityCreator.fail(
                ExceptionCode.ALREADY_BOOKMARKED,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SelfBookmarkNotAllowedException.class)
    public ResponseEntity<?> handleSelfBookmarkNotAllowedException() {
        return ResponseEntityCreator.fail(
                ExceptionCode.SELF_BOOKMARK_NOT_ALLOWED,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookmarkCountExceedException.class)
    public ResponseEntity<?> handleBookmarkCountExceedException() {
        return ResponseEntityCreator.fail(
                ExceptionCode.BOOKMARK_COUNT_EXCEED,
                String.valueOf(BookmarkConst.MAX_BOOKMARK_NUMBER_PER_USER),
                true,
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotBookmarkedException.class)
    public ResponseEntity<?> handleNotBookmarkedException() {
        return ResponseEntityCreator.fail(
                ExceptionCode.NOT_BOOKMARKED,
                HttpStatus.NOT_FOUND);
    }

}
