package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соединения с БД

    private static Connection connection = null;
    private static SessionFactory sessionFactory;

    private static final String PASSWORD = "root1234";
    private static final String USER_NAME = "root";
    private static final String URL = "jdbc:mysql://localhost:3306/test";

    public static Connection getMySQLConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
        properties.setProperty(Environment.URL, URL);
        properties.setProperty(Environment.USER, USER_NAME);
        properties.setProperty(Environment.PASS, PASSWORD);
        properties.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
        properties.setProperty(Environment.SHOW_SQL, "true");
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        return properties;
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.setProperties(hibernateProperties());
        configuration.addAnnotatedClass(User.class);

        ServiceRegistry registry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        if (sessionFactory == null) {
            sessionFactory = configuration.buildSessionFactory(registry);
        }
        return sessionFactory;
    }
}