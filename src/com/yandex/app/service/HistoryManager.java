package com.yandex.app.service;

import com.yandex.app.model.Task;
import java.util.List;

public interface HistoryManager {
    void add(Task task);  // Добавить задачу в историю!

    void remove(int id);  // Удалить задачу по её ID из истории!

    List<Task> getHistory();  // Вернуть список задач в порядке просмотра!
}
