package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getConnection;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection = getConnection();

    public UserDaoJDBCImpl() {

    }


    @Override
    public void createUsersTable() {

        if (connection == null) {
            System.err.println("ОШИБКА: соединение с базой данных не установлено");
            return;
        }

        String sql = "CREATE TABLE IF NOT EXISTS USER (" +
                "ID INT PRIMARY KEY AUTO_INCREMENT, " +
                "NAME VARCHAR(45) NOT NULL, " +
                "LASTNAME VARCHAR(45) NOT NULL, " +
                "AGE TINYINT" +
                ")";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица USER успешно создана или уже существует");

        } catch (SQLException e) {
            System.err.println("ОШИБКА при создании таблицы: " + e.getMessage());
            e.printStackTrace();

        }
    }

    @Override
    public void dropUsersTable() {

        if (connection == null) {
            System.err.println("ОШИБКА: соединение с базой данных не установлено");
            return;
        }

        String sql = "DROP TABLE IF EXISTS USER";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.executeUpdate();
            System.out.println("Таблица USER успешно удалена или не существовала");

        } catch (SQLException e) {
            System.err.println("ОШИБКА при удалении таблицы: " + e.getMessage());
            e.printStackTrace();

        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {


        String sql = "INSERT INTO USER (NAME, LASTNAME, AGE) VALUES(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User с именем - " + name + " добавлен в базу данных");

    }

    @Override
    public void removeUserById(long id) {

        if (connection == null) {
            System.err.println("ОШИБКА: соединение с базой данных не установлено");
            return;
        }

        String sql = "DELETE FROM USER WHERE ID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setLong(1, id);

            int resDrop = preparedStatement.executeUpdate();

            if (resDrop > 0) {
                System.out.println("Пользователь с ID " + id + " успешно удален");
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
            }

        } catch (SQLException e) {
            System.err.println("ОШИБКА при удалении пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        if (connection == null) {
            System.err.println("ОШИБКА: соединение с базой данных не установлено");
            return userList;
        }

        String sql = "SELECT ID, NAME, LASTNAME, AGE FROM USER";

        try (ResultSet resultSet = connection.createStatement().executeQuery(sql)){

            while (resultSet.next()) {
                User user = new User();

                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setAge(resultSet.getByte("AGE"));
                userList.add(user);
            }

            System.out.println("Получено " + userList.size() + " пользователей из базы данных");

        } catch (SQLException e) {
            System.err.println("ОШИБКА при получении списка пользователей: " + e.getMessage());
            e.printStackTrace();

        }
        System.out.println("Все пользователи:");
        userList.forEach(System.out::println);

        return userList;
    }

    @Override
    public void cleanUsersTable() {


        if (connection == null) {
            System.err.println("ОШИБКА: соединение с базой данных не установлено");
            return;
        }

        String sql = "TRUNCATE TABLE USER";

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
            System.out.println("Таблица USER успешно очищена");

        } catch (SQLException e) {
            System.err.println("ОШИБКА при очистке таблицы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        Util.closeConnection(connection);
    }
}
