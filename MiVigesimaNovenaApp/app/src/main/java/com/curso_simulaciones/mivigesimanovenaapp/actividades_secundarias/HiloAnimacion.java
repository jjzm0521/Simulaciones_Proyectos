package com.curso_simulaciones.mivigesimanovenaapp.actividades_secundarias;

import com.curso_simulaciones.mivigesimanovenaapp.datos.AlmacenDatosRAM;
import com.github.mikephil.charting.data.Entry;

/**
 * Clase HiloAnimacion
 * 
 * Esta clase extiende Thread para ejecutar un hilo secundario
 * que muestrea datos del sensor a intervalos regulares y
 * los agrega a la gráfica.
 * 
 * Características:
 * - Periodo de muestreo configurable (500ms por defecto)
 * - Almacena datos en AlmacenDatosRAM
 * - Actualiza la gráfica en el hilo de UI usando runOnUiThread
 * - Se detiene automáticamente después de 21 muestras
 * 
 * Nota: Esta clase es una alternativa al muestreo directo
 * en onSensorChanged y puede usarse para control más preciso
 * del timing de muestreo.
 */
public class HiloAnimacion extends Thread {

    // Bandera para controlar el ciclo del hilo
    public boolean corriendo;

    // Periodo de muestreo en milisegundos
    private long periodo_muestreo = 500;

    // Tiempo acumulado en segundos (eje X de la gráfica)
    public float tiempo = 0;

    // Contador de muestras tomadas
    public int contador = 0;

    // Referencia a la actividad para acceder a la gráfica
    private ActividadDesplegadoraDatos actividad;

    /**
     * Constructor del hilo
     * 
     * @param actividad Referencia a la actividad que contiene el graficador
     */
    public HiloAnimacion(ActividadDesplegadoraDatos actividad) {
        this.actividad = actividad;
    }

    /**
     * Método run - se ejecuta cuando se inicia el hilo
     * 
     * Ciclo principal:
     * 1. Esperar periodo_muestreo milisegundos
     * 2. Crear nueva entrada con el dato actual
     * 3. Almacenar en RAM
     * 4. Actualizar gráfica (en hilo UI)
     * 5. Incrementar tiempo y contador
     * 6. Repetir hasta contador > 20
     */
    @Override
    public void run() {
        corriendo = true;

        while (corriendo) {
            try {
                // Esperar el periodo de muestreo
                Thread.sleep(periodo_muestreo);

                // Crear nueva entrada con tiempo y dato actual
                Entry nuevaEntrada = new Entry(tiempo, AlmacenDatosRAM.datoActual);

                // Almacenar en memoria RAM
                AlmacenDatosRAM.datos.add(nuevaEntrada);

                /*
                 * Actualizar gráfica en el hilo de UI
                 * 
                 * Las actualizaciones de Views deben hacerse
                 * en el hilo principal de UI, por eso usamos
                 * runOnUiThread()
                 */
                actividad.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        actividad.graficador.agregarDato(tiempo, AlmacenDatosRAM.datoActual);
                    }
                });

                // Incrementar tiempo (convertir ms a segundos)
                tiempo = tiempo + 0.001f * periodo_muestreo;

                // Limitar a 21 muestras (pueden ser más si se desea)
                if (contador > 20) {
                    corriendo = false;
                }

                // Incrementar contador de muestras
                contador = contador + 1;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
