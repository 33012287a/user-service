package com.astondev.app;

import com.astondev.app.dao.user.UserDaoImpl;
import com.astondev.app.exceptions.UserDaoException;
import com.astondev.app.model.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("user-service")
                    .withUsername("testuser")
                    .withPassword("test");

    private SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    @BeforeAll
    void setup() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml")
                .setProperty("hibernate.connection.url", postgres.getJdbcUrl())
                .setProperty("hibernate.connection.username", postgres.getUsername())
                .setProperty("hibernate.connection.password", postgres.getPassword())
                .setProperty("hibernate.hbm2ddl.auto", "create-drop");

        sessionFactory = configuration.buildSessionFactory();
        userDao = new UserDaoImpl(sessionFactory);
    }

    private User createTestUser(String name, String email, int age) throws UserDaoException {
        User user = new User(name, email, age);
        userDao.createUser(user);
        return user;
    }

    @Test
    void testCreateUserSuccess() throws UserDaoException {
        User user = new User("Ivan", "Ivan@example.com", 25);
        boolean result = userDao.createUser(user);
        assertTrue(result);
        assertNotNull(user.getId());
    }

    @Test
    void testUpdateUser() throws UserDaoException {
        User user = createTestUser("Ivan", "ivan@example.com", 25);

        user.setName("Ivan2");
        assertTrue(userDao.updateUser(user));

        User fromDb = userDao.getUserById(user.getId());
        assertEquals("Ivan2", fromDb.getName());
    }

    @Test
    void testDeleteUser() throws UserDaoException {
        User user = createTestUser("Ivan", "ivan@example.com", 25);;

        assertTrue(userDao.deleteUserById(user.getId()));
        assertNull(userDao.getUserById(user.getId()));
    }

    @Test
    void testDeleteUserInvalid() throws UserDaoException {
        User deleted = userDao.getUserById(-1L);
        assertNull(deleted);
    }

    @Test
    void testGetAllUsers() throws UserDaoException {
        userDao.createUser(new User("Ivan", "ivan@examole.com", 25));
        userDao.createUser(new User("Anna", "anna@example.com", 30));

        List<User> users = userDao.getAllUsers();
        assertTrue(users.size() > 2);
    }

}
