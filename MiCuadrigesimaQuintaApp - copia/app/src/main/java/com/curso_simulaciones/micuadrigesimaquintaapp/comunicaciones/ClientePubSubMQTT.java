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

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(actividad.getApplicationContext(), MQTTHOST, clientId, Ack.AUTO_ACK);
        client.setCallback(this);
        options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        // no estaba
        // options.setCleanSession(true);

        // hacer conexión
        client.connect(options, null, this);
        // Log.d(TAG, "Conectado...");
        AlmacenDatosRAM.conectado_PubSub = "Conectado con el broker...";
        AlmacenDatosRAM.conectado = true;
    }

    // método automático
    // suscripción del tópico
    @Override
    public void onSuccess(IMqttToken asyncActionToken) {

        // Log.d(TAG, "onSuccess: ");

        client.subscribe(topicStr, 0);
        AlmacenDatosRAM.conectado_PubSub = "Se hizo la suscripción al tópico...";
        AlmacenDatosRAM.conectado = true;
        // Log.d(TAG, "Se hizo la suscripción al tópico...");

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

        // Log.d(TAG, "entro a lost");

    }

    // método automático
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        if (AlmacenDatosRAM.conectado == true) {
            datoString = new String(mqttMessage.getPayload());
            AlmacenDatosRAM.conectado_PubSub = "Recibiendo datos...";
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

        return datoString;

    }

    /*
     * 2. Enviar mensajes
     */
    public void setEnviarMensajes(byte[] datoBytesEnviar) {

        Log.d(TAG, "enviando datos: ");

        if (AlmacenDatosRAM.conectado == true) {
            AlmacenDatosRAM.conectado_PubSub = "Enviando datos... ";
            MqttMessage message = new MqttMessage();

            message.setQos(2);// estaba en 0
            message.setRetained(true);// false

            // message.setQos(0);//estaba en 0
            // message.setRetained(false);//false

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

        client.unsubscribe(topicStr);
        options.setCleanSession(true);// no estaba
        client.disconnect();

    }

}
