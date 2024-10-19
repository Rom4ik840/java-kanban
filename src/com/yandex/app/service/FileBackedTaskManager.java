package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    // Конструктор для инициализации менеджера с файлом и менеджером истории
    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    // Сохраняем текущие задачи и историю в файл
    public void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,epicId\n");
            for (Task task : getAllTasks()) {
                sb.append(taskToString(task)).append("\n");
            }
            for (Epic epic : getAllEpics()) {
                sb.append(epicToString(epic)).append("\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                sb.append(subtaskToString(subtask)).append("\n");
            }
            Files.write(file.toPath(), sb.toString().getBytes());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных в файл: " + e.getMessage());
        }
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save(); // Сохраняем данные после добавления задачи
        return task;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save(); // Сохраняем данные после добавления подзадачи
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save(); // Сохраняем данные после добавления эпика
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save(); // Сохраняем данные после обновления задачи
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save(); // Сохраняем данные после обновления подзадачи
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save(); // Сохраняем данные после обновления эпика
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save(); // Сохраняем данные после удаления задачи
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save(); // Сохраняем данные после удаления подзадачи
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save(); // Сохраняем данные после удаления эпика
    }

    // Преобразуем задачу в строку для сохранения в файл
    private String taskToString(Task task) {
        return String.format("%d,TASK,%s,%s,%s,",
                task.getId(), task.getTitle(), task.getStatus(), task.getDescription());
    }

    // Преобразуем эпик в строку для сохранения в файл
    private String epicToString(Epic epic) {
        return String.format("%d,EPIC,%s,%s,%s,",
                epic.getId(), epic.getTitle(), epic.getStatus(), epic.getDescription());
    }

    // Преобразуем подзадачу в строку для сохранения в файл
    private String subtaskToString(Subtask subtask) {
        return String.format("%d,SUBTASK,%s,%s,%s,%d",
                subtask.getId(), subtask.getTitle(), subtask.getStatus(), subtask.getDescription(), subtask.getEpic().getId());
    }

    // Загружаем задачи из файла
    public void loadFromFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                if (task instanceof Epic) {
                    addEpic((Epic) task); // добавляем эпик
                } else if (task instanceof Subtask) {
                    addSubtask((Subtask) task); // добавляем подзадачу
                } else {
                    addTask(task); // добавляем обычную задачу
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки данных из файла: " + e.getMessage());
        }
    }

    // Преобразуем строку обратно в объект задачи
    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        String type = parts[1];
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, status);
                task.setId(id); // сохраняем id задачи
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id); // сохраняем id эпика
                return epic;
            case "SUBTASK":
                int epicId = Integer.parseInt(parts[5]);
                Epic epicForSubtask = getEpicById(epicId);
                Subtask subtask = new Subtask(name, description, status, epicForSubtask);
                subtask.setId(id); // сохраняем id подзадачи
                return subtask;
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип задачи: " + type);
        }
    }

    // Статический метод для загрузки задач из файла
    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, historyManager);
        manager.loadFromFile(file); // Вызов метода для загрузки задач
        return manager;
    }
}
