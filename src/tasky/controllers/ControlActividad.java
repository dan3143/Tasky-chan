package tasky.controllers;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import tasky.actividades.Actividad;
import static tasky.actividades.Estado.*;
import tasky.actividades.Meta;
import tasky.actividades.Prioridad;
import tasky.actividades.TareaSecuencial;
import tasky.utilidades.Utilidades;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class ControlActividad implements Initializable
{

    @FXML
    private Text nombreActividad;
    @FXML
    private CheckBox cbCompletada;
    @FXML
    private Text txtFecha;
    @FXML
    private Text txtPrioridad;
    @FXML
    private GridPane gpActividad;
    @FXML
    private ImageView imgBorrar;
    @FXML
    private VBox vbSub;
    @FXML
    private VBox vbAct;
    
    private Actividad actividad;

    private ControlActividad(Actividad actividad) {
        this.actividad = actividad;
        this.actividad.setOnStatusChanged(status -> {
            this.cbCompletada.setSelected(status == TERMINADA);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String n = "";
        boolean disable = false;
        if (actividad instanceof Meta) {
            Meta act = (Meta) actividad;
            n = "(" + act.getActual() + "/" + act.getMaximo() + ")";
            act.setOnValueChanged(value -> {
                String na = " (" + act.getActual() + "/" + act.getMaximo() + ")";
                this.nombreActividad.setText(getActividad().getNombre() + na);
            });
            gpActividad.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                if (e.getButton().equals(MouseButton.SECONDARY)) {
                    act.disminuirContador(1);
                } else {
                    act.aumentarContador(1);
                }
            });
            disable = true;
        } else if (actividad instanceof TareaSecuencial) {
            TareaSecuencial ts = (TareaSecuencial) actividad;
            if (ts.tienePasos()) {
                vbSub.getChildren().add(ControlActividad.crearControlActividad(ts.getPasoActual(), true));
            }
            ts.setOnPasoRemoved(element -> {
                vbSub.getChildren().clear();
                if (ts.tienePasos()) {
                    vbSub.getChildren().add(ControlActividad.crearControlActividad(ts.getPasoActual(), true));
                }
            });
            disable = true;
        }
        nombreActividad.setText(actividad.getNombre() + " " + n);
        nombreActividad.setStrikethrough(actividad.getEstado() == TERMINADA);
        cbCompletada.setDisable(disable);
        cbCompletada.setSelected(actividad.getEstado() == TERMINADA);
        this.txtFecha.setText(getFechaAsString(actividad.getFechaFin()));
        this.cbCompletada.selectedProperty().addListener((obs, viejo, nuevo) -> {
            if (actividad.getEstado() != VENCIDA) {
                if (viejo) {
                    actividad.setEstado(PENDIENTE);
                    nombreActividad.setStrikethrough(false);
                } else {
                    new MediaPlayer(new Media(tasky.Tasky.class.getResource("/resources/sounds/wink.mp3").toExternalForm())).play();
                    actividad.setEstado(TERMINADA);
                    nombreActividad.setStrikethrough(true);
                }
            }
        });
        setPrioridad(actividad.getPrioridad());
        if (actividad.getEstado() == VENCIDA) {
            nombreActividad.setFill(Color.RED);
            this.txtPrioridad.setText("Vencida");
        }
        imgBorrar.setOnMouseClicked(e -> borrar());
        Tooltip.install(this.gpActividad, new Tooltip(this.actividad.getDescripcion()));
    }

    public void setPrioridad(Prioridad p) {
        switch (p) {
            case ALTA:
                if (Utilidades.isDateInRange(actividad.getFechaFin(), LocalDate.now(), LocalDate.now().plusDays(2))) {
                    txtPrioridad.setStyle("-fx-fill: red");
                    txtPrioridad.setText("¡¡Termina esta rápido!!");
                } else {
                    txtPrioridad.setText("Prioridad alta");
                }
                break;
            case NORMAL:
                txtPrioridad.setText("Prioridad normal");
                break;
            case BAJA:
                txtPrioridad.setText("No es tan importante...");
                break;
        }
    }

    private String getFechaAsString(LocalDate fin) {
        String dia = "";
        if (actividad.getFechaFin().equals(LocalDate.now())) {
            dia = "hoy";
        } else if (Utilidades.isDateInRange(fin, LocalDate.now(), LocalDate.now().plusDays(7))) {
            switch (fin.getDayOfWeek()) {
                case SUNDAY:
                    dia = "domingo";
                    break;
                case MONDAY:
                    dia = "lunes";
                    break;
                case TUESDAY:
                    dia = "martes";
                    break;
                case WEDNESDAY:
                    dia = "miércoles";
                    break;
                case THURSDAY:
                    dia = "jueves";
                    break;
                case FRIDAY:
                    dia = "viernes";
                    break;
                case SATURDAY:
                    dia = "sábado";
                    break;
            }
        } else {
            dia = fin.getDayOfMonth() + " de " + fin.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        }
        return dia;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }

    public void setSub(Boolean sub){
        if (sub){
            this.vbAct.setStyle("-fx-background-color: beige");
            this.vbAct.prefWidthProperty().set(100);
        }
    }

    public static VBox crearControlActividad(Actividad actividad, boolean sub) {
        FXMLLoader cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/layouts/ControlActividad.fxml"));
        ControlActividad controlador = new ControlActividad(actividad);
        cargador.setController(controlador);
        VBox pane;
        try {
            pane = cargador.load();
            controlador.setSub(sub);
            return pane;
        } catch (IOException e) {
            System.err.println("Error al cargar un ControlActividad");
            e.printStackTrace();
            return new VBox();
        }
    }
    
    public static VBox crearControlActividad(Actividad actividad) {
        return crearControlActividad(actividad, false);
    }

    @FXML
    private void borrar() {
        System.out.println("Terminando la actividad...");
        actividad.terminar();
    }

}
