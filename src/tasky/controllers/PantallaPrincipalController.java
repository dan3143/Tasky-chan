package tasky.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tasky.Tasky;
import tasky.actividades.Categoria;
import tasky.controllers.ControlCategoria;
import tasky.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class PantallaPrincipalController implements Initializable
{

    @FXML
    private BorderPane pane;
    @FXML
    private ScrollPane scTareas;
    @FXML
    private ScrollPane scCategorias;
    @FXML
    private ImageView imgSettings;
    @FXML
    private ImageView imgMinimizar;
    @FXML
    private ImageView imgCerrar;

    private VBox vbTareas;
    private VBox vbCategorias;
    private CategoriasPaneController controladorCategorias;
    private TareasPaneController controladorTareas;
    private double X;
    private double Y;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarComponentes(true);
        Tasky.getInstance().getCategorias().setOnElementAdded(categoria -> agregarCategoria(categoria, true));
        Tasky.getInstance().getCategorias().setOnElementRemoved(categoria -> {
            inicializarComponentes(false);
            this.controladorTareas.cargarTareas(Tasky.getInstance().getCategoria(Tasky.getInstance().getCategorias().size() - 1));        
        });
    }

    //<editor-fold desc="Inicialización" defaultstate="collapsed">
    public void inicializarComponentes(boolean inicio) {
        inicializarPanes(inicio);
        inicializarVistasCategoria();
        permitirDrag();
        this.imgCerrar.setOnMouseClicked(e -> imgCerrar.getScene().getWindow().hide());
        this.imgMinimizar.setOnMouseClicked(e -> ((Stage)imgMinimizar.getScene().getWindow()).setIconified(true));
    }

    /**
     * Inicializa los panes
     *
     * @param inicio indica si se llama al método durante el inicio de la aplicación o después
     */
    private void inicializarPanes(boolean inicio) {
        TareasPaneController controlador = null;
        try {
            FXMLLoader cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/layouts/CategoriasPane.fxml"));
            vbCategorias = cargador.load();
            controladorCategorias = cargador.getController();
            cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/layouts/TareasPane.fxml"));
            vbTareas = cargador.load();
            controladorTareas = cargador.getController();
            
            scTareas.setContent(vbTareas);
            scTareas.setFitToWidth(true);
            controladorTareas.setSc(scTareas);
            scTareas.setHbarPolicy(ScrollBarPolicy.NEVER);
            scCategorias.setContent(vbCategorias);
            scCategorias.setFitToWidth(true);
            scCategorias.setHbarPolicy(ScrollBarPolicy.NEVER);
            
            if (inicio) {
                controladorTareas.cargarTareas(Tasky.getInstance().getCategoria(0));
            }
        } catch (IOException ex) {
            System.err.println("Error al inicializar uno de los vbox");
        }
    }

    private void agregarCategoria(Categoria c, boolean cargarAutomaticamente) {
        ControlCategoria control = new ControlCategoria(c);//, vbCategorias);
        control.setOnMouseClicked(e -> {
            controladorTareas.cargarTareas(control.getCategoria());
            cambiarColor(control);
        });
        vbCategorias.getChildren().add(control);
        if (cargarAutomaticamente) {
            controladorTareas.cargarTareas(control.getCategoria());
        }
    }

    private void cambiarColor(ControlCategoria control) {
        for (Node node : vbCategorias.getChildren()) {
            if (node instanceof ControlCategoria) {
                ((ControlCategoria) node).setColor("white");
            }
        }
        control.setColor("white");
    }

    private void inicializarVistasCategoria() {
        controladorCategorias.getCosasHoy().setOnMouseClicked(e -> controladorTareas.cargarTareas(Tasky.getInstance().getCategoria(0)));
        boolean primero = true;
        for (Categoria categoria : Tasky.getInstance().getCategorias()) {
            if (!primero) {
                agregarCategoria(categoria, false);
            } else {
                primero = false;
            }
        }
    }
    //</editor-fold>

    @FXML
    private void abrirConfiguracion() {
        RotateTransition rt = new RotateTransition(Duration.millis(300), imgSettings);
        rt.setByAngle(90);
        rt.play();
        Utilidades.cargarVentanaFXML("/tasky/layouts/Configuracion.fxml", "Configuración de Tasky", false, false).show();
    }

    private void permitirDrag() {
        pane.setOnMousePressed(event -> {
            X = event.getSceneX();
            Y = event.getSceneY();
        });
        pane.setOnMouseDragged(event -> {
            pane.getScene().getWindow().setX(event.getScreenX() - X);
            pane.getScene().getWindow().setY(event.getScreenY() - Y);
        });
    }

}
