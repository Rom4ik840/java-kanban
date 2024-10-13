package com.yandex.app.service;

import com.yandex.app.model.Task;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head;  // Начало списка!
    private Node tail;  // Конец списка!
    private final Map<Integer, Node> nodeMap = new HashMap<>();  // HashMap для быстрого доступа к узлам по ID задачи

    // Добавление задачи в конец истории!
    @Override
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null.");
        }

        // Если задача уже есть в истории, удаляем её перед добавлением!
        if (nodeMap.containsKey(task.getId())) {
            removeNode(nodeMap.get(task.getId()));
        }

        // Создаем новый узел для задачи и добавляем его в конец списка!
        Node newNode = new Node(task);
        linkLast(newNode);
        nodeMap.put(task.getId(), newNode);  // Обновляем ссылку на узел в HashMap!
    }

    // Удаление задачи из истории по её ID
    @Override
    public void remove(int id) {
        Node node = nodeMap.remove(id);  // Удаляем запись из HashMap и получаем узел
        if (node != null) {
            removeNode(node);  // Удаляем узел из списка
        }
    }

    // Возвращает историю задач в виде списка
    @Override
    public List<Task> getHistory() {
        return Stream.iterate(head, Objects::nonNull, node -> node.next)
                .map(node -> node.task)
                .collect(Collectors.toList());
    }

    // Приватные методы для работы с двусвязным списком

    // Добавляет узел в конец списка
    private void linkLast(Node node) {
        if (tail == null) {  // Если список пуст
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
    }

    // Удаляет узел из списка
    private void removeNode(Node node) {
        if (node.prev != null) {
            node.prev.next = node.next;  // Переставляем ссылку с предыдущего узла
        } else {
            head = node.next;  // Если удаляется первый узел
        }

        if (node.next != null) {
            node.next.prev = node.prev;  // Переставляем ссылку с следующего узла
        } else {
            tail = node.prev;  // Если удаляется последний узел
        }
    }
}