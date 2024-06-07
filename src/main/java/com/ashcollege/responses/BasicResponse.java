package com.ashcollege.responses;

import com.ashcollege.entities.Grade;
import com.ashcollege.entities.User;

import java.util.List;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;
    private User user;

    private List<Grade> grades;
    private Double average;

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public BasicResponse(boolean success, Integer errorCode, User user) {
        this.success = success;
        this.errorCode = errorCode;
        this.user = user;
    }

    public BasicResponse(boolean success, Integer errorCode, List<Grade> grades, double average) {
        this.success = success;
        this.errorCode = errorCode;
        this.grades = grades;
        this.average = average;
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

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
