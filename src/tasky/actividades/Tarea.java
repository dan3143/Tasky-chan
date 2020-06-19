package tasky.actividades;

import java.time.LocalDate;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class Tarea extends Actividad
{
    public Tarea(String nombre, String descripcion, LocalDate fecha, Prioridad prioridad) {
        super(nombre, descripcion, fecha, prioridad);
    }
}
