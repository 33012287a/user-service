package com.astondev.app.repository;

import java.util.List;
import java.util.Scanner;

import com.astondev.app.dao.user.UserDao;
import com.astondev.app.model.user.User;

public class UserConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final UserDao userDao = new UserDao();

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
        System.out.println("""
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
        Integer age = Integer.parseInt(scanner.nextLine());

        userDao.create(new User(name, email, age));
        System.out.println("Пользователь успешно добавлен.");
    }

    public void deleteUser() {
        System.out.println("Введите ID пользователя для удаления:");
        Long userId = Long.parseLong(scanner.nextLine());
        boolean deleted = userDao.deleteUserById(userId);
        if (!deleted) {
            System.out.println("Пользователь с ID " + userId + " не найден.");
            return;
        }
        System.out.println("Пользователь с ID " + userId + " успешно удален.");
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
        Long age = Long.parseLong(scanner.nextLine());

        User updated = new User(userId,
                name.isEmpty() ? user.getName() : name,
                email.isEmpty() ? user.getEmail() : email,
                age.intValue() >= 0 ? user.getAge() : age.intValue());

        userDao.updateUser(updated);
        System.out.println("Пользователь с ID " + userId + " успешно изменен.");
    }

    public void showAllUsers() {
        List<User> userList = userDao.findAllUsers();
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
