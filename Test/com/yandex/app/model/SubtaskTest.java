package com.yandex.app.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//Тесты класса Subtask
class SubtaskTest {

    @Test
    void testEquals() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic);
        subtask1.setId(1);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2);
    }

    @Test
    void testNotEquals() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic);
        subtask1.setId(1);
        subtask2.setId(2);

        assertNotEquals(subtask1, subtask2);
    }

    @Test
    void testSetEpicToSelf() {
        Epic epic = new Epic("Эпик 1", "Описание 1");
        Subtask subtask = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic);
        assertThrows(IllegalArgumentException.class, () -> subtask.setEpic(epic)); // Исправление здесь
    }
}