package com.yandex.app.model;

import com.yandex.app.model.TaskType;

// Класс Task представляет базовую задачу с идентификатором, заголовком, описанием и статусом.
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

    // Конструктор класса Task.
    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.taskType = TaskType.TASK; // Установить тип задачи по умолчанию
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

    // Переопределение методов equals и hashCode для корректного сравнения задач.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Проверка на ссылочное равенство.
        if (!(o instanceof Task)) return false; // Проверка на принадлежность к классу Task.
        Task task = (Task) o; // Приведение к классу Task.
        return id == task.id; // Сравнение по идентификатору.
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id); // Генерация хеш-кода по идентификатору.
    }

    // Возвращает строковое представление задачи для сохранения в файл.
    @Override
    public String toString() {
        return id + "," + getTaskType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + ","; // Использование метода getTaskType.
    }
}
