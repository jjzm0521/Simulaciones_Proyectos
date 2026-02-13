package com.curso_simulaciones.micuadrigesimaquintaapp.comunicaciones;

import android.app.Activity;
import android.util.Log;

import com.curso_simulaciones.micuadrigesimaquintaapp.datos.AlmacenDatosRAM;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class ClientePubSubMQTT implements MqttCallback, IMqttActionListener {

    private Activity actividad;

    private static String MQTTHOST;
    private static String USERNAME;
    private static String PASSWORD;
    private String topicStr;

    private MqttAndroidClient client;
    private MqttConnectOptions options;
    private IMqttToken token;
    private JSONObject obj;
    private String dato;
    private String datoString;

    private static final String TAG = "ClientePubSubMQTT";

    public ClientePubSubMQTT(Activity actividad) {

        this.actividad = actividad;

        MQTTHOST = AlmacenDatosRAM.MQTTHOST;
        USERNAME = AlmacenDatosRAM.USERNAME;
        PASSWORD = AlmacenDatosRAM.PASSWORD;
        topicStr = AlmacenDatosRAM.topicStr;

    }

    /*
     * 1. Establecer conexión
     */

    public void conectar() {

        // Asegurar que se usen los datos más recientes de AlmacenDatosRAM
        MQTTHOST = AlmacenDatosRAM.MQTTHOST;
        USERNAME = AlmacenDatosRAM.USERNAME;
        PASSWORD = AlmacenDatosRAM.PASSWORD;
        topicStr = AlmacenDatosRAM.topicStr;

        if (MQTTHOST == null || MQTTHOST.isEmpty()) {
            AlmacenDatosRAM.conectado_PubSub = "Error: Host no configurado";
            return;
        }

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(actividad.getApplicationContext(), MQTTHOST, clientId, Ack.AUTO_ACK);
        client.setCallback(this);
        options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        if (PASSWORD != null) {
            options.setPassword(PASSWORD.toCharArray());
        }
        options.setCleanSession(true);

        // hacer conexión
        client.connect(options, actividad.getApplicationContext(), this);
        AlmacenDatosRAM.conectado_PubSub = "Conectando con el broker...";
    }

    // método automático
    // suscripción del tópico
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        if (client != null && topicStr != null && !topicStr.isEmpty()) {
            client.subscribe(topicStr, 0);
            AlmacenDatosRAM.conectado_PubSub = "Se hizo la suscripción al tópico...";
            AlmacenDatosRAM.conectado = true;
        } else {
            AlmacenDatosRAM.conectado_PubSub = "Error: Tópico no configurado";
        }
    }

    // método automático
    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        Log.d(TAG, "falla conexión");
        AlmacenDatosRAM.conectado_PubSub = "Falla conexión con el broker...";
        AlmacenDatosRAM.conectado = false;
    }

    // método automático
    @Override
    public void connectionLost(Throwable throwable) {
        AlmacenDatosRAM.conectado_PubSub = "Conexión pérdida...";
        AlmacenDatosRAM.conectado = false;
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        if (AlmacenDatosRAM.conectado && mqttMessage != null) {
            datoString = new String(mqttMessage.getPayload());
            AlmacenDatosRAM.conectado_PubSub = "Recibiendo datos...";

            try {
                JSONObject jsonObject = new JSONObject(datoString);
                
                // Extraer datos del JSON y guardarlos en AlmacenDatosRAM
                if (jsonObject.has("unidad")) {
                    AlmacenDatosRAM.unidades = jsonObject.getString("unidad");
                }
                if (jsonObject.has("valor")) {
                    AlmacenDatosRAM.valor_distancia = (float) jsonObject.getDouble("valor");
                }
                if (jsonObject.has("temp")) {
                    AlmacenDatosRAM.temperatura = (float) jsonObject.getDouble("temp");
                }
                if (jsonObject.has("hum")) {
                    AlmacenDatosRAM.humedad = (float) jsonObject.getDouble("hum");
                }
                if (jsonObject.has("tiempo")) {
                    AlmacenDatosRAM.tiempo = jsonObject.getString("tiempo");
                }

                Log.d(TAG, "Datos interpretados: " + datoString);

            } catch (Exception e) {
                Log.e(TAG, "Error al interpretar JSON: " + e.getMessage());
            }
        }

    }

    // método automático
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "deliveryComplete: ");
    }

    /*
     * 2. Recibir mensajes
     */
    public String leerString() {
        String data = datoString;
        datoString = null;
        return data;
    }

    /*
     * 2. Enviar mensajes
     */
    public void setEnviarMensajes(byte[] datoBytesEnviar) {

        if (client != null && AlmacenDatosRAM.conectado == true) {
            AlmacenDatosRAM.conectado_PubSub = "Enviando datos... ";
            MqttMessage message = new MqttMessage();
            message.setQos(2);
            message.setRetained(true);
            message.setPayload(datoBytesEnviar);
            client.publish(topicStr, message);
        }
    }

    /*
     * 3. Mantener conexión
     */

    /*
     * 4. Desconectarse
     */

    public void desconectar() {
        if (client != null) {
            client.unsubscribe(topicStr);
            client.disconnect();
            AlmacenDatosRAM.conectado = false;
            AlmacenDatosRAM.conectado_PubSub = "Desconectado...";
        }
    }

}
