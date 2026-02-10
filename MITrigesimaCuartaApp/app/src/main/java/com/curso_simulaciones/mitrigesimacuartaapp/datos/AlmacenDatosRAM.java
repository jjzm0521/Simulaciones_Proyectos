package com.curso_simulaciones.mitrigesimacuartaapp.datos;

/**
 * Clase para almacenar datos compartidos entre actividades.
 * Almacena configuración de resolución, datos del acelerómetro y parámetros de
 * muestreo.
 */
public class AlmacenDatosRAM {

    // Variables de resolución de pantalla
    public static int dimensioReferencia;
    public static int alto;
    public static int ancho;
    public static int tamanoLetraResolucionIncluida;

    // Variable para control de configuración
    public static boolean configurar;

    // Parámetros de muestreo
    public static int periodoMuestreo = 500; // en ms
    public static float tiempo;
    public static int nDatos = 50;

    // Ruta para guardar archivos
    public static String path;

    // Datos del acelerómetro
    public static float ax; // Aceleración en X (m/s²)
    public static float ay; // Aceleración en Y (m/s²)
    public static float az; // Aceleración en Z (m/s²)
    public static float a; // Magnitud total: sqrt(ax² + ay² + az²)

}
