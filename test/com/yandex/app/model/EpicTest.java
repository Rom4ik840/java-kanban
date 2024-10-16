package com.yandex.app.model;

import com.yandex.app.service.Managers;
import com.yandex.app.service.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//Тесты класса Epic
class EpicTest {
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        epic1 = new Epic("Эпик 1", "Описание 1");
        epic2 = new Epic("Эпик 2", "Описание 2");

        // Создаем подзадачи
        subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic1);
        subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.DONE, epic1);

        // Добавляем эпик и подзадачи в менеджер
        taskManager.addEpic(epic1);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
    }

    @Test
    void testEquals() {
        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2);
    }

    @Test
    void testNotEquals() {
        epic1.setId(1);
        epic2.setId(2);
        assertNotEquals(epic1, epic2);
    }

    @Test
    void testAddSubtaskToSelf() {
        Subtask subtaskToSelf = new Subtask("Подзадача самому себе", "Описание", Status.NEW, epic1);
        subtaskToSelf.setId(epic1.getId()); // Устанавливаем ID подзадачи равным ID эпика
        assertThrows(IllegalArgumentException.class, () -> {
            epic1.addSubtask(subtaskToSelf);
        });
    }

    @Test
    void testRemoveSubtaskUpdatesEpic() {
        taskManager.deleteSubtaskById(subtask1.getId()); // Удаляем подзадачу по ее ID

        // Проверяем, что в списке подзадач эпика больше нет ID удаленной подзадачи
        assertFalse(epic1.getSubtasks().contains(subtask1),
                "Эпик не должен содержать удаленную подзадачу.");
    }

    @Test
    void testEpicStatusUpdate() {
        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика должен быть IN_PROGRESS.");

        taskManager.deleteSubtaskById(subtask1.getId());
        assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика должен быть DONE после удаления подзадачи.");
    }

    @Test
    void testEpicWithoutSubtasksIsNew() {
        taskManager.deleteSubtaskById(subtask1.getId()); // Удаляем первую подзадачу
        taskManager.deleteSubtaskById(subtask2.getId()); // Удаляем вторую подзадачу
        assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика без подзадач должен быть NEW.");
    }
}
