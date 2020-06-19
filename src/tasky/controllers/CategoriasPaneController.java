package tasky.controllers;

import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tasky.Tasky;
import tasky.actividades.Categoria;
import tasky.utilidades.Utilidades;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class CategoriasPaneController implements Initializable
{
    @FXML
    private JFXTextField tfAgregarCategoria;
    @FXML
    private VBox vbCategorias;
    @FXML
    private HBox hbCosasParaHoy;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfAgregarCategoria.setOnAction(this::agregarCategoria);
    }
    /*public void bindWidthWith(ObservableDoubleValue property){
        vbCategorias.prefWidthProperty().bind(property);
    }*/
    private void agregarCategoria(ActionEvent e) {
        String nombre = tfAgregarCategoria.getText();
        String color = ";-fx-text-fill: white;-fx-prompt-text-fill: white;";
        if (Utilidades.quitarEspacios(nombre).equals("")){
            tfAgregarCategoria.setStyle("-fx-border-color: #9B2915" + color);   
        }else{
            Tasky.getInstance().agregarCategoria(new Categoria(tfAgregarCategoria.getText(), true));
            tfAgregarCategoria.setText("");
            tfAgregarCategoria.setStyle("-fx-border-color: transparent" + color);
        }
    }
    public HBox getCosasHoy(){
        return hbCosasParaHoy;
    }
}
