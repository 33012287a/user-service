package com.astondev.app.dao.user;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.astondev.app.db.HibernateUtils;
import com.astondev.app.model.user.User;


public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class.getName());
    
    public void create(User user) {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created: " + user.getName());
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.severe("Error creating user: " + e.getMessage());
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
                logger.info("User deleted: " + user.getName());
                return true;
            } else {
                logger.warning("User with ID " + id + " not found.");
            }
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.severe("Error deleting user: " + e.getMessage());
        }
        return false;
    }

    public User getUserById(long id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                Hibernate.initialize(user);
                return user;
            } else {
                logger.warning("User with ID " + id + " not found.");
            }
        } catch (Exception e) {
            logger.severe("Error retrieving user: " + e.getMessage());
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
            logger.info("User updated: " + updatedUser.getName());
            return true;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            logger.severe("Error updating user: " + e.getMessage());
        }
        return false;
    }

    public List<User> findAllUsers() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            return users;
        } catch (Exception e) {
            logger.severe("Error retrieving all users: " + e.getMessage());
        }
        return null;
    }

}
