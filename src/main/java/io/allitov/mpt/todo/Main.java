package io.allitov.mpt.todo;

import io.allitov.mpt.todo.view.MainWindowImpl;

import java.awt.EventQueue;

/**
 * Точка входа приложения менеджера задач.
 */
public final class Main {

    /**
     * Запускает окно менеджера задач в потоке обработки событий Swing.
     */
    static void main() {
        EventQueue.invokeLater(() -> new MainWindowImpl().show());
    }
}
