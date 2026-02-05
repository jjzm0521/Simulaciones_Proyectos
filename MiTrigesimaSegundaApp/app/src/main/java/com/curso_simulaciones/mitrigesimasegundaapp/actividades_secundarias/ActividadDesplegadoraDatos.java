package com.curso_simulaciones.mitrigesimasegundaapp.actividades_secundarias;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.curso_simulaciones.mitrigesimasegundaapp.AlmacenDatosRAM;
import com.curso_simulaciones.mitrigesimasegundaapp.modelo.GuardarDatosPersistentesTXT;
import com.curso_simulaciones.mitrigesimasegundaapp.modelo.HiloAnimacion;
import com.curso_simulaciones.mitrigesimasegundaapp.vista.DialogoSalir;
import com.curso_simulaciones.mitrigesimasegundaapp.vista.Luxometro;
import com.curso_simulaciones.mitrigesimasegundaapp.vista.TablaSimple;

public class ActividadDesplegadoraDatos extends Activity {

    private Luxometro luxometro;
    private TablaSimple tabla;

    private Button botonGuardar, botonEmpezar;

    private HiloAnimacion hilo;

    private final Handler myHandler = new Handler();
    public boolean activarBotones = false;

    private GuardarDatosPersistentesTXT archivoTxt;

    private int tamanoLetraResolucionIncluida;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionandoResolucion();
        crearElementosGUI();

        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(crearGUI(), parametro_layout_principal);

        archivoTxt = new GuardarDatosPersistentesTXT();
        eventos();
    }

    private void gestionandoResolucion() {
        tamanoLetraResolucionIncluida = AlmacenDatosRAM.tamanoLetraResolucionIncluida;
    }

    private void crearElementosGUI() {
        luxometro = new Luxometro(this);
        luxometro.setUnidades("lx");
        luxometro.setAngulosSectores(20, 190, 40);
        luxometro.setColorSectores(Color.GREEN, Color.argb(100, 200, 200, 0), Color.YELLOW);
        luxometro.setColorFranjaDinámica(Color.RED);

        tabla = new TablaSimple(this);
        tabla.setEtiquetaColumnas("Tiempo (s)", "Iluminancia (lx)");

        botonGuardar = new Button(this);
        botonGuardar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonGuardar.getBackground().setColorFilter(Color.rgb(255, 255, 100), PorterDuff.Mode.MULTIPLY);
        botonGuardar.setText("GUARDAR");
        botonGuardar.setEnabled(false);

        botonEmpezar = new Button(this);
        botonEmpezar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        botonEmpezar.getBackground().setColorFilter(Color.rgb(255, 255, 100), PorterDuff.Mode.MULTIPLY);
        botonEmpezar.setText("EMPEZAR");
    }

    private LinearLayout crearGUI() {
        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_principal.setGravity(Gravity.FILL);
        linear_layout_principal.setBackgroundColor(Color.WHITE);
        linear_layout_principal.setWeightSum(10.0f);

        LinearLayout linear_layout_primera_columna = new LinearLayout(this);
        linear_layout_primera_columna.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_primera_columna.setGravity(Gravity.FILL);
        linear_layout_primera_columna.setBackgroundColor(Color.rgb(245, 245, 245));

        LinearLayout linear_layout_segunda_columna = new LinearLayout(this);
        linear_layout_segunda_columna.setOrientation(LinearLayout.VERTICAL);
        linear_layout_segunda_columna.setGravity(Gravity.FILL);
        linear_layout_segunda_columna.setBackgroundColor(Color.rgb(245, 245, 245));
        linear_layout_segunda_columna.setWeightSum(10.0f);

        LinearLayout linear_layout_primera_fila_segunda_columna = new LinearLayout(this);
        linear_layout_primera_fila_segunda_columna.setOrientation(LinearLayout.VERTICAL);
        linear_layout_primera_fila_segunda_columna.setGravity(Gravity.FILL);

        LinearLayout linear_layout_segunda_fila_segunda_columna = new LinearLayout(this);
        linear_layout_segunda_fila_segunda_columna.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_segunda_fila_segunda_columna.setGravity(Gravity.FILL);
        linear_layout_segunda_fila_segunda_columna.setWeightSum(2.0f);

        LinearLayout.LayoutParams parametros_primera_columna = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_primera_columna.weight = 5.0f;
        parametros_primera_columna.setMargins(20, 20, 20, 20);
        linear_layout_primera_columna.setLayoutParams(parametros_primera_columna);
        linear_layout_principal.addView(linear_layout_primera_columna);

        LinearLayout.LayoutParams parametros_segunda_columna = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_segunda_columna.weight = 5.0f;
        parametros_segunda_columna.setMargins(20, 20, 20, 20);
        linear_layout_segunda_columna.setLayoutParams(parametros_segunda_columna);
        linear_layout_principal.addView(linear_layout_segunda_columna);

        linear_layout_primera_columna.addView(luxometro);

        LinearLayout.LayoutParams parametros_primera_fila_segunda_columna = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_primera_fila_segunda_columna.weight = 8.0f;
        parametros_primera_fila_segunda_columna.setMargins(20, 20, 20, 20);
        linear_layout_primera_fila_segunda_columna.setLayoutParams(parametros_primera_fila_segunda_columna);
        linear_layout_segunda_columna.addView(linear_layout_primera_fila_segunda_columna);

        LinearLayout.LayoutParams parametros_segunda_fila_segunda_columna = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametros_segunda_fila_segunda_columna.weight = 2.0f;
        parametros_segunda_fila_segunda_columna.setMargins(20, 20, 20, 20);
        linear_layout_segunda_fila_segunda_columna.setLayoutParams(parametros_segunda_fila_segunda_columna);
        linear_layout_segunda_columna.addView(linear_layout_segunda_fila_segunda_columna);

        linear_layout_primera_fila_segunda_columna.addView(tabla);

        LinearLayout.LayoutParams parametros_botones_segunda_fila_segunda_columna = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT);
        parametros_botones_segunda_fila_segunda_columna.weight = 1.0f;
        botonEmpezar.setLayoutParams(parametros_botones_segunda_fila_segunda_columna);
        botonGuardar.setLayoutParams(parametros_botones_segunda_fila_segunda_columna);
        linear_layout_segunda_fila_segunda_columna.addView(botonEmpezar);
        linear_layout_segunda_fila_segunda_columna.addView(botonGuardar);

        return linear_layout_principal;
    }

    private void eventos() {
        botonGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                guardar();
            }
        });

        botonEmpezar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                empezar();
            }
        });
    }

    private void empezar() {
        if (hilo != null)
            hilo = null;
        activarBotones = false;
        AlmacenDatosRAM.tiempo = 0;
        borrarDatos();
        archivoTxt.borrarDatos();
        hilo = new HiloAnimacion(this);
        hilo.corriendo = true;
        hilo.start();
    }

    private void guardar() {
        String carpeta = AlmacenDatosRAM.path;
        archivoTxt.guardar(this, carpeta);
    }

    private void borrarDatos() {
        tabla.borrar();
    }

    public void hacerTrabajoDuro() {
        myHandler.post(updateRunnable);
    }

    final Runnable updateRunnable = new Runnable() {
        public void run() {
            float tiempo = 0.001f * AlmacenDatosRAM.tiempo;
            tiempo = (float) (Math.floor(100 * tiempo) / 100f);
            float medida = AlmacenDatosRAM.datoActual;
            medida = (float) (Math.floor(100 * medida) / 100f);

            tabla.enviarDatos(tiempo, medida);
            archivoTxt.llenarDatos(tiempo, medida);

            if (activarBotones == true) {
                botonGuardar.setEnabled(true);
                botonEmpezar.setEnabled(true);
            } else {
                botonGuardar.setEnabled(false);
                botonEmpezar.setEnabled(false);
            }
        }
    };

    protected void onPause() {
        super.onPause();
        // Los datos ya no se borran aquí para permitir que el guardado funcione
        // correctamente
        // Los datos se borran cuando el usuario presiona "EMPEZAR" para una nueva
        // medición
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DialogoSalir dialogo_salir = new DialogoSalir(this);
            dialogo_salir.mostrarPopMenu();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
