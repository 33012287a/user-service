package com.astondev.app.dao.user;

import com.astondev.app.model.user.User;
import java.util.List;

public interface UserDao {
    void createUser(User user);
    User getUserById(long id);
    List<User> getAllUsers();
    boolean deleteUserById(Long id);
    boolean updateUser(User user);
}
