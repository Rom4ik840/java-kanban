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
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        manager = Managers.getDefault();
        task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        epic1 = new Epic("Эпик 1", "Описание 1");
        epic2 = new Epic("Эпик 2", "Описание 2");
        subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic1);
        subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic1);
    }

    @Test
    void testAddAndGetTasks() {
        manager.addTask(task1);
        manager.addTask(task2);

        List<Task> tasks = manager.getAllTasks();
        assertEquals(2, tasks.size());
        assertTrue(tasks.contains(task1));
        assertTrue(tasks.contains(task2));
    }

    @Test
    void testAddAndGetEpics() {
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        List<Epic> epics = manager.getAllEpics();
        assertEquals(2, epics.size());
        assertTrue(epics.contains(epic1));
        assertTrue(epics.contains(epic2));
    }

    @Test
    void testAddAndGetSubtasks() {
        manager.addEpic(epic1);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(2, subtasks.size());
        assertTrue(subtasks.contains(subtask1));
        assertTrue(subtasks.contains(subtask2));
    }

    @Test
    void testRemoveSubtaskUpdatesEpic() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        manager.deleteSubtaskById(subtask1.getId());

        List<Subtask> subtasks = manager.getAllSubtasks();
        assertEquals(0, subtasks.size());

        List<Epic> epics = manager.getAllEpics();
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void testEpicStatusUpdate() {
        manager.addEpic(epic1);

        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());

        manager.deleteSubtaskById(subtask1.getId());
        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void testEpicWithoutSubtasksIsNew() {
        manager.addEpic(epic1);

        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void testGetEpicById() {
        manager.addEpic(epic1);

        Epic fetchedEpic = manager.getEpicById(epic1.getId());
        assertEquals(epic1, fetchedEpic);
    }

    @Test
    void testSubtaskDoesNotRetainOldIdAfterRemoval() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        int removedId = subtask1.getId();
        manager.deleteSubtaskById(removedId);

        // Проверяем, что подзадача не хранит старый ID
        assertNull(manager.getSubtaskById(removedId));
    }

    @Test
    void testEpicDoesNotRetainOldSubtaskIds() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);

        manager.deleteSubtaskById(subtask1.getId());

        // Проверяем, что эпик не содержит ID удалённой подзадачи
        assertFalse(epic1.getSubtasks().contains(subtask1));
    }

    @Test
    void testTaskFieldUpdateAffectsManagerData() {
        manager.addTask(task1);

        // Изменяем название задачи
        task1.setTitle("Новая Задача");

        Task updatedTask = manager.getTaskById(task1.getId());
        assertEquals("Новая Задача", updatedTask.getTitle());
    }

    @Test
    void testSubtaskFieldUpdateAffectsEpic() {
        manager.addEpic(epic1);
        manager.addSubtask(subtask1);

        // Изменяем статус подзадачи
        subtask1.setStatus(Status.DONE);

        manager.deleteSubtaskById(subtask1.getId());

        // Проверяем, что статус эпика обновился
        assertEquals(Status.NEW, epic1.getStatus());
    }
}