package org.vieseltodo;

import org.vieseltodo.database.DatabaseHandler;
import org.vieseltodo.model1.Task;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final DatabaseHandler dbHandler = new DatabaseHandler();

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            printMenu();
            System.out.print("Выберите действие: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addTask();
                case "2" -> showAllTasks();
                case "3" -> showTaskById();
                case "4" -> updateTask();
                case "5" -> deleteTask();
                case "6" -> {
                    exit = true;
                    System.out.println("Выход из программы. Пока!");
                }
                default -> System.out.println("Неверный выбор. Попробуйте ещё раз.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Меню ---");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Показать все задачи");
        System.out.println("3. Показать задачу по ID");
        System.out.println("4. Обновить задачу");
        System.out.println("5. Удалить задачу");
        System.out.println("6. Выход");
    }

    private static void addTask() {
        System.out.print("Введите название задачи: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Название не может быть пустым.");
            return;
        }

        System.out.print("Введите описание задачи: ");
        String description = scanner.nextLine().trim();

        Task task = new Task(title, description, "NEW");
        dbHandler.addTask(task);
    }

    private static void showAllTasks() {
        List<Task> tasks = dbHandler.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Задач нет.");
            return;
        }
        System.out.println("\nСписок всех задач:");
        for (Task t : tasks) {
            System.out.printf("ID: %d | Название: %s | Статус: %s | Создана: %s%n",
                    t.getId(), t.getTitle(), t.getStatus(), t.getCreatedAt());
        }
    }

    private static void showTaskById() {
        System.out.print("Введите ID задачи: ");
        int id = readInt();
        if (id == -1) return;

        Task task = dbHandler.getTaskById(id);
        if (task == null) {
            System.out.println("Задача с таким ID не найдена.");
        } else {
            System.out.println(task);
        }
    }

    private static void updateTask() {
        System.out.print("Введите ID задачи для обновления: ");
        int id = readInt();
        if (id == -1) return;

        Task existingTask = dbHandler.getTaskById(id);
        if (existingTask == null) {
            System.out.println("Задача с таким ID не найдена.");
            return;
        }

        System.out.print("Новое название (оставьте пустым, чтобы не менять): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) {
            existingTask.setTitle(title);
        }

        System.out.print("Новое описание (оставьте пустым, чтобы не менять): ");
        String description = scanner.nextLine().trim();
        if (!description.isEmpty()) {
            existingTask.setDescription(description);
        }

        System.out.print("Новый статус (NEW, IN_PROGRESS, DONE) (оставьте пустым, чтобы не менять): ");
        String status = scanner.nextLine().trim().toUpperCase();
        if (!status.isEmpty()) {
            if (status.equals("NEW") || status.equals("IN_PROGRESS") || status.equals("DONE")) {
                existingTask.setStatus(status);
            } else {
                System.out.println("Неверный статус. Статус не изменён.");
            }
        }

        dbHandler.updateTask(existingTask);
    }

    private static void deleteTask() {
        System.out.print("Введите ID задачи для удаления: ");
        int id = readInt();
        if (id == -1) return;

        dbHandler.deleteTask(id);
    }

    private static int readInt() {
        String input = scanner.nextLine();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод. Операция отменена.");
            return -1;
        }
    }
}
