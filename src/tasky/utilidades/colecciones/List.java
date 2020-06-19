package tasky.utilidades.colecciones;

/**
 *
 * @author "Víctor Daniel Guevara"
 */
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A list
 *
 * @author d-ani
 * @param <T>
 */
public abstract class List<T> implements Iterable<T>, Serializable
{
    transient protected ElementRemovedListener<T> onElementRemoved;
    transient protected ElementAddedListener<T> onElementAdded;
    
    public List(){
        onElementRemoved = element -> {};
        onElementAdded = element -> {};
    }
    
    public void setOnElementRemoved(ElementRemovedListener<T> er){
        onElementRemoved = er;
    }
    public void setOnElementAdded(ElementAddedListener<T> ea){
        onElementAdded = ea;
    }
    
    /**
     * Adds an element at the end of the list
     *
     * @param t the element to be added
     */
    public abstract void add(T t);

    /**
     * Returns the index of the given element, starting from zero
     *
     * @param t the element
     * @return index of the given element
     */
    public abstract int indexOf(T t);

    /**
     * Returns the size of the list
     *
     * @return size of the list
     */
    public abstract int size();

    /**
     * Checks if the given element exists in the list
     *
     * @param t the element
     * @return true if t exists, false if it does not
     */
    public abstract boolean exists(T t);

    /**
     * Removes the given element from the list
     *
     * @param t the element to be removed
     * @return true, if the operation was succesful; false if not
     */
    public abstract boolean remove(T t);

    /**
     * Removes the element at the given index
     *
     * @param index the index of the element
     * @return true, if the operation was succesful; false if not
     * @throws IndexOutOfBoundsException
     */
    public abstract boolean remove(int index);

    /**
     * Checks if the list is empty
     *
     * @return true if the list is empty; false if not
     */
    public abstract boolean isEmpty();

    /**
     * Removes all the elements from the list
     */
    public abstract void clear();

    /**
     * Replaces the data in the element at the given index, with the given data
     *
     * @param index the index of the element to be replaced
     * @param info the info the element is going to be replaced with
     */
    public abstract void set(int index, T info);

    /**
     * Gets the element at the given index
     *
     * @param index the index
     * @return the element
     */
    public abstract T get(int index);

    /**
     * Deletes the sub-lists from the current list
     *
     * @param list
     */
    //public abstract void removeAllSubLists(List<T> list);

    /**
     * Checks if the given sub-list is contained within the current list
     *
     * @param lista the sub-list
     * @return a SimpleLinkedList of two integers, indicating where the sub-list begins and where it ends within the current list
     */
    public boolean subListExists(List<T> lista) {
        boolean existe = true;
        int index = indexOf(lista.get(0));
        boolean subListExists;
        if (index == -1) {
            return false;
        }
        for (int i = index, indexLista = 0; i < lista.size(); i++, indexLista++) {
            if (!lista.get(indexLista).equals(get(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Prints the current list
     */
    public void imprimir() {
        for (T t : this) {
            System.out.print(t + " ");
        }
        System.out.println();
    }

    //Makes the list iterable
    public class IteradorLista implements Iterator<T> {
        int actual = 0;
        @Override
        public boolean hasNext() {
            return actual < List.this.size();
        }
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(actual++);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }
}
