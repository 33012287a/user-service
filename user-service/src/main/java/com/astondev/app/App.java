package com.astondev.app;

import java.util.List;

import com.astondev.app.dao.user.UserDao;
import com.astondev.app.model.user.User;

public class App {

    public static void main(String[] args) {
        UserDao userDao = new UserDao();

        List<User> user = userDao.findAllUsers();
        if (user != null) {
            for (User u : user) {
                System.out.println("User ID: " + u.getId() + ", Name: " + u.getName() + ", Age: " + u.getAge() +", Email: " + u.getEmail() + ", Created At: " + u.getCreatedAt());
            }
        }
        System.out.println("----------------------------------------------------------");
        User newUser = new User("John", "asa@.as", 35);
        userDao.create(newUser);
        System.out.println("-----------------------------------------------------------");

        userDao.deleteUserById(7);

        System.out.println("-----------------------------------------------------------");

        List<User> updatedUserList = userDao.findAllUsers();
        if (updatedUserList != null) {
            for (User u : updatedUserList) {
                System.out.println("User ID: " + u.getId() + ", Name: " + u.getName() + ", Age: " + u.getAge() +", Email: " + u.getEmail() + ", Created At: " + u.getCreatedAt());
            }
        }
        System.out.println("-----------------------------------------------------------");

        userDao.updateUser(new User(2L, "Kristina", "ad", 20));
        List<User> finalUserList = userDao.findAllUsers();
        if (finalUserList != null) {
            for (User u : finalUserList) {
                System.out.println("User ID: " + u.getId() + ", Name: " + u.getName() + ", Age: " + u.getAge() +", Email: " + u.getEmail() + ", Created At: " + u.getCreatedAt());
            }
        }
        System.out.println("-----------------------------------------------------------");
    }
}
