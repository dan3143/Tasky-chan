package tasky.utilidades;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class InputDialogController implements Initializable
{
    public static enum Seleccion{
        CANCELAR,
        ACEPTAR
    }
    @FXML
    private JFXTextField tfDatos;
    @FXML
    private JFXButton btAceptar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private Text textMessage;
    @FXML
    private AnchorPane pane;
    
    private double X;
    private double Y;
    
    private Seleccion seleccion;
    private String informacion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(()->tfDatos.requestFocus());
        seleccion = Seleccion.CANCELAR;
        btAceptar.setDefaultButton(true);
        informacion = "";
        pane.setOnMousePressed(event
                -> {
            X = event.getSceneX();
            Y = event.getSceneY();
        });
        pane.setOnMouseDragged(event
                -> {
            pane.getScene().getWindow().setX(event.getScreenX() - X);
            pane.getScene().getWindow().setY(event.getScreenY() - Y);
        });
    }
    @FXML
    private void aceptar(){
        seleccion = Seleccion.ACEPTAR;
        informacion = tfDatos.getText();
        cerrar();
    }
    @FXML
    private void cancelar(){
        seleccion = Seleccion.CANCELAR;
        cerrar();
    }
    private void cerrar(){
        textMessage.getParent().getScene().getWindow().hide();
    }
    
    public void setText(String message){
        textMessage.setText(message);
    }
    
    public String getResult(){
        return informacion;
    }
    
    public Seleccion getSeleccion(){
        return seleccion;
    }
    
    public void setDefault(String def){
        this.tfDatos.setText(def);
    }
}
