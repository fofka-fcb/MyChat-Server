package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.exceptions.UserNotCreatedException;
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
                props.getValue("db.password"))) {
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
    public boolean checkLogin(String login) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))) {
            PreparedStatement findLogin = connection.prepareStatement("select count(*) cnt from usersofchat " +
                    "where login = '" + login + "'");
            ResultSet resultSet = findLogin.executeQuery();
            resultSet.next();

            int loginCount = resultSet.getInt("cnt");

            if (loginCount == 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean checkNickname(String nickname) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))) {
            PreparedStatement findNickname = connection.prepareStatement("select count(*) cnt from usersofchat " +
                    "where nickname = '" + nickname + "'");
            ResultSet resultSet = findNickname.executeQuery();
            resultSet.next();

            int userCount = resultSet.getInt("cnt");

            if (userCount == 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public User regNewNickname(String login, String password, String nickname) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"))) {
            PreparedStatement regNewUser = connection.prepareStatement("insert into server_schema.usersofchat values (?, ?, ?, ?)");
            regNewUser.setBytes(1, null);
            regNewUser.setString(2, nickname);
            regNewUser.setString(3, login);
            regNewUser.setString(4, password);
            regNewUser.executeUpdate();

            return findUser(login, password);

        } catch (SQLException e) {
            throw new UserNotCreatedException("User not created");
        }
    }

}
