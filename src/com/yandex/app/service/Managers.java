package com.yandex.app.service;

import java.io.File;

public class Managers {

    // Приватный конструктор, чтобы предотвратить создание экземпляров класса!
    private Managers() {
    }

    // Метод для получения объекта менеджера задач по умолчанию!
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    // Метод для получения объекта менеджера задач с сохранением в файл!
    public static TaskManager getDefaultFileBacked(File file) {
        return new FileBackedTaskManager(file, getDefaultHistory());
    }

    // Метод для получения объекта менеджера истории по умолчанию!
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}