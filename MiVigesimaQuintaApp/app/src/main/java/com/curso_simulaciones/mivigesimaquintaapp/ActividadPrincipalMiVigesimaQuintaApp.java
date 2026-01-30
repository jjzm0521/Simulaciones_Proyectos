package com.curso_simulaciones.mivigesimaquintaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.curso_simulaciones.mivigesimaquintaapp.controlador.ActividadControladora;
import com.curso_simulaciones.mivigesimaquintaapp.modelo.AlmacenDatosRAM;

/**
 * Actividad principal de la aplicación.
 * Gestiona la resolución de pantalla y lanza la actividad controladora.
 */
public class ActividadPrincipalMiVigesimaQuintaApp extends Activity {

    private int tamanoLetraResolucionIncluida;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        lanzarActividadControladora();
    }

    /**
     * Calcula y almacena las dimensiones de la pantalla
     * y el tamaño de letra adecuado.
     */
    private void gestionarResolucion() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;
        int dimensionReferencia;

        // Tomar el menor valor entre alto y ancho
        if (alto > ancho) {
            dimensionReferencia = ancho;
        } else {
            dimensionReferencia = alto;
        }

        // Estimación de buen tamaño de letra
        int tamanoLetra = dimensionReferencia / 20;
        tamanoLetraResolucionIncluida = (int) (tamanoLetra / displayMetrics.scaledDensity);

        // Guardar en el almacén de datos
        AlmacenDatosRAM.tamanoLetraResolucionIncluida = tamanoLetraResolucionIncluida;
        AlmacenDatosRAM.ancho_pantalla = ancho;
        AlmacenDatosRAM.alto_pantalla = alto;
    }

    /**
     * Lanza la actividad controladora y cierra esta actividad.
     */
    private void lanzarActividadControladora() {
        Intent intent = new Intent(this, ActividadControladora.class);
        startActivity(intent);
        finish();
    }
}
