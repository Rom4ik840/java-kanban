package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.List;

public interface TaskManager {
    // Создание задачи!
    Task addTask(Task task);

    // Создание эпика!
    Epic addEpic(Epic epic);

    // Создание подзадачи!
    Subtask addSubtask(Subtask subtask);

    // Получение всех задач!
    List<Task> getAllTasks();

    // Получение всех эпиков!
    List<Epic> getAllEpics();

    // Получение всех подзадач!
    List<Subtask> getAllSubtasks();

    // Получение задачи по ID!
    Task getTaskById(int id);

    // Получение эпика по ID!
    Epic getEpicById(int id);

    // Получение подзадачи по ID!
    Subtask getSubtaskById(int id);

    // Удаление задачи по ID!
    void deleteTaskById(int id);

    // Удаление эпика по ID (вместе с подзадачами)!
    void deleteEpicById(int id);

    // Удаление подзадачи по ID!
    void deleteSubtaskById(int id);

    // Удаление всех задач!
    void deleteAllTasks();

    // Удаление всех подзадач!
    void deleteAllSubtasks();

    // Удаление всех эпиков!
    void deleteAllEpics();

    // Обновление задачи!
    void updateTask(Task task);

    // Обновление эпика!
    void updateEpic(Epic epic);

    // Обновление подзадачи!
    void updateSubtask(Subtask subtask);

    // Получение списка подзадач для эпика по ID!
    List<Subtask> getSubtasksOfEpic(int epicId);

    // Получение истории просмотров задач!
    List<Task> getHistory();
}