package com.xplor.android.models;

public class XplorError<T> {
    int errorCode;
    private T errorObj;
    private String name;
    private String message;

    public XplorError(int errorCode) {
        this.errorCode = errorCode;
    }

    public XplorError(String name, String message, int errorCode) {
        this.name = name;
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getErrorObj() {
        return errorObj;
    }

    public void setErrorObj(T errorObj) {
        this.errorObj = errorObj;
    }
}
