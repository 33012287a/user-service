package com.astondev.app.service;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.astondev.app.dao.user.UserDaoImpl;
import com.astondev.app.exceptions.UserDaoException;
import com.astondev.app.model.user.User;
import com.astondev.app.utils.InputValidator;

public class UserServiceApp {
    private static final Logger logger = Logger.getLogger(UserServiceApp.class.getName());
    private final Scanner scanner;
    private final UserDaoImpl userDao;

    public UserServiceApp() {
        this(new Scanner(System.in), new UserDaoImpl());
    }

    public UserServiceApp(Scanner scanner, UserDaoImpl userDaoImpl) {
        this.scanner = scanner;
        this.userDao = userDaoImpl;
    }

    public void start() {
        while(true) {
            printMenu();
            String input = scanner.nextLine().trim();
            switch (input) {
                case "1" -> createUser();
                case "2" -> deleteUser();
                case "3" -> updateUser();
                case "4" -> showAllUsers();
                case "5" -> {
                    System.out.println("Выход из приложения...");
                    return;
                }
                default -> System.out.println("Неверный ввод, попробуйте еще раз.");
            }
        }
    }

    public void printMenu() {
        System.out.print("""
                ========== МЕНЮ ===========
                1 - Добавить пользователя
                2 - Удалить пользователя
                3 - Изменить пользователя
                4 - Показать всех пользователей
                5 - Выход
                ===========================
                Введите команду:  """);
    }

    public boolean createUser() {
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите email пользователя:");
        String email = scanner.nextLine();
        System.out.println("Введите возраст пользователя:");

        try {
            String ageInput = scanner.nextLine();
            if (!InputValidator.isValidAge(ageInput)) {
                System.out.println("Некорректный возраст. Пожалуйста, введите корректный возраст.");
                return false;
            }
            Integer age = Integer.parseInt(ageInput);
            boolean created = userDao.createUser(new User(name, email, age));
            if (created) {
                System.out.println("Пользователь успешно добавлен.");
            } else {
                System.out.println("Не удалось добавить пользователя.");
            }
            return created;
        } catch (UserDaoException e) {
            logger.log(Level.WARNING, "Ошибка базы данных при добавлении пользователя", e);
            System.out.println("Ошибка базы данных: " + e.getMessage());
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Неизвестная ошибка при добавлении пользователя", e);
            System.out.println("Произошла ошибка при добавлении пользователя: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteUser() {
        System.out.println("Введите ID пользователя для удаления:");
        String inputId = scanner.nextLine();

        if (!InputValidator.isValidId(inputId)) {
            System.out.println("Некорректный ID пользователя.");
            return false;
        }
        try {
            long userId = Long.parseLong(inputId);
            boolean deleted = userDao.deleteUserById(userId);
            if (deleted) {
                System.out.println("Пользователь с ID " + userId + " успешно удален.");
            } else {
                System.out.println("Пользователь с ID " + userId + " не найден.");
            }
            return deleted;
        } catch (UserDaoException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Произошла ошибка при удалении пользователя: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUser() {
        System.out.println("Введите ID пользователя для изменения:");
        String inputId = scanner.nextLine();
        if (!InputValidator.isValidId(inputId)) {
            System.out.println("Некорректный ID пользователя.");
            return false;
        }
        try {
            long userId = Long.parseLong(inputId);
            User user = userDao.getUserById(userId);
            System.out.println("Введите новое имя пользователя:");
            String name = scanner.nextLine();
            System.out.println("Введите новый email пользователя:");
            String email = scanner.nextLine();
            System.out.println("Введите новый возраст пользователя:");
            String ageInput = scanner.nextLine();
            if (!InputValidator.isValidAge(ageInput)) {
                System.out.println("Некорректный возраст.");
                return false;
            }
            Integer age = Integer.parseInt(ageInput);
            User updated = new User(
                    name.isEmpty() ? user.getName() : name,
                    email.isEmpty() ? user.getEmail() : email,
                    age
            );
            updated.setId(userId);

            boolean updateOk = userDao.updateUser(updated);
            if (updateOk) {
                System.out.println("Пользователь с ID " + userId + " успешно изменен.");
                return true;
            } else {
                System.out.println("Не удалось изменить пользователя с ID " + userId + ".");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ID пользователя. Пожалуйста, введите числовой ID.");
            return false;
        } catch (UserDaoException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Произошла ошибка при изменении пользователя: " + e.getMessage());
            return false;
        }
    }

    public void showAllUsers() {
        try {
            List<User> userList = userDao.getAllUsers();
            if (userList != null && !userList.isEmpty()) {
                System.out.println("Список всех пользователей:");
                for (User user : userList) {
                    System.out.println("Id "+ user.getId() +
                                " | Имя: " + user.getName() +
                                " | Email: " + user.getEmail() +
                                " | Возраст: " + user.getAge());
            }
        } else {
            System.out.println("Нет пользователей для отображения.");
        }
    } catch (UserDaoException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Произошла ошибка при получении списка пользователей: " + e.getMessage());
            return;
        }
    }
}
