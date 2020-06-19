package tasky;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import tasky.actividades.Actividad;
import tasky.actividades.Categoria;
import tasky.utilidades.Utilidades;
import tasky.utilidades.colecciones.DoubleLinkedList;
import static java.lang.Math.random;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import static tasky.utilidades.Utilidades.reemplazar;
import static tasky.utilidades.Utilidades.isDateInRange;

/**
 *
 * @author Víctor Daniel Guevara
 */
public class Tasky implements Serializable
{
    private static Tasky tasky = null;
    
    private final DoubleLinkedList<Categoria> categorias;
    private final Usuario usuario;
    private String estado;
    private int puntos;
    private long periodo;
    private boolean enVacaciones;
    
    private static final String RUTA_TASKY = obtenerDirectorio();

    private static String obtenerDirectorio() {
        String os = (System.getProperty("os.name")).toUpperCase();
        String directorio;
        directorio = (os.contains("WIN") ? System.getenv("AppData") : System.getProperty("user.home")) + "/Tasky";
        crearCarpeta(directorio);
        return directorio;
    }
    
    private static void crearCarpeta(String directorio){
        Path path;
        if (Files.notExists(path = Paths.get(directorio))){
            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                System.err.println("Error de I/O");
            }
       }
    }

    private Tasky() {
        categorias = new DoubleLinkedList<>();
        categorias.add(new Categoria("Cosas para hoy", false));
        usuario = new Usuario();
        estado = "neutral";
        periodo = (1 * 60 * 60 * 1000);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void aumentarPuntos(int cantidad) {
        if (!this.isEnVacaciones()){
            puntos += cantidad;
        }
        System.out.println("puntos = " + puntos);
    }

    public int getPuntos() {
        return puntos;
    }
    
    public long getPeriodo(){
        return periodo;
    }
    
    public void setPeriodo(long periodo){
        this.periodo = periodo;
    }

    public DoubleLinkedList<Categoria> getCategorias() {
        return categorias;
    }

    public void agregarCategoria(Categoria c) {
        categorias.add(c);
    }
    
    public void removerCategoria(Categoria c) {
        categorias.remove(c);
    }

    public Categoria getCategoria(int index) {
        return categorias.get(index);
    }

    public int nTareas() {
        int n = 0;
        for (Categoria c : categorias) {
            for (Actividad a : c.getActividades()) {
                n++;
            }
        }
        return n;
    }

    public void saludar() {
        try {
            DoubleLinkedList<String> saludosPosibles = new DoubleLinkedList<>();
            if (!enVacaciones) {
                if (puntos <= -10) {
                    estado = "motivacional";
                } else if (puntos > -10 && puntos <= 10) {
                    estado = "neutral";
                } else {
                    estado = "feliz";
                }
            } else {
                estado = "vacaciones";
            }
            BufferedReader buffer = Utilidades.openFileRead("/resources/ficheros/dialogos/" + estado);
            while (buffer.ready()) {
                saludosPosibles.add(buffer.readLine());
            }
            int random = (int) (random() * (saludosPosibles.size()));
            String saludo = saludosPosibles.get(random);
            notificar(reemplazar(saludo, "[usuario]", getUsuario().getNombre()));
        } catch (IOException ex) {
            System.err.println("Error de I/O");
        }
    }
    
    public void notificar(String saludo){
        ImageView imagen = new ImageView(new Image("/resources/imagenes/icon.png"));
            /*Notifications.create()
                    .title("Tasky")
                    .text(saludo)
                    .graphic(imagen)
                    .hideAfter(Duration.seconds(5))
                    .position(Pos.BOTTOM_RIGHT)
                    .darkStyle()
                    .show();*/
    }

    //<editor-fold desc="serialización" defaultstate="collapsed">
    public static void inicializarTasky() {
        deserializar();
        tasky = getInstance();
    }

    public static void serializar() {
        try (FileOutputStream fos = new FileOutputStream(RUTA_TASKY + "/Tasky.bin"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            System.out.println("Serializando...");
            oos.writeObject(getInstance());
        } catch (NotSerializableException e) {
            System.out.println("Error de I/O");
        } catch (IOException e) {
            System.out.println("Error de I/O");
        }
    }

    public static Tasky getInstance() {
        if (tasky == null) {
            tasky = new Tasky();
        }
        return tasky;
    }

    public static void deserializar() {
        if (new File(RUTA_TASKY + "/Tasky.bin").exists()) {
            System.out.println("Deserializando...");
            try (FileInputStream file = new FileInputStream(RUTA_TASKY + "/Tasky.bin");
                    ObjectInputStream in = new ObjectInputStream(file)) {
                tasky = (Tasky) in.readObject();
            } catch (IOException e) {
                System.err.println("Error de I/O:");
                System.out.println("Eliminando archivo binario...");
                if (new File(RUTA_TASKY + "Tasky.bin").delete()) {
                    System.out.println("Archivo eliminado");
                } else {
                    System.out.println("Archivo no eliminado");
                }
            } catch (ClassNotFoundException ex) {
                System.err.println("No se encontró la clase");
            }
        } else {
            System.err.println("No se encontro el archivo en " + RUTA_TASKY + "Tasky.bin");
        }
    }

    //</editor-fold>

    public boolean isEnVacaciones() {
        return enVacaciones;
    }

    public void setEnVacaciones(boolean enVacaciones) {
        this.enVacaciones = enVacaciones;
    }

    public Actividad getActividadMasImportante() {
        Actividad importanter = getCategoria(0).getActividades().isEmpty() ? null : getCategoria(0).getActividad(0);
        for (Categoria categoria : categorias) {
            for (Actividad actividad : categoria.getActividades()) {
                if (importanter == null) {
                    importanter = actividad;
                } else {
                    if (isDateInRange(actividad.getFechaFin(), LocalDate.now(), LocalDate.now().plusDays(2)) && actividad.getPrioridad().getValue() > importanter.getPrioridad().getValue()) {
                        importanter = actividad;
                    }
                }
            }
        }
        return importanter;
    }

}
