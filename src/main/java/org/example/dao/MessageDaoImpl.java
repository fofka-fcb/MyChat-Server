package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.utils.Props;

import java.sql.*;

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

    public StringBuilder readMessages() {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))) {
            PreparedStatement readMessage = connection.prepareStatement("SELECT nickname, message FROM messages ORDER BY id DESC LIMIT 1");
            ResultSet resultSet = readMessage.executeQuery();
            resultSet.next();

            StringBuilder builder = new StringBuilder();
            builder.append(resultSet.getString(1));
            builder.append(": ");
            builder.append(resultSet.getString(2));

            return builder;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
