package tasky.utilidades.colecciones;

import java.io.Serializable;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class Cola<T> implements Serializable
{

    private final DoubleLinkedList<T> elementos;

    public Cola() {
        elementos = new DoubleLinkedList();
    }

    public void setOnElementAdded(ElementAddedListener<T> elementAdded) {
        this.elementos.setOnElementAdded(elementAdded);
    }

    public void setOnElementRemoved(ElementRemovedListener<T> elementRemoved) {
        this.elementos.setOnElementRemoved(elementRemoved);
    }

    public T front() {
        return elementos.getFirst();
    }

    public T dequeue() {
        T elemento = elementos.getFirst();
        elementos.removeFirst();
        return elemento;
    }

    public void enqueue(T elemento) {
        elementos.add(elemento);
    }
    
    public boolean isEmpty(){
        return elementos.isEmpty();
    }
}
