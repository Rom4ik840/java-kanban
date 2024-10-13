package com.yandex.app.service;

import com.yandex.app.model.Task;

import java.util.List;

//Интерфейс для управления историей просмотров задач!
public interface HistoryManager {
    void add(Task task);
    List<Task> getHistory();
}