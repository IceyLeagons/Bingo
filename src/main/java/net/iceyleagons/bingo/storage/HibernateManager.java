package net.iceyleagons.bingo.storage;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.iceyleagons.bingo.storage.data.FreezedPlayer;
import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;

/**
 * @author TOTHTOMI
 */
public class HibernateManager {

    private static SessionFactory sessionFactory;
    @Setter
    @Getter
    private static DatabaseType databaseType = DatabaseType.MYSQL;
    @Getter
    @Setter
    private static DatabaseParams databaseParams;

    @Getter
    @Setter
    private static boolean enabled;

    @SneakyThrows
    public static SessionFactory getSessionFactory() {
        if (!isEnabled()) return null;
        if (getDatabaseParams() == null) {
            throw new RuntimeException("No Database Parameters have been given, please fill out the config file!");
        }
        if (getSessionFactory() == null) {
            Configuration configuration = new Configuration();

            Properties properties = new Properties();
            properties.put(Environment.DRIVER,getDatabaseType().getDriver());
            properties.put(Environment.URL,getDatabaseParams().getUrl());
            properties.put(Environment.USER,getDatabaseParams().getUsername());
            properties.put(Environment.PASS,getDatabaseParams().getPassword());
            properties.put(Environment.DIALECT,getDatabaseType().getDialect());
            properties.put(Environment.SHOW_SQL,"false");
            properties.put(Environment.CURRENT_SESSION_CONTEXT_CLASS,"thread");
            properties.put(Environment.HBM2DDL_AUTO,"create-drop");

            configuration.setProperties(properties);
            configuration.addAnnotatedClass(FreezedPlayer.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        }
        return sessionFactory;
    }

}
