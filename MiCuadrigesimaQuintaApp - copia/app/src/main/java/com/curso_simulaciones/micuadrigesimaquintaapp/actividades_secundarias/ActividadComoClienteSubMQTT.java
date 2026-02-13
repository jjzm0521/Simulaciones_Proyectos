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

    private Gauge gaugeDistancia;
    private Gauge gaugeTemperatura;
    private Gauge gaugeHumedad;

    // Layouts contenedores
    private LinearLayout layoutGaugeDistancia;
    private LinearLayout layoutGaugesPequenos;

    private Tabla tabla;
    private Graficador graficador;

    private ClientePubSubMQTT cliente;

    // private JSONObject obj;

    private Thread hilo;

    private int periodo_muestreo = 200;
    private int contador = 0;
    // private int numero_datos=0;
    // private int n=-1;
    private float medidaDistancia;
    private float medidaTemperatura;
    private float medidaHumedad;

    // private int tiempo_base=0;
    // private int tiempo_anterior;
    // private int tiempo_real;

    // hilo para actualizar tabla
    private final Handler myHandler = new Handler();

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        gestionarResolucion();

        crearElementosGUI();

        /*
         * Para informar cómo se debe pegar el administrador de
         * diseño LinearLayout obtenido con el método crearGui()
         */
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // pegar el contenedor con la GUI
        this.setContentView(crearGUI(), parametro_layout_principal);

        eventos();

        crearCliente();

        // hilo = new Thread(this); //Se inicia al conectar

    }

    private void gestionarResolucion() {

        // tamano de letra para usar acomodado a la resolución de pantalla
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);

    }

    /* método responsable de la creación de los elementos de la GUI */
    private void crearElementosGUI() {

        // gauge distancia
        gaugeDistancia = new Gauge(this);
        gaugeDistancia.setRango(0, 400);
        gaugeDistancia.setUnidades("cm");
        gaugeDistancia.setSeparacionesDivisionesGrandes(5);
        gaugeDistancia.setPrecision(2); // 2 decimales para distancia

        // gauge temperatura
        gaugeTemperatura = new Gauge(this);
        gaugeTemperatura.setRango(0, 50);
        gaugeTemperatura.setUnidades("°C");
        gaugeTemperatura.setSeparacionesDivisionesGrandes(5);

        // gauge humedad
        gaugeHumedad = new Gauge(this);
        gaugeHumedad.setRango(0, 100);
        gaugeHumedad.setUnidades("%HR");
        gaugeHumedad.setSeparacionesDivisionesGrandes(10);

        // tabla
        tabla = new Tabla(this);
        tabla.setEtiquetaColumnas("Tiempo (s)", "Distancia (cm)");

        // graficador
        graficador = new Graficador(this);
        // se está muestreando cada segundo (1000 ms)
        graficador.setTituloEjeX("Tiempo (s)");
        graficador.setTituloEjeY("Distancia (cm)");
        graficador.setGrosorLinea(2f);
        graficador.setColorLinea(Color.RED);
        graficador.setColorValores(Color.YELLOW);
        graficador.setColorMarcadores(Color.GREEN);
        graficador.setColorFondo(Color.BLACK);
        graficador.setColorTextoEjes(Color.WHITE);

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

    /* método responsable de administrar el diseño de la GUI */
    private LinearLayout crearGUI() {

        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.VERTICAL);
        linear_layout_principal.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_principal.setWeightSum(10.0f);

        // LinearLayout primera fila: Gauge Grande (Distancia)
        layoutGaugeDistancia = new LinearLayout(this);
        layoutGaugeDistancia.setOrientation(LinearLayout.VERTICAL);
        layoutGaugeDistancia.setGravity(Gravity.FILL);
        layoutGaugeDistancia.setBackgroundColor(Color.WHITE);

        // LinearLayout para Gauges Pequeños (Temperatura y Humedad)
        layoutGaugesPequenos = new LinearLayout(this);
        layoutGaugesPequenos.setOrientation(LinearLayout.HORIZONTAL);
        layoutGaugesPequenos.setGravity(Gravity.FILL);
        layoutGaugesPequenos.setBackgroundColor(Color.WHITE);
        layoutGaugesPequenos.setWeightSum(2.0f);

        // LinearLayout fila Tabla/Grafica
        linear_layout_tabla_grafica = new LinearLayout(this);
        linear_layout_tabla_grafica.setOrientation(LinearLayout.VERTICAL);
        linear_layout_tabla_grafica.setGravity(Gravity.FILL);
        linear_layout_tabla_grafica.setBackgroundColor(Color.WHITE);
        // linear_layout_tabla_grafica.setWeightSum(1.0f);

        // Fila botones
        LinearLayout linear_layout_botones = new LinearLayout(this);
        linear_layout_botones.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_botones.setBackgroundColor(Color.rgb(183, 216, 199));
        linear_layout_botones.setWeightSum(2.0f);

        // Fila aviso
        LinearLayout linear_layout_aviso = new LinearLayout(this);
        linear_layout_aviso.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_aviso.setBackgroundColor(Color.rgb(183, 216, 199));

        // pegar layouts al principal

        // Gauge distancia: Weight 3.5
        LinearLayout.LayoutParams paramsGaugeDistancia = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsGaugeDistancia.weight = 3.5f;
        paramsGaugeDistancia.setMargins(5, 5, 5, 5);
        layoutGaugeDistancia.setLayoutParams(paramsGaugeDistancia);
        linear_layout_principal.addView(layoutGaugeDistancia);

        // Gauges pequeños: Weight 2.5
        LinearLayout.LayoutParams paramsGaugesPequenos = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsGaugesPequenos.weight = 2.5f;
        paramsGaugesPequenos.setMargins(5, 5, 5, 5);
        layoutGaugesPequenos.setLayoutParams(paramsGaugesPequenos);
        linear_layout_principal.addView(layoutGaugesPequenos);

        // Tabla/Grafica: Weight 3.0
        LinearLayout.LayoutParams paramsTablaGrafica = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsTablaGrafica.weight = 3.0f;
        paramsTablaGrafica.setMargins(5, 5, 5, 5);
        linear_layout_tabla_grafica.setLayoutParams(paramsTablaGrafica);
        linear_layout_principal.addView(linear_layout_tabla_grafica);

        // Aviso: Weight 0.3
        LinearLayout.LayoutParams paramsAviso = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsAviso.weight = 0.3f;
        linear_layout_aviso.setLayoutParams(paramsAviso);
        linear_layout_principal.addView(linear_layout_aviso);

        // Botones: Weight 0.7
        LinearLayout.LayoutParams paramsBotones = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        paramsBotones.weight = 0.7f;
        linear_layout_botones.setLayoutParams(paramsBotones);
        linear_layout_principal.addView(linear_layout_botones);

        // Pegar elementos en layouts
        layoutGaugeDistancia.addView(gaugeDistancia);

        LinearLayout.LayoutParams paramsGaugePeq = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsGaugePeq.weight = 1.0f;
        layoutGaugesPequenos.addView(gaugeTemperatura, paramsGaugePeq);
        layoutGaugesPequenos.addView(gaugeHumedad, paramsGaugePeq);

        parametros_tabla_grafica = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        // Iniciar con Tabla
        linear_layout_tabla_grafica.addView(tabla, parametros_tabla_grafica);

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

                    // Detener hilo no es trivial con Thread.stop() (deprecated)
                    // Dejaremos que corra o usaremos una flag, pero aquí simplemente
                    // desactivamos UI.

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
                    // pegar tabla en segunda fila
                    linear_layout_tabla_grafica.addView(tabla, parametros_tabla_grafica);

                } else {

                    botonTablaGrafica.setText("TABLA");
                    linear_layout_tabla_grafica.removeView(tabla);
                    // pegar graficador en segunda fila
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

    // no estaba
    private void borrarDatos() {
        contador = 0;
        AlmacenDatosRAM.datosDistancia.clear();
        // n=-1;
        // numero_datos=0;
        tabla.borrar();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // Esto es lo que hace mi botón al pulsar ir a atrás
            // alerta();
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

    /*
     * comunicaciones IoT SUB
     */
    private void leer() {

        // String JSON
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
            // Si es null es que no ha llegado nada nuevo aun o no hay conexión
            // Mantener estado anterior o mostrar desconectado?
            // Si cliente dice conectado, es que estamos esperando.
            if (!AlmacenDatosRAM.conectado) {
                AlmacenDatosRAM.estado_conexion_nube = 1;
            }
        }

        // Actualizar aviso siempre en UI Thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actualizarAviso();
            }
        });

    }

    // obtener la información del JSON
    public boolean convertirStrigJson(String datoString) {

        try {
            JSONObject obj = new JSONObject(datoString);

            // Ajustar claves para coincidir con el código del Pub (Arduino)
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
                // Actualizar UI en el hilo principal
                try {
                    gaugeDistancia.setMedida(medidaDistancia);
                    gaugeTemperatura.setMedida(medidaTemperatura);
                    gaugeHumedad.setMedida(medidaHumedad);

                    AlmacenDatosRAM.datosDistancia.add(medidaDistancia);
                    contador++;

                    tabla.enviarDatos((float) contador, medidaDistancia);

                    actualizarGrafica();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void actualizarGrafica() {

        if (AlmacenDatosRAM.datosDistancia.isEmpty())
            return;

        ArrayList<Entry> entries = new ArrayList<>();
        int inicio = 0;
        int total = AlmacenDatosRAM.datosDistancia.size();

        if (total > AlmacenDatosRAM.nDatosGraficar) {
            inicio = total - AlmacenDatosRAM.nDatosGraficar;
        }

        for (int i = inicio; i < total; i++) {
            entries.add(new Entry(i, AlmacenDatosRAM.datosDistancia.get(i)));
        }

        graficador.setDatos(entries);
        graficador.notifyDataSetChanged();
        graficador.invalidate();

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
            // textviewAviso.setBackgroundColor(Color.RED);

        } else if (AlmacenDatosRAM.estado_conexion_nube == 4) {

            String distFormateada = String.format(java.util.Locale.US, "%.2f", medidaDistancia);
            AlmacenDatosRAM.conectado_PubSub = "  Recibiendo datos: D=" + distFormateada + " T=" + medidaTemperatura
                    + " H=" + medidaHumedad;
            textviewAviso.setText(AlmacenDatosRAM.conectado_PubSub);
            textviewAviso.setBackgroundColor(Color.GREEN);

        }

    }

}
