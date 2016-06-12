package com.techdegree.nikiforo.log;

public class Logger {
    private String mErrorMessage;
    private String mSuccessMessage;

    public String getHintMessage() {
        return mHintMessage;
    }

    public void setHintMessage(String hintMessage) {
        mHintMessage = hintMessage;
        System.out.println(hintMessage);
    }

    private String mHintMessage;

    public Logger() {

    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        mErrorMessage = errorMessage;
        System.out.println(errorMessage);
    }

    public String getSuccessMessage() {
        return mSuccessMessage;
    }

    public void setSuccessMessage(String successMessage) {
        mSuccessMessage = successMessage;
        System.out.println(successMessage);
    }

}
