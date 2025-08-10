package com.astondev.app;

import java.util.List;
import java.util.Scanner;

import com.astondev.app.dao.user.UserDaoImpl;
import com.astondev.app.model.user.User;
import com.astondev.app.utils.InputValidator;

public class UserConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDaoImpl userDao = new UserDaoImpl();

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
                Введите команду: """);
    }

    public void createUser() {
        System.out.println("Введите имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите email пользователя:");
        String email = scanner.nextLine();
        System.out.println("Введите возраст пользователя:");

        try {
            String ageInput = scanner.nextLine();
            if (!InputValidator.isValidAge(ageInput)) {
                System.out.println("Некорректный возраст. Пожалуйста, введите корректный возраст.");
                return;
            }
            Integer age = Integer.parseInt(ageInput);
            userDao.createUser(new User(name, email, age));

            System.out.println("Пользователь успешно добавлен.");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при добавлении пользователя: " + e.getMessage());
            return;
        }
    }

    public void deleteUser() {
        System.out.println("Введите ID пользователя для удаления:");
        try {
            Long userId = Long.parseLong(scanner.nextLine());

            boolean deleted = userDao.deleteUserById(userId);
            if (!deleted) {
                System.out.println("Пользователь с ID " + userId + " не найден.");
                return;
            }
            System.out.println("Пользователь с ID " + userId + " успешно удален.");
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ID пользователя. Пожалуйста, введите числовой ID.");
            return;
        } catch (Exception e) {
            System.out.println("Произошла ошибка при удалении пользователя: " + e.getMessage());
            return;
        }
    }

    public void updateUser() {
        System.out.println("Введите ID пользователя для изменения:");
        Long userId = Long.parseLong(scanner.nextLine());

        User user = userDao.getUserById(userId);
        if (user == null) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return;
        }

        System.out.println("Введите новое имя пользователя:");
        String name = scanner.nextLine();
        System.out.println("Введите новый email пользователя:");
        String email = scanner.nextLine();
        System.out.println("Введите новый возраст пользователя:");

        try {
            String ageInput = scanner.nextLine();
            if (!InputValidator.isValidAge(ageInput)) {
                System.out.println("Некорректный возраст. Пожалуйста, введите корректный возраст.");
                return;
            }
            Integer age = Integer.parseInt(ageInput);
            User updated = new User(
                    name.isEmpty() ? user.getName() : name,
                    email.isEmpty() ? user.getEmail() : email,
                    age
            );
            updated.setId(userId);

            userDao.updateUser(updated);
            System.out.println("Пользователь с ID " + userId + " успешно изменен.");
        } catch (Exception e) {
            System.out.println("Произошла ошибка при изменении пользователя: " + e.getMessage());
            return;
        }

    }

    public void showAllUsers() {
        List<User> userList = userDao.getAllUsers();
        if (userList != null && !userList.isEmpty()) {
            System.out.println("Список всех пользователей:");
            for (User user : userList) {
                System.out.println("Id "+ user.getId() + "\t Имя: " + user.getName() + "\t Email: " + user.getEmail() + "\t Возраст: " + user.getAge());
            }
        } else {
            System.out.println("Нет пользователей для отображения.");
        }
    }
}
