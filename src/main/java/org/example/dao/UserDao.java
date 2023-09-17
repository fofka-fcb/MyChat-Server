package org.example.dao;

import org.example.domain.User;

public interface UserDao {
    User findByNickname(String nickname);
}
