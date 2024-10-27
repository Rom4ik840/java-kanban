package com.yandex.app.service;

import com.yandex.app.model.Task;
import com.yandex.app.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//Тесты класса InMemoryHistoryManager
class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        task3 = new Task("Задача 3", "Описание 3", Status.NEW);

        task1.setId(1); // Устанавливаем ID для задач
        task2.setId(2);
        task3.setId(3);
    }

    @Test
    void testAddAndGetHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertTrue(history.contains(task1));
        assertTrue(history.contains(task2));
    }

    @Test
    void testRemoveFromHistory() {
        historyManager.add(task1);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void testDuplicateAdd() {
        historyManager.add(task1);
        historyManager.add(task1); // Добавляем повторно

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size()); // Должен остаться только один экземпляр
        assertTrue(history.contains(task1));
    }

    @Test
    void testOrderOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task2); // Повторное добавление

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task2, history.get(2));
    }

    @Test
    void testRemoveUpdatesHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId()); // Удаляем task1
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertFalse(history.contains(task1)); // task1 не должен быть в истории
        assertTrue(history.contains(task2)); // task2 должен остаться
    }
}