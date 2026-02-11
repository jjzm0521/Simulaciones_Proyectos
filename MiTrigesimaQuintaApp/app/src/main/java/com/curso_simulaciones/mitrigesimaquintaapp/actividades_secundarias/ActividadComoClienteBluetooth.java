package com.curso_simulaciones.mitrigesimaquintaapp.actividades_secundarias;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.mitrigesimaquintaapp.AlmacenDatosRAM;
import com.curso_simulaciones.mitrigesimaquintaapp.modelo.ClienteBluetooth;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Acelerometro;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Luxometro;

import org.json.JSONException;
import org.json.JSONObject;

public class ActividadComoClienteBluetooth extends Activity implements Runnable {

    private Acelerometro acelerometro;
    private Luxometro luxometro;

    private Button botonConectar, botonBuscar;
    private TextView textviewRol;
    private TextView textviewAviso;

    private int tamanoLetraResolucionIncluida;
    private int COLOR_1 = Color.rgb(220, 156, 80);

    private final Handler myHandler = new Handler();

    private ClienteBluetooth cliente;

    private JSONObject obj;

    private long periodo_muestreo = 50;// pausas de 50 ms

    private boolean corriendo;

    private Thread hilo;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        gestionarResolucion();

        creacionElementosGUI();

        setContentView(crearGUI());

        eventos();

    }

    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);
    }

    private void creacionElementosGUI() {

        acelerometro = new Acelerometro(this);
        acelerometro.setUnidades(" a m/s^2");
        acelerometro.setAngulosSectores(50, 100, 100);
        acelerometro.setColorFranjaDinámica(Color.rgb(0, 255, 0));

        luxometro = new Luxometro(this);
        luxometro.setUnidades("lx");

        textviewRol = new TextView(this);
        textviewRol.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (0.8 * tamanoLetraResolucionIncluida));
        textviewRol.setBackgroundColor(Color.YELLOW);
        textviewRol.setText("CLIENTE");
        textviewRol.setTextColor(Color.RED);
        textviewRol.setGravity(Gravity.CENTER);
        textviewRol.setEnabled(false);

        textviewAviso = new TextView(this);
        textviewAviso.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (0.8 * tamanoLetraResolucionIncluida));
        textviewAviso.setBackgroundColor(Color.YELLOW);
        textviewAviso.setTextColor(Color.RED);
        textviewAviso.setGravity(Gravity.CENTER);
        textviewAviso.setText(AlmacenDatosRAM.conexion_bluetooth);

        botonBuscar = new Button(this);
        botonBuscar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonBuscar.setText("BUSCAR");
        botonBuscar.getBackground().setColorFilter(COLOR_1, PorterDuff.Mode.MULTIPLY);
        botonBuscar.setEnabled(true);

        botonConectar = new Button(this);
        botonConectar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonConectar.setText("CONECTAR");
        botonConectar.getBackground().setColorFilter(COLOR_1, PorterDuff.Mode.MULTIPLY);
        botonConectar.setEnabled(false);

    }

    private LinearLayout crearGUI() {

        LinearLayout linearLayoutPrincipal = new LinearLayout(this);
        linearLayoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPrincipal.setBackgroundColor(Color.WHITE);
        linearLayoutPrincipal.setWeightSum(10f);

        LinearLayout linearLayoutFilaUno = new LinearLayout(this);
        linearLayoutFilaUno.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaDos = new LinearLayout(this);
        linearLayoutFilaDos.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaTres = new LinearLayout(this);
        linearLayoutFilaTres.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaCuatro = new LinearLayout(this);
        linearLayoutFilaCuatro.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCuatro.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaCinco = new LinearLayout(this);
        linearLayoutFilaCinco.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCinco.setBackgroundColor(Color.WHITE);
        linearLayoutFilaCinco.setWeightSum(2f);

        LinearLayout.LayoutParams parametrosFilaUno = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0);
        parametrosFilaUno.weight = 1.0f;
        linearLayoutFilaUno.setLayoutParams(parametrosFilaUno);

        LinearLayout.LayoutParams parametrosFilaDos = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                0);
        parametrosFilaDos.weight = 3.75f;
        linearLayoutFilaDos.setLayoutParams(parametrosFilaDos);

        LinearLayout.LayoutParams parametrosFilaTres = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaTres.weight = 3.75f;
        linearLayoutFilaTres.setLayoutParams(parametrosFilaTres);

        LinearLayout.LayoutParams parametrosFilaCuatro = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaCuatro.weight = 0.5f;
        linearLayoutFilaCuatro.setLayoutParams(parametrosFilaCuatro);

        LinearLayout.LayoutParams parametrosFilaCinco = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosFilaCinco.weight = 1.0f;
        linearLayoutFilaCinco.setLayoutParams(parametrosFilaCinco);

        linearLayoutPrincipal.addView(linearLayoutFilaUno);
        linearLayoutPrincipal.addView(linearLayoutFilaDos);
        linearLayoutPrincipal.addView(linearLayoutFilaTres);
        linearLayoutPrincipal.addView(linearLayoutFilaCuatro);
        linearLayoutPrincipal.addView(linearLayoutFilaCinco);

        LinearLayout.LayoutParams parametrosPegadoEditTextRol = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaUno.setGravity(Gravity.CENTER);
        textviewRol.setPadding(20, 20, 20, 20);
        parametrosPegadoEditTextRol.setMargins(20, 20, 20, 20);
        textviewRol.setLayoutParams(parametrosPegadoEditTextRol);
        linearLayoutFilaUno.addView(textviewRol);

        LinearLayout.LayoutParams parametrosPegadoAcelerometro = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaDos.setGravity(Gravity.CENTER);
        linearLayoutFilaDos.addView(acelerometro, parametrosPegadoAcelerometro);

        LinearLayout.LayoutParams parametrosPegadoLuxometro = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaTres.setGravity(Gravity.CENTER);
        linearLayoutFilaTres.addView(luxometro, parametrosPegadoLuxometro);

        LinearLayout.LayoutParams parametrosPegadoAviso = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaCuatro.addView(textviewAviso, parametrosPegadoAviso);

        LinearLayout.LayoutParams parametrosPegadoBotones = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosPegadoBotones.weight = 1.0f;
        linearLayoutFilaCinco.addView(botonBuscar, parametrosPegadoBotones);
        linearLayoutFilaCinco.addView(botonConectar, parametrosPegadoBotones);

        return linearLayoutPrincipal;
    }

    private void eventos() {

        botonBuscar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                lanzarBuscandoDispositivos();
                botonConectar.setEnabled(true);
                botonBuscar.setEnabled(false);

            }
        });

        botonConectar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (botonConectar.getText() == "CONECTAR") {
                    botonConectar.setText("EMPEZAR");
                    botonBuscar.setEnabled(false);
                    crearCliente();
                    cliente.conectarSocketCliente();

                } else {

                    botonConectar.setEnabled(false);
                    empezarHilo();

                }

            }
        });

    }

    private void lanzarBuscandoDispositivos() {

        Intent intent = new Intent(this, ActividadEscaneoDispositivos.class);
        startActivity(intent);

    }

    public void empezarHilo() {

        hilo = new Thread(this);
        hilo.start();

    }

    private void crearCliente() {

        String direccion = AlmacenDatosRAM.direccion;
        cliente = new ClienteBluetooth();

        cliente.abrirSocketCliente(direccion);
        empezarComunicacionConServidor();

    }

    private void empezarComunicacionConServidor() {

        cliente.abrirFlujoEntrada();
        cliente.abrirFlujoSalida();

    }

    private void terminarComunicacionConServidor() {

        if (cliente != null) {
            cliente.cerrarFlujoEntrada();
            cliente.cerrarFlujoSalida();
            cliente.cerrarSocketCliente();
        }
    }

    @Override
    public void run() {
        corriendo = true;

        while (corriendo) {

            try {
                Thread.sleep(periodo_muestreo);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            leer();
            hacerTrabajoDuro();

        }

        AlmacenDatosRAM.conexion_bluetooth = " ";

    }

    private void leer() {

        if (cliente != null) {
            String nuevo_dato_string = cliente.leerString();
            if (nuevo_dato_string != null) {
                AlmacenDatosRAM.conexion_bluetooth = "RX: " + nuevo_dato_string;
                convertirStrigJson(nuevo_dato_string);
            }
        }
    }

    public void convertirStrigJson(String datoString) {

        try {
            obj = new JSONObject(datoString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (obj != null && obj.has("componente")) {
                int componente_aceleracion = obj.getInt("componente");
                acelerometro.setComponenteAcelerometro(componente_aceleracion);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            if (obj != null && obj.has("valor_acelerometro") && obj.has("valor_luxometro")) {
                float medida_aclerometro = (float) (obj.getDouble("valor_acelerometro"));
                acelerometro.setMedida(medida_aclerometro);
                float medida_luxometro = (float) (obj.getDouble("valor_luxometro"));
                luxometro.cambiarEscala(medida_luxometro);
                luxometro.setMedida(medida_luxometro);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onPause() {
        super.onPause();
        corriendo = false;

    }

    protected void onDestroy() {
        super.onDestroy();
        terminarComunicacionConServidor();

    }

    public void hacerTrabajoDuro() {
        myHandler.post(updateRunnable);
    }

    final Runnable updateRunnable = new Runnable() {
        public void run() {
            avisoEstadoComunicacion();
        }
    };

    private void avisoEstadoComunicacion() {

        textviewAviso.setText(AlmacenDatosRAM.conexion_bluetooth);

    }

}
