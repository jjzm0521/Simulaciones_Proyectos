package com.curso_simulaciones.micuadrigesimaquintaapp.datos;

import java.util.ArrayList;

public class AlmacenDatosRAM {

    public static int ancho, alto, dimensionReferencia, tamanoLetraResolucionIncluida;

    public static int nDatos = 50;

    public static int nDatosGraficar = 20;

    public static int estado_conexion_nube = 1;

    public static String MQTTHOST = "tcp://168.176.136.61:1883"; // Default broker
    public static String USERNAME = "usuario";
    public static String PASSWORD = "password";
    public static String topicStr = "iot/simulaciones/equipo_0"; // Default topic

    public static String conectado_PubSub = "Hacer clic en CONECTAR para acceder al BROKER...";

    public static boolean conectado = false;

    public static String unidades = "cm"; // Unidad principal (distancia)

    public static String tiempo;

    public static ArrayList<Float> datosDistancia = new ArrayList<>();
    // Si necesitamos graficar otras cosas, podemos añadir más listas
}
