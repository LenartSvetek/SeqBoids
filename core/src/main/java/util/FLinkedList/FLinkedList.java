package util.FLinkedList;

import java.util.function.Consumer;

public class FLinkedList<T> {
    FLinkedNode<T> head = null;
    FLinkedNode<T> tail = null;
    private int size = 0;

    public FLinkedList() {}

    public FLinkedList(T data) {
        head = new FLinkedNode<>(data);
        tail = head;
        size = 1;
    }

    public void add(T data) {
        if(head == null) {
            head = new FLinkedNode<>(data);
            tail = head;
            size = 1;
            return;
        }
        FLinkedNode<T> newNode = new FLinkedNode<>(data);
        tail.next = newNode;
        tail = newNode;
        size++;
    }

    public void addLast(T data) {
        add(data);
    }

    public void addFirst(T data) {
        if(head == null) {
            head = new FLinkedNode<>(data);
            tail = head;
            size = 1;
            return;
        }

        FLinkedNode<T> newNode = new FLinkedNode<>(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addAll(FLinkedList<T> list) {
        if(list.isEmpty()) return;
        if(head == null) {
            head = list.head;

            tail = list.tail;

            size = list.size;
            return;
        }

        tail.next = list.head;
        tail = list.tail;
        size += list.size;
    }

    public void foreach(Consumer<T> action) {
        FLinkedNode<T> current = head;

        while (current != null) {
            action.accept(current.data);
            current = current.next;
        }
    }

    public int size(){
        return size;
    }
}
