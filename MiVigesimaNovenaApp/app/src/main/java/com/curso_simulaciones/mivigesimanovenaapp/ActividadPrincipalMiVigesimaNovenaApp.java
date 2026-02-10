package com.curso_simulaciones.mivigesimanovenaapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curso_simulaciones.mivigesimanovenaapp.actividades_secundarias.ActividadDesplegadoraDatos;
import com.curso_simulaciones.mivigesimanovenaapp.utilidades.Boton;

/**
 * Clase ActividadPrincipalMiVigesimaNovenaApp
 * 
 * Actividad principal - GAUSSIMETRO (medidor de campo magnético)
 * utiliza el magnetómetro del dispositivo móvil.
 * 
 * Estructura GUI:
 * - Imagen de presentación (80%)
 * - Botones Consultar y Salir (20%)
 */
public class ActividadPrincipalMiVigesimaNovenaApp extends Activity {

    // Botones de la interfaz
    private Boton consultar, salir;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear elementos de la GUI
        crearElementosGUI();

        // Parámetros del layout principal
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Pegar el contenedor con la GUI
        this.setContentView(crearGUI(), parametro_layout_principal);

        // Verificar existencia del magnetómetro
        existenciaSensor();

        // Configurar eventos
        eventos();
    }

    // Crea los botones con sus imágenes
    private void crearElementosGUI() {
        consultar = new Boton(this);
        consultar.setImagen(R.drawable.consultar);

        salir = new Boton(this);
        salir.setImagen(R.drawable.salir);
    }

    // Construye el layout de la GUI
    private LinearLayout crearGUI() {
        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setGravity(Gravity.FILL);
        linear_layout_principal.setBackgroundColor(Color.WHITE);
        linear_layout_principal.setWeightSum(10);

        // Primera fila: imagen (weight=8)
        LinearLayout linear_layout_primera_fila = new LinearLayout(this);
        LinearLayout.LayoutParams parametros_primera_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila.weight = 8.0f;
        linear_layout_primera_fila.setLayoutParams(parametros_primera_fila);
        Drawable fondo = getResources().getDrawable(R.drawable.imagen_entrada_app_29);
        linear_layout_primera_fila.setBackgroundDrawable(fondo);

        // Segunda fila: botones (weight=2)
        LinearLayout linear_layout_segunda_fila = new LinearLayout(this);
        linear_layout_segunda_fila.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams parametros_segunda_fila = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_segunda_fila.weight = 2.0f;
        linear_layout_segunda_fila.setWeightSum(2.0f);
        linear_layout_segunda_fila.setLayoutParams(parametros_segunda_fila);

        // Parámetros de botones
        LinearLayout.LayoutParams parametros_pegado_boton = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_pegado_boton.weight = 1.0f;
        consultar.setLayoutParams(parametros_pegado_boton);
        salir.setLayoutParams(parametros_pegado_boton);
        linear_layout_segunda_fila.addView(consultar);
        linear_layout_segunda_fila.addView(salir);

        linear_layout_principal.addView(linear_layout_primera_fila);
        linear_layout_principal.addView(linear_layout_segunda_fila);

        return linear_layout_principal;
    }

    // Configura eventos de clic
    private void eventos() {
        consultar.setOnClickListener(v -> lanzarDatos());
        salir.setOnClickListener(v -> finish());
    }

    // Lanza la actividad del gaussímetro
    private void lanzarDatos() {
        Intent intent = new Intent(this, ActividadDesplegadoraDatos.class);
        startActivity(intent);
    }

    // Verifica si existe el magnetómetro
    private boolean existenciaSensor() {
        boolean existe = false;
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            existe = true;
        } else {
            desplegarAviso();
        }
        return existe;
    }

    // Muestra aviso si no hay magnetómetro
    private void desplegarAviso() {
        Toast toast = Toast.makeText(getApplicationContext(), "SU DISPOSITIVO NO POSEE MAGNETOMETRO",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }
}
