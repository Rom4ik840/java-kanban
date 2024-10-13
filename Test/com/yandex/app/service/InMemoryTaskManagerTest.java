package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//Тесты класса InMemoryTaskManager
class InMemoryTaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        // Используем фабрику для получения объекта TaskManager
        manager = Managers.getDefault();
    }

    @Test
    void testAddAndGetTasks() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);

        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testAddAndGetEpics() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        manager.addEpic(epic1);
        manager.addEpic(epic2);

        List<Epic> epics = manager.getAllEpics();
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void testAddAndGetSubtasks() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        manager.addEpic(epic1);

        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic1);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testTaskIdConflict() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task1.setId(1);
        manager.addTask(task1);

        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        manager.addTask(task2);

        assertNotEquals(task1.getId(), task2.getId());
    }

    @Test
    void testTaskUnchangedOnAdd() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        manager.addTask(task1);

        Task addedTask = manager.getTaskById(task1.getId());
        assertEquals(task1, addedTask);
    }
}