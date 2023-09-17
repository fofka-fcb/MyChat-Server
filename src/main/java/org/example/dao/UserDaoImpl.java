package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.utils.Props;

import java.sql.*;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final Props props;

    @Override
    public User findByNickname(String nickname) {
        try (Connection connection = DriverManager.getConnection(
                props.getValue("db.url"),
                props.getValue("db.login"),
                props.getValue("db.password"));
             PreparedStatement preparedStatement = connection.prepareStatement("select count(*) cnt from server_schema.usersofchat " +
                     "where nickname = '" + nickname +"'"))
        {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();

            int userCount = resultSet.getInt("cnt");

            if (userCount == 1){
                return new User(nickname);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("User not found");
    }
}
