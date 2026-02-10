package com.curso_simulaciones.mivigesimanovenaapp.datos;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

/**
 * Clase AlmacenDatosRAM
 * 
 * Esta clase sirve como almacén temporal de datos en memoria RAM.
 * Permite compartir datos entre diferentes componentes de la aplicación
 * sin necesidad de pasar parámetros entre actividades.
 */
public class AlmacenDatosRAM {

    // Variable estática que almacena el dato actual del sensor
    public static float datoActual;

    /*
     * ArrayList es una clase que permite almacenar
     * objetos con la diferencia respecto a los
     * arreglos [], que ella misma va cambiando
     * dinámicamente su tamaño a medida que se le
     * agregan elementos.
     * 
     * En este caso almacena objetos Entry que representan
     * puntos (x, y) para graficar con MPAndroidChart.
     */
    public static ArrayList<Entry> datos = new ArrayList<>();

}
