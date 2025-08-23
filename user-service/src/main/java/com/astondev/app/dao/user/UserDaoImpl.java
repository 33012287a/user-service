package com.astondev.app.dao.user;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.astondev.app.exceptions.UserDaoException;
import com.astondev.app.model.user.User;
import com.astondev.app.utils.HibernateUtils;


public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());
    
    public boolean createUser(User user) throws UserDaoException{
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
            logger.info("User created: " + user.getName());
            return true;
        } catch (ConstraintViolationException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Нарушение уникальности при создании пользователя: " + e.getMessage(), e);
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Ошибка работы с Hibernate при создании пользователя: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Неизвестная ошибка при создании пользователя: " + e.getMessage(), e);
        }
    }

    public boolean deleteUserById(Long id) throws UserDaoException {
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
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Ошибка Hibernate при удалении пользовател: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Неизвестная ошибка при удалении пользователя: " + e.getMessage(), e);
        }
        return false;
    }

    public User getUserById(long id) throws UserDaoException {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);
            if (user != null) {
                Hibernate.initialize(user);
                return user;
            } else {
                logger.warning("User with ID " + id + " not found.");
                }
        } catch (HibernateException e) {
            throw new UserDaoException("Ошибка Hibernate при получении пользователя: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserDaoException("Неизвестная ошибка при получении пользователя: " + e.getMessage(), e);
        }
        return null;
    }

    public boolean updateUser(User updatedUser) throws UserDaoException {
        Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, updatedUser.getId());
            session.merge(updatedUser);
            transaction.commit();
            logger.info("User updated: " + updatedUser.getName());
            return true;   
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Ошибка Hibernate при обновлении пользователя: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Неизвестная ошибка при обновлении пользователя: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() throws UserDaoException{
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            return users;
        } catch (HibernateException e) {
            throw new UserDaoException("Ошибка Hibernate при получении списка пользователей: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserDaoException("Неизвестная ошибка при получении списка пользователей: " + e.getMessage(), e);
        }
    }
    
    private void rollbackIfActive(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}

