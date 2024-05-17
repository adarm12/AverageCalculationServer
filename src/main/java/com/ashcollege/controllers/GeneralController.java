package com.ashcollege.controllers;

import com.ashcollege.Persist;
import com.ashcollege.entities.Grade;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import com.ashcollege.responses.LoginResponse;
import com.ashcollege.utils.DbUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ashcollege.utils.Errors.*;

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
    public BasicResponse addGrade(String courseName, int courseGrade, int coursePoints, String userSecret) {
        return persist.addGrade(courseName, courseGrade, coursePoints, userSecret);
    }

    @RequestMapping(value = "/grades", method = {RequestMethod.GET, RequestMethod.POST})
    public List<Grade> getGradesBySecret(String userSecret) {
        return persist.getGradesBySecret(userSecret);
    }

}
