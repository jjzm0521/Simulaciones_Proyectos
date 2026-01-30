package com.curso_simulaciones.mivigesimasegundaapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.curso_simulaciones.mivigesimasegundaapp.controlador.ActividadControladora;
import com.curso_simulaciones.mivigesimasegundaapp.datos.AlmacenDatosRAM;

public class ActividadPrincipalMiVigesimaSegundaApp extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();

        lanzarActividadControladora();

    }// fin onCreate

    /* Método auxiliar para asuntos de resolución */
    private void gestionarResolucion() {

        // Usamos el WindowManager para obtener las métricas de la ventana ACTIVA (no
        // del contexto global)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int alto = displayMetrics.heightPixels;
        int ancho = displayMetrics.widthPixels;

        // guardar en almacen ancho y alto de pantalla
        AlmacenDatosRAM.ancho_pantalla = ancho;
        AlmacenDatosRAM.alto_pantalla = alto;

    }

    private void lanzarActividadControladora() {

        Intent intent = new Intent(this, ActividadControladora.class);
        startActivity(intent);
        finish(); // Cerramos la principal para quedar en la controladora

    }

}
