package tasky.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tasky.Tasky;
import tasky.actividades.Actividad;
import tasky.actividades.Categoria;
import tasky.controllers.ControlActividad;
import tasky.utilidades.Utilidades;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class TareasPaneController implements Initializable
{

    @FXML
    private HBox hbAgregarTarea;
    @FXML
    private HBox hbInformacionCategoria;
    @FXML
    private Text txtNombre;
    @FXML
    private ImageView imgBorrar;
    @FXML
    private ImageView imgEditar;
    @FXML
    private VBox vbTareas;

    private Categoria categoria;
    private ScrollPane sc;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void setTitulo(String texto) {
        txtNombre.setText(texto);
    }
    
    public void setSc(ScrollPane sc){
        this.sc = sc;
    }

    public void setControlVisible(boolean visible) {
        hbAgregarTarea.setVisible(visible);
        hbInformacionCategoria.setVisible(visible);
        imgBorrar.setVisible(visible);
        imgEditar.setVisible(visible);
    }

    public HBox getAgregar() {
        return hbAgregarTarea;
    }
    
//    private void bindWidth(ScrollPane pane){
//        this.vbTareas.prefWidthProperty().bind(pane.widthProperty());
//    }

    public void cargarTareas(Categoria c) {
        categoria = c;
        categoria.getActividades().setOnElementAdded(actividad -> this.vbTareas.getChildren().add(ControlActividad.crearControlActividad(actividad)));
        categoria.getActividades().setOnElementRemoved(actividad -> cargarTareas(categoria));
        setControlVisible(true);
        vbTareas.getChildren().clear();
        setTitulo(categoria.getNombre());
        if (categoria != Tasky.getInstance().getCategoria(0)) {
            imgBorrar.setOnMouseClicked(e -> {
                Tasky.getInstance().removerCategoria(categoria);
            });
            imgEditar.setOnMouseClicked(e -> {
                editarNombre(categoria);
            });
            cargarControlesTareas();
        } else {
            imgBorrar.setVisible(false);
            imgEditar.setVisible(false);
            cargarControlesTareasHoy();
        }
    }
    
    private void editarNombre(Categoria c){
        String nuevoNombre = Utilidades.inputDialog("Ingresa el nuevo nombre", c.getNombre());
        if (!Utilidades.quitarEspacios(nuevoNombre).equals("")){
            c.setNombre(nuevoNombre);
            txtNombre.setText(nuevoNombre);
        }
    }

    private void cargarControlesTareas() {
        for (Actividad actividad : categoria.getActividades()) {
            VBox actividadControl = ControlActividad.crearControlActividad(actividad);            
            this.vbTareas.getChildren().add(actividadControl);
        }
    }

    private void cargarControlesTareasHoy() {
        for (Categoria cat : Tasky.getInstance().getCategorias()) {
            for (Actividad actividad:cat.getActividades() ) {
                if (actividad.getFechaFin().equals(LocalDate.now())) {
                    VBox actividadControl = ControlActividad.crearControlActividad(actividad);
                    this.vbTareas.getChildren().add(actividadControl);
                }
            }
        }
    }

    @FXML
    private void abrirAgregarTarea() {
        try {
            FXMLLoader cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/layouts/AgregarTarea.fxml"));
            VBox pane = cargador.load();
            AgregarTareaController controlador = cargador.getController();
            controlador.setCategoria(categoria);
            controlador.setToday(categoria == Tasky.getInstance().getCategoria(0));
            controlador.setSub(false);
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Agregar tarea");
            stage.showAndWait();
        } catch (IOException ex) {
            System.err.println("Error al abrir ventana para agregar tareas");
            ex.printStackTrace();
        }
    }
}
