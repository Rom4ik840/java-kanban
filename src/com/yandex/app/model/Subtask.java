package com.yandex.app.model;

// Класс Subtask представляет подзадачу, которая является типом задачи и связана с эпиком!
public class Subtask extends Task {

    // Эпик, к которому относится эта подзадача.
    private Epic epic;

    // Конструктор класса Subtask!
    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }

    // Возвращает эпик, к которому относится эта подзадача.
    public Epic getEpic() {
        return epic;
    }

    // Устанавливает эпик для этой подзадачи.
    public void setEpic(Epic epic) {
        if (epic == null) {
            throw new IllegalArgumentException("Эпик не может быть null.");
        }
        this.epic = epic;
    }

    // Возвращает строковое представление подзадачи.
    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}