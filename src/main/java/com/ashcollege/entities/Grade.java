package com.ashcollege.entities;

public class Grade {
    private int id;
    private String courseName;
    private int courseGrade;
    private int coursePoints;
    private String userSecret;

    public Grade(int id, String courseName, int grade, int coursePoints, String userSecret) {
        this.id = id;
        this.courseName = courseName;
        this.courseGrade = grade;
        this.coursePoints = coursePoints;
        this.userSecret = userSecret;
    }

    public Grade(String courseName, int grade, int coursePoints, String userSecret) {
        this.courseName = courseName;
        this.courseGrade = grade;
        this.coursePoints = coursePoints;
        this.userSecret = userSecret;
    }

    public Grade() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(int grade) {
        this.courseGrade = grade;
    }

    public int getCoursePoints() {
        return coursePoints;
    }

    public void setCoursePoints(int coursePoints) {
        this.coursePoints = coursePoints;
    }

    public String getUserSecret() {
        return userSecret;
    }

    public void setUserSecret(String userSecret) {
        this.userSecret = userSecret;
    }
}
