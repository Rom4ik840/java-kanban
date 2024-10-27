package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;
import com.yandex.app.model.TaskType;

// Класс Epic наследует от класса Task и представляет собой эпик, содержащий подзадачи.
public class Epic extends Task {
    // Список подзадач, связанных с эпиком.
    private final List<Subtask> subtasks;

    // Конструктор класса Epic.
    // Принимает заголовок и описание, статус устанавливается по умолчанию в NEW.
    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasks = new ArrayList<>();
        setTaskType(TaskType.EPIC); // Установить тип задачи как EPIC
    }

    // Возвращает список подзадач эпика.
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    // Добавляет подзадачу в список подзадач.
    public void addSubtask(Subtask subtask) {
        if (subtask.getEpic() != this) {
            throw new IllegalArgumentException("Подзадача не принадлежит этому эпику");
        }
        if (subtask.getId() == this.getId()) {
            throw new IllegalArgumentException("Эпик не может быть добавлен как подзадача самому себе");
        }
        subtasks.add(subtask);
        updateStatus(); // Обновление статуса после добавления подзадачи
    }

    // Удаляет подзадачу по ID.
    public void removeSubtask(int subtaskId) {
        subtasks.removeIf(subtask -> subtask.getId() == subtaskId);
        updateStatus(); // Обновление статуса после удаления подзадачи
    }

    // Обновляет статус эпика на основе статусов подзадач.
    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
            return;
        }

        boolean allNew = subtasks.stream().allMatch(subtask -> subtask.getStatus() == Status.NEW);
        boolean allDone = subtasks.stream().allMatch(subtask -> subtask.getStatus() == Status.DONE);

        if (allNew) {
            setStatus(Status.NEW);
        } else if (allDone) {
            setStatus(Status.DONE);
        } else {
            setStatus(Status.IN_PROGRESS);
        }
    }

    // Возвращает строковое представление эпика для сохранения в файл.
    @Override
    public String toString() {
        return getId() + "," + getTaskType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + ",";
    }
}
