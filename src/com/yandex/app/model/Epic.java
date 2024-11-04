package com.yandex.app.model;

import com.yandex.app.service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// Класс Epic наследует от класса Task и представляет собой эпик, содержащий подзадачи.
public class Epic extends Task {
    // Список подзадач, связанных с эпиком.
    private final List<Subtask> subtasks;

    // Время окончания эпика.
    public LocalDateTime endTime;

    // Конструктор класса Epic.
    // Принимает заголовок и описание, статус устанавливается по умолчанию в NEW.
    public Epic(String title, String description) {
        super(title, description, Status.NEW, Duration.ZERO, null);
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
        updateDurationAndTimes(); // Обновление продолжительности и времени
    }

    // Удаляет подзадачу по ID.
    public void removeSubtask(int subtaskId) {
        subtasks.removeIf(subtask -> subtask.getId() == subtaskId);
        updateStatus(); // Обновление статуса после удаления подзадачи
        updateDurationAndTimes(); // Обновление продолжительности и времени
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

    // Обновляет продолжительность и время начала/окончания эпика.
    private void updateDurationAndTimes() {
        if (subtasks.isEmpty()) {
            setDuration(Duration.ZERO);
            setStartTime(null);
            endTime = null;
            return;
        }

        LocalDateTime minStartTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .filter(startTime -> startTime != null)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime maxEndTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .filter(endTime -> endTime != null)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration totalDuration = subtasks.stream()
                .map(Subtask::getDuration)
                .filter(duration -> duration != null)
                .reduce(Duration.ZERO, Duration::plus);

        setStartTime(minStartTime);
        endTime = maxEndTime;
        setDuration(totalDuration);
    }

    // Возвращает время окончания эпика.
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Возвращает строковое представление эпика для сохранения в файл.
    @Override
    public String toString() {
        return getId() + "," + getTaskType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + "," +
                (getDuration() != null ? getDuration().toMinutes() : "null") + "," +
                (getStartTime() != null ? getStartTime().toString() : "null") + "," +
                (endTime != null ? endTime.toString() : "null");
    }
}