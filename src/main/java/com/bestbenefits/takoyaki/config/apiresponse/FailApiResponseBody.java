package com.bestbenefits.takoyaki.config.apiresponse;

import com.bestbenefits.takoyaki.exception.ExceptionCode;

public class FailApiResponseBody {
    public boolean success;
    public String code;
    public String msg;

    FailApiResponseBody(ExceptionCode code) {
        this.success = false;
        this.code = code.toString();
        this.msg = code.getMsg();
    }

    FailApiResponseBody(ExceptionCode code, String additional) {
        this.success = false;
        this.code = code.toString();
        this.msg = additional + ": " + code.getMsg();
    }

    FailApiResponseBody(ExceptionCode code, String customMsg, boolean custom) {
        this(code);
        if (custom) {
            this.msg = customMsg;
        }
    }
}
