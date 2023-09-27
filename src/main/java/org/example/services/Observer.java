package org.example.services;

public interface Observer {
    void notifyUsers(String nickname, String message);

    void notifyMe(String message);
}
