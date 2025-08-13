package com.astondev.app.service;

import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astondev.app.dao.user.UserDao;

@ExtendWith(MockitoExtension.class)
public class UserServiceAppTest {


    private UserDao userDao;
    private Scanner scanner;
    private UserServiceApp serviceApp;

    @BeforeEach
    public void setUp() {
        userDao = mock(UserDao.class);
        scanner = mock(Scanner.class);
        serviceApp = new UserServiceApp(scanner, userDao);
    }

    @Test
    void testCreateUser_Success() throw UserDaoException{
        when(Scanner.nextLine()).thenReturn("Иван", "ivan@example.com", "25");

        service.createUser();

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(UserDao.class);
        verify(userDao, times(1)).createUser(captor.capture());

        User capturedUser = captor.getValue();
        assertEquals("Иван", capturedUser.getName());
        assertEquals("ivan@example.com", capturedUser.getEmail());
        assertEquals(25, capturedUser.getAge());
    }


}
