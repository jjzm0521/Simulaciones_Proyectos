package com.curso_simulaciones.midecimacuartaapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.midecimacuartaapp.elementos_del_espacio.*;
import com.curso_simulaciones.midecimacuartaapp.vista.CR;
import com.curso_simulaciones.midecimacuartaapp.vista.Pizarra;

/**
 * ActividadPrincipalMiDecimaCuartaApp
 * Actividad principal con sistema de animación usando Thread
 * Basada en el código de ayuda del taller
 */
public class ActividadPrincipalMiDecimaCuartaApp extends Activity implements Runnable {

    // Pizarra para dibujar
    Pizarra pizarra;

    // objetos dibujables para Pizarra
    private Marciano marciano_1;
    private Selenita selenita_1;
    private Venusiano venusiano_1;
    private Estrella estrella_1, estrella_2, estrella_3, estrella_4, estrella_5;
    private ObjetoEspacial[] objetos_espaciales = new ObjetoEspacial[10];

    private float aumento_1 = 1;
    private float angulo_selenita = 0;
    private float desplazamiento_horizontal_selenita = 0;
    private float desplazamiento_horizontal_venusiano = 0;

    // período de muestreo en milisegundos
    private long periodo_muestreo = 50;
    private float tiempo;
    // hilo responsable de controlar la animación
    private Thread hilo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * llamada al método para crear los elementos de la
         * interfaz gráfica de usuario (GUI)
         */
        crearElementosGui();

        /*
         * para informar cómo se debe adaptar la GUI a la pantalla del dispositivo
         */
        ViewGroup.LayoutParams parametro_layout_principal = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        /*
         * pegar al contenedor la GUI:
         * en el argumento se está llamando al método crearGui()
         */
        this.setContentView(crearGui(), parametro_layout_principal);

        /*
         * los objetos con responsividad se crearán dentro
         * del hilo con el fin de garantizar que las dimensiones
         * de la pizarra donde se desplegarán con responsividad
         * tenga ya dimensiones no nulas
         */
        // hilo que administra la animación
        hilo = new Thread(this);
        hilo.start();
    }

    // crear los objetos de la interfaz gráfica de usuario (GUI)
    private void crearElementosGui() {
        // crear pizarra sabiendo de antemano sus dimensiones
        pizarra = new Pizarra(this);
        pizarra.setBackgroundColor(Color.BLACK);
    }

    // organizar la distribución de los objetos de de la GUI usando administradores
    // de diseño
    private LinearLayout crearGui() {
        // el linear principal
        LinearLayout linearPrincipal = new LinearLayout(this);
        linearPrincipal.setOrientation(LinearLayout.VERTICAL);
        linearPrincipal.setBackgroundColor(Color.BLACK);
        linearPrincipal.setWeightSum(10.0f);

        // linear secundario arriba
        LinearLayout linearArriba = new LinearLayout(this);

        // linear secundario abajo
        LinearLayout linearAbajo = new LinearLayout(this);
        linearAbajo.setBackgroundColor(Color.YELLOW);

        // pegar linearArriba al principal
        LinearLayout.LayoutParams parametrosPegadoArriba = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosPegadoArriba.weight = 8.5f;
        parametrosPegadoArriba.setMargins(50, 50, 50, 50);
        linearPrincipal.addView(linearArriba, parametrosPegadoArriba);

        // pegar linearAbajo al principal
        LinearLayout.LayoutParams parametrosPegadoAbajo = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0);
        parametrosPegadoAbajo.weight = 1.5f;
        linearPrincipal.addView(linearAbajo, parametrosPegadoAbajo);

        // pegar pizarra a linearArriba
        linearArriba.addView(pizarra);

        return linearPrincipal;
    }

    @Override
    public void run() {
        boolean ON = true;

        // hilo sin fin
        while (true) {
            try {
                Thread.sleep(periodo_muestreo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /*
             * hacer la creación de los objetos con
             * responsividad SÓLO cuando se garantice que la
             * GUI se conformó completamente con el fin de que
             * las dimensiones de la pizarra NO SEAN NULAS.
             */
            if (pizarra.getWidth() != 0 && ON == true) {
                crearObjetosConResponsividad();
                ON = false;
            }

            // ya creados los objetos con responsividad hacer efectiva la animación
            if (ON == false) {
                tiempo = tiempo + 0.05f;
                // cambio de estado de la escena física en la pizarra
                cambiarEstadosEscenaPizarra(tiempo);
            }
        }
    }

    private void crearObjetosConResponsividad() {
        CR.anchoPizarra = pizarra.getWidth();
        CR.altoPizarra = pizarra.getHeight();
        crearObjetosLaboratorio();
    }

    /*
     * Crea los objetos espaciales con su estado inicial
     * -X está en porcentaje del ancho del canvas
     * -Y está en porcentaje del alto del canvas
     * -Cualquier otra dimensión está en porcentaje del menor
     * entre el alto y el ancho del canvas
     */
    private void crearObjetosLaboratorio() {
        // Crear 5 estrellas fijas en diferentes posiciones, tamaños y colores
        estrella_1 = new Estrella(CR.pcApxX(15), CR.pcApxY(15), 20, Color.YELLOW);
        estrella_2 = new Estrella(CR.pcApxX(85), CR.pcApxY(20), 15, Color.WHITE);
        estrella_3 = new Estrella(CR.pcApxX(25), CR.pcApxY(80), 25, Color.rgb(255, 200, 100));
        estrella_4 = new Estrella(CR.pcApxX(70), CR.pcApxY(70), 18, Color.rgb(200, 200, 255));
        estrella_5 = new Estrella(CR.pcApxX(50), CR.pcApxY(10), 22, Color.rgb(255, 255, 200));

        // agregar estrellas al arreglo de objetos espaciales
        objetos_espaciales[0] = estrella_1;
        objetos_espaciales[1] = estrella_2;
        objetos_espaciales[2] = estrella_3;
        objetos_espaciales[3] = estrella_4;
        objetos_espaciales[4] = estrella_5;

        // Marciano con movimiento parabólico
        float x_1 = CR.pcApxX(10);
        float y_1 = CR.pcApxY(75);
        marciano_1 = new Marciano(x_1, y_1);
        // agregarlo al arreglo de objetos espaciales
        objetos_espaciales[5] = marciano_1;

        // Selenita con traslación horizontal y rotación
        float x_2 = CR.pcApxX(10);
        float y_2 = CR.pcApxY(40);
        selenita_1 = new Selenita(x_2, y_2);
        selenita_1.setColor(Color.CYAN);
        objetos_espaciales[6] = selenita_1;

        // Venusiano con MAS vertical y traslación horizontal
        float x_3 = CR.pcApxX(10);
        float y_3 = CR.pcApxY(60);
        venusiano_1 = new Venusiano(x_3, y_3);
        venusiano_1.setColor(Color.GREEN);
        objetos_espaciales[7] = venusiano_1;

        // desplegar la escena inicial
        pizarra.setEstadoEscena(objetos_espaciales);
    }

    /*
     * Cambia el estado de movimiento de los objetos espaciales
     * -X está en porcentaje del ancho del canvas
     * -Y está en porcentaje del alto del canvas
     * -Cualquier otra dimensión está en porcentaje del menor
     * entre el alto y el ancho del canvas
     */
    private void cambiarEstadosEscenaPizarra(float tiempo) {
        // Modelo del marciano_1
        // Movimiento parabólico
        float desplazamiento_x_1 = CR.pcApxX(10 * tiempo); // MU
        float desplazamiento_y_1 = CR.pcApxY(-35 * tiempo + 4.9f * tiempo * tiempo); // caída libre
        // mover al marciano
        marciano_1.mover(desplazamiento_x_1, desplazamiento_y_1);
        // aumentar tamaño de marciano_1
        aumento_1 = aumento_1 + 0.01f;
        marciano_1.setMagnificar(aumento_1);
        // cambiar color de marciano_1 en el instante que su tamaño
        // esté en 40% mayor que el inicial
        if (aumento_1 > 1.4f) {
            marciano_1.setColor(Color.GREEN);
        }

        // Modelo del selenita_1
        // Traslación horizontal con velocidad constante
        desplazamiento_horizontal_selenita += CR.pcApxX(0.5f);
        // Rotación con velocidad angular constante
        angulo_selenita += 2f; // grados por frame
        selenita_1.mover(desplazamiento_horizontal_selenita, 0, angulo_selenita);

        // Modelo del venusiano_1
        // Traslación horizontal con velocidad constante
        desplazamiento_horizontal_venusiano += CR.pcApxX(0.3f);
        // Oscilación vertical con MAS (Movimiento Armónico Simple)
        float amplitud = CR.pcApxY(10); // Amplitud de 10% del alto
        float frecuencia_angular = 2f; // rad/s
        float desplazamiento_y_venusiano = amplitud * (float) Math.sin(frecuencia_angular * tiempo);
        venusiano_1.mover(desplazamiento_horizontal_venusiano, desplazamiento_y_venusiano);

        // Actualizar la escena
        pizarra.setEstadoEscena(objetos_espaciales);
    }
}
