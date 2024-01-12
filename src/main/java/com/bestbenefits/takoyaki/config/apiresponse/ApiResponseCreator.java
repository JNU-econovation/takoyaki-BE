package com.bestbenefits.takoyaki.config.apiresponse;

import com.bestbenefits.takoyaki.exception.ExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ApiResponseCreator {

    public static ApiResponse<?> success() {
        return new ApiResponse<>(new SuccessApiResponseBody<>(null, null), HttpStatus.OK);
    }

    public static ApiResponse<?> success(HttpStatusCode status) {
        return new ApiResponse<>(new SuccessApiResponseBody<>(null, null), status);
    }


    public static <D> ApiResponse<?> success(D data) {
        return new ApiResponse<>(new SuccessApiResponseBody<>(null, data), HttpStatus.OK);
    }


    public static <D> ApiResponse<?> success(D data, HttpStatusCode status) {
        return new ApiResponse<>(new SuccessApiResponseBody<>(null, data), status);
    }

    public static <M, D> ApiResponse<?> success(M meta, D data) {
        return new ApiResponse<>(new SuccessApiResponseBody<>(meta, data), HttpStatus.OK);
    }

    public static <M, D> ApiResponse<?> success(M meta, D data, HttpStatusCode status) {
        return new ApiResponse<>(new SuccessApiResponseBody<>(meta, data), status);
    }

    public static ApiResponse<?> fail(ExceptionCode code) {
        return new ApiResponse<>(new FailApiResponseBody(code), HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse<?> fail(ExceptionCode code, HttpStatusCode status) {
        return new ApiResponse<>(new FailApiResponseBody(code), status);
    }

    public static ApiResponse<?> fail(ExceptionCode code, String additional) {
        return new ApiResponse<>(new FailApiResponseBody(code, additional), HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse<?> fail(ExceptionCode code, String additional, HttpStatusCode status) {
        return new ApiResponse<>(new FailApiResponseBody(code, additional), status);
    }

    public static ApiResponse<?> fail(ExceptionCode code, String additional, boolean custom) {
        return new ApiResponse<>(new FailApiResponseBody(code, additional, custom), HttpStatus.BAD_REQUEST);
    }

    public static ApiResponse<?> fail(ExceptionCode code, String additional, boolean custom, HttpStatusCode status) {
        return new ApiResponse<>(new FailApiResponseBody(code, additional, custom), status);
    }
}
