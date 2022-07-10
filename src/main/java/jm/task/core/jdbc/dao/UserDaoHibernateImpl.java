package jm.task.core.jdbc.dao;

//import jakarta.persistence.EntityNotFoundException;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;


import java.util.ArrayList;
import java.util.List;

// Методы создания и удаления таблицы пользователей в классе UserHibernateDaoImpl
// должны быть реализованы с помощью SQL.

// 1. Criteria не используем
// 2. Делаем ролбек в случае неудачной транзакции

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory sessionFactory;

    public UserDaoHibernateImpl() {
    }

    private void nativeSQL(String nativeSQL) {
        Session session = getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try (session) {
            NativeQuery<User> nativeQuery = session.createNativeQuery(nativeSQL, User.class);
            nativeQuery.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void createUsersTable() {
        String sqlCreateTable = """
                CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY ,
                name TEXT NOT NULL ,
                last_name TEXT NOT NULL ,
                age TINYINT NOT NULL )
                """;
        nativeSQL(sqlCreateTable);
    }

    @Override
    public void dropUsersTable() {
        String sqlDropTable = "DROP TABLE IF EXISTS users";
        nativeSQL(sqlDropTable);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try (session) {
            session.persist(user);
            transaction.commit();
            System.out.printf("User с именем – %s добавлен в базу данных\n", name);
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = getSessionFactory().getCurrentSession();
        Transaction transaction = null;
        User user;

        try (session) {
            transaction = session.beginTransaction();
            user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
            }
            transaction.commit();
        } catch (IllegalStateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.printf("user with this id = %d was not found\n", id);
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        Session session = getSessionFactory().getCurrentSession();
        Transaction transaction = session.beginTransaction();

        try (session) {
            users = session.createQuery("from User")
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void cleanUsersTable() {
        getAllUsers().forEach(user -> removeUserById(user.getId()));
    }

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            return Util.getSessionFactory();
        }
        return sessionFactory;
    }
}