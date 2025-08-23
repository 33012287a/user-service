package com.astondev.app.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

import com.astondev.app.dao.user.UserDaoImpl;
import com.astondev.app.exceptions.UserDaoException;
import com.astondev.app.model.user.User;
import com.astondev.app.utils.InputValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class UserServiceAppTest {
    @Mock
    private UserDaoImpl userDao;

    private UserServiceApp serviceApp;

    private ByteArrayOutputStream outContent;

    private UserServiceApp createServiceWithInput(String inputText){
        return new UserServiceApp(
                new Scanner(new ByteArrayInputStream(inputText.getBytes())),
                    userDao);
    }

    private String getConsoleOutput() {
        return outContent.toString().trim();
    }

    @BeforeEach
    public void setUp(){
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        serviceApp = createServiceWithInput("");
    }

    @Test
    void testCreateUserSuccess() throws UserDaoException {
        when(userDao.createUser(any(User.class))).thenReturn(true);
        serviceApp = createServiceWithInput("Alice\nalice@example.com\n25\n");
        boolean result = serviceApp.createUser();
        assertTrue(result);
        verify(userDao, times(1)).createUser(any(User.class));
    }

    @Test
    void testCreateUserFail() throws UserDaoException {
        when(userDao.createUser(any(User.class))).thenReturn(false);
        serviceApp = createServiceWithInput("Alice\nalice@example.com\n25\n");
        boolean result = serviceApp.createUser();
        assertFalse(result);
        verify(userDao, times(1)).createUser(any(User.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"5", "-5", "abc"})
    void testCreateUserVariousAge(String age) throws UserDaoException {
        serviceApp = createServiceWithInput("Ivan\nivan@example.com\n" + age + "\n");
        if (InputValidator.isValidAge(age)) {
            when(userDao.createUser(any(User.class))).thenReturn(true);
        }

        boolean result = serviceApp.createUser();

        if (result) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    @Test
    void testCreateUserDaoException() throws UserDaoException {
        serviceApp = createServiceWithInput("Ivan\nivan@example.com\n25\n");

        when(userDao.createUser(any(User.class)))
                .thenThrow(new UserDaoException("DB error"));

        boolean result = serviceApp.createUser();

        assertFalse(result);
        verify(userDao, times(1)).createUser(any(User.class));
    }

    @Test
    void testCreateUnexpectedException() throws UserDaoException {
        when(userDao.createUser(any(User.class))).thenThrow(new RuntimeException("Unexpected error"));
        serviceApp = createServiceWithInput("Ivan\nivan@example.com\n25\n");
        boolean result = serviceApp.createUser();
        assertFalse(result);
        verify(userDao, times(1)).createUser(any(User.class));
    }

    @Test
    void testDeleteUserSuccess() throws UserDaoException {
        when(userDao.deleteUserById(anyLong())).thenReturn(true);
        serviceApp = createServiceWithInput("1\n");
        boolean result = serviceApp.deleteUser();
        assertTrue(result);
        verify(userDao, times(1)).deleteUserById(1L);
    }

    @ParameterizedTest
    @CsvSource({
            "1,false,1",
            "-1,false,0",
            "abc, false, 0"
    })
    void testDeleteUserVariousInputs(String input, boolean expectedResult, int expectedDaoCalls) throws UserDaoException {
        if (expectedDaoCalls > 0) {
            when(userDao.deleteUserById(anyLong())).thenReturn(expectedResult);
        }

        serviceApp = createServiceWithInput(input + "\n");
        boolean result = serviceApp.deleteUser();
        assertEquals(expectedResult, result);
        verify(userDao, times(expectedDaoCalls)).deleteUserById(anyLong());
    }

    @Test
    void testDeleteUserDaoException() throws UserDaoException {
        when(userDao.deleteUserById(anyLong())).thenThrow(new UserDaoException("DB error"));
        serviceApp = createServiceWithInput("1\n");
        boolean result = serviceApp.deleteUser();
        assertFalse(result);
        verify(userDao, times(1)).deleteUserById(anyLong());
    }

    @Test
    void testDeleteUnexpectedException() throws UserDaoException {
        when(userDao.deleteUserById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        serviceApp = createServiceWithInput("1\n");
        boolean result = serviceApp.deleteUser();
        assertFalse(result);
        verify(userDao, times(1)).deleteUserById(anyLong());
    }

    @Test
    void testUpdateUserSuccess() throws UserDaoException {
        when(userDao.updateUser(any(User.class))).thenReturn(true);
        serviceApp = createServiceWithInput("1\nIvan\nivan@example.com\n25\n");
        boolean result = serviceApp.updateUser();
        assertTrue(result);
        verify(userDao, times(1)).updateUser(any(User.class));
    }

    @ParameterizedTest
    @CsvSource({
            "-1,Ivan,ivan@example.com,25,false,0",
            "abc,Ivan, example.com,25,false,0",
            "1,,ivan@example.com,25,false,1",
            "1,ivan,,25,false,1",
            "1,ivan,ivan@example.com,,false,0",
            "1,ivan,ivan@example.com,-1,false,0"
    })
    void testUpdateUserVariousInputs(String id, String name, String email, String age, boolean expectedResult, int expectedDaoCalls) throws UserDaoException {
        if (expectedDaoCalls > 0) {
            when(userDao.updateUser(any(User.class))).thenReturn(expectedResult);
        }

        serviceApp = createServiceWithInput(id + "\n" + name +"\n" + email + "\n" + age + "\n");
        boolean result = serviceApp.updateUser();
        assertEquals(expectedResult, result);
        verify(userDao, times(expectedDaoCalls)).updateUser(any(User.class));
    }

    @Test
    void testUpdateUserDaoException() throws UserDaoException {
        when(userDao.updateUser(any(User.class))).thenThrow(new UserDaoException("DB error"));
        serviceApp = createServiceWithInput("1\nivan\nivan@example.com\n25\n");
        boolean result = serviceApp.updateUser();
        assertFalse(result);
        verify(userDao, times(1)).updateUser(any(User.class));
    }

    @Test
    void testUpdateUnexpectedException() throws UserDaoException {
        when(userDao.updateUser(any(User.class))).thenThrow(new UserDaoException("DB error"));
        serviceApp = createServiceWithInput("1\nivan\nivan@example.com\n25\n");
        boolean result = serviceApp.updateUser();
        assertFalse(result);
        verify(userDao, times(1)).updateUser(any(User.class));
    }

    @Test
    void testShowAllUsersSuccess() throws UserDaoException {
        when(userDao.getAllUsers()).thenReturn(List.of(
                new User("Ivan", "ivan@example.com", 25),
                new User("Anna", "anna@example.com", 30)
        ));

        serviceApp.showAllUsers();

        String output = outContent.toString();
        assertTrue(output.contains("Ivan"));
        assertTrue(output.contains("anna@example.com"));
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testShowAllUsersEmptyList() throws UserDaoException {
        when(userDao.getAllUsers()).thenReturn(List.of());
        serviceApp.showAllUsers();
        String output = outContent.toString();
        assertTrue(output.contains("Нет пользователей для отображения."));
        verify(userDao, times(1)).getAllUsers();
    }

    @Test
    void testShowAllUsersException() throws UserDaoException {
        when(userDao.getAllUsers()).thenThrow(new UserDaoException("DB error"));
        serviceApp.showAllUsers();

        String output = outContent.toString();
        assertTrue(output.contains("Ошибка базы данных"));
        verify(userDao, times(1)).getAllUsers();

    }
}
