package com.astondev.app.dao.user;

import com.astondev.app.model.user.User;
import java.util.List;
import com.astondev.app.exceptions.UserDaoException;

public interface UserDao {
    boolean createUser(User user) throws UserDaoException;
    User getUserById(long id) throws UserDaoException;
    List<User> getAllUsers() throws UserDaoException;
    boolean deleteUserById(Long id) throws UserDaoException;
    boolean updateUser(User user) throws UserDaoException;
}
