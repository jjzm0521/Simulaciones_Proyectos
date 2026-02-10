package com.curso_simulaciones.mitrigesimacuartaapp.datos;

import android.app.Activity;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Clase GuardarDatosPersistentesTXT.
 * Para guardar datos del acelerómetro en archivo .txt
 * con 6 columnas: numero, tiempo, ax, ay, az, a
 */
public class GuardarDatosPersistentesTXT {

    /*
     * El vector es similar a un arreglo []
     * con la diferencia que el mismo se redimensiona
     * al doble de su tamaño cuando se llena (se duplica).
     * Además, proporciona métodos adicionales
     * para añadir, eliminar elementos, e insertar
     * elementos entre otros dos existentes.
     * Se pueden guardar objetos de diferentes tipo.
     */

    private Vector<Integer> almacen_numero = new Vector<>();
    private Vector<Double> almacen_tiempo = new Vector<>();
    private Vector<Double> almacen_ax = new Vector<>();
    private Vector<Double> almacen_ay = new Vector<>();
    private Vector<Double> almacen_az = new Vector<>();
    private Vector<Double> almacen_a = new Vector<>();

    /**
     * Constructor del Manejador de Archivos
     */
    public GuardarDatosPersistentesTXT() {

    }

    /**
     * Para recibir los datos que se grabarán
     * en archivo .txt en una carpeta definida
     * por el usuario en un documento.
     * Serán seis columnas: numero, tiempo, ax, ay, az, a
     * 
     * @param numero
     * @param tiempo
     * @param ax
     * @param ay
     * @param az
     * @param a
     */

    public void llenarDatos(int numero, double tiempo, double ax, double ay, double az, double a) {

        almacen_numero.addElement(numero);
        almacen_tiempo.addElement(tiempo);
        almacen_ax.addElement(ax);
        almacen_ay.addElement(ay);
        almacen_az.addElement(az);
        almacen_a.addElement(a);

    }

    /**
     * Para borrar los datos. Estos no serán
     * guardados en el arcivo .txt.
     */
    public void borrarDatos() {

        almacen_numero.removeAllElements();
        almacen_tiempo.removeAllElements();
        almacen_ax.removeAllElements();
        almacen_ay.removeAllElements();
        almacen_az.removeAllElements();
        almacen_a.removeAllElements();

    }

    /**
     * Para guradar los datos en formato .txt
     * 
     * @param actividad
     * @param carpeta
     */

    public void guardar(Activity actividad, String carpeta) {

        Date date = new Date();
        DateFormat hora_fecha = new SimpleDateFormat("yy-MM-dd_hh-mm-ss");

        try {

            /*
             * Paso 1 y Paso 2: Crear y abrir flujo de salida
             * Esto es, crear y abrir el canal de salida
             */

            String marca = hora_fecha.format(date).toString();
            String nombre_archivo = "datos_acelerometro_" + marca + ".txt";

            File file = null;
            File path = null;

            // Usar almacenamiento externo para todas las versiones
            // Requiere permiso MANAGE_EXTERNAL_STORAGE en Android 11+
            path = new File(Environment.getExternalStorageDirectory(), carpeta);

            // Asegurar que el directorio existe
            if (!path.exists()) {
                path.mkdirs();
            }

            file = new File(path, nombre_archivo);
            FileOutputStream flujoSalida = new FileOutputStream(file);

            /*
             * Agregar filtro. En este caso se le pasa a
             * OutputStreamWriter para que escriba
             */
            OutputStreamWriter escritor = new OutputStreamWriter(flujoSalida);

            /*
             * Paso 3: Escribir información mientras haya
             */

            // Escribir encabezado
            escritor.write("# Dato\t\tTiempo(s)\t\tax(m/s²)\t\tay(m/s²)\t\taz(m/s²)\t\ta(m/s²)\r\n");

            // Escribimos los datos en el archivo
            for (int i = 0; i < almacen_tiempo.size(); i = i + 1) {
                // importar los datos
                int numero = almacen_numero.get(i);
                double tiempo = almacen_tiempo.get(i);
                double ax = almacen_ax.get(i);
                double ay = almacen_ay.get(i);
                double az = almacen_az.get(i);
                double a = almacen_a.get(i);

                // desplegar con dos decimales
                float tiempo_dos_decimales = (float) (Math.round(tiempo * 100) / 100f);
                float ax_dos_decimales = (float) (Math.round(ax * 100) / 100f);
                float ay_dos_decimales = (float) (Math.round(ay * 100) / 100f);
                float az_dos_decimales = (float) (Math.round(az * 100) / 100f);
                float a_dos_decimales = (float) (Math.round(a * 100) / 100f);

                escritor.write(numero + "\t\t" + tiempo_dos_decimales + "\t\t" +
                        ax_dos_decimales + "\t\t" + ay_dos_decimales + "\t\t" +
                        az_dos_decimales + "\t\t" + a_dos_decimales + "\r\n");

            }

            /*
             * Paso 4: cerrar canal
             */
            escritor.flush();
            escritor.close();

            // Mostramos que se ha guardado
            String aviso = "Datos guardados en:\n" + "Mis archivos/" + carpeta;
            Toast.makeText(actividad, aviso, Toast.LENGTH_LONG).show();

        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(actividad, "Error al guardar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

}
