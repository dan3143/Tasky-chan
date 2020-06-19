package tasky.actividades;

import java.io.Serializable;
import java.time.LocalDate;
import tasky.Tasky;
import static tasky.actividades.Estado.*;
import tasky.utilidades.Utilidades;
/**
 *
 * @author "Víctor Daniel Guevara"
 */
public abstract class Actividad implements Serializable
{
    protected String nombre;
    protected String descripcion;
    protected LocalDate fechaFin;
    protected Estado estado;
    protected Prioridad prioridad;
    protected ActivityFinishedListener finishedListener;
    transient protected NameChangedListener nameListener;
    transient protected StatusChangedListener statusListener;
    
    protected Actividad(String nombre, String descripcion, LocalDate fechaFin, Prioridad prioridad){
        this.nombre = nombre;
        this.fechaFin = fechaFin;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
        nameListener = name -> {};
        statusListener = status -> {};
        finishedListener = activity -> {};
        if (LocalDate.now().isAfter(fechaFin)){
            setEstado(VENCIDA);
            Tasky.getInstance().aumentarPuntos(-prioridad.getValue());
        }else{
            setEstado(PENDIENTE);
        }
    }
    
    public interface StatusChangedListener extends Serializable{
        void onStatusChanged(Estado estado);
    }
    
    public interface ActivityFinishedListener extends Serializable{
        void onActivityFinished(Actividad actividad);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        nameListener.onNameChanged(nombre);
    }
    
    public String getDescripcion(){
        return this.descripcion;
    }
    
    public void setOnNameChanged(NameChangedListener nl) {
        nameListener = nl;
    }
    
    public void setOnStatusChanged(StatusChangedListener sl){
        statusListener = sl;
    }
    
    public void setOnActivityFinished(ActivityFinishedListener al){
        finishedListener = al;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
    
    public Estado getEstado() {
        return estado;
    }

    public final void setEstado(Estado estado) {
        this.estado = estado;
        this.statusListener.onStatusChanged(estado);
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }
    
    public void terminar(){
        int puntos = this.prioridad.getValue();
        if (Utilidades.isDateInRange(this.getFechaFin(), LocalDate.now().plusDays(2), LocalDate.now().plusDays(7))){
            puntos *= 2;
        }
        if (this.getEstado() == TERMINADA) {
            Tasky.getInstance().aumentarPuntos(puntos);
        }
        Meta meta = null;
        if (this instanceof Meta && (meta = (Meta)this).getActual() > meta.getMaximo()){
            puntos += meta.getActual() - meta.getMaximo();
        }
        finishedListener.onActivityFinished(this);
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }
}
