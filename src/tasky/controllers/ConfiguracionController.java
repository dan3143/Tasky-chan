package tasky.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import tasky.Tasky;
import static tasky.utilidades.Utilidades.*;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class ConfiguracionController implements Initializable
{
    @FXML
    private JFXButton btGuardar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXToggleButton tgVacaciones;
    @FXML
    private HBox hbCambiarNombre;
    @FXML
    private Text txtNombreActual;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarNombre();
    }   
    
    private void inicializarNombre(){
        if (Tasky.getInstance().isEnVacaciones()){
            tgVacaciones.setSelected(true);
        }else{
            tgVacaciones.setSelected(false);
        }
        txtNombreActual.setText(Tasky.getInstance().getUsuario().getNombre());
    }
    
    @FXML
    private void cambiarNombre(){
        String texto = inputDialog("Ingresa un nuevo nombre", Tasky.getInstance().getUsuario().getNombre());
        if (quitarEspacios(texto).equals("")){
            
        }else{
            txtNombreActual.setText(texto);
        }
    }
    
    @FXML
    private void cerrar(){
        hbCambiarNombre.getScene().getWindow().hide();
    }
    
    @FXML
    private void guardar(){
        Tasky.getInstance().setEnVacaciones(tgVacaciones.isSelected());
        Tasky.getInstance().getUsuario().setNombre(txtNombreActual.getText());
        hbCambiarNombre.getScene().getWindow().hide();
    }
    
}
