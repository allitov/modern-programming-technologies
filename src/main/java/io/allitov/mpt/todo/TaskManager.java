package io.allitov.mpt.todo;

import java.util.List;

/**
 * Управляет задачами и проектами.
 */
public class TaskManager {

    private final Repository<Task> taskRepository = new Repository<>();

    private final Repository<Project> projectRepository = new Repository<>();

    private int nextTaskId = 1;

    private int nextProjectId = 1;

    /**
     * Добавляет новую задачу.
     *
     * @param title название задачи.
     * @param description описание задачи.
     * @param priority приоритет задачи.
     * @param status статус задачи.
     */
    public void addTask(String title, String description, String priority, String status) {
        Task task = new Task(nextTaskId++, title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        taskRepository.add(task);
    }

    /**
     * Обновляет задачу по ее индексу, сохраняя исходный идентификатор.
     *
     * @param index индекс обновляемой задачи.
     * @param title новое название задачи.
     * @param description новое описание задачи.
     * @param priority новый приоритет задачи.
     * @param status новый статус задачи.
     */
    public void updateTask(int index, String title, String description, String priority, String status) {
        Task task = taskRepository.getAll().get(index);
        task.setTitle(title);
        task.setDescription(description);
        task.setPriority(priority);
        task.setStatus(status);
        taskRepository.update(index, task);
    }

    /**
     * Удаляет задачу по ее индексу.
     *
     * @param index индекс удаляемой задачи.
     */
    public void deleteTask(int index) {
        taskRepository.remove(index);
    }

    /**
     * Возвращает список всех задач.
     *
     * @return список задач.
     */
    public List<Task> getTasks() {
        return taskRepository.getAll();
    }

    /**
     * Добавляет новый проект.
     *
     * @param name название проекта.
     */
    public void addProject(String name) {
        projectRepository.add(new Project(nextProjectId++, name));
    }

    /**
     * Удаляет проект по его индексу.
     *
     * @param index индекс удаляемого проекта.
     */
    public void deleteProject(int index) {
        projectRepository.remove(index);
    }

    /**
     * Возвращает список всех проектов.
     *
     * @return список проектов.
     */
    public List<Project> getProjects() {
        return projectRepository.getAll();
    }
}
