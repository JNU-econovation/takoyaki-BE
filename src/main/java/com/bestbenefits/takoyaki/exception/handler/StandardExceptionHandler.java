package com.bestbenefits.takoyaki.exception.handler;

import com.bestbenefits.takoyaki.config.apiresponse.ApiResponse;
import com.bestbenefits.takoyaki.config.apiresponse.ApiResponseCreator;
import com.bestbenefits.takoyaki.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class StandardExceptionHandler {
    //body가 없는 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleHttpMessageNotReadableException(){
        return ApiResponseCreator.fail(
                ExceptionCode.HTTP_MSG_NOT_READABLE);
    }

    //URI는 맞으나 요청 방식이 잘못된 경우
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        return ApiResponseCreator.fail(
                ExceptionCode.HTTP_METHOD_NOT_SUPPORTED,
                e.getMethod(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }

    //@Vaild 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        FieldError error = e.getBindingResult().getFieldError();
        String message = error == null ? ExceptionCode.VALIDATION_FAILED.getMsg()
                : error.getField() + ": " + error.getDefaultMessage();
        return ApiResponseCreator.fail(
                ExceptionCode.VALIDATION_FAILED,
                message,
                true);
    }

    //파라미터를 입력하지 않은 경우
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        return ApiResponseCreator.fail(
                ExceptionCode.MISSING_SERVLET_REQUEST_PARAMETER,
                e.getParameterName());
    }

    //잘못된 Argument 제공
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<?> handleIllegalArgumentException(IllegalArgumentException e){
        return ApiResponseCreator.fail(
                ExceptionCode.ILLEGAL_ARGUMENT,
                e.getMessage(),
                true);
    }

    //세션이 없을 경우
    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<?> handleIllegalStateException(IllegalStateException e) {
        return ApiResponseCreator.fail(ExceptionCode.ILLEGAL_STATE,
                e.getMessage(),
                true);
    }

    //JsonNode에 없는 값에 접근하려는 경우
    //값을 받지 못한 경우
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<?> handleNullPointerException(NullPointerException e){
        return ApiResponseCreator.fail(
                ExceptionCode.NULL_POINTER_EXCEPTION,
                e.getMessage(),
                true);
    }

    //처리할 메서드가 없는 경우
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResponse<?> handleNoResourceFoundException(NoResourceFoundException e){
        return ApiResponseCreator.fail(
                ExceptionCode.NO_RESOURCE_FOUND,
                e.getResourcePath(),
                HttpStatus.NOT_FOUND);
    }

    //WebClient 요청 혹은 응답에 실패할 경우
    //WebClient 예외 메세지에 요청한 주소가 포함되어 나와서 임시 방편으로 메세지 직접 넣음
    @ExceptionHandler(WebClientException.class)
    public ApiResponse<?> handleWebClientException() {
        return ApiResponseCreator.fail(
                ExceptionCode.WEB_CLIENT_EXCEPTION);
    }

    //json 변환에 실패할 경우
    @ExceptionHandler(JsonProcessingException.class)
    public ApiResponse<?> handleJsonProcessingException(JsonProcessingException e){
        return ApiResponseCreator.fail(
                ExceptionCode.JSON_PROCESSING_FAILED,
                e.getLocation().toString());
    }

    //빈 객체 생성 실패
    //get parties에서 쿼리 파라미터가 잘못된 경우 생성됨
    //TODO: 커스텀 예외 처리로 바꾸기
    @ExceptionHandler(BeanInstantiationException.class)
    public ApiResponse<?> handleBeanInstantiationException(BeanInstantiationException e) {
        return ApiResponseCreator.fail(
                ExceptionCode.INVALID_QUERY_PARAM
        );
    }
    //TODO: DataIntegrityException, InvalidDataAccessApiUsageException
}