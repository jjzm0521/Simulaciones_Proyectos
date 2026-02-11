package com.curso_simulaciones.mitrigesimaquintaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.curso_simulaciones.mitrigesimaquintaapp.AlmacenDatosRAM;
import com.curso_simulaciones.mitrigesimaquintaapp.R;
import com.curso_simulaciones.mitrigesimaquintaapp.modelo.ServidorBluetooth;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Acelerometro;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Boton;
import com.curso_simulaciones.mitrigesimaquintaapp.vista.Luxometro;

import org.json.JSONException;
import org.json.JSONObject;

public class ActividadComoServidorBluetooth extends Activity implements Runnable {

    private Boton ax, ay, az, a;

    private float medida_acelerometro, medida_luxometro;

    private Acelerometro acelerometro;
    private Luxometro luxometro;
    private int componente_aceleracion;

    private EditText editTextRecibir;
    private TextView textviewAviso;

    private TextView textviewRol;
    private Button botonEmpezar;
    private int tamanoLetraResolucionIncluida;
    private int COLOR_2 = Color.rgb(156, 220, 80);

    private final Handler myHandler = new Handler();

    private ServidorBluetooth servidor;

    private boolean corriendo;
    private long periodo_muestreo = 100;// pausas de 100 ms
    private Thread hilo;

    private float medida_campo_magnetico, medida_aceleracion;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        gestionarResolucion();

        creacionElementosGUI();

        setContentView(crearGUI());

        eventos();

        hilo = new Thread(this);

    }

    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.8 * AlmacenDatosRAM.tamanoLetraResolucionIncluida);
    }

    private void creacionElementosGUI() {

        ax = new Boton(this);
        ax.setImagen(R.drawable.ax);
        ay = new Boton(this);
        ay.setImagen(R.drawable.ay);
        az = new Boton(this);
        az.setImagen(R.drawable.az);
        a = new Boton(this);
        a.setImagen(R.drawable.a);

        acelerometro = new Acelerometro(this);
        acelerometro.setUnidades(" m.s-2");
        acelerometro.setRango(0, 50);
        acelerometro.setAngulosSectores(50, 100, 100);
        acelerometro.setColorFranjaDinámica(Color.rgb(0, 255, 0));
        acelerometro.captarSensor(this);

        luxometro = new Luxometro(this);
        luxometro.setUnidades("lx");
        luxometro.captarSensor(this);

        textviewRol = new TextView(this);
        textviewRol.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (0.8 * tamanoLetraResolucionIncluida));
        textviewRol.setBackgroundColor(Color.YELLOW);
        textviewRol.setText("SERVIDOR");
        textviewRol.setTextColor(Color.RED);
        textviewRol.setGravity(Gravity.CENTER);
        textviewRol.setEnabled(false);

        textviewAviso = new TextView(this);
        textviewAviso.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int) (0.8 * tamanoLetraResolucionIncluida));
        textviewAviso.setBackgroundColor(Color.YELLOW);
        textviewAviso.setTextColor(Color.RED);
        textviewAviso.setGravity(Gravity.CENTER);
        textviewAviso.setText(AlmacenDatosRAM.conexion_bluetooth);

        editTextRecibir = new EditText(this);
        editTextRecibir.setBackgroundColor(Color.BLACK);
        editTextRecibir.setTextColor(Color.YELLOW);
        editTextRecibir.setTextSize((int) (0.8 * tamanoLetraResolucionIncluida));
        editTextRecibir.setText("Despliega la magnitud de la aceleración sensada por el dispositivo móvil CLIENTE");
        editTextRecibir.setEnabled(false);
        editTextRecibir.setKeyListener(null);

        botonEmpezar = new Button(this);
        botonEmpezar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonEmpezar.setText("EMPEZAR");
        botonEmpezar.getBackground().setColorFilter(COLOR_2, PorterDuff.Mode.MULTIPLY);

        crearServidor();

    }

    private LinearLayout crearGUI() {

        LinearLayout linearLayoutPrincipal = new LinearLayout(this);
        linearLayoutPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearLayoutPrincipal.setBackgroundColor(Color.BLACK);
        linearLayoutPrincipal.setWeightSum(10f);

        LinearLayout linearLayoutFilaUno = new LinearLayout(this);
        linearLayoutFilaUno.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaDos = new LinearLayout(this);
        linearLayoutFilaDos.setBackgroundColor(Color.WHITE);
        linearLayoutFilaDos.setWeightSum(10f);

        LinearLayout linearLayoutColumnaDosIzquierda = new LinearLayout(this);

        LinearLayout linearLayoutColumnaDosDerecha = new LinearLayout(this);
        linearLayoutColumnaDosDerecha.setOrientation(LinearLayout.VERTICAL);
        linearLayoutColumnaDosDerecha.setWeightSum(4f);

        LinearLayout linearLayoutFilaTres = new LinearLayout(this);
        linearLayoutFilaTres.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaCuatro = new LinearLayout(this);
        linearLayoutFilaCuatro.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCuatro.setBackgroundColor(Color.WHITE);

        LinearLayout linearLayoutFilaCinco = new LinearLayout(this);
        linearLayoutFilaCinco.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutFilaCinco.setBackgroundColor(Color.WHITE);

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

        LinearLayout.LayoutParams parametrosColumnaDosIzquierda = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaDosIzquierda.weight = 8.0f;
        linearLayoutFilaDos.addView(linearLayoutColumnaDosIzquierda, parametrosColumnaDosIzquierda);

        LinearLayout.LayoutParams parametrosColumnaDosDerecha = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametrosColumnaDosDerecha.weight = 2.0f;
        linearLayoutFilaDos.addView(linearLayoutColumnaDosDerecha, parametrosColumnaDosDerecha);

        linearLayoutColumnaDosIzquierda.addView(acelerometro);

        LinearLayout.LayoutParams parametrosPegadoBotonesAceleracion = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosPegadoBotonesAceleracion.weight = 1.0f;
        linearLayoutColumnaDosDerecha.addView(ax, parametrosPegadoBotonesAceleracion);
        linearLayoutColumnaDosDerecha.addView(ay, parametrosPegadoBotonesAceleracion);
        linearLayoutColumnaDosDerecha.addView(az, parametrosPegadoBotonesAceleracion);
        linearLayoutColumnaDosDerecha.addView(a, parametrosPegadoBotonesAceleracion);

        LinearLayout.LayoutParams parametrosPegadoEditTextRol = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayoutFilaUno.setGravity(Gravity.CENTER);
        textviewRol.setPadding(20, 20, 20, 20);
        parametrosPegadoEditTextRol.setMargins(20, 20, 20, 20);
        textviewRol.setLayoutParams(parametrosPegadoEditTextRol);
        linearLayoutFilaUno.addView(textviewRol);

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

        linearLayoutFilaCinco.addView(botonEmpezar, parametrosPegadoBotones);

        return linearLayoutPrincipal;
    }

    private void eventos() {

        botonEmpezar.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                empezarComunicacionConCliente();
                hilo.start();
                botonEmpezar.setEnabled(false);

            }
        });

        ax.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                acelerometro.setComponenteAcelerometro(1);
                componente_aceleracion = 1;

            }
        });

        ay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                acelerometro.setComponenteAcelerometro(2);
                componente_aceleracion = 2;

            }
        });

        az.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                acelerometro.setComponenteAcelerometro(3);
                componente_aceleracion = 3;

            }
        });

        a.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                acelerometro.setComponenteAcelerometro(4);
                componente_aceleracion = 4;

            }
        });

    }

    private void crearServidor() {

        servidor = new ServidorBluetooth();
        servidor.abrirSocketServidor();

    }

    private void empezarComunicacionConCliente() {

        servidor.abrirSocketCliente();
        servidor.abrirFlujoSalida();
        servidor.abrirFlujoEntrada();

    }

    private void terminarComunicacionConCliente() {

        if (servidor != null) {
            servidor.cerrarFlujoSalida();
            servidor.cerrarFlujoEntrada();
            servidor.cerrarSocketCliente();
        }
    }

    public void detener() {

        corriendo = false;
        terminarComunicacionConCliente();

    }

    public void run() {

        corriendo = true;

        while (corriendo) {

            try {
                Thread.sleep(periodo_muestreo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            String dato = getStringJSON();
            escribir(dato);

            AlmacenDatosRAM.conexion_bluetooth = "TX: " + dato;
            hacerTrabajoDuro();

        }

        AlmacenDatosRAM.conexion_bluetooth = " ";

    }

    private void escribir(String dato) {

        // Add newline delimiter for proper framing
        String datoConSalto = dato + "\n";
        byte[] dato_en_byte = datoConSalto.getBytes();
        if (dato_en_byte != null && servidor != null) {
            servidor.escribirBytes(dato_en_byte);

        }
    }

    private String getStringJSON() {

        JSONObject obj = new JSONObject();

        try {

            medida_acelerometro = acelerometro.getMedida();
            medida_luxometro = luxometro.getMedida();
            obj.put("componente", componente_aceleracion);
            obj.put("valor_acelerometro", medida_acelerometro);
            obj.put("valor_luxometro", medida_luxometro);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj.toString();

    }

    protected void onPause() {
        super.onPause();
        corriendo = false;

    }

    protected void onDestroy() {
        super.onDestroy();
        terminarComunicacionConCliente();

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
