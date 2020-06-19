package tasky.actividades;

import java.io.Serializable;

/**
 *
 * @author Víctor Daniel Guevara
 */
public interface NameChangedListener extends Serializable
{
    void onNameChanged(String name);
}
