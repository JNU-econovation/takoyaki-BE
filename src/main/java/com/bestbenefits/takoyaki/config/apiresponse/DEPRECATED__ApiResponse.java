package com.bestbenefits.takoyaki.config.apiresponse;

@Deprecated
public record DEPRECATED__ApiResponse<D>(boolean success, D data, int status) { }