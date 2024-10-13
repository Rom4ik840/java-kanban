package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }

    // Возвращает список подзадач эпика.
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    // Добавляет подзадачу к эпику.
    public void addSubtask(Subtask subtask) {
        if (subtask == null) {
            throw new IllegalArgumentException("Подзадача не может быть null.");
        }
        subtasks.add(subtask);
        updateStatus();
    }

    // Удаляет подзадачу из эпика!
    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        updateStatus();
    }

    // Обновляет статус эпика в зависимости от статусов его подзадач!
    public void updateStatus() {
        if (subtasks.isEmpty()) {
            setStatus(Status.NEW);
        } else {
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
    }

    // Возвращает строковое представление эпика!
    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasks=" + subtasks +
                '}';
    }
}