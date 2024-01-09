package com.bestbenefits.takoyaki.config.apiresponse;

public class SuccessResponseBody<M, D> {
    public boolean success;
    public M meta;
    public D data;

    SuccessResponseBody(M meta, D data) {
        this.success = true;
        this.meta = meta;
        this.data = data;
    }
}
