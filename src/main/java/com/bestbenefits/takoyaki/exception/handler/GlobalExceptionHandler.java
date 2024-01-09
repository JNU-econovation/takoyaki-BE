package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.DEPRECATED__ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.DEPRECATED__ApiResponseCreator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //body가 없는 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public DEPRECATED__ApiResponse<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        return DEPRECATED__ApiResponseCreator.fail("plz check request's body.");
    }

    //@Vaild 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DEPRECATED__ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        String message;
        if (e.getBindingResult().getFieldError() != null)
            message = e.getBindingResult().getFieldError().getDefaultMessage();
        else
            message = "Validation error";
        return DEPRECATED__ApiResponseCreator.fail(message);

    }
    //파라미터를 입력하지 않은 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public DEPRECATED__ApiResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage());
    }

    //URI는 맞으나 요청 방식이 잘못된 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public DEPRECATED__ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED.value());
    }

    //enum(OAuthSocialType) fromValue() -> 값이 없을 경우
    @ExceptionHandler(IllegalArgumentException.class)
    public DEPRECATED__ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage());
    }

    //세션이 없을 경우
    @ExceptionHandler(IllegalStateException.class)
    public DEPRECATED__ApiResponse<?> handleIllegalStateException(IllegalStateException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage());
    }

    //JsonNode에 없는 값에 접근하려는 경우
    //값을 받지 못한 경우
    @ExceptionHandler(NullPointerException.class)
    public DEPRECATED__ApiResponse<?> handleNullPointerException(NullPointerException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage());
    }

    //처리할 메서드가 없는 경우
    @ExceptionHandler(NoResourceFoundException.class)
    public DEPRECATED__ApiResponse<?> handleNoResourceFoundException(NoResourceFoundException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    //WebClient 요청 혹은 응답에 실패할 경우
    //WebClient 예외 메세지에 요청한 주소가 포함되어 나와서 임시 방편으로 메세지 직접 넣음
    @ExceptionHandler(WebClientException.class)
    public DEPRECATED__ApiResponse<?> handleWebClientException(){
        return DEPRECATED__ApiResponseCreator.fail("Request to other server failed");
    }

    //json 변환에 실패할 경우
    @ExceptionHandler(JsonProcessingException.class)
    public DEPRECATED__ApiResponse<?> handleJsonProcessingException(JsonProcessingException e){
        return DEPRECATED__ApiResponseCreator.fail(e.getMessage());
    }

    //TODO: DataIntegrityException, InvalidDataAccessApiUsageException
}