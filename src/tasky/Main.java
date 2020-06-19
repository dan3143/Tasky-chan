package tasky;

import java.io.BufferedReader;
import java.io.IOException;
import static java.lang.Math.random;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import tasky.actividades.Actividad;
import static tasky.utilidades.Utilidades.*;
import tasky.utilidades.colecciones.DoubleLinkedList;

/**
 *
 * @author d-ani
 */
public class Main extends Application
{

    private final TimerTask task = new TimerTask()
    {
        @Override
        public void run() {
            Platform.runLater(() -> {
                Actividad act = Tasky.getInstance().getActividadMasImportante();
                if (act == null) {
                    Tasky.getInstance().saludar();
                } else {
                    try 
                    {
                        DoubleLinkedList<String> saludosPosibles = new DoubleLinkedList();
                        BufferedReader buffer = openFileRead("/resources/ficheros/dialogos/recordatorio");
                        while (buffer.ready()) {
                            saludosPosibles.add( buffer.readLine());
                        }
                        int random = (int) (random() * (saludosPosibles.size()));
                        String mensaje = saludosPosibles.get(random);
                        mensaje = reemplazar(mensaje, "[usuario]", Tasky.getInstance().getUsuario().getNombre());
                        mensaje = reemplazar(mensaje, "[actividad]", act.getNombre());
                        Tasky.getInstance().notificar(mensaje);
                    } catch (IOException ex) {
                        System.err.println("Error de IO al cargar mensaje");
                    }
                }
            });
        }
    };

    @Override
    public void init() {
        Tasky.inicializarTasky();
    }

    private static Timer timer;

    @Override
    public void start(Stage primaryStage) throws IOException {
        if (quitarEspacios(Tasky.getInstance().getUsuario().getNombre()).equals("")) {
            cargarVentanaFXML("/tasky/layouts/Bienvenida.fxml", "¡Hola!").show();
        } else {
            cargarVentanaFXML("/tasky/layouts/PantallaPrincipal.fxml", "Pantalla principal", true, false).show();
            Tasky.getInstance().saludar();
        }
        timer = new Timer();
        timer.schedule(task, Tasky.getInstance().getPeriodo(), Tasky.getInstance().getPeriodo());
    }

    public static void main(String[] args) {
        launch(args);
        Tasky.serializar();
        System.exit(0);
    }
}
