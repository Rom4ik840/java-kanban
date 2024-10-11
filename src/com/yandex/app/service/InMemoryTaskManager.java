package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private int currentId = 0;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, List<Task>> taskVersions = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    // Добавляет новую задачу в менеджер задач.
    @Override
    public Task addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null.");
        }
        task.setId(++currentId);
        tasks.put(task.getId(), task);
        taskVersions.put(task.getId(), new ArrayList<>());
        taskVersions.get(task.getId()).add(task);
        return task;
    }

    // Добавляет новый эпик в менеджер задач.
    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Эпик не может быть null.");
        }
        epic.setId(++currentId);
        epics.put(epic.getId(), epic);
        return epic;
    }

    // Добавляет новую подзадачу в менеджер задач.
    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("Подзадача не может быть null.");
        }
        if (subtask.getEpic() == null || !epics.containsKey(subtask.getEpic().getId())) {
            throw new IllegalArgumentException("Эпик для подзадачи не существует.");
        }
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

    // Возвращает задачу по её ID.
    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    // Возвращает эпик по его ID.
    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    // Возвращает подзадачу по её ID.
    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    // Удаляет задачу по её ID.
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    // Удаляет эпик по его ID. Также удаляются все подзадачи, связанные с этим эпиком.
    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    // Удаляет подзадачу по её ID.
    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null && subtask.getEpic() != null) {
            subtask.getEpic().removeSubtask(subtask.getId());
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
        for (Subtask subtask : new ArrayList<>(subtasks.values())) {
            deleteSubtaskById(subtask.getId());
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
        if (task == null || !tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Задача с ID " + (task != null ? task.getId() : "null") + " не существует.");
        }
        tasks.put(task.getId(), task);
        taskVersions.get(task.getId()).add(task);
    }

    // Обновляет эпик.
    @Override
    public void updateEpic(Epic epic) {
        if (epic == null || !epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Эпик с ID " + (epic != null ? epic.getId() : "null") + " не существует.");
        }
        epics.put(epic.getId(), epic);
        epic.updateStatus();
    }

    // Обновляет подзадачу.
    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null || !subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача с ID " + (subtask != null ? subtask.getId() : "null") + " не существует.");
        }
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().updateStatus();
    }

    // Возвращает список подзадач эпика.
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

    // Возвращает все версии задачи по её ID.
    public List<Task> getTaskVersions(int taskId) {
        return taskVersions.getOrDefault(taskId, new ArrayList<>());
    }
}