package com.curso_simulaciones.micuadrigesimaquintaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.micuadrigesimaquintaapp.comunicaciones.ClientePubSubMQTT;
import com.curso_simulaciones.micuadrigesimaquintaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.micuadrigesimaquintaapp.gui_auxiliares.DialogoSalir;
import com.curso_simulaciones.micuadrigesimaquintaapp.utilidades.Gauge;
import com.curso_simulaciones.micuadrigesimaquintaapp.utilidades.Graficador;
import com.curso_simulaciones.micuadrigesimaquintaapp.utilidades.Tabla;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActividadComoClienteSubMQTT extends Activity implements Runnable {

    private int tamanoLetraResolucionIncluida;
    private LinearLayout linear_layout_tabla_grafica;
    private LinearLayout.LayoutParams parametros_tabla_grafica;
    private Button botonConectar, botonTablaGrafica;
    private TextView textviewAviso;

    // Gauges
    private Gauge gaugeDistancia;
    private Gauge gaugeTemperatura;
    private Gauge gaugeHumedad;
    private FrameLayout layoutGauges; // Contenedor para superponer

    private Tabla tabla;
    private Graficador graficador;

    private ClientePubSubMQTT cliente;

    private Thread hilo;

    private int periodo_muestreo = 200;
    private int contador = 0;
    private float medidaDistancia;
    private float medidaTemperatura;
    private float medidaHumedad;

    // hilo para actualizar tabla
    private final Handler myHandler = new Handler();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        gestionarResolucion();

        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setContentView(crearGUI(), parametro_layout_principal);

        eventos();

        crearCliente();

    }

    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);
    }

    private void crearElementosGUI() {

        // Gauge Distancia (Grande/Principal)
        gaugeDistancia = new Gauge(this);
        gaugeDistancia.setRango(0, 20);
        gaugeDistancia.setUnidad("cm");
        // Reducir ángulos para evitar solapamiento con los gauges pequeños
        // Inicio: 160 grados (casi a las 9 en punto)
        // Barrido: 220 grados (termina casi a las 3 en punto)
        gaugeDistancia.setAngulos(160, 220);

        // Gauge Temperatura (Pequeño)
        gaugeTemperatura = new Gauge(this);
        gaugeTemperatura.setRango(-20, 60);
        gaugeTemperatura.setUnidad("°C");

        // Gauge Humedad (Pequeño)
        gaugeHumedad = new Gauge(this);
        gaugeHumedad.setRango(0, 100);
        gaugeHumedad.setUnidad("%");

        // Tabla
        tabla = new Tabla(this);
        tabla.setEtiquetaColumnas("Tiempo (s)", "Distancia (cm)");

        // Graficador
        graficador = new Graficador(this);
        graficador.setTituloEjeX("Tiempo (s)");
        // El título del Eje Y es genérico o se omite ya que hay 3 unidades distintas
        // graficador.setTituloEjeY("Valores");

        // Colores y estilo ya definidos en Graficador.java
        // graficador.setGrosorLinea(2f);
        // graficador.setColorFondo(Color.BLACK);
        // graficador.setColorTextoEjes(Color.WHITE);

        botonConectar = new Button(this);
        botonConectar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonConectar.setText("CONECTAR");
        botonConectar.getBackground().setColorFilter(Color.rgb(183, 216, 199), PorterDuff.Mode.MULTIPLY);

        botonTablaGrafica = new Button(this);
        botonTablaGrafica.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonTablaGrafica.setText("GRAFICA");
        botonTablaGrafica.getBackground().setColorFilter(Color.rgb(183, 216, 199), PorterDuff.Mode.MULTIPLY);
        botonTablaGrafica.setEnabled(false);

        textviewAviso = new TextView(this);
        textviewAviso.setGravity(Gravity.FILL_VERTICAL);
        textviewAviso.setBackgroundColor(Color.rgb(183, 216, 199));
        textviewAviso.setTextSize(0.8f * tamanoLetraResolucionIncluida);
        textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);
        textviewAviso.setTextColor(Color.BLACK);

    }// fin crearElemnetosGUI

    private LinearLayout crearGUI() {

        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_principal.setWeightSum(10.0f);

        // 1. FrameLayout para los Gauges (Superposición)
        layoutGauges = new FrameLayout(this);
        layoutGauges.setBackgroundColor(Color.WHITE);
        // El Gauge de distancia ocupa todo el fondo
        layoutGauges.addView(gaugeDistancia, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Contenedor horizontal para los gauges pequeños en la parte inferior
        LinearLayout linearSmallGauges = new LinearLayout(this);
        linearSmallGauges.setOrientation(LinearLayout.HORIZONTAL);
        linearSmallGauges.setWeightSum(2.0f);

        // Parametros para T y H (Peso 1 cada uno)
        LinearLayout.LayoutParams paramsSmall = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsSmall.weight = 1.0f;
        paramsSmall.setMargins(10, 0, 10, 0); // Margenes laterales solo para aprovechar altura

        linearSmallGauges.addView(gaugeTemperatura, paramsSmall);
        linearSmallGauges.addView(gaugeHumedad, paramsSmall);

        // Calcular altura dinámica para los gauges pequeños (aprox 17% de la pantalla)
        int altoPantalla = AlmacenDatosRAM.alto;
        if (altoPantalla == 0) {
            altoPantalla = getResources().getDisplayMetrics().heightPixels;
        }
        int alturaSmall = (int) (altoPantalla * 0.17f);

        // Añadir contenedor small al FrameLayout, alineado abajo
        FrameLayout.LayoutParams paramsSmallContainer = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, alturaSmall);
        paramsSmallContainer.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        layoutGauges.addView(linearSmallGauges, paramsSmallContainer);

        // 2. LinearLayout para Tabla/Grafica
        linear_layout_tabla_grafica = new LinearLayout(this);
        linear_layout_tabla_grafica.setOrientation(LinearLayout.VERTICAL);
        linear_layout_tabla_grafica.setGravity(Gravity.FILL);
        linear_layout_tabla_grafica.setBackgroundColor(Color.WHITE);

        // ... Botones y Aviso ...
        LinearLayout linear_layout_botones = new LinearLayout(this);
        linear_layout_botones.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_botones.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_botones.setWeightSum(2.0f);

        LinearLayout linear_layout_aviso = new LinearLayout(this);
        linear_layout_aviso.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_aviso.setBackgroundColor(Color.rgb(183, 216, 199));

        // Pesos Layout Principal
        // Gauges: 5.0 (Más espacio para evitar solapamiento crítico)
        LinearLayout.LayoutParams paramsGauges = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsGauges.weight = 5.0f;
        paramsGauges.setMargins(5, 5, 5, 5);

        // Tabla/Grafica: 4.0
        LinearLayout.LayoutParams paramsTablaGrafica = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsTablaGrafica.weight = 4.0f;
        paramsTablaGrafica.setMargins(5, 5, 5, 5);

        // Aviso: 0.3
        LinearLayout.LayoutParams paramsAviso = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsAviso.weight = 0.3f;

        // Botones: 0.7
        LinearLayout.LayoutParams paramsBotones = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsBotones.weight = 0.7f;

        linear_layout_principal.addView(layoutGauges, paramsGauges);
        linear_layout_principal.addView(linear_layout_tabla_grafica, paramsTablaGrafica);
        linear_layout_principal.addView(linear_layout_aviso, paramsAviso);
        linear_layout_principal.addView(linear_layout_botones, paramsBotones);

        // Iniciar con Graficador
        parametros_tabla_grafica = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linear_layout_tabla_grafica.addView(graficador, parametros_tabla_grafica);

        // ... add views to sub layouts ...
        linear_layout_aviso.addView(textviewAviso, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout.LayoutParams paramsBoton = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsBoton.weight = 1.0f;
        linear_layout_botones.addView(botonConectar, paramsBoton);
        linear_layout_botones.addView(botonTablaGrafica, paramsBoton);

        return linear_layout_principal;
    }// fin gui

    protected void eventos() {

        // evento cliente
        botonConectar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (botonConectar.getText() == "CONECTAR") {
                    botonConectar.setText("EMPEZAR");
                    // conectar cliente
                    cliente.conectar();
                    AlmacenDatosRAM.estado_conexion_nube = 2;
                    actualizarAviso();

                    empezarHilo(); // Iniciar polling

                } else {

                    borrarDatos();
                    botonTablaGrafica.setEnabled(true);
                    botonConectar.setEnabled(false);

                }

            }
        });

        // eventos botones
        botonTablaGrafica.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (botonTablaGrafica.getText() == "TABLA") {
                    botonTablaGrafica.setText("GRAFICA");
                    linear_layout_tabla_grafica.removeView(graficador);
                    linear_layout_tabla_grafica.addView(tabla, parametros_tabla_grafica);

                } else {

                    botonTablaGrafica.setText("TABLA");
                    linear_layout_tabla_grafica.removeView(tabla);
                    linear_layout_tabla_grafica.addView(graficador, parametros_tabla_grafica);

                }

            }
        });

    }

    public void crearCliente() {
        cliente = new ClientePubSubMQTT(this);
    }

    public void empezarHilo() {
        hilo = new Thread(this);
        hilo.start();
    }

    private void borrarDatos() {
        contador = 0;
        AlmacenDatosRAM.datosDistancia.clear();
        tabla.borrar();
        graficador.limpiarGrafica();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogoSalir dialogo_salir = new DialogoSalir(this);
            dialogo_salir.mostrarPopMenuCoeficientes();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void run() {

        while (true) {

            try {
                Thread.sleep(periodo_muestreo);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // leer IoT
            leer();

        }

    }

    private void leer() {

        final String nuevo_dato_string = cliente.leerString();

        if (nuevo_dato_string != null) {

            boolean exito = convertirStrigJson(nuevo_dato_string);

            if (exito) {
                hacerTrabajoDuro();
                AlmacenDatosRAM.estado_conexion_nube = 4;
            } else {
                AlmacenDatosRAM.estado_conexion_nube = 3; // Error de formateo
            }

        } else {
            if (!AlmacenDatosRAM.conectado) {
                AlmacenDatosRAM.estado_conexion_nube = 1;
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actualizarAviso();
            }
        });

    }

    public boolean convertirStrigJson(String datoString) {

        try {
            JSONObject obj = new JSONObject(datoString);

            boolean tieneDatos = false;

            if (obj.has("valor")) {
                medidaDistancia = (float) obj.getDouble("valor");
                tieneDatos = true;
            }
            if (obj.has("temp")) {
                medidaTemperatura = (float) obj.getDouble("temp");
                tieneDatos = true;
            }
            if (obj.has("hum")) {
                medidaHumedad = (float) obj.getDouble("hum");
                tieneDatos = true;
            }
            if (obj.has("unidad")) {
                AlmacenDatosRAM.unidades = obj.getString("unidad");
            }

            return tieneDatos;

        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

    private void hacerTrabajoDuro() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Actualizar los 3 Gauges independientes
                    gaugeDistancia.setMedida(medidaDistancia);
                    gaugeTemperatura.setMedida(medidaTemperatura);
                    gaugeHumedad.setMedida(medidaHumedad);

                    AlmacenDatosRAM.datosDistancia.add(medidaDistancia);
                    contador++;

                    tabla.enviarDatos((float) contador, medidaDistancia);

                    // Grafica sigue recibiendo los 3 datos
                    graficador.agregarDatos((float) contador, medidaDistancia, medidaTemperatura, medidaHumedad);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void actualizarAviso() {

        if (AlmacenDatosRAM.estado_conexion_nube == 1) {

            AlmacenDatosRAM.conectado_PubSub = " Hacer clic en CONECTAR para acceder al BROKER...";
            textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);
            textviewAviso.setBackgroundColor(Color.GRAY);

        } else if (AlmacenDatosRAM.estado_conexion_nube == 2) {

            AlmacenDatosRAM.conectado_PubSub = " Intentando conectar...";
            textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);
            textviewAviso.setBackgroundColor(Color.YELLOW);

        } else if (AlmacenDatosRAM.estado_conexion_nube == 3) {

            AlmacenDatosRAM.conectado_PubSub = "  Recibiendo datos nulos o inválidos...";
            textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);

        } else if (AlmacenDatosRAM.estado_conexion_nube == 4) {

            String distFormateada = String.format(java.util.Locale.US, "%.2f", medidaDistancia);
            AlmacenDatosRAM.conectado_PubSub = "  Recibiendo datos: D=" + distFormateada + " T=" + medidaTemperatura
                    + " H=" + medidaHumedad;
            textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);
            textviewAviso.setBackgroundColor(Color.GREEN);

        }

    }

}
