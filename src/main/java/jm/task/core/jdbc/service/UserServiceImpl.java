package jm.task.core.jdbc.service;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserDaoHibernateImpl userDaoHibernate = new UserDaoHibernateImpl();

    // Создание таблицы для User(ов) – не должно приводить к исключению, если такая таблица уже существует
    public void createUsersTable() {
        userDaoHibernate.createUsersTable();
    }

    // Удаление таблицы User(ов) – не должно приводить к исключению, если таблицы не существует
    public void dropUsersTable() {
        userDaoHibernate.dropUsersTable();
    }

    // Добавление User в таблицу
    public void saveUser(String name, String lastName, byte age) {
        userDaoHibernate.saveUser(name, lastName, age);
    }

    // Удаление User из таблицы (по id)
    public void removeUserById(long id) {
        userDaoHibernate.removeUserById(id);
    }

    // Получение всех User(ов) из таблицы
    public List<User> getAllUsers() {
        return userDaoHibernate.getAllUsers();
    }

    // Очистка содержания таблицы
    public void cleanUsersTable() {
        userDaoHibernate.cleanUsersTable();
    }
}