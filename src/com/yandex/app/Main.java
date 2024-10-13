package com.yandex.app;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.service.HistoryManager;
import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;

public class Main {
    public static void main(String[] args) {
        // Используем фабрику для получения объекта HistoryManager
        HistoryManager historyManager = Managers.getDefaultHistory();

        // Используем фабрику для получения объекта TaskManager
        TaskManager manager = Managers.getDefault();


        // Создание задач, эпиков и подзадач
        Task task1 = new Task("Переезд", "Собрать коробки, упаковать кошку, сказать слова прощания", Status.NEW);
        manager.addTask(task1);

        Epic epic1 = new Epic("Важный эпик 2", "Описание эпика 2");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Задача 1", "Описание задачи 1", Status.NEW, epic1);
        Subtask subtask2 = new Subtask("Задача 2", "Описание задачи 2", Status.NEW, epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        // Вывод всех задач, эпиков и подзадач
        System.out.println("Список всех задач:");
        System.out.println(manager.getAllTasks());

        System.out.println("Список всех эпиков:");
        System.out.println(manager.getAllEpics());

        System.out.println("Список всех подзадач:");
        System.out.println(manager.getAllSubtasks());

        // Изменение заголовков, описаний и статусов задач
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setTitle("Обновленная задача 1");
        subtask1.setDescription("Обновленное описание задачи 1");

        subtask2.setStatus(Status.DONE);
        subtask2.setTitle("Обновленная задача 2");
        subtask2.setDescription("Обновленное описание задачи 2");

        // Обновление задач в менеджере
        manager.updateSubtask(subtask1);
        manager.updateSubtask(subtask2);

        // Вывод обновленных данных
        System.out.println("Обновленные задачи и эпики:");
        System.out.println(subtask1);
        System.out.println(subtask2);
        System.out.println(manager.getEpicById(epic1.getId()));

        // Получение списка подзадач эпика
        System.out.println("Список подзадач эпика с ID " + epic1.getId() + ":");
        System.out.println(manager.getSubtasksOfEpic(epic1.getId()));

        // Удаление подзадачи, задачи и эпика по ID
        manager.deleteSubtaskById(subtask1.getId());
        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic1.getId());

        // Проверка удаления
        System.out.println("После удаления:");
        System.out.println("Список всех задач:");
        System.out.println(manager.getAllTasks());

        System.out.println("Список всех эпиков:");
        System.out.println(manager.getAllEpics());

        System.out.println("Список всех подзадач:");
        System.out.println(manager.getAllSubtasks());

        // Просмотр задач для проверки истории!
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getSubtaskById(subtask1.getId());

        // Создаем 11 задач и добавляем их в историю!
        for (int i = 1; i <= 11; i++) {
            Task task = new Task("Задача " + i, "Описание задачи " + i, Status.NEW);
            manager.addTask(task);
            manager.getTaskById(task.getId());
        }

        // Вывод истории просмотров!
        System.out.println("История просмотров:");
        System.out.println(manager.getHistory());

        // Проверка, что в истории не более 10 элементов!
        if (manager.getHistory().size() <= 10) {
            System.out.println("История содержит не более 10 элементов, как и ожидалось.");
        } else {
            System.out.println("Ошибка: История содержит более 10 элементов.");
        }
    }
}