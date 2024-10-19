package com.yandex.app.service;

import com.yandex.app.model.Task;

public class Node {
    private Task task;
    private Node prev;
    private Node next;

    // Конструктор для создания узла с указанием соседей
    public Node(Task task, Node prev, Node next) {
        this.task = task;
        this.prev = prev;
        this.next = next;
    }

    // Геттер для task
    public Task getTask() {
        return task;
    }

    // Сеттер для task
    public void setTask(Task task) {
        this.task = task;
    }

    // Геттер для prev
    public Node getPrev() {
        return prev;
    }

    // Сеттер для prev
    public void setPrev(Node prev) {
        this.prev = prev;
    }

    // Геттер для next
    public Node getNext() {
        return next;
    }

    // Сеттер для next
    public void setNext(Node next) {
        this.next = next;
    }
}
