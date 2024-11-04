package com.yandex.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Эпик 1", "Описание 1");
        subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 10, 0));
        subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 12, 0));
    }

    @Test
    void testEquals() {
        subtask1.setId(1);
        subtask2.setId(1);

        // Устанавливаем одинаковые значения для всех полей подзадач
        subtask2.setTitle(subtask1.getTitle());
        subtask2.setDescription(subtask1.getDescription());
        subtask2.setStatus(subtask1.getStatus());
        subtask2.setDuration(subtask1.getDuration());
        subtask2.setStartTime(subtask1.getStartTime());

        assertEquals(subtask1, subtask2);
    }

    @Test
    void testNotEquals() {
        subtask1.setId(1);
        subtask2.setId(2);
        assertNotEquals(subtask1, subtask2);
    }

    @Test
    void testSetEpicToSelf() {
        assertThrows(IllegalArgumentException.class, () -> subtask1.setEpic(epic));
    }

    @Test
    void testSetEpicToNull() {
        assertThrows(IllegalArgumentException.class, () -> subtask1.setEpic(null));
    }

    @Test
    void testUpdateStatus() {
        subtask1.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, subtask1.getStatus(), "Статус подзадачи должен быть обновлен.");
    }

    @Test
    void testUpdateTitle() {
        subtask1.setTitle("Новая Подзадача");
        assertEquals("Новая Подзадача", subtask1.getTitle(), "Название подзадачи должно быть обновлено.");
    }

    @Test
    void testUpdateDescription() {
        subtask1.setDescription("Новое описание");
        assertEquals("Новое описание", subtask1.getDescription(), "Описание подзадачи должно быть обновлено.");
    }

    @Test
    void testUpdateDuration() {
        subtask1.setDuration(Duration.ofHours(2));
        assertEquals(Duration.ofHours(2), subtask1.getDuration(), "Продолжительность подзадачи должна быть обновлена.");
    }

    @Test
    void testUpdateStartTime() {
        subtask1.setStartTime(LocalDateTime.of(2023, 10, 1, 14, 0));
        assertEquals(LocalDateTime.of(2023, 10, 1, 14, 0), subtask1.getStartTime(), "Время начала подзадачи должно быть обновлено.");
    }

    @Test
    void testGetEndTime() {
        LocalDateTime endTime = subtask1.getEndTime();
        assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0), endTime, "Время окончания подзадачи должно быть рассчитано правильно.");
    }

    @Test
    void testAddSubtaskToSelf() {
        Subtask subtaskToSelf = new Subtask("Подзадача самому себе", "Описание", Status.NEW, epic, Duration.ofHours(1), LocalDateTime.of(2023, 10, 1, 14, 0));
        subtaskToSelf.setId(epic.getId()); // Устанавливаем ID подзадачи равным ID эпика
        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtaskToSelf));
    }
}