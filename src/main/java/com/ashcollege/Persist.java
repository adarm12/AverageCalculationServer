package com.ashcollege;


import com.ashcollege.entities.Grade;
import com.ashcollege.entities.User;
import com.ashcollege.responses.BasicResponse;
import com.github.javafaker.Faker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.ashcollege.utils.Errors.*;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }

    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public BasicResponse signUp(String username, String password, String repeatPassword, String degreeName) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS, null);
        if (!username.isEmpty()) {
            if (checkIfUsernameAvailable(username)) {
                if (!password.isEmpty()) {
                    if (!repeatPassword.isEmpty()) {
                        if (password.equals(repeatPassword)) {
                            if (!degreeName.isEmpty()) {
                                User user = new User(username, password, degreeName);
                                Faker faker = new Faker();
                                user.setSecret(faker.random().hex(5));
                                basicResponse.setSuccess(true);
                                save(user);
                            } else
                                basicResponse.setErrorCode(NO_DEGREE_NAME);
                        } else
                            basicResponse.setErrorCode(PASSWORDS_NOT_MATCH);
                    } else
                        basicResponse.setErrorCode(NO_REPEAT_PASSWORD);
                } else
                    basicResponse.setErrorCode(NO_PASSWORD);
            } else
                basicResponse.setErrorCode(ERROR_NO_SUCH_USER);
        } else
            basicResponse.setErrorCode(NO_USERNAME);
        return basicResponse;
    }

    public BasicResponse login(String username, String password) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS, null);
        if (!username.isEmpty()) {
            if (!checkIfUsernameAvailable(username)) {
                if (!password.isEmpty()) {
                    if (userExits(username, password) != null) {
                        basicResponse.setUser(userExits(username, password));
                        basicResponse.setSuccess(true);
                    } else
                        basicResponse.setErrorCode(ERROR_NO_SUCH_USER);
                } else
                    basicResponse.setErrorCode(NO_PASSWORD);
            } else
                basicResponse.setErrorCode(ERROR_NO_SUCH_USERNAME);
        } else
            basicResponse.setErrorCode(NO_USERNAME);
        return basicResponse;
    }

    public BasicResponse addGrade(String courseName, int courseGrade, int coursePoints, String userSecret) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS);
        if (!courseName.isEmpty()) {
            if (courseGrade >= 0 && courseGrade <= 100) {
                if (coursePoints > 0 && coursePoints <= 5) {
                    basicResponse.setSuccess(true);
                    Grade grade = new Grade(courseName, courseGrade, coursePoints, userSecret);
                    save(grade);
                } else
                    basicResponse.setErrorCode(NO_VALID_POINTS);
            } else
                basicResponse.setErrorCode(NO_VALID_GRADE);
        } else
            basicResponse.setErrorCode(NO_COURSE_NAME);
        return basicResponse;
    }


    public boolean checkIfUsernameAvailable(String username) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username = :username")
                .setParameter("username", username)
                .setMaxResults(1)
                .uniqueResult();
        return (user == null);
    }

    public User userExits(String username, String password) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE username = :username AND password = :password")
                .setParameter("username", username)
                .setParameter("password", password)
                .setMaxResults(1)
                .uniqueResult();
        return user;
    }

    public User findUserBySecret(String secret) {
        User user;
        user = (User) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM User WHERE secret = :secret ")
                .setParameter("secret", secret)
                .setMaxResults(1)
                .uniqueResult();
        return user;
    }

    public List<Grade> getGradesBySecret(String userSecret) {
        List<Grade> grades;
        User user;
        grades = (List<Grade>) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Grade WHERE userSecret = :userSecret")
                .setParameter("userSecret", userSecret)
                .list();
        user = findUserBySecret(userSecret);
        user.setAverage(calculateAverage(grades));
        save(user);
        return grades;
    }

    public Double calculateAverage(List<Grade> grades) {
        Double average = 0.0;
        Double pointsSum = 0.0;
        for (Grade grade : grades) {
            average = average + ((double) (grade.getCourseGrade() * grade.getCoursePoints()));
        }
        for (Grade grade : grades) {
            pointsSum = pointsSum + grade.getCoursePoints();
        }
        average = average / pointsSum;
        return average;
    }

}