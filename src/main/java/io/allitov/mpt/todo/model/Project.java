package io.allitov.mpt.todo.model;

/**
 * Проект в менеджере задач.
 */
public interface Project {

    /**
     * Возвращает идентификатор проекта.
     *
     * @return идентификатор проекта.
     */
    int getId();

    /**
     * Задает идентификатор проекта.
     *
     * @param id идентификатор проекта.
     */
    void setId(int id);

    /**
     * Возвращает название проекта.
     *
     * @return название проекта.
     */
    String getName();

    /**
     * Задает название проекта.
     *
     * @param name название проекта.
     */
    void setName(String name);
}
