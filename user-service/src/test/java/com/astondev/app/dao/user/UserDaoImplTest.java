package com.astondev.app.dao.user;

import static org.mockito.Mockito.when;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.astondev.app.model.user.User;


@DisplayName("UserDaoImpl Test")
@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {
    @Mock
    Session session;

    @Mock
    Transaction transaction;

    @Test
    public void testCreateUser() {
        
}
