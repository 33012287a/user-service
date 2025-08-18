package com.astondev.app.service;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import com.astondev.app.dao.user.UserDao;

import com.astondev.app.exceptions.UserDaoException;

@ExtendWith(MockitoExtension.class)
public class UserServiceAppTest {

    @Mock
    private UserDao userDao;
    @Mock
    private Scanner scanner;
    private UserServiceApp serviceApp;

    @BeforeEach
    void setUp() {
        serviceApp = new UserServiceApp(scanner, userDao);
    }

    @Test
    void testCreateUser_Success() throws UserDaoException{
        when (scanner.nextLine())
            .thenReturn("Иван")
            .thenReturn("ivan@example.com")
            .thenReturn("25");

        serviceApp.createUser();

        verify(userDao).createUser(argThat(user ->
            user.getName().equals("Иван") &&
            user.getEmail().equals("ivan@example.com") &&
            user.getAge() == 25
        ));
    }

    @Test
    void testDeleteUser_Success() throws UserDaoException {
        when (scanner.nextLine())
            .thenReturn("1");

        serviceApp.deleteUser();

        verify(userDao).deleteUserById(true);
    }
