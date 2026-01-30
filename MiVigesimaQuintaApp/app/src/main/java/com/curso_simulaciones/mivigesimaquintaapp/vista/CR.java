package com.curso_simulaciones.mivigesimaquintaapp.vista;

/**
 * Clase de Conversión Responsiva.
 * Convierte porcentajes a píxeles para dibujar de forma responsiva.
 */
public class CR {

    public static float anchoPizarra;
    public static float altoPizarra;

    public CR() {
    }

    /**
     * Convierte porcentaje de posición en X a píxeles.
     * 
     * @param pcX Porcentaje (0-100)
     * @return Posición en píxeles
     */
    public static float pcApxX(float pcX) {
        return pcX * anchoPizarra / 100f;
    }

    /**
     * Convierte porcentaje de posición en Y a píxeles.
     * 
     * @param pcY Porcentaje (0-100)
     * @return Posición en píxeles
     */
    public static float pcApxY(float pcY) {
        return pcY * altoPizarra / 100f;
    }

    /**
     * Convierte porcentaje de longitud a píxeles.
     * Usa el menor entre ancho y alto como referencia.
     * 
     * @param pcL Porcentaje de longitud
     * @return Longitud en píxeles
     */
    public static float pcApxL(float pcL) {
        if (anchoPizarra > altoPizarra) {
            return pcL * altoPizarra / 100f;
        } else {
            return pcL * anchoPizarra / 100f;
        }
    }

    /**
     * Convierte píxeles de posición X a porcentaje.
     */
    public static float pxXApc(float pxX) {
        return pxX * 100f / anchoPizarra;
    }

    /**
     * Convierte píxeles de posición Y a porcentaje.
     */
    public static float pxYApc(float pxY) {
        return pxY * 100f / altoPizarra;
    }

    /**
     * Convierte píxeles de longitud a porcentaje.
     * Usa el menor entre ancho y alto como referencia.
     */
    public static float pxApcL(float pxL) {
        if (anchoPizarra > altoPizarra) {
            return pxL * 100f / altoPizarra;
        } else {
            return pxL * 100f / anchoPizarra;
        }
    }
}
