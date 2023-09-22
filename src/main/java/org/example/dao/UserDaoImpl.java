package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.exceptions.UserNotFoundException;
import org.example.utils.Props;

import java.sql.*;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final Props props;

    @Override
    public User findUser(String login, String password) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password")))
        {
            PreparedStatement findNickname = connection.prepareStatement("select nickname from usersofchat " +
                    "where login = '" + login + "'" + "and password = '" + password + "'");
            ResultSet resultSet = findNickname.executeQuery();
            resultSet.next();
            String nickname = resultSet.getString("nickname");
            return new User(nickname);
        } catch (SQLException e) {
            throw new UserNotFoundException("User not found");
        }
    }

    @Override
    public boolean regNewNickname(String nickname) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password")))
        {
            PreparedStatement registrationNewClient = connection.prepareStatement("insert into server_schema.usersofchat values (?, ?)");
            registrationNewClient.setBytes(1, null);
            registrationNewClient.setString(2, nickname);
            registrationNewClient.executeUpdate();

            PreparedStatement checkNewClient = connection.prepareStatement("select count(*) cnt from server_schema.usersofchat " +
                    "where nickname = '" + nickname + "'");
            ResultSet resultSet = checkNewClient.executeQuery();
            resultSet.next();
            int userCount = resultSet.getInt("cnt");
            if (userCount == 1) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

}
