package com.yandex.app.model;

import com.yandex.app.service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

// Класс Task представляет базовую задачу с идентификатором, заголовком, описанием, статусом, продолжительностью и временем начала.
public class Task {
    // Уникальный идентификатор задачи.
    private int id;

    // Заголовок задачи.
    private String title;

    // Описание задачи.
    private String description;

    // Статус задачи.
    private Status status;

    // Тип задачи.
    private TaskType taskType; // Добавлено поле для типа задачи.

    // Продолжительность задачи.
    private Duration duration;

    // Время начала задачи.
    private LocalDateTime startTime;

    // Время окончания задачи.
    private LocalDateTime endTime;

    // Конструктор класса Task.
    public Task(String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
        this.taskType = TaskType.TASK; // Установить тип задачи по умолчанию
        updateEndTime(); // Обновляем время окончания при создании задачи
    }

    // Геттеры и сеттеры.
    // Возвращает идентификатор задачи.
    public int getId() {
        return id;
    }

    // Устанавливает идентификатор задачи.
    public void setId(int id) {
        this.id = id;
    }

    // Возвращает заголовок задачи.
    public String getTitle() {
        return title;
    }

    // Устанавливает заголовок задачи.
    public void setTitle(String title) {
        this.title = title;
    }

    // Возвращает описание задачи.
    public String getDescription() {
        return description;
    }

    // Устанавливает описание задачи.
    public void setDescription(String description) {
        this.description = description;
    }

    // Возвращает статус задачи.
    public Status getStatus() {
        return status;
    }

    // Устанавливает статус задачи.
    public void setStatus(Status status) {
        this.status = status;
    }

    // Возвращает тип задачи.
    public TaskType getTaskType() {
        return taskType; // Геттер для типа задачи.
    }

    // Устанавливает тип задачи.
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType; // Сеттер для типа задачи.
    }

    // Возвращает продолжительность задачи.
    public Duration getDuration() {
        return duration;
    }

    // Устанавливает продолжительность задачи.
    public void setDuration(Duration duration) {
        this.duration = duration;
        updateEndTime(); // Обновляем время окончания при изменении продолжительности
    }

    // Возвращает время начала задачи.
    public LocalDateTime getStartTime() {
        return startTime;
    }

    // Устанавливает время начала задачи.
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        updateEndTime(); // Обновляем время окончания при изменении времени начала
    }

    // Возвращает время окончания задачи.
    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Устанавливает время окончания задачи.
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // Обновляет время окончания задачи на основе времени начала и продолжительности.
    private void updateEndTime() {
        if (startTime != null && duration != null) {
            this.endTime = startTime.plus(duration);
        } else {
            this.endTime = null;
        }
    }

    // Переопределение методов equals и hashCode для корректного сравнения задач.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Проверка на ссылочное равенство.
        if (!(o instanceof Task)) return false; // Проверка на принадлежность к классу Task.
        Task task = (Task) o; // Приведение к классу Task.
        return id == task.id &&
                title.equals(task.title) &&
                description.equals(task.description) &&
                status == task.status &&
                taskType == task.taskType &&
                duration.equals(task.duration) &&
                startTime.equals(task.startTime) &&
                endTime.equals(task.endTime);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + taskType.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + startTime.hashCode();
        result = 31 * result + endTime.hashCode();
        return result;
    }

    // Возвращает строковое представление задачи для сохранения в файл.
    @Override
    public String toString() {
        return getId() + "," + getTaskType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + "," +
                (duration != null ? duration.toMinutes() : "null") + "," +
                (startTime != null ? startTime.toString() : "null") + "," +
                (endTime != null ? endTime.toString() : "null");
    }
}