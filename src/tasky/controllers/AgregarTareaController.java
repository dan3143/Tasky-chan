package tasky.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tasky.Tasky;
import tasky.actividades.Actividad;
import tasky.actividades.Categoria;
import tasky.actividades.Meta;
import tasky.actividades.Prioridad;
import tasky.actividades.Tarea;
import tasky.actividades.TareaSecuencial;
import tasky.utilidades.Utilidades;
import tasky.utilidades.colecciones.Cola;

/**
 * FXML Controller class
 *
 * @author Víctor Daniel Guevara
 */
public class AgregarTareaController implements Initializable
{

    @FXML
    private JFXTextField tfNombre;
    @FXML
    private DatePicker dtDate;
    @FXML
    private JFXButton btAgregar;
    @FXML
    private JFXButton btCancelar;
    @FXML
    private JFXComboBox<String> cbTipo;
    @FXML
    private JFXRadioButton rbAlta;
    @FXML
    private JFXRadioButton rbNormal;
    @FXML
    private JFXRadioButton rbBaja;
    @FXML
    private JFXTextField tfMax;
    @FXML
    private JFXTextArea taNota;
    @FXML
    private JFXTextArea taSubActividades;
    @FXML
    private JFXButton btAgregarSub;
    @FXML
    private VBox vbSub;
    @FXML
    private VBox vbMain;
    
    private Cola<Actividad> pasos;

    private ToggleGroup group;
    private boolean today = false;
    private final ObservableList<String> tiposDeActividad = FXCollections.observableArrayList("Tarea", "Meta");
    private Categoria categoria;
    private boolean sub = false;
    private boolean escondidoMax = true;
    private boolean escondidoSub = true;
    private int i = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        btAgregar.setDefaultButton(true);
        cbTipo.setItems(tiposDeActividad);
        group = new ToggleGroup();
        rbAlta.setToggleGroup(group);
        rbNormal.setToggleGroup(group);
        rbBaja.setToggleGroup(group);
        rbNormal.setSelected(true);
        cbTipo.valueProperty().addListener((obs, viejo, nuevo) -> {
            i = 0;
            System.out.println("cbTipo.getValue() = " + cbTipo.getValue());
            if (cbTipo.getValue().equals("Meta")) {
                mostrarAnimacionMax(true);
                escondidoMax = false;
                if (!escondidoSub){    
                    vbSub.setVisible(false);
                    vbSub.setPadding(new Insets(-100, 0, 0, 0));
                    escondidoSub = true;
                }
            } else if (cbTipo.getValue().equals("Tarea secuencial")) {
                if (!escondidoMax){
                    mostrarAnimacionMax(false);
                    mostrarAnimacionSub(false);
                    escondidoMax = true;
                }
                vbSub.setVisible(true);
                vbSub.setPadding(new Insets(0, 0, 0, 0));
                mostrarAnimacionSub(true);
                pasos = new Cola();
                pasos.setOnElementAdded(elementAdded -> {
                    this.taSubActividades.setText(taSubActividades.getText() + "\n" + elementAdded.getNombre());
                });
                escondidoSub = false;
            } else {
                if (!escondidoMax){
                    mostrarAnimacionMax(false);
                    mostrarAnimacionSub(false);
                    escondidoMax = true;
                }
                if (!escondidoSub){    
                    vbSub.setVisible(false);
                    vbSub.setPadding(new Insets(-100, 0, 0, 0));
                    escondidoSub = true;
                }
            }
        });
        
    }

    private void mostrarAnimacionMax(boolean ida) {
        System.out.println("Corriendo animación...");
        new Timer().scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run() {
                if (i < 50) {
                    System.out.println("i = " + i);
                    tfMax.prefWidthProperty().set(tfMax.prefWidthProperty().get() + (ida ? 3 : -3));
                    cbTipo.prefWidthProperty().set(cbTipo.prefWidthProperty().get() + (ida ? -3 : 3));
                } else {
                    this.cancel();
                }
                i++;
            }
        }, 0, 7);
    }

    public void mostrarAnimacionSub(boolean ida) {
        System.out.println("Corriendo animación...");
        new Timer().scheduleAtFixedRate(new TimerTask()
        {
            private int i = 0;

            @Override
            public void run() {
                if (i < 100) {
                    vbSub.prefHeightProperty().set(tfMax.prefHeightProperty().get() + (ida ? 3 : -3));
                    vbMain.prefHeightProperty().set(cbTipo.prefHeightProperty().get() + (ida ? -3 : 3));
                } else {
                    this.cancel();
                }
                i++;
            }
        }, 0, 8);
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setToday(boolean today) {
        this.today = today;
        if (today) {
            dtDate.setDisable(true);
            dtDate.setPromptText("Se usará el día de hoy");
        }
    }

    @FXML
    private void cerrar() {
        this.btAgregar.getScene().getWindow().hide();
    }
    
    public void setSub(boolean sub){
        this.sub = sub;  
        if (!sub){
            tiposDeActividad.add("Tarea secuencial");
        }
    }
    
    @FXML
    private void agregarTarea() {
        tfNombre.setStyle("-fx-border-color: transparent");
        dtDate.setStyle("-fx-border-color: transparent");
        cbTipo.setStyle("-fx-border-color: transparent");
        tfMax.setStyle("-fx-border-color: transparent");
        if (comprobarDatos()) {
            Prioridad prioridad = getPrioridad();
            System.out.println("taNota.getText() = " + taNota.getText());
            if (cbTipo.getValue().equals("Tarea")) {
                add(new Tarea(tfNombre.getText(), taNota.getText(), today ? LocalDate.now() : dtDate.getValue(), prioridad));
            } else if (cbTipo.getValue().equals("Tarea secuencial")) {
                add(new TareaSecuencial(tfNombre.getText(), taNota.getText(), today ? LocalDate.now() : dtDate.getValue(), prioridad, pasos));
            } else {
                int maximo = Integer.parseInt(tfMax.getText());
                add(new Meta(tfNombre.getText(), taNota.getText(), today ? LocalDate.now() : dtDate.getValue(), prioridad, maximo));
            }
            this.btAgregar.getScene().getWindow().hide();
        }
    }

    public void add(Actividad ac) {
        if (sub) {
            this.pasos.enqueue(ac);
        } else {
            this.categoria.addActividad(ac);
        }
    }
    @FXML
    private void addSub() {
        //if (comprobarDatos()) {
            try {
                FXMLLoader cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/layouts/AgregarTarea.fxml"));
                VBox pane = cargador.load();
                AgregarTareaController controlador = cargador.getController();
                System.out.println("dtDate.getValue() = " + dtDate.getValue());
                controlador.setSub(true);
                controlador.pasos = this.pasos;
                controlador.setToday(categoria == Tasky.getInstance().getCategoria(0));
                Stage stage = new Stage();
                stage.setScene(new Scene(pane));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setTitle("Agregar tarea");
                stage.showAndWait();
            } catch (IOException ex) {
                System.err.println("Error al abrir ventana para agregar tareas");
            }
        //}
    }

    private boolean comprobarDatos() {
        LocalDate date = today ? LocalDate.now() : dtDate.getValue();
        String nombre = tfNombre.getText();
        String tipo = cbTipo.getValue();
        if (Utilidades.quitarEspacios(nombre).equals("")) {
            tfNombre.setStyle("-fx-border-color: #EF476F");
            tfNombre.setPromptText("Debe ingresar un nombre");
            return false;
        }
        if (date == null) {
            dtDate.setStyle("-fx-border-color: #EF476F");
            return false;
        }
        if (tipo == null) {
            cbTipo.setStyle("-fx-border-color: #EF476F");
            return false;
        }
        if (tipo.equals("Meta")) {
            String max = tfMax.getText();
            if (Utilidades.quitarEspacios(max).equals("") || !Utilidades.esNumero(max) || Integer.parseInt(max) < 0) {
                tfMax.setStyle("-fx-border-color: #EF476F");
                return false;
            }
        }
        return true;
    }
    private Prioridad getPrioridad(){
        return this.rbAlta.isSelected() ? Prioridad.ALTA
                    : this.rbNormal.isSelected() ? Prioridad.NORMAL : Prioridad.NORMAL;
    }
}
