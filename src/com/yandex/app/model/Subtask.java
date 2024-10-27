package com.yandex.app.model;

import com.yandex.app.model.TaskType;

// Класс Subtask представляет подзадачу, которая является типом задачи и связана с эпиком.
public class Subtask extends Task {
    // Эпик, к которому относится эта подзадача.
    private Epic epic;

    // Конструктор класса Subtask.
    public Subtask(String title, String description, Status status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
        setTaskType(TaskType.SUBTASK); // Установить тип задачи как SUBTASK
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
        if (this.epic != null && this.epic.equals(epic)) {
            throw new IllegalArgumentException("Подзадача не может быть связана с самим собой.");
        }
        this.epic = epic;
    }



    // Возвращает строковое представление подзадачи в формате CSV.
    @Override
    public String toString() {
        return getId() + "," + getTaskType() + "," + getTitle() + "," + getStatus() + "," + getDescription() + "," + (epic != null ? epic.getId() : "null");
    }
}
