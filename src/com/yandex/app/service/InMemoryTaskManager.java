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

    @Override
    public Task addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null.");
        }
        task.setId(++currentId);
        tasks.put(task.getId(), task);
        // Создаем список версий для задачи и добавляем первую версию
        taskVersions.put(task.getId(), new ArrayList<>());
        taskVersions.get(task.getId()).add(task);
        return task;
    }

    @Override
    public Epic addEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Эпик не может быть null.");
        }
        epic.setId(++currentId);
        epics.put(epic.getId(), epic);
        return epic;
    }

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

        // Создаем список версий для подзадачи и добавляем первую версию
        taskVersions.put(subtask.getId(), new ArrayList<>());
        taskVersions.get(subtask.getId()).add(subtask);
        return subtask;
    }

    @Override
    public List<Task> getAllTasks() {
        return List.copyOf(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return List.copyOf(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return List.copyOf(subtasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.remove(id) == null) {
            throw new IllegalArgumentException("Задача с ID " + id + " не найдена.");
        }
        taskVersions.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subtasks.remove(subtask.getId());
                taskVersions.remove(subtask.getId());
            }
        } else {
            throw new IllegalArgumentException("Эпик с ID " + id + " не найден.");
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null && subtask.getEpic() != null) {
            subtask.getEpic().removeSubtask(subtask.getId());
            taskVersions.remove(id);
        } else {
            throw new IllegalArgumentException("Подзадача с ID " + id + " не найдена.");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        taskVersions.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Subtask subtask : new ArrayList<>(subtasks.values())) {
            deleteSubtaskById(subtask.getId());
        }
        subtasks.clear();
        taskVersions.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
        taskVersions.clear();
    }

    @Override
    public void updateTask(Task task) {
        if (task == null || !tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Задача с ID " + (task != null ? task.getId() : "null") + " не существует.");
        }
        tasks.put(task.getId(), task);
        taskVersions.get(task.getId()).add(task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic == null || !epics.containsKey(epic.getId())) {
            throw new IllegalArgumentException("Эпик с ID " + (epic != null ? epic.getId() : "null") + " не существует.");
        }
        epics.put(epic.getId(), epic);
        epic.updateStatus();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask == null || !subtasks.containsKey(subtask.getId())) {
            throw new IllegalArgumentException("Подзадача с ID " + (subtask != null ? subtask.getId() : "null") + " не существует.");
        }
        subtasks.put(subtask.getId(), subtask);
        // Используем computeIfAbsent для получения списка версий
        taskVersions.computeIfAbsent(subtask.getId(), k -> new ArrayList<>()).add(subtask);
        subtask.getEpic().updateStatus();
    }

    @Override
    public List<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic != null) {
            return new ArrayList<>(epic.getSubtasks());
        } else {
            throw new IllegalArgumentException("Эпик с ID " + epicId + " не существует.");
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public List<Task> getTaskVersions(int taskId) {
        return taskVersions.getOrDefault(taskId, new ArrayList<>());
    }

    @Override
    public int getSubtasksCount() {
        return subtasks.size(); // Возвращаем количество подзадач
    }

    @Override
    public int getEpicsCount() {
        return epics.size(); // Возвращаем количество эпиков
    }

    @Override
    public int getTasksCount() {
        return tasks.size(); // Возвращаем количество задач
    }
}
