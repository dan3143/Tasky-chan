package tasky.actividades;
import java.io.Serializable;
import tasky.utilidades.colecciones.DoubleLinkedList;
/**
 * @author Víctor Daniel Guevara
 */
public class Categoria implements Serializable{
    
    private String nombre;
    private final boolean eliminable;
    private final DoubleLinkedList<Actividad> actividades;
    transient private NameChangedListener nameChangedListener;
    
    public Categoria(String nombre, boolean eliminable) {
        this.nombre = nombre;
        this.actividades = new DoubleLinkedList<>();
        this.eliminable = eliminable;
        this.nameChangedListener = name -> {};
    }
    
    public String getNombre() {
        return nombre;  
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        nameChangedListener.onNameChanged(nombre);
    }
    
    public void setOnNameChanged(NameChangedListener cl){
        this.nameChangedListener = cl;
    }

    public Actividad getActividad(int index) {
        return this.actividades.get(index);
    }
    
    public DoubleLinkedList<Actividad> getActividades(){
        return actividades;
    }
    
    public void addActividad(Actividad a){
        this.actividades.add(a);
        a.setOnActivityFinished(actividad -> {
            this.actividades.remove(actividad);
        });
    }

    public boolean isEliminable() {
        return eliminable;
    }
    
    @Override
    public boolean equals(Object o){
        if (o instanceof Categoria){
            Categoria cat = (Categoria) o;
            return cat.isEliminable() == this.isEliminable() && cat.getActividades().equals(this.getActividades()) && cat.getNombre().equals(this.getNombre());
        }
        return false;
    }
}
