package com.curso_simulaciones.mivigesimaquintaapp.controlador;

import com.curso_simulaciones.mivigesimaquintaapp.modelo.AlmacenDatosRAM;
import com.curso_simulaciones.mivigesimaquintaapp.modelo.ModeloFisico;

/**
 * Hilo que controla la animación del polipasto.
 * Ejecuta los cálculos físicos periódicamente y actualiza la escena.
 */
public class HiloAnimacion extends Thread {

    public boolean pausa = true;
    private boolean corriendo = true;
    private long periodo_muestreo = 50; // 50ms = 20 FPS
    public float tiempo = 0f;

    private ModeloFisico modelo;
    private ActividadControladora actividad;

    public HiloAnimacion(ActividadControladora actividad) {
        this.actividad = actividad;
        this.modelo = new ModeloFisico();
    }

    @Override
    public void run() {
        while (corriendo) {
            try {
                Thread.sleep(periodo_muestreo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (!pausa) {
                tiempo += 0.02f; // Incremento de tiempo
                setCalculos(tiempo);
                actualizarFisica();
            }
        }
    }

    /**
     * Realiza los cálculos físicos para el tiempo actual.
     */
    private void setCalculos(float tiempo) {
        float m1 = AlmacenDatosRAM.m1;
        float m2 = AlmacenDatosRAM.m2;
        float m3 = AlmacenDatosRAM.m3;
        modelo.setCalculos(tiempo, m1, m2, m3);
    }

    /**
     * Actualiza la escena en la actividad y verifica límites físicos.
     * Resetea la animación si alguna masa alcanza un límite.
     */
    private void actualizarFisica() {
        actividad.cambiarEstadosEscenaPizarra();

        float radio = AlmacenDatosRAM.radio;

        // ================================================================
        // LÍMITES FÍSICOS
        // ================================================================

        // Límite inferior: el suelo (línea negra) - restar altura de masa para que no
        // la atraviese
        float ySuelo = AlmacenDatosRAM.ySuelo_en_pixeles - radio * 1.35f;

        // Límite superior para m1: la polea 1 + margen
        float yMinM1 = AlmacenDatosRAM.yPolea1_en_pixeles + radio * 1.5f;

        // Límite superior para m2/m3: la polea P actual + margen
        float yP_actual = AlmacenDatosRAM.yP_en_pixeles;
        float yMinM2M3 = yP_actual + radio * 1.5f;

        // Verificar si m1 tocó el suelo o llegó a la polea
        boolean m1TocoSuelo = AlmacenDatosRAM.y1_en_pixeles >= ySuelo;
        boolean m1TocoPolea = AlmacenDatosRAM.y1_en_pixeles <= yMinM1;

        // Verificar si m2 tocó el suelo o llegó a la polea P
        boolean m2TocoSuelo = AlmacenDatosRAM.y2_en_pixeles >= ySuelo;
        boolean m2TocoPolea = AlmacenDatosRAM.y2_en_pixeles <= yMinM2M3;

        // Verificar si m3 tocó el suelo o llegó a la polea P
        boolean m3TocoSuelo = AlmacenDatosRAM.y3_en_pixeles >= ySuelo;
        boolean m3TocoPolea = AlmacenDatosRAM.y3_en_pixeles <= yMinM2M3;

        // Si algún límite se alcanza, RESETEAR la animación
        if (m1TocoSuelo || m1TocoPolea || m2TocoSuelo || m2TocoPolea || m3TocoSuelo || m3TocoPolea) {
            tiempo = 0f;
        }
    }

    /**
     * Reinicia el tiempo de la animación.
     */
    public void reiniciar() {
        tiempo = 0f;
    }

    /**
     * Detiene el hilo completamente.
     */
    public void detener() {
        corriendo = false;
    }
}
