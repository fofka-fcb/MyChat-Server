package org.example.dao;

import org.example.domain.User;

public interface UserDao {
    User findUser(String login, String password);

    boolean checkLogin(String login);
    boolean checkNickname(String nickname);

    User regNewNickname(String login, String password, String nickname);
}
