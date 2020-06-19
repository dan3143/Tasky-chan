package tasky.utilidades.colecciones;

import java.io.Serializable;

/**
 *
 * @author Víctor Daniel Guevara
 * @param <T> tipo de datos en la pila
 */
public class Pila<T> implements Serializable{
    private final DoubleLinkedList<T> elementos;
    
    public Pila(){
        elementos = new DoubleLinkedList();
    }
    
    public void setOnElementAdded(ElementAddedListener elementAdded){
        this.elementos.setOnElementAdded(elementAdded);
    }
    
    public void setOnElementRemoved(ElementRemovedListener elementRemoved){
        this.elementos.setOnElementRemoved(elementRemoved);
    }
    
    public void push(T t){
        elementos.add(t);
    }
    public T pop(){
        T elemento = elementos.getLast();
        elementos.removeLast();
        return elemento;
    }
    public T peek(){
        return elementos.getLast();
    }
    public boolean isEmpty(){
        return elementos.isEmpty();
    }
    
}
