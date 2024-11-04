package com.yandex.app.service;

import com.yandex.app.model.Task;
import com.yandex.app.model.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {

    // Тест на пустую историю задач
    @Test
    public void testEmptyHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty(), "История задач должна быть пустой.");
    }

    // Тест на дублирование задачи в истории
    @Test
    public void testDuplicateHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null);
        task.setId(1); // Устанавливаем ID для задачи
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Дублирование задачи не должно увеличивать размер истории.");
    }

    // Тест на удаление задачи из истории
    @Test
    public void testRemoveFromHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ZERO, null);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW, Duration.ZERO, null);
        task1.setId(1); // Устанавливаем ID для задач
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());
        assertEquals(2, historyManager.getHistory().size(), "Размер истории должен уменьшиться после удаления задачи.");
        assertFalse(historyManager.getHistory().contains(task2), "Удаленная задача не должна присутствовать в истории.");
    }

    // Тест на удаление задачи из начала истории
    @Test
    public void testRemoveFromBeginning() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ZERO, null);
        task1.setId(1); // Устанавливаем ID для задач
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());
        assertEquals(1, historyManager.getHistory().size(), "Размер истории должен уменьшиться после удаления задачи.");
        assertFalse(historyManager.getHistory().contains(task1), "Удаленная задача не должна присутствовать в истории.");
    }

    // Тест на удаление задачи из середины истории
    @Test
    public void testRemoveFromMiddle() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ZERO, null);
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW, Duration.ZERO, null);
        task1.setId(1); // Устанавливаем ID для задач
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());
        assertEquals(2, historyManager.getHistory().size(), "Размер истории должен уменьшиться после удаления задачи.");
        assertFalse(historyManager.getHistory().contains(task2), "Удаленная задача не должна присутствовать в истории.");
    }

    // Тест на удаление задачи из конца истории
    @Test
    public void testRemoveFromEnd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ZERO, null);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ZERO, null);
        task1.setId(1); // Устанавливаем ID для задач
        task2.setId(2);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task2.getId());
        assertEquals(1, historyManager.getHistory().size(), "Размер истории должен уменьшиться после удаления задачи.");
        assertFalse(historyManager.getHistory().contains(task2), "Удаленная задача не должна присутствовать в истории.");
    }
}