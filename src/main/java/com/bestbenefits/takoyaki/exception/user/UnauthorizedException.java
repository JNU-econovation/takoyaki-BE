package com.bestbenefits.takoyaki.exception.user;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final String caused;

    public UnauthorizedException(String caused) {
        this.caused = caused;
    }
}