package io.allitov.mpt.todo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Представляет проект в менеджере задач.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectImpl implements Project {

    private int id;

    private String name = "";
}
