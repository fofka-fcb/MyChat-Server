package org.example.dao;

public interface UserDao {
    boolean findByNickname(String nickname);

    boolean regNewNickname(String name);
}
