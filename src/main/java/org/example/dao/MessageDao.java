package org.example.dao;

public interface MessageDao {
    void recordMessage(String nickname, String message);

    void notifyMeAboutMessages();
}
