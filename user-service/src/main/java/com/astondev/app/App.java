package com.astondev.app;

import java.util.List;

import com.astondev.app.dao.user.UserDao;
import com.astondev.app.model.user.User;
import com.astondev.app.repository.UserConsoleApp;

public class App {

    public static void main(String[] args) {
        new UserConsoleApp().start();
    }
}
