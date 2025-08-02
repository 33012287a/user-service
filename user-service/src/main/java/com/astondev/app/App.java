package com.astondev.app;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        System.out.println("SessionFactory created: " + sessionFactory);
    }
}
