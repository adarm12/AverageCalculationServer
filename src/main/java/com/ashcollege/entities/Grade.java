package com.ashcollege.entities;

public class Grade {
    private int id;
    private String courseName;
    private Integer courseGrade;
    private Integer coursePoints;
    private String userSecret;

    public Grade(int id, String courseName, Integer grade, Integer coursePoints, String userSecret) {
        this.id = id;
        this.courseName = courseName;
        this.courseGrade = grade;
        this.coursePoints = coursePoints;
        this.userSecret = userSecret;
    }

    public Grade(String courseName, Integer grade, Integer coursePoints, String userSecret) {
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

    public Integer getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(int grade) {
        this.courseGrade = grade;
    }

    public Integer getCoursePoints() {
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
