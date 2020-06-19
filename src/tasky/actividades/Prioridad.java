package tasky.actividades;

/**
 *
 * @author Víctor Daniel Guevara
 */
public enum Prioridad
{
    ALTA(3),
    NORMAL(2),
    BAJA(1);
    
    private final int value;
    
    private Prioridad(int value){
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }
    
}
