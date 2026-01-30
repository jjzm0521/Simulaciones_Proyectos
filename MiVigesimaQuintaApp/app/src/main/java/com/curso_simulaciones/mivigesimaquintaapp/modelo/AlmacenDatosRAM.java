package com.curso_simulaciones.mivigesimaquintaapp.modelo;

/**
 * Clase para almacenar datos compartidos entre clases.
 * Usa variables estáticas para facilitar el acceso.
 */
public class AlmacenDatosRAM {

    // Dimensiones de pantalla en píxeles
    public static float ancho_pantalla, alto_pantalla;
    public static int tamanoLetraResolucionIncluida;

    // Masas en kg
    public static float m1 = 15f;
    public static float m2 = 10f;
    public static float m3 = 8f;

    // Radio de las poleas en píxeles
    public static float radio;

    // Tiempo de simulación en segundos
    public static float tiempo = 0f;

    // Aceleraciones de las masas en m/s²
    public static float a1, a2, a3, aP;

    // Tensiones en las cuerdas en N
    public static float T1 = 0f;
    public static float T2 = 0f;

    // Desplazamiento angular de las poleas en grados
    public static float teta_1, teta_2, teta_P;

    // Origen del sistema de coordenadas en píxeles
    public static float origenY_en_pixeles;
    public static float origenY_en_metros = 0.1f;

    // Posiciones en píxeles
    public static float x1_en_pixeles, x2_en_pixeles, x3_en_pixeles, xP_en_pixeles;
    public static float y1_en_pixeles, y2_en_pixeles, y3_en_pixeles, yP_en_pixeles;

    // Posiciones iniciales en píxeles
    public static float yi1_en_pixeles, yi2_en_pixeles, yi3_en_pixeles, yiP_en_pixeles;

    // Posiciones en metros
    public static float yi1_en_metros = 0.5f, yi2_en_metros = 0.6f, yi3_en_metros = 0.4f;
    public static float y1_en_metros, y2_en_metros, y3_en_metros, yP_en_metros;

    // Desplazamientos en metros y píxeles
    public static float desplazamiento_m1_en_metros, desplazamiento_m2_en_metros, desplazamiento_m3_en_metros;
    public static float desplazamiento_m1_en_pixeles, desplazamiento_m2_en_pixeles, desplazamiento_m3_en_pixeles;
    public static float desplazamiento_P_en_pixeles;

    // ================================================================
    // LÍMITES FÍSICOS (para detener la animación)
    // ================================================================
    // Posición Y del suelo (línea negra) en píxeles
    public static float ySuelo_en_pixeles;

    // Posición Y de las poleas fijas en píxeles (techo para m1)
    public static float yPolea1_en_pixeles;

    // Posición Y inicial de la polea P (referencia para m2/m3)
    // m2 y m3 no pueden subir más allá de la posición de P
}
