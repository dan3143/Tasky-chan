package tasky;

import java.io.Serializable;

/**
 *
 * @author "Víctor Daniel Guevara"
 */
public class Usuario implements Serializable
{   
    private String nombre;
    
    public Usuario(){
        nombre = "";
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}
