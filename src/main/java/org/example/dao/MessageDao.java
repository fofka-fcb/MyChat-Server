package org.example.dao;

import org.example.domain.Message;

public interface MessageDao {
    void recordMessage(String nickname, String message);
    StringBuilder readMessages();
}
