package tasky.actividades;

import java.io.Serializable;
import java.time.LocalDate;
import static tasky.actividades.Estado.*;
        

/**
 *
 * @author Víctor Daniel Guevara
 */
public class Meta extends Actividad
{
    private Integer maximo;
    private Integer actual;
    transient private ValueChangedListener valueListener;

    public Meta(String nombre, String descripcion, LocalDate fechaFin, Prioridad prioridad, Integer maximo) {
        super(nombre, descripcion, fechaFin, prioridad);
        this.maximo = maximo;
        actual = 0;
        valueListener = n -> {};
    }
    
    public interface ValueChangedListener extends Serializable{
        void onValueChanged(int value);
    }
    
    public void setOnValueChanged(ValueChangedListener vc){
        valueListener = vc;
    }
    
    public void aumentarContador(Integer n) {
        actual += n;
        if (actual.equals(maximo)) {
            super.setEstado(TERMINADA);
        }
        valueListener.onValueChanged(actual);
    }

    public void disminuirContador(Integer n) {
        Integer tot = actual - n;
        if (tot.compareTo(maximo) < 0){
            setEstado(PENDIENTE);
        }
        if (tot < 0) {
            actual = 0;
        } else {
            actual = tot;
        }
        valueListener.onValueChanged(actual);
    }

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public int getActual() {
        return actual;
    }
}
