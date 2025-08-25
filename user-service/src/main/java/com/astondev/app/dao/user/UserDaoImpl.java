package com.astondev.app.dao.user;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.*;
import org.hibernate.exception.ConstraintViolationException;

import com.astondev.app.exceptions.UserDaoException;
import com.astondev.app.model.user.User;
import com.astondev.app.utils.HibernateUtils;


public class UserDaoImpl implements UserDao {
    private static final Logger logger = Logger.getLogger(UserDaoImpl.class.getName());
    private SessionFactory sessionFactory;

    public UserDaoImpl() {
        this.sessionFactory = HibernateUtils.getSessionFactory();
    }

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


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
            throw new UserDaoException("User creation failed due to constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Hibernate error during user creation: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Error creating user: " + e.getMessage(), e);
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
        } catch (ConstraintViolationException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("User deletion failed due to constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Hibernate error during user deletion: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Error deleting user: " + e.getMessage(), e);
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
        } catch (ConstraintViolationException e) {
            throw new UserDaoException("User retrieval failed due to constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            throw new UserDaoException("Hibernate error during user retrieval: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserDaoException("Error retrieving user: " + e.getMessage(), e);
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
        } catch (ConstraintViolationException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("User update failed due to constraint violation: " + e.getMessage(), e);
        } catch (HibernateException e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Hibernate error during user update: " + e.getMessage(), e);
        } catch (Exception e) {
            rollbackIfActive(transaction);
            throw new UserDaoException("Error updating user: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUsers() throws UserDaoException{
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<User> users = session.createQuery("from User", User.class).list();
            return users;
        } catch (HibernateException e) {
            throw new UserDaoException("Hibernate error during user retrieval: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new UserDaoException("Error retrieving users: " + e.getMessage(), e);
        }
    }
    
    private void rollbackIfActive(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}

