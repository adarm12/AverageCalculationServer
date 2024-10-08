package com.ashcollege.controllers;

import com.ashcollege.Persist;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {

    @Autowired
    private DbUtils dbUtils;

    @Autowired
    private Persist persist;

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
    public Object test() {
        return "Hello From Server";
    }

    @RequestMapping(value = "/sign-up", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse signUp(String username, String password, String repeatPassword, String degreeName) {
        return persist.signUp(username, password, repeatPassword, degreeName);
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse login(String username, String password) {
        return persist.login(username, password);
    }

    @RequestMapping(value = "/add-grade", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse addGrade(String courseName, Integer courseGrade, Integer coursePoints, String userSecret) {
        return persist.addGrade(courseName, courseGrade, coursePoints, userSecret);
    }

    @RequestMapping(value = "/edit-grade", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse editGrade(String oldCourseName, String courseName, Integer courseGrade, Integer coursePoints, String userSecret) {
        return persist.editGrade(oldCourseName, courseName, courseGrade, coursePoints, userSecret);
    }

    @RequestMapping(value = "/delete-grade", method = {RequestMethod.GET, RequestMethod.POST})
    public boolean deleteGrade(String courseName, String userSecret) {
        return persist.deleteGrade(courseName, userSecret);
    }

    @RequestMapping(value = "/grades", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse getGradesBySecret(String userSecret) {
        return persist.getGradesBySecret(userSecret);
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public BasicResponse searchCourseByName(String courseName, String userSecret) {
        return persist.searchCourseByName(courseName, userSecret);
    }

}
