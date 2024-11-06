package com.yandex.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task("Задача 1", "Описание 1", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        task2 = new Task("Задача 2", "Описание 2", Status.NEW, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
    }

    @Test
    void testEquals() {
        task1.setId(1);
        task2.setId(1);

        // Устанавливаем одинаковые значения для всех полей задач
        task2.setTitle(task1.getTitle());
        task2.setDescription(task1.getDescription());
        task2.setStatus(task1.getStatus());
        task2.setDuration(task1.getDuration());
        task2.setStartTime(task1.getStartTime());

        assertEquals(task1, task2);
    }

    @Test
    void testNotEquals() {
        task1.setId(1);
        task2.setId(2);
        assertNotEquals(task1, task2);
    }

    @Test
    void testUpdateStatus() {
        task1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task1.getStatus(), "Статус задачи должен быть обновлен.");
    }

    @Test
    void testUpdateTitle() {
        task1.setTitle("Новая Задача");
        assertEquals("Новая Задача", task1.getTitle(), "Название задачи должно быть обновлено.");
    }

    @Test
    void testUpdateDescription() {
        task1.setDescription("Новое описание");
        assertEquals("Новое описание", task1.getDescription(), "Описание задачи должно быть обновлено.");
    }

    @Test
    void testUpdateDuration() {
        task1.setDuration(Duration.ofHours(2));
        assertEquals(Duration.ofHours(2), task1.getDuration(), "Продолжительность задачи должна быть обновлена.");
    }

    @Test
    void testUpdateStartTime() {
        task1.setStartTime(LocalDateTime.of(2023, 10, 1, 14, 0));
        assertEquals(LocalDateTime.of(2023, 10, 1, 14, 0), task1.getStartTime(), "Время начала задачи должно быть обновлено.");
    }

    @Test
    void testGetEndTime() {
        LocalDateTime endTime = task1.getEndTime();
        assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0), endTime, "Время окончания задачи должно быть рассчитано правильно.");
    }
}