package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection;
    private Statement statement;

    public UserDaoJDBCImpl() {
    }

    private void commandSQL(String sqlCommand) {
        try {
            getStatement().execute(sqlCommand);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует

    public void createUsersTable() {
        String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY ,
                name TEXT NOT NULL ,
                last_name TEXT NOT NULL ,
                age TINYINT NOT NULL )
                """;
        commandSQL(sqlCreateTable);
    }

    // Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
    public void dropUsersTable() {
        String sqlDropTable = "DROP TABLE IF EXISTS users";
        commandSQL(sqlDropTable);

    }

    // Добавление User в таблицу
    public void saveUser(String name, String lastName, byte age) {

        String sqlInsertInto = "INSERT INTO users (name, last_name, age) " +
                "VALUES ('" + name + "', '" + lastName + "' , " + age + ");";

        commandSQL(sqlInsertInto);
        System.out.printf("User с именем – %s добавлен в базу данных\n", name);

    }

    // Удаление User из таблицы (по id)
    public void removeUserById(long id) {
        String sqlDeleteUserFromId = "DELETE FROM users WHERE id = " + id;
        commandSQL(sqlDeleteUserFromId);

    }

    // Получение всех User(ов) из таблицы
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        String sqlGetAllUsers = "SELECT  * FROM users";

        try {
            ResultSet resultSet = statement.executeQuery(sqlGetAllUsers);

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getByte("age"));

                users.add(user);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return users;
    }

    // Очистка содержания таблицы
    public void cleanUsersTable() {
        String sqlCleanTable = "TRUNCATE users";
        commandSQL(sqlCleanTable);

    }

    private Statement getStatement() {
        try {
            if (statement == null || statement.isClosed()) {
                statement = getConnection().createStatement();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statement;
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = Util.getMySQLConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}