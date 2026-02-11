package com.debesh.snaphire.user_ms.service;

import com.debesh.snaphire.user_ms.entity.User;

import java.util.List;

public interface UserService {
    void createUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email); // Useful for login

    List<User> getAllUsers();

    void deleteUser(Long id);
}
