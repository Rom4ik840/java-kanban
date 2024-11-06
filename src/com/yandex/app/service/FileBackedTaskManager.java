package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import com.yandex.app.model.Status;
import com.yandex.app.service.TaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.Comparator;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder())));

    // Конструктор для инициализации менеджера с файлом и менеджером истории
    public FileBackedTaskManager(File file, HistoryManager historyManager) {
        super(historyManager);
        this.file = file;
    }

    // Сохраняем текущие задачи и историю в файл
    public void save() {
        try {
            // Проверяем, существует ли файл. Если нет, создаем новый файл.
            if (!file.exists()) {
                file.createNewFile();
            }

            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,duration,startTime,endTime,epicId\n");
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
        if (task.getStartTime() != null && task.getEndTime() != null) {
            if (isOverlapping(task)) {
                throw new IllegalArgumentException("Задача пересекается с другими задачами по времени выполнения.");
            }
        }
        Task addedTask = super.addTask(task);
        addToPrioritizedTasks(addedTask);
        save(); // Сохраняем данные после добавления задачи
        return addedTask;
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getEndTime() != null) {
            if (isOverlapping(subtask)) {
                throw new IllegalArgumentException("Подзадача пересекается с другими задачами по времени выполнения.");
            }
        }
        Subtask addedSubtask = super.addSubtask(subtask);
        addToPrioritizedTasks(addedSubtask);
        save(); // Сохраняем данные после добавления подзадачи
        return addedSubtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        Epic addedEpic = super.addEpic(epic);
        save(); // Сохраняем данные после добавления эпика
        return addedEpic;
    }

    @Override
    public void updateTask(Task task) {
        if (task.getStartTime() != null && task.getEndTime() != null) {
            if (isOverlapping(task)) {
                throw new IllegalArgumentException("Задача пересекается с другими задачами по времени выполнения.");
            }
        }
        super.updateTask(task);
        updatePrioritizedTasks(task);
        save(); // Сохраняем данные после обновления задачи
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask.getStartTime() != null && subtask.getEndTime() != null) {
            if (isOverlapping(subtask)) {
                throw new IllegalArgumentException("Подзадача пересекается с другими задачами по времени выполнения.");
            }
        }
        super.updateSubtask(subtask);
        updatePrioritizedTasks(subtask);
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
        removeFromPrioritizedTasks(id);
        save(); // Сохраняем данные после удаления задачи
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        removeFromPrioritizedTasks(id);
        save(); // Сохраняем данные после удаления подзадачи
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = getEpicById(id);
        if (epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                removeFromPrioritizedTasks(subtask.getId());
            }
        }
        super.deleteEpicById(id);
        save(); // Сохраняем данные после удаления эпика
    }

    // Преобразуем задачу в строку для сохранения в файл
    private String taskToString(Task task) {
        return String.format("%d,TASK,%s,%s,%s,%d,%s,%s,",
                task.getId(), task.getTitle(), task.getStatus(), task.getDescription(),
                task.getDuration() != null ? task.getDuration().toMinutes() : "null",
                task.getStartTime() != null ? task.getStartTime().toString() : "null",
                task.getEndTime() != null ? task.getEndTime().toString() : "null");
    }

    // Преобразуем эпик в строку для сохранения в файл
    private String epicToString(Epic epic) {
        return String.format("%d,EPIC,%s,%s,%s,%d,%s,%s,",
                epic.getId(), epic.getTitle(), epic.getStatus(), epic.getDescription(),
                epic.getDuration() != null ? epic.getDuration().toMinutes() : "null",
                epic.getStartTime() != null ? epic.getStartTime().toString() : "null",
                epic.getEndTime() != null ? epic.getEndTime().toString() : "null");
    }

    // Преобразуем подзадачу в строку для сохранения в файл
    private String subtaskToString(Subtask subtask) {
        return String.format("%d,SUBTASK,%s,%s,%s,%d,%s,%s,%d",
                subtask.getId(), subtask.getTitle(), subtask.getStatus(), subtask.getDescription(),
                subtask.getDuration() != null ? subtask.getDuration().toMinutes() : "null",
                subtask.getStartTime() != null ? subtask.getStartTime().toString() : "null",
                subtask.getEndTime() != null ? subtask.getEndTime().toString() : "null",
                subtask.getEpic() != null ? subtask.getEpic().getId() : "null");
    }

    // Загружаем задачи из файла
    public void loadFromFile(File file) {
        if (!file.exists()) {
            throw new RuntimeException("Файл не существует: " + file.getAbsolutePath());
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            for (String line : lines.subList(1, lines.size())) {
                Task task = fromString(line);
                addTaskToManager(task);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки данных из файла: " + e.getMessage());
        }
    }

    // Преобразуем строку обратно в объект задачи
    private Task fromString(String value) {
        String[] parts = value.split(",");
        int id = Integer.parseInt(parts[0]);
        TaskType type = TaskType.valueOf(parts[1]);
        String name = parts[2];
        Status status = Status.valueOf(parts[3]);
        String description = parts[4];
        Duration duration = parts[5].equals("null") ? null : Duration.ofMinutes(Long.parseLong(parts[5]));
        LocalDateTime startTime = parts[6].equals("null") ? null : LocalDateTime.parse(parts[6]);
        LocalDateTime endTime = parts[7].equals("null") ? null : LocalDateTime.parse(parts[7]);

        switch (type) {
            case TASK:
                Task task = new Task(name, description, status, duration, startTime); // Создаём без id
                task.setId(id); // Устанавливаем id
                return task;
            case EPIC:
                Epic epic = new Epic(name, description); // Создаём без id
                epic.setId(id); // Устанавливаем id
                epic.setDuration(duration);
                epic.setStartTime(startTime);
                epic.endTime = endTime;
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(parts[8]);
                Epic epicForSubtask = getEpicById(epicId);
                Subtask subtask = new Subtask(name, description, status, epicForSubtask, duration, startTime); // Создаём без id
                subtask.setId(id); // Устанавливаем id
                return subtask;
            default:
                throw new IllegalArgumentException("Неподдерживаемый тип задачи: " + type);
        }
    }

    // Добавляем задачу в менеджер в зависимости от ее типа
    private void addTaskToManager(Task task) {
        switch (task.getTaskType()) {
            case TASK:
                addTask(task);
                break;
            case EPIC:
                addEpic((Epic) task);
                break;
            case SUBTASK:
                addSubtask((Subtask) task);
                break;
        }
    }

    // Статический метод для загрузки задач из файла
    public static FileBackedTaskManager loadFromFile(File file, HistoryManager historyManager) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, historyManager);
        manager.loadFromFile(file); // Вызов метода для загрузки задач
        return manager;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
    }

    private void removeFromPrioritizedTasks(int taskId) {
        prioritizedTasks.removeIf(task -> task.getId() == taskId);
    }

    private void updatePrioritizedTasks(Task task) {
        removeFromPrioritizedTasks(task.getId());
        addToPrioritizedTasks(task);
    }

    private boolean isOverlapping(Task task) {
        return getPrioritizedTasks().stream()
                .filter(existingTask -> existingTask.getId() != task.getId()) // Исключаем саму задачу
                .anyMatch(existingTask -> isOverlapping(existingTask, task));
    }

    private boolean isOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getEndTime() == null || task2.getStartTime() == null || task2.getEndTime() == null) {
            return false;
        }
        return task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getEndTime());
    }
}