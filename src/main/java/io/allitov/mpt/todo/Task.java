package io.allitov.mpt.todo;

/**
 * Задача в менеджере задач.
 */
public interface Task {

    /**
     * Возвращает идентификатор задачи.
     *
     * @return идентификатор задачи.
     */
    int getId();

    /**
     * Задает идентификатор задачи.
     *
     * @param id идентификатор задачи.
     */
    void setId(int id);

    /**
     * Возвращает название задачи.
     *
     * @return название задачи.
     */
    String getTitle();

    /**
     * Задает название задачи.
     *
     * @param title название задачи.
     */
    void setTitle(String title);

    /**
     * Возвращает описание задачи.
     *
     * @return описание задачи.
     */
    String getDescription();

    /**
     * Задает описание задачи.
     *
     * @param description описание задачи.
     */
    void setDescription(String description);

    /**
     * Возвращает статус задачи.
     *
     * @return статус задачи.
     */
    String getStatus();

    /**
     * Задает статус задачи.
     *
     * @param status статус задачи.
     */
    void setStatus(String status);

    /**
     * Возвращает приоритет задачи.
     *
     * @return приоритет задачи.
     */
    String getPriority();

    /**
     * Задает приоритет задачи.
     *
     * @param priority приоритет задачи.
     */
    void setPriority(String priority);
}
