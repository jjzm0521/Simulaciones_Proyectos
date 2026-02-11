package com.curso_simulaciones.micuadragesimasegundaapp.datos;

public class AlmacenDatosRAM {

    public static int ancho, alto, dimensionReferencia, tamanoLetraResolucionIncluida;

    public static String MQTTHOST = "tcp://broker.emqx.io:1883"; // Default
    public static String USERNAME = "";
    public static String PASSWORD = "";
    public static String topicStr = "curso/iot";

    public static String conectado_PubSub = " ";

    public static boolean conectado = false;

}
