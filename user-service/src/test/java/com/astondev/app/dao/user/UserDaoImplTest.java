package com.astondev.app.dao.user;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@DisplayName("UserDaoImpl Test")
@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {
    @Mock
    Session session;

    @Mock
    Transaction transaction;

    @Test
    public void testCreateUser() {
        System.out.println("Test for createUser method");
    }
}
