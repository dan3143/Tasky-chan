package tasky.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import tasky.Tasky;
import tasky.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author "Víctor Daniel Guevara"
 */
public class BienvenidaController implements Initializable
{
    @FXML
    private AnchorPane pane;
    @FXML
    private JFXButton btComencemos;
    @FXML
    private JFXTextField tfNombre;
    private double X;
    private double Y;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes();
    }    
    private void inicializarComponentes()
    {
        permitirDrag();
        btComencemos.setDefaultButton(true);
    }
    private void permitirDrag()
    {
        pane.setOnMousePressed(event -> {
            X = event.getSceneX();
            Y = event.getSceneY();
        });
        pane.setOnMouseDragged(event -> {
            pane.getScene().getWindow().setX(event.getScreenX() - X);
            pane.getScene().getWindow().setY(event.getScreenY() - Y);
        });
    }
    
    @FXML
    private void registrarNombre() {
        Tasky.getInstance().getUsuario().setNombre(tfNombre.getText());
        Utilidades.cargarVentanaFXML("/tasky/layouts/PantallaPrincipal.fxml", "Pantalla principal", true, false).show();
        pane.getScene().getWindow().hide();
    }
}
