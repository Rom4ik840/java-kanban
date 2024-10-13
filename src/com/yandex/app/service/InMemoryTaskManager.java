package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Реализация интерфейса TaskManager для управления задачами, эпиками и подзадачами в памяти.
public class InMemoryTaskManager implements TaskManager {

    // Текущий идентификатор для задач, эпиков и подзадач.
    private int currentId = 0;

    // Карта для хранения задач по их идентификаторам.
    private final Map<Integer, Task> tasks = new HashMap<>();

    // Карта для хранения эпиков по их идентификаторам.
    private final Map<Integer, Epic> epics = new HashMap<>();

    // Карта для хранения подзадач по их идентификаторам.
    private final Map<Integer, Subtask> subtasks = new HashMap<>();

    // Менеджер истории просмотров задач.
    private final HistoryManager historyManager;

    // Конструктор класса InMemoryTaskManager.
    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    // Добавляет задачу в менеджер.
    @Override
    public Task addTask(Task task) {
        task.setId(++currentId);
        tasks.put(task.getId(), task);
        return task;
    }

    // Добавляет эпик в менеджер.
    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(++currentId);
        epics.put(epic.getId(), epic);
        return epic;
    }

    // Добавляет подзадачу в менеджер.
    @Override
    public Subtask addSubtask(Subtask subtask) {
        subtask.setId(++currentId);
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtask(subtask);
        return subtask;
    }

    // Возвращает список всех задач.
    @Override
    public List<Task> getAllTasks() {
        return List.copyOf(tasks.values());
    }

    // Возвращает список всех эпиков.
    @Override
    public List<Epic> getAllEpics() {
        return List.copyOf(epics.values());
    }

    // Возвращает список всех подзадач.
    @Override
    public List<Subtask> getAllSubtasks() {
        return List.copyOf(subtasks.values());
    }

    // Возвращает задачу по её идентификатору.
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    // Возвращает эпик по его идентификатору.
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    // Возвращает подзадачу по её идентификатору.
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    // Удаляет задачу по её идентификатору.
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    // Удаляет эпик по его идентификатору.
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    // Удаляет подзадачу по её идентификатору.
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            subtask.getEpic().removeSubtask(subtask);
        }
    }

    // Удаляет все задачи.
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удаляет все подзадачи.
    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().removeSubtask(subtask);
        }
        subtasks.clear();
    }

    // Удаляет все эпики и подзадачи.
    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // Обновляет задачу.
    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            throw new IllegalArgumentException("Задача с ID " + task.getId() + " не существует.");
        }
    }

    // Обновляет эпик.
    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            epic.updateStatus();
        } else {
            throw new IllegalArgumentException("Эпик с ID " + epic.getId() + " не существует.");
        }
    }

    // Обновляет подзадачу!
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            subtask.getEpic().updateStatus();
        } else {
            throw new IllegalArgumentException("Подзадача с ID " + subtask.getId() + " не существует.");
        }
    }

    // Возвращает список подзадач эпика!
    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        } else {
            throw new IllegalArgumentException("Эпик с ID " + epicId + " не существует.");
        }
    }

    // Возвращает историю просмотров задач.
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}