package io.allitov.mpt.todo;

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
public class Task {

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
    public Task(int id, String title) {
        this.id = id;
        this.title = title;
    }
}
