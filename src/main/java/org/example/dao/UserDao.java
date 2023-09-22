package org.example.dao;

import org.example.domain.User;

public interface UserDao {
    User findUser(String login, String password);

    boolean regNewNickname(String name);
}
