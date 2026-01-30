package com.curso_simulaciones.mivigesimaquintaapp.controlador;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.curso_simulaciones.mivigesimaquintaapp.modelo.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.vista.CR;
import com.curso_simulaciones.mivigesimaquintaapp.vista.Pizarra;
import com.curso_simulaciones.simulphysics.objetos_laboratorio.*;

/**
 * Actividad controladora que maneja la interfaz y la simulación.
 * Layout: 80% Pizarra (izquierda) + 20% Controles (derecha)
 * Usa OnLayoutChangeListener para responsividad correcta.
 */
public class ActividadControladora extends Activity {

    private int tamanoLetraResolucionIncluida;

    private Pizarra pizarra;
    private TextView text_m1, text_m2, text_m3;
    private SeekBar seek_bar_m1, seek_bar_m2, seek_bar_m3;
    private Button boton_empezar, boton_pausar;

    // Valores de las masas
    private float m1 = 15f;
    private float m2 = 10f;
    private float m3 = 8f;

    // Posiciones iniciales
    private float x1_pixeles, x2_pixeles, x3_pixeles, xP_pixeles;
    private float y1_pixeles, y2_pixeles, y3_pixeles, yP_pixeles;
    private float xp1_pixeles, xp2_pixeles, yp1_pixeles, yp2_pixeles;
    private float radio;
    private float ancho_bloque, alto_bloque;

    // Objetos de laboratorio
    private CuerpoRectangular barra;
    private Polea polea_1, polea_2, polea_P;
    private Masa masa_1, masa_2, masa_3;

    private ObjetoLaboratorio[] objetos = new ObjetoLaboratorio[8];

    private HiloAnimacion hilo;

    // Flag para evitar múltiples inicializaciones
    private boolean objetosCreados = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestionarResolucion();
        crearElementosGUI();

        ViewGroup.LayoutParams parametros = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        this.setContentView(crearGUI(), parametros);

        // Listener para responsividad: espera a que Android dibuje la ventana
        // y usa las dimensiones reales exactas
        pizarra.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                    int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Calcular dimensiones reales de la pizarra
                int nuevoAncho = right - left;
                int nuevoAlto = bottom - top;

                // Solo actualizar si hay dimensiones válidas y hubo cambio
                if (nuevoAncho > 0 && nuevoAlto > 0 &&
                        (nuevoAncho != (oldRight - oldLeft) || nuevoAlto != (oldBottom - oldTop) || !objetosCreados)) {

                    // Actualizar dimensiones en CR y AlmacenDatosRAM
                    CR.anchoPizarra = nuevoAncho;
                    CR.altoPizarra = nuevoAlto;
                    AlmacenDatosRAM.ancho_pantalla = nuevoAncho;
                    AlmacenDatosRAM.alto_pantalla = nuevoAlto;

                    // Recrear objetos con las nuevas dimensiones
                    crearObjetosLaboratorio();
                    objetosCreados = true;

                    // Redibujar
                    pizarra.invalidate();
                }
            }
        });

        hilo = new HiloAnimacion(this);
        hilo.start();

        eventos();
    }

    /**
     * Configura el tamaño de letra inicial.
     * Las dimensiones de la pizarra se configuran en el OnLayoutChangeListener
     * para garantizar responsividad correcta.
     */
    private void gestionarResolucion() {
        tamanoLetraResolucionIncluida = (int) (0.8f * AlmacenDatosRAM.tamanoLetraResolucionIncluida);
        if (tamanoLetraResolucionIncluida < 10)
            tamanoLetraResolucionIncluida = 12;

        // Valores iniciales temporales (serán actualizados por el listener)
        // Usar estimaciones para evitar división por cero antes del layout
        CR.anchoPizarra = AlmacenDatosRAM.alto_pantalla > 0 ? 0.80f * AlmacenDatosRAM.alto_pantalla : 800;
        CR.altoPizarra = AlmacenDatosRAM.ancho_pantalla > 0 ? AlmacenDatosRAM.ancho_pantalla : 600;
    }

    /**
     * Crea los elementos de la interfaz gráfica.
     */
    private void crearElementosGUI() {
        // Labels para las masas
        text_m1 = new TextView(this);
        text_m1.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m1.setGravity(Gravity.CENTER);
        text_m1.setTextColor(Color.WHITE);
        text_m1.setText("M1\n5-25 kg");

        text_m2 = new TextView(this);
        text_m2.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m2.setGravity(Gravity.CENTER);
        text_m2.setTextColor(Color.WHITE);
        text_m2.setText("M2\n5-25 kg");

        text_m3 = new TextView(this);
        text_m3.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        text_m3.setGravity(Gravity.CENTER);
        text_m3.setTextColor(Color.WHITE);
        text_m3.setText("M3\n5-25 kg");

        // SeekBars para las masas
        seek_bar_m1 = new SeekBar(this);
        seek_bar_m1.setMax(20);
        seek_bar_m1.setProgress(10); // m1 = 15kg (5 + 10)
        AlmacenDatosRAM.m1 = m1;

        seek_bar_m2 = new SeekBar(this);
        seek_bar_m2.setMax(20);
        seek_bar_m2.setProgress(5); // m2 = 10kg (5 + 5)
        AlmacenDatosRAM.m2 = m2;

        seek_bar_m3 = new SeekBar(this);
        seek_bar_m3.setMax(20);
        seek_bar_m3.setProgress(3); // m3 = 8kg (5 + 3)
        AlmacenDatosRAM.m3 = m3;

        // Botones
        boton_empezar = new Button(this);
        boton_empezar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        boton_empezar.setText("EMPEZAR");
        boton_empezar.getBackground().setColorFilter(Color.rgb(100, 180, 100), PorterDuff.Mode.MULTIPLY);

        boton_pausar = new Button(this);
        boton_pausar.setTextSize(TypedValue.COMPLEX_UNIT_SP, tamanoLetraResolucionIncluida);
        boton_pausar.setText("PAUSAR");
        boton_pausar.getBackground().setColorFilter(Color.rgb(180, 140, 80), PorterDuff.Mode.MULTIPLY);
        boton_pausar.setEnabled(false);

        // Pizarra
        pizarra = new Pizarra(this);
        pizarra.setBackgroundColor(Color.rgb(250, 250, 250));
        // El sistema de coordenadas y objetos se configuran en el
        // OnLayoutChangeListener
        // para usar las dimensiones reales de la pizarra
    }

    /**
     * Crea el layout principal: 80% pizarra + 20% controles.
     */
    private LinearLayout crearGUI() {
        LinearLayout linear_principal = new LinearLayout(this);
        linear_principal.setOrientation(LinearLayout.HORIZONTAL);
        linear_principal.setWeightSum(10.0f);

        // Panel izquierdo (Pizarra) - 80%
        LinearLayout linear_izquierda = new LinearLayout(this);
        linear_izquierda.setOrientation(LinearLayout.VERTICAL);

        // Panel derecho (Controles) - 20%
        LinearLayout linear_derecha = new LinearLayout(this);
        linear_derecha.setOrientation(LinearLayout.VERTICAL);
        linear_derecha.setBackgroundColor(Color.rgb(60, 140, 80)); // Verde elegante
        linear_derecha.setWeightSum(9.0f);
        linear_derecha.setPadding(15, 20, 15, 20);

        // Agregar paneles al principal
        LinearLayout.LayoutParams params_izq = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params_izq.weight = 8.0f;
        linear_principal.addView(linear_izquierda, params_izq);

        LinearLayout.LayoutParams params_der = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params_der.weight = 2.0f;
        linear_principal.addView(linear_derecha, params_der);

        // Agregar controles al panel derecho
        LinearLayout.LayoutParams params_componente = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params_componente.weight = 1.0f;
        params_componente.setMargins(5, 5, 5, 5);

        linear_derecha.addView(text_m1, params_componente);
        linear_derecha.addView(seek_bar_m1, params_componente);
        linear_derecha.addView(text_m2, params_componente);
        linear_derecha.addView(seek_bar_m2, params_componente);
        linear_derecha.addView(text_m3, params_componente);
        linear_derecha.addView(seek_bar_m3, params_componente);
        linear_derecha.addView(boton_empezar, params_componente);
        linear_derecha.addView(boton_pausar, params_componente);

        // Agregar pizarra al panel izquierdo
        linear_izquierda.addView(pizarra);

        return linear_principal;
    }

    /**
     * Configura los eventos de los controles.
     */
    private void eventos() {
        // Botón EMPEZAR/NUEVO
        boton_empezar.setOnClickListener(v -> {
            if (boton_empezar.getText().toString().equals("EMPEZAR")) {
                hilo.pausa = false;
                seek_bar_m1.setEnabled(false);
                seek_bar_m2.setEnabled(false);
                seek_bar_m3.setEnabled(false);
                boton_empezar.setText("NUEVO");
                boton_pausar.setEnabled(true);
                boton_pausar.setText("PAUSAR");
            } else {
                hilo.pausa = true;
                hilo.reiniciar();
                seek_bar_m1.setEnabled(true);
                seek_bar_m2.setEnabled(true);
                seek_bar_m3.setEnabled(true);
                boton_empezar.setText("EMPEZAR");
                boton_pausar.setEnabled(false);
                reiniciarPosiciones();
            }
        });

        // Botón PAUSAR/CONTINUAR
        boton_pausar.setOnClickListener(v -> {
            if (boton_pausar.getText().toString().equals("PAUSAR")) {
                hilo.pausa = true;
                boton_pausar.setText("CONTINUAR");
            } else {
                hilo.pausa = false;
                boton_pausar.setText("PAUSAR");
            }
        });

        // SeekBar m1
        seek_bar_m1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m1 = 5 + progress;
                AlmacenDatosRAM.m1 = m1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // SeekBar m2
        seek_bar_m2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m2 = 5 + progress;
                AlmacenDatosRAM.m2 = m2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // SeekBar m3
        seek_bar_m3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m3 = 5 + progress;
                AlmacenDatosRAM.m3 = m3;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * Crea los objetos de laboratorio con sus posiciones iniciales.
     * SISTEMA RESPONSIVO: Usa porcentajes centrados (50%) y layout vertical
     * (20%-88% para que la animación dure más)
     */
    private void crearObjetosLaboratorio() {
        // Escala del radio basada en el menor entre ancho y alto
        radio = CR.pcApxL(2.5f);
        AlmacenDatosRAM.radio = radio;

        // Dimensiones de las masas (proporcionales al radio)
        ancho_bloque = radio * 1.3f;
        alto_bloque = radio * 2.0f;

        // Layout vertical basado en porcentajes (20% techo - 88% suelo)
        // El suelo está a 88% para que la animación dure más
        float yTopeBarra = CR.pcApxY(20f);
        float ySuelo = CR.pcApxY(88f);
        float anchoBarra = CR.pcApxL(45f);
        float desplazamiento = radio * 1.1f;

        // Guardar posición del suelo para límites
        AlmacenDatosRAM.ySuelo_en_pixeles = ySuelo;

        // Centro horizontal de la escena
        float centroX = CR.pcApxX(50f);

        // 0. Barra amarilla centrada
        barra = new CuerpoRectangular(centroX, (yTopeBarra + ySuelo) / 2f, anchoBarra, ySuelo - yTopeBarra);
        barra.setColor(Color.rgb(255, 220, 80)); // Amarillo elegante
        objetos[0] = barra;

        // Esquinas de la barra
        float xEsquinaIzq = centroX - anchoBarra / 2f;
        float xEsquinaDer = centroX + anchoBarra / 2f;

        // Posiciones de las poleas fijas (azules) - en las esquinas superiores
        xp1_pixeles = xEsquinaIzq - desplazamiento;
        yp1_pixeles = yTopeBarra - desplazamiento;
        xp2_pixeles = xEsquinaDer + desplazamiento;
        yp2_pixeles = yTopeBarra - desplazamiento;

        // 1. Polea 1 (azul, fija, izquierda)
        polea_1 = new Polea(xp1_pixeles, yp1_pixeles, radio);
        polea_1.setColor(Color.rgb(70, 130, 200)); // Azul elegante
        polea_1.setGrosorLinea(CR.pcApxL(0.4f));
        polea_1.setSoportePolea(true);
        polea_1.rotarEje(-45);
        objetos[1] = polea_1;

        // Guardar posición de polea 1 para límites (techo de m1)
        AlmacenDatosRAM.yPolea1_en_pixeles = yp1_pixeles;

        // 2. Polea 2 (azul, fija, derecha)
        polea_2 = new Polea(xp2_pixeles, yp2_pixeles, radio);
        polea_2.setColor(Color.rgb(70, 130, 200));
        polea_2.setGrosorLinea(CR.pcApxL(0.4f));
        polea_2.setSoportePolea(true);
        polea_2.rotarEje(45);
        objetos[2] = polea_2;

        // 3. Polea P (verde, móvil) - a la derecha de polea 2
        xP_pixeles = xp2_pixeles + radio;
        yP_pixeles = CR.pcApxY(40f);
        AlmacenDatosRAM.yiP_en_pixeles = yP_pixeles;
        AlmacenDatosRAM.xP_en_pixeles = xP_pixeles;

        polea_P = new Polea(xP_pixeles, yP_pixeles, radio);
        polea_P.setColor(Color.rgb(80, 180, 100)); // Verde elegante
        polea_P.setGrosorLinea(CR.pcApxL(0.4f));
        objetos[3] = polea_P;

        // Posiciones de las masas
        // Masa m1: cuelga de polea 1 (izquierda)
        x1_pixeles = xp1_pixeles - radio;
        y1_pixeles = CR.pcApxY(60f);
        AlmacenDatosRAM.x1_en_pixeles = x1_pixeles;
        AlmacenDatosRAM.yi1_en_pixeles = y1_pixeles;

        // Masa m2: cuelga del lado izquierdo de polea P
        x2_pixeles = xP_pixeles - radio;
        y2_pixeles = CR.pcApxY(67f);
        AlmacenDatosRAM.x2_en_pixeles = x2_pixeles;
        AlmacenDatosRAM.yi2_en_pixeles = y2_pixeles;

        // Masa m3: cuelga del lado derecho de polea P
        x3_pixeles = xP_pixeles + radio;
        y3_pixeles = CR.pcApxY(53f);
        AlmacenDatosRAM.x3_en_pixeles = x3_pixeles;
        AlmacenDatosRAM.yi3_en_pixeles = y3_pixeles;

        // 4. Masa m1 (verde oscuro)
        masa_1 = new Masa(x1_pixeles, y1_pixeles, ancho_bloque, alto_bloque);
        masa_1.setColor(Color.rgb(80, 160, 90));
        masa_1.setColorMarca(Color.WHITE);
        objetos[4] = masa_1;

        // 5. Masa m2 (verde oscuro)
        masa_2 = new Masa(x2_pixeles, y2_pixeles, ancho_bloque * 0.9f, alto_bloque * 0.9f);
        masa_2.setColor(Color.rgb(80, 160, 90));
        masa_2.setColorMarca(Color.WHITE);
        objetos[5] = masa_2;

        // 6. Masa m3 (verde oscuro)
        masa_3 = new Masa(x3_pixeles, y3_pixeles, ancho_bloque * 0.9f, alto_bloque * 0.9f);
        masa_3.setColor(Color.rgb(80, 160, 90));
        masa_3.setColorMarca(Color.WHITE);
        objetos[6] = masa_3;

        // 7. Barra negra del suelo
        CuerpoRectangular suelo = new CuerpoRectangular(centroX, ySuelo, CR.pcApxL(80f), CR.pcApxL(1.5f));
        suelo.setColor(Color.rgb(50, 50, 50));
        objetos[7] = suelo;

        pizarra.setEstadoEscena(objetos);
    }

    /**
     * Reinicia las posiciones de los objetos al estado inicial.
     * Recrea los objetos de laboratorio para volver al estado inicial.
     */
    private void reiniciarPosiciones() {
        // Reiniciar valores en AlmacenDatosRAM
        AlmacenDatosRAM.y1_en_pixeles = y1_pixeles;
        AlmacenDatosRAM.y2_en_pixeles = y2_pixeles;
        AlmacenDatosRAM.y3_en_pixeles = y3_pixeles;
        AlmacenDatosRAM.yP_en_pixeles = yP_pixeles;
        AlmacenDatosRAM.yi1_en_pixeles = y1_pixeles;
        AlmacenDatosRAM.yi2_en_pixeles = y2_pixeles;
        AlmacenDatosRAM.yi3_en_pixeles = y3_pixeles;
        AlmacenDatosRAM.yiP_en_pixeles = yP_pixeles;
        AlmacenDatosRAM.desplazamiento_m1_en_pixeles = 0;
        AlmacenDatosRAM.desplazamiento_m2_en_pixeles = 0;
        AlmacenDatosRAM.desplazamiento_m3_en_pixeles = 0;
        AlmacenDatosRAM.desplazamiento_P_en_pixeles = 0;
        AlmacenDatosRAM.tiempo = 0;

        // Recrear objetos de laboratorio
        crearObjetosLaboratorio();
    }

    /**
     * Actualiza las posiciones de los objetos según los cálculos físicos.
     * Llamado por el HiloAnimacion.
     */
    public void cambiarEstadosEscenaPizarra() {
        // Obtener desplazamientos calculados
        float desp_m1 = AlmacenDatosRAM.desplazamiento_m1_en_pixeles;
        float desp_P = AlmacenDatosRAM.desplazamiento_P_en_pixeles;
        float desp_m2 = AlmacenDatosRAM.desplazamiento_m2_en_pixeles;
        float desp_m3 = AlmacenDatosRAM.desplazamiento_m3_en_pixeles;

        // Mover masa 1
        if (masa_1 != null) {
            masa_1.mover(0, desp_m1);
        }

        // Mover polea P
        if (polea_P != null) {
            polea_P.mover(0, desp_P, AlmacenDatosRAM.teta_P);
        }

        // Mover masas 2 y 3
        if (masa_2 != null) {
            masa_2.mover(0, desp_m2);
        }
        if (masa_3 != null) {
            masa_3.mover(0, desp_m3);
        }

        // Rotar poleas fijas
        if (polea_1 != null) {
            polea_1.mover(AlmacenDatosRAM.teta_1);
        }
        if (polea_2 != null) {
            polea_2.mover(AlmacenDatosRAM.teta_2);
        }
    }

    @Override
    protected void onDestroy() {
        if (hilo != null) {
            hilo.detener();
        }
        super.onDestroy();
    }
}
