package io.allitov.mpt.todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Универсальное хранилище элементов в памяти.
 *
 * @param <T> тип хранимого элемента.
 */
public class Repository<T> {

    private final List<T> items = new ArrayList<>();

    /**
     * Добавляет элемент в конец хранилища.
     *
     * @param item добавляемый элемент.
     */
    public void add(T item) {
        items.add(item);
    }

    /**
     * Удаляет элемент по его индексу.
     *
     * @param index индекс удаляемого элемента.
     */
    public void remove(int index) {
        items.remove(index);
    }

    /**
     * Заменяет элемент по его индексу.
     *
     * @param index индекс заменяемого элемента.
     * @param item новый элемент.
     */
    public void update(int index, T item) {
        items.set(index, item);
    }

    /**
     * Возвращает количество элементов в хранилище.
     *
     * @return количество элементов.
     */
    public int size() {
        return items.size();
    }

    /**
     * Возвращает копию списка всех элементов хранилища.
     *
     * @return список всех элементов.
     */
    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    /**
     * Удаляет все элементы из хранилища.
     */
    public void clear() {
        items.clear();
    }
}
