package com.bestbenefits.takoyaki.config.apiresponse;

import org.springframework.http.HttpStatus;

@Deprecated
public class DEPRECATED__ApiResponseCreator {
    public static <D> DEPRECATED__ApiResponse<D> success(D data){
        return new DEPRECATED__ApiResponse<>(true, data, HttpStatus.OK.value());
    }
    public static <D> DEPRECATED__ApiResponse<D> success(D data, int status){
        return new DEPRECATED__ApiResponse<>(true, data, status);
    }
    public static DEPRECATED__ApiResponse<DEPRECATED__ApiMessage> fail(String message){
        return new DEPRECATED__ApiResponse<>(false, new DEPRECATED__ApiMessage(message), HttpStatus.BAD_REQUEST.value());
    }
    public static DEPRECATED__ApiResponse<DEPRECATED__ApiMessage> fail(String message, int status){
        return new DEPRECATED__ApiResponse<>(false, new DEPRECATED__ApiMessage(message), status);
    }
}
