package com.yandex.app.model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks;

    public Epic(String title, String description) {
        super(title, description, Status.NEW);
        this.subtasks = new ArrayList<>();
    }
    // Возвращает список подзадач эпика.
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        if (subtask.getEpic() != this) {
            throw new IllegalArgumentException("Подзадача не принадлежит этому эпику");
        }
        if (subtask.getId() == this.getId()) {
            throw new IllegalArgumentException("Эпик не может быть добавлен как подзадача самому себе");
        }
        subtasks.add(subtask);
        updateStatus();
    }

    public void removeSubtask(int subtaskId) {
        subtasks.removeIf(subtask -> subtask.getId() == subtaskId);
        updateStatus();
    }

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
}