package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.domain.Message;
import org.example.services.ClientServices;
import org.example.utils.Props;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {
    private final Props props;
    public final List<Message> listOfMessage = new ArrayList<>();
    private final ClientServices client;

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

    public void notifyMeAboutMessages() {
        readMessages();
        for (int i = listOfMessage.size() - 1; i >= 0; i--) {
            client.notifyMe(listOfMessage.get(i).getFrom() + ": " + listOfMessage.get(i).getTo_text());
        }
    }

    private void readMessages() {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT nickname, message FROM messages ORDER BY id DESC LIMIT 10");
            while (resultSet.next()) {
                String nickname = resultSet.getString(1);
                String message = resultSet.getString(2);

                Message messageAndNickname = new Message();
                messageAndNickname.setFrom(nickname);
                messageAndNickname.setTo_text(message);

                listOfMessage.add(messageAndNickname);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
