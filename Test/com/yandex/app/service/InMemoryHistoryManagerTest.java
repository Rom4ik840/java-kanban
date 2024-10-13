package com.yandex.app.service;

import com.yandex.app.model.Status;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//Тесты класса InMemoryHistoryManager
class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void testAddAndGetHistory() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    void testHistoryPreservesPreviousVersion() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        historyManager.add(task1);

        task1.setTitle("Обновленная Задача 1");
        task1.setDescription("Обновленное Описание 1");
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals("Обновленная Задача 1", history.get(0).getTitle());
        assertEquals("Обновленное Описание 1", history.get(0).getDescription());
    }
}