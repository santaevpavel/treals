package ru.nsu.fit.nsuschedule.util;

/**
 * Created by Pavel on 06.10.2016.
 */
public class RequestInfo {

    public boolean isRequestProcessing = false;
    public boolean isRequestSuccess = false;

    public boolean tryToRequest(){
        if (isRequestProcessing){
            return false;
        } else {
            isRequestProcessing = true;
            return true;
        }
    }

    public void finish(boolean success){
        isRequestProcessing = false;
        isRequestSuccess = success;
    }
}
