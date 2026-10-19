package com.studentapp.collection;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

public class DynamicArray<E> extends AbstractList<E> implements RandomAccess {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] elements;
    private int size;

    public DynamicArray() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public DynamicArray(Collection<? extends E> source) {
        Object[] data = source.toArray();
        elements = Arrays.copyOf(data, Math.max(data.length, DEFAULT_CAPACITY), Object[].class);
        size = data.length;
    }

    @Override
    public boolean add(E element) {
        add(size, element);
        return true;
    }

    @Override
    public void add(int index, E element) {
        Objects.checkIndex(index, size + 1);
        ensureCapacity(size + 1);
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
        modCount++;
    }

    @Override
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementAt(index);
    }

    @Override
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E previous = elementAt(index);
        elements[index] = element;
        return previous;
    }

    @Override
    public E remove(int index) {
        Objects.checkIndex(index, size);
        E removed = elementAt(index);
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        modCount++;
        return removed;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
        modCount++;
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return elements.length;
    }

    private void ensureCapacity(int required) {
        if (required > elements.length) {
            int grown = elements.length + (elements.length >> 1);
            elements = Arrays.copyOf(elements, Math.max(grown, required));
        }
    }

    @SuppressWarnings("unchecked")
    private E elementAt(int index) {
        return (E) elements[index];
    }
}
