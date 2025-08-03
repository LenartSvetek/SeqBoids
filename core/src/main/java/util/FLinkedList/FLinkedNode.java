package util.FLinkedList;

public class FLinkedNode<T> {
    public FLinkedNode<T> next = null;
    public T data;

    public FLinkedNode(T data) {
        this.data = data;
    }
}
