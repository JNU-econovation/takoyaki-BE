package com.bestbenefits.takoyaki.config.apiresponse;

public class SuccessApiResponseBody<M, D> {
    public boolean success;
    public M meta;
    public D data;

    SuccessApiResponseBody(M meta, D data) {
        this.success = true;
        this.meta = meta;
        this.data = data;
    }
}
