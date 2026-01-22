package com.curso_simulaciones.midecimacuartaapp.vista;

/**
 * Clase CR (Coordenadas Relativas)
 * Proporciona métodos para convertir coordenadas relativas y porcentajes a
 * píxeles
 */
public class CR {
    // Dimensiones de la pantalla en píxeles (públicas para acceso directo)
    public static int anchoPizarra;
    public static int altoPizarra;

    /**
     * Convierte un porcentaje del ancho de la pizarra a píxeles
     * 
     * @param porcentajeAncho Porcentaje del ancho (0-100)
     * @return Valor en píxeles
     */
    public static float pcApxX(float porcentajeAncho) {
        return (porcentajeAncho / 100f) * anchoPizarra;
    }

    /**
     * Convierte un porcentaje del alto de la pizarra a píxeles
     * 
     * @param porcentajeAlto Porcentaje del alto (0-100)
     * @return Valor en píxeles
     */
    public static float pcApxY(float porcentajeAlto) {
        return (porcentajeAlto / 100f) * altoPizarra;
    }

    /**
     * Convierte una coordenada X relativa (0-1) a píxeles
     * 
     * @param xRelativo Coordenada X relativa (0-1)
     * @return Coordenada X en píxeles
     */
    public static float x(float xRelativo) {
        return xRelativo * anchoPizarra;
    }

    /**
     * Convierte una coordenada Y relativa (0-1) a píxeles
     * 
     * @param yRelativo Coordenada Y relativa (0-1)
     * @return Coordenada Y en píxeles
     */
    public static float y(float yRelativo) {
        return yRelativo * altoPizarra;
    }

    /**
     * Convierte un ancho relativo (0-1) a píxeles
     * 
     * @param anchoRelativo Ancho relativo (0-1)
     * @return Ancho en píxeles
     */
    public static float ancho(float anchoRelativo) {
        return anchoRelativo * anchoPizarra;
    }

    /**
     * Convierte un alto relativo (0-1) a píxeles
     * 
     * @param altoRelativo Alto relativo (0-1)
     * @return Alto en píxeles
     */
    public static float alto(float altoRelativo) {
        return altoRelativo * altoPizarra;
    }
}
