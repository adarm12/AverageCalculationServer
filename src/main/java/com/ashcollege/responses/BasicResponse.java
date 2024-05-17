package com.ashcollege.responses;

import com.ashcollege.entities.User;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;
    private User user;

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public BasicResponse(boolean success, Integer errorCode, User user) {
        this.success = success;
        this.errorCode = errorCode;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
