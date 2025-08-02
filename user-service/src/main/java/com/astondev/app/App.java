package com.astondev.app;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class App {
    private final SessionFactory sessionFactory;

    public App() {
        this.sessionFactory = new Configuration()
            .configure()
            .buildSessionFactory();
    }

    public List<Users> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Users", Users.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addUser(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            session.flush();
            transaction.commit();
            System.out.println("Добавлен новый юзер: " + user.getName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Users getUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Users user = session.find(Users.class, id);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean saveUser(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public boolean removeUser(Users user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }
    

    public static void main(String[] args) {
        App app = new App();

        Users newUser = new Users("Petr", "asqq@ss.com");
        app.addUser(newUser);
        System.out.println(newUser.toString());

        List<Users> users = app.getAllUsers();
        if (users != null) {
            for (Users user : users) {
                System.out.println("User ID: " + user.getId() + ", Name: " + user.getName() + ", Email: " + user.getEmail());
            }
        }
    }
}
