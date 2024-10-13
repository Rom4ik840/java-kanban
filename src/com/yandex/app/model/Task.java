package com.yandex.app.model;

// Класс Task представляет базовую задачу с идентификатором, заголовком, описанием и статусом.
public class Task {

    // Уникальный идентификатор задачи.
    private int id;

    // Заголовок задачи.
    private String title;

    // Описание задачи.
    private String description;

    // Статус задачи!
    private Status status;

    // Конструктор класса Task.
    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    // Геттеры и сеттеры!
    // Возвращает идентификатор задачи!
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

    // Возвращает строковое представление задачи.
    @Override
    public String toString() {
        return "Task{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}