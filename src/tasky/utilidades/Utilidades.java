package tasky.utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tasky.Main;
import tasky.controllers.PantallaPrincipalController;
import tasky.utilidades.colecciones.DoubleLinkedList;

/**
 *
 * @author "Víctor Daniel Guevara"
 */
public class Utilidades
{

    public static String RUTA_ABSOLUTA = new File("").getAbsolutePath();

    private static void centrarStage(Stage stage, double height, double width) {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }

    public static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }
    
    /**
     * Indica si una fecha está en el rango indicado
     * @param date la fecha
     * @param min el extremo inferior del rango, exclusivo
     * @param max el extremo superior, inclusivo
     * @return true si está en el rango, false si no
     */
    public static boolean isDateInRange(LocalDate date, LocalDate min, LocalDate max){
        return date.isAfter(min) && date.isBefore(max) || date.equals(min) || date.equals(max);
    }

    /**
     * Abre una ventana FXML, con estilo UNDECORATED
     *
     * @param URL la ubicación del archivo FXML
     * @param nombre el nombre de la ventana
     * @param undecorated un booleano que indica si la ventana será undecorated o no
     * @param resizable indica si la ventana será resizable
     * @return ventana con el FXML cargado
     */
    public static Stage cargarVentanaFXML(String URL, String nombre, boolean undecorated, boolean resizable) {
        Pane pane;
        Stage ventana = new Stage();
        FXMLLoader cargador = new FXMLLoader(Main.class.getResource(URL));
        try {
            pane = cargador.load();
            if (undecorated) {
                ventana.initStyle(StageStyle.UNDECORATED);
            }
            Scene s = new Scene(pane);
            s.setFill(Color.TRANSPARENT);
            ventana.setScene(s);
            if (undecorated){
                ventana.initStyle(StageStyle.UNDECORATED);
            }
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setResizable(resizable);
            ventana.setMaximized(resizable);
            ventana.setTitle(nombre);
            ventana.getIcons().add(new Image(tasky.Main.class.getResourceAsStream("/resources/imagenes/icon.png")));
            centrarStage(ventana, pane.getPrefHeight(), pane.getPrefWidth());
//            if (cargador.getController() instanceof PantallaPrincipalController) {
//                ((PantallaPrincipalController) cargador.getController()).setStage(ventana);
//            }
        } catch (IOException e) {
            System.err.println("Error al abrir ventana: " + URL);
            e.printStackTrace();
        }
        return ventana;
    }

    public static Stage cargarVentanaFXML(String URL, String nombre, boolean undecorated) {
        return cargarVentanaFXML(URL, nombre, undecorated, false);
    }

    public static Stage cargarVentanaFXML(String URL, String nombre) {
        return cargarVentanaFXML(URL, nombre, true);
    }

    public static final String getRutaAbs(String archivo) {
        return RUTA_ABSOLUTA + "\\" + archivo;
    }

    /**
     * Abre un archivo en modo de escritura
     *
     * @param ruta ruta del archivo, relativa al proyecto
     * @param append si está en false, se sobrescribirá lo que hay en el archivo
     * @return un PrintWriter representando al archivo abierto
     */
    public static PrintWriter openFileWrite(String ruta, boolean append) {
        try {
            FileOutputStream fileStream = new FileOutputStream(new File(getRutaAbs(ruta)), append);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(fileStream, "UTF-8"));
            return pw;
        } catch (FileNotFoundException ex) {
            System.err.println("Archivo no encontrado");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Codificación no soportada");
        }
        return null;
    }

    /**
     * Abre un archivo en modo de escritura, que por defecto, sobrescribirá lo que haya en el archivo actual
     *
     * @param ruta ruta del archivo, relativa al proyecto
     * @return un PrintWriter representando al archivo abierto
     */
    public static PrintWriter openFileWrite(String ruta) {
        return openFileWrite(ruta, false);
    }

    /**
     * Abre un archivo en modo lectura
     * @param ruta la ubicación del archivo en el classpath
     * @return un BufferedReader con el archivo abierto
     */
    public static BufferedReader openFileRead(String ruta) {
        try {
            BufferedReader buffer;
            buffer = new BufferedReader(new InputStreamReader(tasky.Main.class.getResourceAsStream(ruta), "UTF-8"));
            return buffer;
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Codificación no soportada");
        }
        return null;
    }
    
    /**
     * Regresa la línea sin espacios
     * @param linea
     * @return 
     */
    public static String quitarEspacios(String linea) {
        String lineaFinal = "";
        for (int i = 0; i < linea.length(); i++) {
            String subLinea = linea.substring(i, i + 1);
            if (!subLinea.equals(" ")) {
                lineaFinal += subLinea;
            }
        }
        return lineaFinal;
    }

    /**
     * Separa un string usando un separador, y regresa cada porción
     * dentro de una lista doblemente enlazada
     * @param linea la línea
     * @param separador el separador
     * @return una lista doblemente enlazada con cada una de las partes del string
     */
    public static DoubleLinkedList<String> split(String linea, String separador) {
        short principio = 0;
        DoubleLinkedList<String> campos = new DoubleLinkedList();
        for (int i = 0; i < linea.length(); i++) {
            if (linea.substring(i, i + 1).equals(separador)) {
                campos.add(linea.substring(principio, i));
                principio = (short) (i + 1);
            }
        }
        campos.add(linea.substring(principio));
        return campos;
    }
    
    /***
     * Muestra un input dialog
     * @param message el mensaje a mostrar
     * @param def información predeterminada en el input dialog
     * @return la información recogida
     */
    public static String inputDialog(String message, String def) {
        try {
            FXMLLoader cargador = new FXMLLoader(tasky.Main.class.getResource("/tasky/utilidades/InputDialog.fxml"));
            AnchorPane pane = cargador.load();
            InputDialogController controlador = cargador.getController();
            controlador.setText(message);
            controlador.setDefault(def);
            Stage ventana = new Stage();
            Scene scene = new Scene(pane);
            scene.setFill(Color.TRANSPARENT);
            ventana.initStyle(StageStyle.TRANSPARENT);
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.setScene(scene);
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.initStyle(StageStyle.UNDECORATED);
            centrarStage(ventana, pane.getPrefHeight(), pane.getPrefWidth());
            ventana.showAndWait();
            return controlador.getSeleccion() == InputDialogController.Seleccion.ACEPTAR ? controlador.getResult() : "";
        } catch (IOException ex) {
            System.err.println("Error al cargar el input dialog");
        }
        return "";
    }
    
    /**
     * Muestra un input dialog
     * @param message el mensaje a mostrar
     * @return la información recogida
     */
    public static String inputDialog(String message){
        return inputDialog(message, "");
    }

    /**
     * Determina si la información dentro de un String es un número
     * @param num el número
     * @return true si el String tiene un número, false si no
     */
    public static boolean esNumero(String num) {
        try{
            Integer.parseInt(num);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
    
    /**
     * Reemplaza una parte de una cadena con otra
     * @param linea la cadana
     * @param buscar la parte de la cadena a reemplazar
     * @param reemplazar con qué se va a reemplazar
     * @return una nueva cadena con todo reemplazado
     */
    public static String reemplazar(String linea, String buscar, String reemplazar){
        String builder = "";
        int index = -1;
        boolean encontrado = false;
        for (int i = 0; i < linea.length(); i++) {
            if (!encontrado && (i+buscar.length()) <= linea.length() && linea.substring(i, i+buscar.length()).equals(buscar)){
                encontrado = true;
                index = i;
                i += buscar.length() - 1;
            }else{
                builder += linea.substring(i, i+1);
            }
        }
        if (encontrado){
            return linea.substring(0, index) + reemplazar + (index < builder.length() ? linea.substring(index + buscar.length()):"");
        }else{
            return linea;
        }
    }
}
