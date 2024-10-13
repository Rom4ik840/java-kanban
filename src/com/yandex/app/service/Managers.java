package com.yandex.app.service;

public class Managers {

    // Приватный конструктор, чтобы предотвратить создание экземпляров класса!
    private Managers() {
    }

    // Метод для получения объекта менеджера задач по умолчанию!
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    // Метод для получения объекта менеджера истории по умолчанию!
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}