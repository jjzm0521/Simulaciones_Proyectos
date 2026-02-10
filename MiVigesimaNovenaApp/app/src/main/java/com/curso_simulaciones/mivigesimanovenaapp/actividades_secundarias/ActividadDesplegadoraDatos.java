package com.curso_simulaciones.mivigesimanovenaapp.actividades_secundarias;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.curso_simulaciones.mivigesimanovenaapp.R;
import com.curso_simulaciones.mivigesimanovenaapp.utilidades.Boton;
import com.curso_simulaciones.mivigesimanovenaapp.utilidades.Gaussimetro;
import com.curso_simulaciones.mivigesimanovenaapp.utilidades.Graficador;

public class ActividadDesplegadoraDatos extends Activity implements SensorEventListener {

    // UI Elements
    private Boton botonBx, botonBy, botonBz, botonB;
    private Gaussimetro gauge;
    public Graficador graficador;

    // Sensor Logic
    private SensorManager sensorManager;
    private Sensor magnetometro;

    // Estado
    private int componenteSeleccionada = 4; // 1: Bx, 2: By, 3: Bz, 4: B (Magnitud)
    private long tiempoInicio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear GUI
        iniciarComponentesGUI();
        setContentView(crearGUI());

        // Configurar Sensores
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometro = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (magnetometro == null) {
            Toast.makeText(this, "No se detectó magnetómetro (Gaussímetro) en este dispositivo.", Toast.LENGTH_LONG)
                    .show();
        }

        configurarEventos();
    }

    private void iniciarComponentesGUI() {
        botonBx = new Boton(this);
        botonBx.setParametros("Bx", Color.GREEN);

        botonBy = new Boton(this);
        botonBy.setParametros("By", Color.GREEN);

        botonBz = new Boton(this);
        botonBz.setParametros("Bz", Color.GREEN);

        botonB = new Boton(this);
        botonB.setParametros("B", Color.rgb(255, 165, 0)); // Orange

        gauge = new Gaussimetro(this);
        gauge.setRango(0, 1000); // Rango inicial para B (µT)

        graficador = new Graficador(this);
        graficador.setTituloEjeX("Tiempo (s)");
        graficador.setTituloEjeY("Campo B (µT)");
    }

    private LinearLayout crearGUI() {
        // Layout Principal (Horizontal)
        LinearLayout linearPrincipal = new LinearLayout(this);
        linearPrincipal.setOrientation(LinearLayout.HORIZONTAL);
        linearPrincipal.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearPrincipal.setBackgroundColor(Color.WHITE);
        linearPrincipal.setWeightSum(10f);

        // Agregar padding para evitar solapamiento
        linearPrincipal.setPadding(30, 30, 30, 30);

        // Columna 1: Gauge (50%)
        LinearLayout columna1 = new LinearLayout(this);
        columna1.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramCol1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 5f);
        paramCol1.setMargins(10, 10, 5, 10);
        columna1.setLayoutParams(paramCol1);
        columna1.setBackgroundColor(Color.rgb(245, 245, 245)); // Gris muy claro
        columna1.addView(gauge, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Columna 2: Gráfica (40%)
        LinearLayout columna2 = new LinearLayout(this);
        columna2.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramCol2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4f);
        paramCol2.setMargins(5, 10, 5, 10);
        columna2.setLayoutParams(paramCol2);
        columna2.setBackgroundColor(Color.BLACK); // Fondo negro para gráfica
        columna2.addView(graficador, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Columna 3: Botones (10%)
        LinearLayout columna3 = new LinearLayout(this);
        columna3.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams paramCol3 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        paramCol3.setMargins(5, 10, 10, 10);
        columna3.setLayoutParams(paramCol3);
        columna3.setBackgroundColor(Color.LTGRAY);
        columna3.setWeightSum(4f);

        // Añadir botones a columna 3
        LinearLayout.LayoutParams paramBoton = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0,
                1f);
        paramBoton.setMargins(20, 20, 20, 20);
        columna3.addView(botonBx, paramBoton);
        columna3.addView(botonBy, paramBoton);
        columna3.addView(botonBz, paramBoton);
        columna3.addView(botonB, paramBoton);

        // Ensamblar
        linearPrincipal.addView(columna1);
        linearPrincipal.addView(columna2);
        linearPrincipal.addView(columna3);

        return linearPrincipal;
    }

    private void configurarEventos() {
        botonBx.setOnClickListener(v -> seleccionarComponente(1));
        botonBy.setOnClickListener(v -> seleccionarComponente(2));
        botonBz.setOnClickListener(v -> seleccionarComponente(3));
        botonB.setOnClickListener(v -> seleccionarComponente(4));
    }

    private void seleccionarComponente(int comp) {
        componenteSeleccionada = comp;
        graficador.limpiarGrafica();
        tiempoInicio = 0; // Reset so first sample sets t=0

        switch (comp) {
            case 1: // Bx
                gauge.setRango(-1000, 1000);
                graficador.setTituloEjeY("Bx (µT)");
                break;
            case 2: // By
                gauge.setRango(-1000, 1000);
                graficador.setTituloEjeY("By (µT)");
                break;
            case 3: // Bz
                gauge.setRango(-1000, 1000);
                graficador.setTituloEjeY("Bz (µT)");
                break;
            case 4: // B (Magnitud)
            default:
                gauge.setRango(0, 1000);
                graficador.setTituloEjeY("Campo B (µT)");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (magnetometro != null) {
            sensorManager.registerListener(this, magnetometro, SensorManager.SENSOR_DELAY_UI);
        }

        // Limpiar gráfica para evitar líneas de retorno al reiniciar tiempo
        if (graficador != null) {
            graficador.limpiarGrafica();
        }
        tiempoInicio = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float bx = event.values[0];
            float by = event.values[1];
            float bz = event.values[2];
            float b = (float) Math.sqrt(bx * bx + by * by + bz * bz);

            float valorMostrar = 0;
            switch (componenteSeleccionada) {
                case 1:
                    valorMostrar = bx;
                    break;
                case 2:
                    valorMostrar = by;
                    break;
                case 3:
                    valorMostrar = bz;
                    break;
                case 4:
                    valorMostrar = b;
                    break;
            }

            // Actualizar Gauge
            gauge.setMedida(valorMostrar);

            // Actualizar Gráfica
            long tiempoActual = System.currentTimeMillis();
            if (tiempoInicio == 0)
                tiempoInicio = tiempoActual;
            float tiempoSegundos = (tiempoActual - tiempoInicio) / 1000f;

            graficador.agregarDato(tiempoSegundos, valorMostrar);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // No necesario
    }
}
