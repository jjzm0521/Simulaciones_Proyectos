package com.curso_simulaciones.minovenaapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.curso_simulaciones.objetos_laboratorio.Rueda;
import com.curso_simulaciones.vista.CR;
import com.curso_simulaciones.vista.Pizarra;

/**
 * Actividad Principal de MiNovenaApp
 * Gestiona 4 ruedas con diferentes comportamientos:
 * - Rueda 1: Esquina inferior derecha, solo rotación
 * - Rueda 2: Centro del canvas, solo rotación
 * - Rueda 3: Altura 0.25, traslación (sin rotar)
 * - Rueda 4: Altura 0.75, traslación y rotación
 */
public class ActividadPrincipalMiNovenaApp extends Activity implements Runnable {

    // Pizarra para dibujar
    private Pizarra pizarra;
    // arreglo que permite contener las 4 ruedas
    private Rueda[] ruedas = new Rueda[4];
    // período de muestreo en milisegundos
    private long periodo_muestreo = 50;
    // variable de tiempo para calcular posiciones
    private float tiempo;
    // hilo responsable de controlar la animación
    private Thread hilo;

    @Override
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
         * las ruedas con responsividad se crearán dentro
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
        pizarra.setBackgroundColor(Color.WHITE);
    }

    // organizar la distribución de los objetos de la GUI usando administradores de
    // diseño
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
        linearAbajo.setBackgroundColor(Color.rgb(50, 50, 50));

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
             * hacer la creación de las ruedas con
             * responsividad SÓLO cuando se garantice que la
             * GUI se conformó completamente con el fin de que
             * las dimensiones de la pizarra NO SEAN NULAS.
             */
            if (pizarra.getWidth() != 0 && ON == true) {
                crearRuedasConResponsividad();
                ON = false;
            }

            // ya creadas las ruedas con responsividad hacer efectiva la animación
            if (ON == false) {
                tiempo = tiempo + 0.05f;
                // cambio de estado de la escena física en la pizarra
                cambiarEstadosEscenaPizarra(tiempo);
            }

        }

    }

    /**
     * Crea las ruedas con responsividad
     * Se ejecuta cuando la pizarra ya tiene dimensiones válidas
     */
    private void crearRuedasConResponsividad() {

        CR.anchoPizarra = pizarra.getWidth();
        CR.altoPizarra = pizarra.getHeight();
        estadoInicialEscenaPizarra();

    }

    /**
     * Crea los 4 objetos Rueda con su estado inicial
     * Según los requisitos del TALLER
     */
    private void estadoInicialEscenaPizarra() {

        // centro de las ruedas
        float x_c = 0;
        float y_c = 0;

        /*
         * RUEDA 1: Esquina inferior derecha, solo rotación
         * Centro en (100%, 100%) con radio 10%
         * Color: ROJO
         */
        float radio_1 = CR.pcApxL(10);
        x_c = CR.pcApxX(100);
        y_c = CR.pcApxY(100);
        ruedas[0] = new Rueda(x_c, y_c, radio_1);
        ruedas[0].setColorRueda(Color.RED);

        /*
         * RUEDA 2: Centro de la pizarra, solo rotación
         * Centro en (50%, 50%) con radio 12%
         * Color: AZUL
         */
        float radio_2 = CR.pcApxL(12);
        x_c = CR.pcApxX(50);
        y_c = CR.pcApxY(50);
        ruedas[1] = new Rueda(x_c, y_c, radio_2);
        ruedas[1].setColorRueda(Color.BLUE);

        /*
         * RUEDA 3: Altura 0.25, traslación (sin rotar)
         * Centro inicial en (0%, 25%) con radio 8%
         * Color: VERDE
         */
        float radio_3 = CR.pcApxL(8);
        x_c = CR.pcApxX(0);
        y_c = CR.pcApxY(25);
        ruedas[2] = new Rueda(x_c, y_c, radio_3);
        ruedas[2].setColorRueda(Color.GREEN);

        /*
         * RUEDA 4: Altura 0.75, traslación y rotación
         * Centro inicial en (0%, 75%) con radio 9%
         * Color: MAGENTA
         */
        float radio_4 = CR.pcApxL(9);
        x_c = CR.pcApxX(0);
        y_c = CR.pcApxY(75);
        ruedas[3] = new Rueda(x_c, y_c, radio_4);
        ruedas[3].setColorRueda(Color.MAGENTA);

        // enviar las ruedas a la pizarra
        pizarra.setEstadoEscena(ruedas);

    }

    /**
     * Cambia el estado de los objetos rueda y lo comunica a pizarra
     * Implementa la física del movimiento según los requisitos
     *
     * @param tiempo tiempo transcurrido en segundos
     */
    private void cambiarEstadosEscenaPizarra(float tiempo) {

        /*
         * RUEDA 1: Solo rotación
         * Rota con velocidad angular W=50 rad/s
         * alrededor del eje que pasa por su centro
         */
        float teta_1 = 50f * tiempo; // ecuación MCU (Movimiento Circular Uniforme)

        /*
         * RUEDA 2: Solo rotación
         * Rota con velocidad angular W=100 rad/s
         * alrededor del eje que pasa por su centro
         */
        float teta_2 = 100f * tiempo; // ecuación MCU

        /*
         * RUEDA 3: Solo traslación (sin rotar)
         * Se desplaza de izquierda a derecha
         * con velocidad Vx = 5%, Vy = 0
         */
        float desplazamiento_3_x = CR.pcApxX(5 * tiempo);
        float desplazamiento_3_y = 0;

        /*
         * RUEDA 4: Traslación y rotación
         * Se desplaza con velocidad Vx = 5%, Vy = 0
         * y a la vez rota con W=200 rad/s alrededor del eje que
         * pasa por su centro
         */
        float teta_4 = 200f * tiempo; // ecuación MCU
        float desplazamiento_4_x = CR.pcApxX(5 * tiempo);
        float desplazamiento_4_y = 0;

        // mover las ruedas (aplicando polimorfismo)

        // mover rueda 1 (solo rotación)
        ruedas[0].moverRueda(teta_1);

        // mover rueda 2 (solo rotación)
        ruedas[1].moverRueda(teta_2);

        // mover rueda 3 (solo traslación)
        ruedas[2].moverRueda(desplazamiento_3_x, desplazamiento_3_y);

        // mover rueda 4 (traslación y rotación)
        ruedas[3].moverRueda(desplazamiento_4_x, desplazamiento_4_y, teta_4);

    }
}
