package com.astondev.app.dao.user;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.astondev.app.db.HibernateUtils;
import com.astondev.app.model.user.User;


public class UserDao {
    
    public void create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            System.out.println("User created: " + user.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public boolean deleteUserById(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
                System.out.println("User deleted: " + user.getName());
                return true;
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public User getUserById(long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                Hibernate.initialize(user);
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(User updatedUser) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            User user = session.find(User.class, updatedUser.getId());
            if (user != null) {
                user.setName(updatedUser.getName());
                user.setAge(updatedUser.getAge());
                user.setEmail(updatedUser.getEmail());
            }
            transaction.commit();
            System.out.println("User updated: " + updatedUser.getName());
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return false;
    }

    public List<User> findAllUsers() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
