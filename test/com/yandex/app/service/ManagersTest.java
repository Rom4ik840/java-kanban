package com.yandex.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void testGetDefaultTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        assertNotNull(taskManager);
    }

    @Test
    void testGetDefaultHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager);
    }

    @Test
    void testGetDefaultFileBackedTaskManager() {
        TaskManager taskManager = Managers.getDefaultFileBacked(null);
        assertNotNull(taskManager);
    }
}