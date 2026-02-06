package com.curso_simulaciones.mivigesimanovenaapp.actividades_secundarias;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.mivigesimanovenaapp.R;
import com.curso_simulaciones.mivigesimanovenaapp.datos.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimanovenaapp.modelo.HiloAnimacion;
import com.curso_simulaciones.mivigesimanovenaapp.vista.Boton;
import com.curso_simulaciones.mivigesimanovenaapp.vista.Gaussimetro;
import com.curso_simulaciones.mivigesimanovenaapp.vista.Graficador;

public class ActividadDesplegadoraDatos extends Activity {

    private Boton bx, by, bz, b;

    private Gaussimetro gaussimetro;
    public Graficador graficador;

    /*
     * Hilo responsable de la animación
     * El trabajo de animación es mejor manejarlo en hilo
     * aparte para evitar bloqueos de la aplicación
     * debido al manejo simultáneo de la GUI con la Acivity
     */
    private HiloAnimacion hilo;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // para crear elementos de la GUI
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

        hilo = new HiloAnimacion(this);
        hilo.start();

    }// fin del método onCreate

    private void crearElementosGUI() {

        // botones
        // botones
        bx = new Boton(this);
        by = new Boton(this);
        bz = new Boton(this);
        b = new Boton(this);

        // gauge
        gaussimetro = new Gaussimetro(this);

        // graficador
        graficador = new Graficador(this);
        // se está muestreando cada segundo (1000 ms)
        graficador.setTituloEjeX("Tiempo (s)");
        graficador.setTituloEjeY("Campo Magnético Bx (µT)");
        graficador.setGrosorLinea(2f);
        graficador.setColorLinea(Color.RED);
        graficador.setColorValores(Color.YELLOW);
        graficador.setColorMarcadores(Color.GREEN);
        graficador.setColorFondo(Color.BLACK);
        graficador.setColorTextoEjes(Color.WHITE);

    }

    /* método responsable de administrar el diseño de la GUI */
    private ViewGroup crearGUI() {
        // --- ROOT CONTAINER ---
        android.widget.FrameLayout root = new android.widget.FrameLayout(this);
        root.setBackgroundColor(Color.parseColor("#111111"));
        root.setFitsSystemWindows(true);

        // --- CONTENEDOR PRINCIPAL ---
        LinearLayout linear_layout_principal = new LinearLayout(this);
        linear_layout_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_layout_principal.setGravity(Gravity.CENTER);
        linear_layout_principal.setBackgroundResource(R.drawable.rounded_border_white);
        linear_layout_principal.setPadding(20, 20, 20, 20);
        linear_layout_principal.setWeightSum(10);
        
        android.widget.FrameLayout.LayoutParams params_principal = new android.widget.FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.MATCH_PARENT
        );
        params_principal.setMargins(30, 30, 30, 30);
        linear_layout_principal.setLayoutParams(params_principal);
        root.addView(linear_layout_principal);

        // --- SECCIÓN 1: GAUGE (Izquierda) ---
        LinearLayout seccion_izquierda = new LinearLayout(this);
        seccion_izquierda.setOrientation(LinearLayout.VERTICAL);
        seccion_izquierda.setGravity(Gravity.CENTER);
        seccion_izquierda.setBackgroundResource(R.drawable.rounded_border_black); // Usar borde negro
        seccion_izquierda.setPadding(20, 20, 20, 20);
        
        LinearLayout.LayoutParams param_izq = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        param_izq.weight = 5.0f;
        param_izq.setMargins(10, 10, 5, 10);
        seccion_izquierda.setLayoutParams(param_izq);
        
        // Contenedor Interno (Rojo Cuadrado - Margen solamente)
        LinearLayout contenedor_rojo = new LinearLayout(this);
        contenedor_rojo.setOrientation(LinearLayout.VERTICAL);
        contenedor_rojo.setGravity(Gravity.CENTER);
        contenedor_rojo.setBackgroundResource(R.drawable.square_border_red); // Solo margen
        // contenedor_rojo.setBackgroundColor(Color.RED); // REMOVED solid red
        
        // Agregar Gauge al contenedor rojo
        LinearLayout.LayoutParams param_gauge = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (gaussimetro.getParent() != null) {
            ((ViewGroup)gaussimetro.getParent()).removeView(gaussimetro);
        }
        contenedor_rojo.addView(gaussimetro, param_gauge);

        // Agregar contenedor rojo a seccion izquierda
        LinearLayout.LayoutParams param_rojo = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        seccion_izquierda.addView(contenedor_rojo, param_rojo);

        // --- SECCIÓN 2: GRÁFICA (Centro) ---
        LinearLayout seccion_central = new LinearLayout(this);
        seccion_central.setOrientation(LinearLayout.VERTICAL);
        seccion_central.setGravity(Gravity.CENTER);
        // Usar borde NEGRO (reutilizamos el del gauge) para que sea el "recuadro negro"
        seccion_central.setBackgroundResource(R.drawable.rounded_border_black);
        // Importantísimo: Padding para que el contenido (grafica blanca) no tape el borde
        seccion_central.setPadding(10, 10, 10, 10); 
        
        LinearLayout.LayoutParams param_cen = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        param_cen.weight = 4.0f;
        param_cen.setMargins(5, 10, 5, 10);
        seccion_central.setLayoutParams(param_cen);
        
        // 2. Gráfica
        // Estilizar gráfico para que parezca osciloscopio limpio
        graficador.setBackgroundColor(Color.WHITE); // Fondo BLANCO
        graficador.setDrawGridBackground(false);
        graficador.setDescription(null); // Quitar descripción default
        
        LinearLayout.LayoutParams parametros_grafica = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (graficador.getParent() != null) {
            ((ViewGroup)graficador.getParent()).removeView(graficador);
        }
        seccion_central.addView(graficador, parametros_grafica);

        // --- SECCIÓN 3: CONTROLES (Derecha) ---
        LinearLayout seccion_derecha = new LinearLayout(this);
        seccion_derecha.setOrientation(LinearLayout.VERTICAL);
        seccion_derecha.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP);
        seccion_derecha.setBackgroundResource(R.drawable.rounded_border_chart);
        seccion_derecha.setPadding(0, 10, 0, 10);
        
        LinearLayout.LayoutParams param_der = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        param_der.weight = 1.0f;
        param_der.setMargins(5, 10, 10, 10);
        seccion_derecha.setLayoutParams(param_der);

        // Botones
        LinearLayout.LayoutParams param_btn = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        param_btn.setMargins(10, 10, 10, 10);

        bx.setText("Bx"); bx.setLayoutParams(param_btn);
        by.setText("By"); by.setLayoutParams(param_btn);
        bz.setText("Bz"); bz.setLayoutParams(param_btn);
        b.setText("B");   b.setLayoutParams(param_btn);

        if (bx.getParent() != null) ((ViewGroup)bx.getParent()).removeView(bx);
        if (by.getParent() != null) ((ViewGroup)by.getParent()).removeView(by);
        if (bz.getParent() != null) ((ViewGroup)bz.getParent()).removeView(bz);
        if (b.getParent() != null)  ((ViewGroup)b.getParent()).removeView(b);

        seccion_derecha.addView(bx);
        seccion_derecha.addView(by);
        seccion_derecha.addView(bz);
        seccion_derecha.addView(b);

        // Agregar secciones al layout principal
        linear_layout_principal.addView(seccion_izquierda);
        linear_layout_principal.addView(seccion_central);
        linear_layout_principal.addView(seccion_derecha);

        return root;
    }

    private void eventos() {

        bx.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                lanzarDatosBx();

            }
        });

        by.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                lanzarDatosBy();
            }
        });

        bz.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                lanzarDatosBz();
            }
        });

        b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                lanzarDatosB();
            }
        });

    }

    private void lanzarDatosBx() {

        resetear();
        gaussimetro.setComponenteCampo(1);
        gaussimetro.setRango(-100, 100);

        graficador.setTituloEjeY("Campo Magnético Bx (µT)");
        hilo.corriendo = true;

    }

    private void lanzarDatosBy() {

        resetear();
        gaussimetro.setComponenteCampo(2);
        gaussimetro.setRango(-100, 100);
        graficador.setTituloEjeY("Campo Magnético By (µT)");
        hilo.corriendo = true;

    }

    private void lanzarDatosBz() {

        resetear();
        gaussimetro.setComponenteCampo(3);
        gaussimetro.setRango(-100, 100);
        graficador.setTituloEjeY("Campo Magnético Bz (µT)");
        hilo.corriendo = true;

    }

    private void lanzarDatosB() {

        resetear();
        gaussimetro.setComponenteCampo(4);
        gaussimetro.setRango(0, 100);
        graficador.setTituloEjeY("Campo Magnético B (µT)");
        hilo.corriendo = true;

    }

    protected void onPause() {

        hilo.corriendo = false;
        AlmacenDatosRAM.datos.clear();
        hilo.contador = 0;
        super.onPause();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        hilo.corriendo = true;
    }

    private void resetear() {

        hilo.corriendo = false;
        AlmacenDatosRAM.datos.clear();
        hilo.tiempo = 0;
        hilo.contador = 0;

    }

}
