package com.yandex.app.service;

import com.yandex.app.model.Task;
import com.yandex.app.model.Epic;
import com.yandex.app.model.Subtask;
import com.yandex.app.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private Task task1;
    private Epic epic1;
    private Subtask subtask1;
    private File tempFile;

    @BeforeEach
    void setUp() throws IOException {
        // Создаем временный файл для тестов
        tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit(); // Файл будет удален при завершении тестов

        // Инициализируем FileBackedTaskManager с временным файлом и менеджером истории
        fileBackedTaskManager = new FileBackedTaskManager(tempFile, new InMemoryHistoryManager());

        task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        epic1 = new Epic("Эпик 1", "Описание эпика");
        subtask1 = new Subtask("Подзадача 1", "Описание подзадачи", Status.NEW, epic1, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
    }

    // Тест на сохранение и загрузку пустого файла
    @Test
    void testSaveAndLoadEmptyFile() throws IOException {
        // Проверяем, что менеджер сохраняет пустой файл без задач
        fileBackedTaskManager.save();

        // Проверяем, что файл существует и не пуст
        assertTrue(Files.size(tempFile.toPath()) > 0, "Файл должен быть создан и не пуст.");
        fileBackedTaskManager.loadFromFile(tempFile);
        assertTrue(fileBackedTaskManager.getAllTasks().isEmpty(), "Список задач должен быть пустым.");
    }

    // Тест на сохранение и загрузку нескольких задач
    @Test
    void testSaveAndLoadMultipleTasks() throws IOException {
        // Добавляем несколько задач и сохраняем
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addEpic(epic1);
        fileBackedTaskManager.addSubtask(subtask1);
        fileBackedTaskManager.save();

        // Загружаем данные из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile, new InMemoryHistoryManager());
        loadedManager.loadFromFile(tempFile);

        // Проверяем, что задачи корректно загружены
        List<Task> tasks = loadedManager.getAllTasks();
        assertEquals(1, tasks.size(), "Должна быть загружена одна задача.");
        assertEquals(task1.getTitle(), tasks.get(0).getTitle(), "Загруженная задача должна соответствовать оригиналу.");

        List<Epic> epics = loadedManager.getAllEpics();
        assertEquals(1, epics.size(), "Должен быть загружен один эпик.");
        assertEquals(epic1.getTitle(), epics.get(0).getTitle(), "Загруженный эпик должен соответствовать оригиналу.");

        List<Subtask> subtasks = loadedManager.getAllSubtasks();
        assertEquals(1, subtasks.size(), "Должна быть загружена одна подзадача.");
        assertEquals(subtask1.getTitle(), subtasks.get(0).getTitle(), "Загруженная подзадача должна соответствовать оригиналу.");
    }

    // Тест на сохранение и загрузку задач с пересекающимися интервалами
    @Test
    void testSaveAndLoadTasksWithOverlappingIntervals() throws IOException {
        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 11, 0));
        fileBackedTaskManager.addTask(task1);
        fileBackedTaskManager.addTask(task2);
        fileBackedTaskManager.save();

        // Загружаем данные из файла
        FileBackedTaskManager loadedManager = new FileBackedTaskManager(tempFile, new InMemoryHistoryManager());
        loadedManager.loadFromFile(tempFile);

        // Проверяем, что задачи корректно загружены
        List<Task> tasks = loadedManager.getAllTasks();
        assertEquals(2, tasks.size(), "Должно быть загружено две задачи.");
        assertTrue(tasks.contains(task1), "Загруженная задача должна соответствовать оригиналу.");
        assertTrue(tasks.contains(task2), "Загруженная задача должна соответствовать оригиналу.");
    }
}