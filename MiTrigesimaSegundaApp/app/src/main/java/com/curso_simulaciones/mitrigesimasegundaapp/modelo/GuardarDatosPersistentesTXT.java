package com.curso_simulaciones.mitrigesimasegundaapp.modelo;

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

public class GuardarDatosPersistentesTXT {

    private Vector almacen_tiempo = new Vector();
    private Vector almacen_datos = new Vector();

    public GuardarDatosPersistentesTXT() {
    }

    public void llenarDatos(double tiempo, double dato) {
        almacen_tiempo.addElement(tiempo);
        almacen_datos.addElement(dato);
    }

    public void borrarDatos() {
        almacen_tiempo.removeAllElements();
        almacen_datos.removeAllElements();
    }

    public void guardar(Activity actividad, String carpeta) {
        // Verificar si hay datos para guardar
        if (almacen_tiempo.size() == 0) {
            Toast.makeText(actividad, "No hay datos para guardar. Presione EMPEZAR primero.", Toast.LENGTH_LONG).show();
            return;
        }

        Date date = new Date();
        DateFormat hora_fecha = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");

        try {
            String marca = hora_fecha.format(date);
            String nombre_archivo = "datos_" + marca + ".txt";

            File file = null;
            File path = null;

            // Siempre guardar en la carpeta pública del almacenamiento
            // Ruta: /storage/emulated/0/almacen_mis_datos/luxometro/
            path = new File(Environment.getExternalStorageDirectory(), carpeta);

            if (!path.exists()) {
                boolean created = path.mkdirs();
            }

            file = new File(path, nombre_archivo);
            FileOutputStream flujoSalida = new FileOutputStream(file);
            OutputStreamWriter escritor = new OutputStreamWriter(flujoSalida);

            int cantidadDatos = almacen_tiempo.size();
            for (int i = 0; i < cantidadDatos; i = i + 1) {
                double tiempo = (Double) almacen_tiempo.get(i);
                double dato = (Double) almacen_datos.get(i);

                float tiempo_dos_decimales = (float) (Math.round(tiempo * 100) / 100f);
                escritor.write("" + tiempo_dos_decimales);
                escritor.write("\t\t\t");

                float dato_dos_decimales = (float) (Math.round(dato * 100) / 100f);
                escritor.write("" + dato_dos_decimales + "\r\n");
            }

            escritor.flush();
            escritor.close();

            // Mostrar información más detallada
            String aviso = "¡Guardado exitoso!\n" + cantidadDatos + " registros guardados.\n" +
                    "Archivo: " + nombre_archivo + "\n" +
                    "Ruta: " + path.getAbsolutePath();
            Toast.makeText(actividad, aviso, Toast.LENGTH_LONG).show();

            // También mostrar en el log para depuración
            android.util.Log.d("LUXOMETRO", "Archivo guardado: " + file.getAbsolutePath());
            android.util.Log.d("LUXOMETRO", "Tamaño del archivo: " + file.length() + " bytes");

        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(actividad, "Error al guardar: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
