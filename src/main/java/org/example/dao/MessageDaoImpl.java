package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.utils.Props;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private final Props props;

    @Override
    public void recordMessage(String nickname, String message) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))) {
            PreparedStatement recordStatement = connection.prepareStatement("insert into server_schema.messages values (?, ?, ?)");
            recordStatement.setBytes(1, null);
            recordStatement.setString(2, nickname);
            recordStatement.setString(3, message);
            recordStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
