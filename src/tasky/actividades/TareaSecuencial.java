package tasky.actividades;

import java.time.LocalDate;
import tasky.utilidades.colecciones.Cola;
import tasky.utilidades.colecciones.ElementAddedListener;
import tasky.utilidades.colecciones.ElementRemovedListener;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class TareaSecuencial extends Actividad
{
    private Cola<Actividad> pasos;
    
    public TareaSecuencial(String nombre, String descripcion, LocalDate fechaFin, Prioridad prioridad) {
        super(nombre, descripcion, fechaFin, prioridad);
        pasos = new Cola();
    }
    public TareaSecuencial(String nombre, String descripcion, LocalDate fechaFin, Prioridad prioridad, Cola<Actividad> pasos) {
        this(nombre, descripcion, fechaFin, prioridad);
        while(!pasos.isEmpty()){
            this.addPaso(pasos.dequeue());
        }
    }    
    public final void addPaso(Actividad act){
        act.setOnActivityFinished(actividad -> finalizarPaso());
        pasos.enqueue(act);
    }
    
    public void setOnPasoAdded(ElementAddedListener<Actividad> ea){
        pasos.setOnElementAdded(ea);
    }
    
    public void setOnPasoRemoved(ElementRemovedListener<Actividad> er){
        pasos.setOnElementRemoved(er);
    }
    
    public void finalizarPaso(){
        pasos.dequeue();
        if (pasos.isEmpty()){
            super.setEstado(Estado.TERMINADA);
        }
    }
    
    public Actividad getPasoActual(){
        return pasos.front();
    }
    
    public boolean tienePasos(){
        System.out.println("pasos.isEmpty() = " + pasos.isEmpty());
        return !pasos.isEmpty();
    }
    
}
