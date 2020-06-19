package tasky.controllers;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import tasky.actividades.Categoria;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class ControlCategoria extends HBox
{
    private Categoria categoria;
    private Text texto;
    
    public ControlCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.categoria.setOnNameChanged(nombre -> texto.setText(nombre));
        inicializarComponente();//parent);
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    private void inicializarComponente() {
        inicializarTexto();
        agregarTransicion();
        this.setMinHeight(50);
        //this.prefWidthProperty().bind(parent.widthProperty());
        this.setAlignment(Pos.CENTER_LEFT);
        this.getChildren().add(texto);
        this.setStyle("-fx-background-color: transparent;");
    }

    private void inicializarTexto() {
        texto = new Text(categoria.getNombre());
        texto.setStyle("-fx-fill: white; -fx-font-size:16px;");
        
    }
    
    public void setColor(String color){
        texto.setStyle("-fx-fill: " + color + "; -fx-font-size:16px;");
    }

    private void agregarTransicion() {
        FadeTransition ft = new FadeTransition(Duration.millis(800), this);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        FadeTransition ft2 = new FadeTransition(Duration.millis(800), this);
        ft2.setFromValue(0.1);
        ft2.setToValue(1.0);
        this.setOnMousePressed(e -> ft.play());
        this.setOnMouseReleased(e -> ft2.play());
    }


}
