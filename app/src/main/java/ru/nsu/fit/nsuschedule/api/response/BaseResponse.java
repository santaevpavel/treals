package ru.nsu.fit.nsuschedule.api.response;

import java.io.Serializable;

/**
 * Created by Pavel on 05.10.2016.
 */
public class BaseResponse implements Serializable{

    private boolean hasError = false;
    private String msg = "";

    public BaseResponse() {
        hasError = false;
    }

    public BaseResponse(String msg) {
        hasError = true;
        this.msg = msg;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getErrorMsg() {
        return msg;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public void setErrorMsg(String msg) {
        this.msg = msg;
        this.hasError = true;
    }
}
