package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private final Connection connection = Util.getConnection();
    private boolean isCreated = isCreated();
    private int idCounter = 1;


    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {

        if (!isCreated) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE users(\n" +
                        "UserID long,\n" +
                        "Name varchar(255),\n" +
                        "LastName varchar(255),\n" +
                        "Age tinyint CHECK (age < 128)\n" +
                        ");"
                );

                preparedStatement.executeUpdate();
                isCreated = true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        if (isCreated) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE users");

                preparedStatement.executeUpdate();
                isCreated = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "INSERT INTO users (userid, name, lastname, age) VALUES (?, ?, ?, ?)");

            preparedStatement.setInt(1, idCounter);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastName);
            preparedStatement.setInt(4, age);

            preparedStatement.executeUpdate();

            idCounter++;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM users where UserID = ?"
            );

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM users";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                User currentUser = new User();

                currentUser.setId(resultSet.getLong("UserID"));
                currentUser.setName(resultSet.getString("Name"));
                currentUser.setLastName(resultSet.getString("LastName"));
                currentUser.setAge(resultSet.getByte("Age"));

                users.add(currentUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public void cleanUsersTable() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE users");

            preparedStatement.executeUpdate();

            idCounter = 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isCreated() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, "users", new String[]{"TABLE"});

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
