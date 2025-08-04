package com.astondev.app.db;

import java.util.logging.Logger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
    private static final Logger logger = Logger.getLogger(HibernateUtils.class.getName());
    private static final SessionFactory sessionFactory;

    static {
        try {
            logger.info("Initializing Hibernate SessionFactory...");
            sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();
            logger.info("Hibernate SessionFactory initialized successfully.");
        } catch (Exception e) {
            logger.severe("Initial SessionFactory creation failed." + e);
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static Session openSession() {
        return getSessionFactory().openSession();
    }
}
