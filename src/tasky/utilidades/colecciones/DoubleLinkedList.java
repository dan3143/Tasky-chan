package tasky.utilidades.colecciones;

import java.io.Serializable;

/**
 * @author Víctor Daniel Guevara Hernández
 * @param <T> data type to be stored in the list
 */
public class DoubleLinkedList<T> extends List<T>
{

    private DoubleNode<T> firstNode;
    private DoubleNode<T> lastNode;
    private int size;

    public class DoubleNode<N> implements Serializable
    {

        public N information;
        public DoubleNode next;
        public DoubleNode previous;

        public DoubleNode(N informacion) {
            this.information = informacion;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof DoubleNode) {
                if (((DoubleNode) o).information.equals(this.information)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void add(T t) {
        DoubleNode<T> newNode = new DoubleNode(t);
        if (firstNode == null) {
            firstNode = newNode;
        } else {
            newNode.previous = lastNode;
            newNode.previous.next = newNode;
        }
        lastNode = newNode;
        size++;
        super.onElementAdded.onElementAdded(t);
    }

    public void addAtStart(T t) {
        DoubleNode<T> newNode = new DoubleNode(t);
        newNode.next = firstNode;
        newNode.previous = null;
        firstNode = newNode;
        size++;
        super.onElementAdded.onElementAdded(t);
    }

    @Override
    public int indexOf(T t) {
        DoubleNode<T> node = firstNode;
        int index = 0;
        while (node != null) {
            if (node.information.equals(t)) {
                return index;
            }
            node = node.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean exists(T t) {
        return indexOf(t) != -1;
    }

    @Override
    public boolean remove(T t) {
        int index = indexOf(t);
        boolean eliminado = false;
        if (index == -1) {
            return false;
        }
        DoubleNode<T> p = firstNode;
        while (p != null && !eliminado) {
            if (p.information.equals(t)) {
                unlink(p);
                eliminado = true;
            } else {
                p = p.next;
            }
        }
        size--;
        super.onElementRemoved.onElementRemoved(t);
        return eliminado;
    }

    public boolean removeLast() {
        T info = lastNode.information;
        lastNode = lastNode.previous;
        size--;
        super.onElementRemoved.onElementRemoved(info);
        return true;
    }

    public boolean removeFirst() {
        T info = firstNode.information;
        firstNode = firstNode.next;
        size--;
        super.onElementRemoved.onElementRemoved(info);
        return true;
    }

    public T getFirst() {
        System.out.println("firstNode = " + firstNode);
        return firstNode.information;
    }

    public T getLast() {
        return lastNode.information;
    }

    private void unlink(DoubleNode<T> node) {
        T element = node.information;
        DoubleNode<T> previous = node.previous;
        DoubleNode<T> next = node.next;
        if (previous == null) {
            this.firstNode = next;
        } else {
            previous.next = next;
            node.previous = null;
        }
        if (next == null) {
            lastNode = previous;
        } else {
            next.previous = previous;
            node.next = null;
        }
        node.information = null;
    }

    @Override
    public boolean remove(int index) {
        T t = getNode(index).information;
        super.onElementRemoved.onElementRemoved(t);
        return remove(t);
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        firstNode = null;
        lastNode = null;
    }

    @Override
    public void set(int index, T info) {
        DoubleNode<T> toChange = getNode(index);
        toChange.information = info;
    }

    /**
     * Returns the node at the given index
     *
     * @param index the index
     * @return the node
     */
    public DoubleNode<T> getNode(int index) {
        if (index >= size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size());
        }
        DoubleNode<T> node = firstNode;
        for (int i = 0; i < index && node != null; i++) {
            node = node.next;
        }
        return node;
    }

    @Override
    public T get(int index) {
        if (getNode(index) == null) {
            return null;
        }
        return getNode(index).information;
    }

    @Override
    public boolean equals(Object anotherList) {
        if (anotherList instanceof DoubleLinkedList) {
            DoubleLinkedList list = (DoubleLinkedList) anotherList;
            if (list.size() != this.size()) {
                return false;
            }
            for (int i = 0; i < list.size; i++) {
                if (!list.get(i).equals(this.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void removeSubList(List<T> list) {
        if (list.equals(this)) {
            this.clear();
        }
    }

    public DoubleLinkedList<T> subList(int firstIndex, int lastIndex) {
        DoubleLinkedList<T> subList = new DoubleLinkedList<>();
        DoubleNode<T> p = this.firstNode;
        if (firstIndex >= lastIndex) {
            throw new IndexOutOfBoundsException("El primer índice es mayor que el segundo");
        }
        if (firstIndex < 0) {
            throw new IndexOutOfBoundsException("Primer índice: " + firstIndex);
        }
        if (lastIndex >= size) {
            throw new IndexOutOfBoundsException("Último índice: " + lastIndex);
        }
        for (int i = 0; i < firstIndex; i++) {
            p = p.next;
        }
        for (int i = firstIndex; i < lastIndex; i++) {
            subList.add(p.information);
            p = p.next;
        }
        return subList;
    }
}
