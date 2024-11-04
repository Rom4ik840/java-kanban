package com.yandex.app.service;

import com.yandex.app.model.Epic;
import com.yandex.app.model.Status;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

// Абстрактный класс для тестирования TaskManager
abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    // Инициализация TaskManager перед каждым тестом
    @BeforeEach
    public void setUp() {
        taskManager = createTaskManager();
    }

    // Метод для создания экземпляра TaskManager
    protected abstract T createTaskManager();

    // Тест на добавление задачи
    @Test
    public void testAddTask() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.addTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Должна быть добавлена одна задача.");
    }

    // Тест на добавление эпика
    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getAllEpics().size(), "Должен быть добавлен один эпик.");
    }

    // Тест на добавление подзадачи
    @Test
    public void testAddSubtask() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Подзадача 1", "Описание подзадачи", Status.NEW, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getAllSubtasks().size(), "Должна быть добавлена одна подзадача.");
    }

    // Тест на обновление задачи
    @Test
    public void testUpdateTask() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.addTask(task);
        task.setTitle("Обновленная задача");
        taskManager.updateTask(task);
        assertEquals("Обновленная задача", taskManager.getTaskById(task.getId()).getTitle(), "Задача должна быть обновлена.");
    }

    // Тест на удаление задачи
    @Test
    public void testRemoveTask() {
        Task task = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        taskManager.addTask(task);
        taskManager.deleteTaskById(task.getId()); // Исправлено на deleteTaskById
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задача должна быть удалена.");
    }

    // Тест на расчет статуса эпика
    @Test
    public void testEpicStatusCalculation() {
        Epic epic = new Epic("Эпик 1", "Описание эпика");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание подзадачи", Status.NEW, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание подзадачи", Status.DONE, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 14, 0));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Эпик должен иметь статус IN_PROGRESS.");
    }

    // Тест на пересечение интервалов задач
    @Test
    public void testIntersectionOfIntervals() {
        Task task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 11, 0));
        taskManager.addTask(task1);
        assertThrows(IllegalArgumentException.class, () -> taskManager.addTask(task2), "Должно быть выброшено исключение из-за пересечения интервалов.");
    }
}