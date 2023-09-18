package org.example.dao;

import lombok.RequiredArgsConstructor;
import org.example.utils.Props;

import java.sql.*;

@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private final Props props;

    @Override
    public boolean findByNickname(String nickname) {
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
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        throw new UserNotFoundException("User not found");
        return false;
    }
}
