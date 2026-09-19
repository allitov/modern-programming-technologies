package io.allitov.mpt.todo.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Представляет задачу в менеджере задач.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TaskImpl implements Task {

    private int id;

    private String title;

    private String description = "";

    private String status = "Todo";

    private String priority = "Medium";

    /**
     * Создает задачу с указанным идентификатором и названием.
     *
     * @param id идентификатор задачи.
     * @param title название задачи.
     */
    public TaskImpl(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
