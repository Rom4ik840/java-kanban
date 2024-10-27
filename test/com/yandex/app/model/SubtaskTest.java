package com.yandex.app.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Тесты класса Subtask
class SubtaskTest {
    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Эпик 1", "Описание 1");
        subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic);
        subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic);
    }

    @Test
    void testEquals() {
        subtask1.setId(1);
        subtask2.setId(1);
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
    void testAddSubtaskToSelf() {
        Subtask subtaskToSelf = new Subtask("Подзадача самому себе", "Описание", Status.NEW, epic);
        subtaskToSelf.setId(epic.getId()); // Устанавливаем ID подзадачи равным ID эпика
        assertThrows(IllegalArgumentException.class, () -> epic.addSubtask(subtaskToSelf));
    }
}
