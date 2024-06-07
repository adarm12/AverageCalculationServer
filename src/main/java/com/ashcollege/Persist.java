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

    public void delete(Object object) {
        this.sessionFactory.getCurrentSession().delete(object);
    }

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public BasicResponse signUp(String username, String password, String repeatPassword, String degreeName) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS, (User) null);
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
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS, (User) null);
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

    public BasicResponse addGrade(String courseName, Integer courseGrade, Integer coursePoints, String userSecret) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS);
        if (courseName != null) {
            if (checkIfCourseNameAvailable(courseName)) {
                if (courseGrade != null) {
                    if (coursePoints != null) {
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
                        basicResponse.setErrorCode(NO_COURSE_POINTS);
                } else
                    basicResponse.setErrorCode(NO_GRADE);
            } else
                basicResponse.setErrorCode(COURSE_NAME_IS_TAKEN);
        } else
            basicResponse.setErrorCode(NO_COURSE_NAME);
        return basicResponse;
    }

    public BasicResponse editGrade(String oldCourseName, String courseName, Integer courseGrade, Integer coursePoints, String userSecret) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS);
        if (courseName != null) {
            if (checkIfCourseNameAvailable(courseName)) {
                if (courseGrade != null) {
                    if (coursePoints != null) {
                        if (courseGrade >= 0 && courseGrade <= 100) {
                            if (coursePoints > 0 && coursePoints <= 5) {
                                if (findGradeBySecret(userSecret, oldCourseName) != null) {
                                    Grade grade = findGradeBySecret(userSecret, oldCourseName);
                                    grade.setCourseName(courseName);
                                    grade.setCourseGrade(courseGrade);
                                    grade.setCoursePoints(coursePoints);
                                    basicResponse.setSuccess(true);
                                    save(grade);
                                } else
                                    basicResponse.setErrorCode(ERROR_NO_SUCH_GRADE);
                            } else
                                basicResponse.setErrorCode(NO_VALID_POINTS);
                        } else
                            basicResponse.setErrorCode(NO_VALID_GRADE);
                    } else
                        basicResponse.setErrorCode(NO_COURSE_POINTS);
                } else
                    basicResponse.setErrorCode(NO_GRADE);
            } else
                basicResponse.setErrorCode(COURSE_NAME_IS_TAKEN);
        } else
            basicResponse.setErrorCode(NO_COURSE_NAME);
        return basicResponse;
    }

    public boolean deleteGrade(String courseName, String userSecret) {
        boolean delete = false;
        Grade grade = findGradeBySecret(userSecret, courseName);
        delete(grade);
        if (findGradeBySecret(userSecret, courseName) == null)
            delete = true;
        return delete;
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

    public boolean checkIfCourseNameAvailable(String courseName) {
        Grade grade;
        grade = (Grade) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Grade WHERE courseName = :courseName")
                .setParameter("courseName", courseName)
                .setMaxResults(1)
                .uniqueResult();
        return (grade == null);
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

    public List<Grade> findGradesBySecret(String userSecret) {
        List<Grade> grades;
        grades = (List<Grade>) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Grade WHERE userSecret = :userSecret")
                .setParameter("userSecret", userSecret)
                .list();
        return grades;
    }

    public Grade findGradeBySecret(String userSecret, String courseName) {
        Grade grade;
        grade = (Grade) this.sessionFactory.getCurrentSession().createQuery(
                        "FROM Grade WHERE userSecret = :userSecret AND courseName = :courseName")
                .setParameter("userSecret", userSecret)
                .setParameter("courseName", courseName)
                .setMaxResults(1)
                .uniqueResult();
        return grade;
    }

    public BasicResponse getGradesBySecret(String userSecret) {
        BasicResponse basicResponse = new BasicResponse(false, NO_ERRORS, null, 0.0);
        if (findGradesBySecret(userSecret) != null) {
            User user = findUserBySecret(userSecret);
            user.setAverage(calculateAverage(findGradesBySecret(userSecret)));
            save(user);
            basicResponse.setAverage(calculateAverage(findGradesBySecret(userSecret)));
            basicResponse.setSuccess(true);
            basicResponse.setGrades(findGradesBySecret(userSecret));
        } else {
            basicResponse.setErrorCode(NO_GRADES);
        }
        return basicResponse;
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