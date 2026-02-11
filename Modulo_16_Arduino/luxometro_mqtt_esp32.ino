// para comunicación WiFi
#include <WiFi.h>
// para el protocolo MQTT de IoT
#include <PubSubClient.h>
// para comunicación del sensor
#include <BH1750.h>
#include <Wire.h>

// para manejar JSON
#include <ArduinoJson.h>
#include <ArduinoJson.hpp>

BH1750 lightMeter(0x23);

StaticJsonDocument<300> doc; // 300 bytes

uint16_t valor;
int tiempo;        // en ms
int periodo = 100; // en ms
int minimo = 0;
int maximo = 100;

// Variables para conexiones WiFi
const char *ssid = "xxxxxx";      // reemplazar SSID;
const char *password = "xxxxxxx"; // reemplazar pasword;
// datos del Broker MQTT:
const char *mqtt_server = "168.176.136.61";
const int mqttPort = 1883;
const char *mqttUser = "fisica";        // reemplazar usuario
const char *mqttPassword = "iotfisica"; // reemplazar pasword
const char *topico = "iot/simulaciones/luxometro/equipo_0"; // reemplazar topico

WiFiClient espCliente;
PubSubClient mqttCliente(espCliente);

// forward declaration
void callback(char *topic, byte *payload, unsigned int length);
void SerializeObject();
void setupMQTT();
void conectarToWiFi();
void reconnect();

void setup() {

  Serial.begin(115200);

  Wire.begin();

  if (lightMeter.begin()) {
    Serial.println(F("BH1750 inicializado"));
  } else {
    Serial.println(F("Error inicializando BH1750"));
  }

  Serial.println(F("BH1750 Test begin"));

  // conectar a WiFi
  conectarToWiFi();

  // configurar MQTT
  setupMQTT();
}

// administrar conexión wiFi
void conectarToWiFi() {
  delay(10);
  // Comenzar conexión a red WiFI
  Serial.println();
  Serial.print("Conectando a...");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi conectado");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

// administrar configuración de conexión al Broker MQTT
void setupMQTT() {
  mqttCliente.setServer(mqtt_server, mqttPort);
  // establecer la función de devolución de llamada
  mqttCliente.setCallback(callback);
}

// conectar el cliente ESP32 MQTT al Broker
void reconnect() {
  Serial.println("Conectando a Broker MQTT...");

  // loop hasta lograr conexión
  while (!mqttCliente.connected()) {
    Serial.println("Reconectando al Broker MQTT..");

    String clientId = "ESP32Client-";
    clientId += String(random(0xffff), HEX);

    if (mqttCliente.connect(clientId.c_str())) {
      Serial.println("Conectado");
      // suscripción al tópico
      mqttCliente.subscribe(topico);
    }
  }
}

/*
 Ahora se especifica una función de devolución de llamada.
 Primero se imprime el nombre del tema y luego se
 recibe el mensaje.
*/

void callback(char *topic, byte *payload, unsigned int length) {
  Serial.print("Callback - ");
  Serial.print("Message:");
  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
  }
}

void loop() {

  if (!mqttCliente.connected())
    reconnect();
  mqttCliente.loop();

  // leer luxometro
  valor = lightMeter.readLightLevel();

  // serializar datos JSON para enviarlos
  SerializeObject();

  // cada periodo de muestreo
  delay(periodo);
  tiempo = tiempo + periodo;

} // fin loop

void SerializeObject() {

  doc["maximo"] = maximo;
  doc["minimo"] = minimo;
  doc["unidad"] = "lx";
  doc["periodo"] = periodo;
  doc["tiempo"] = tiempo;
  doc["valor"] = valor;

  char buffer[200];
  serializeJsonPretty(doc, buffer);

  Serial.println("Enviando mensaje a MQTT tópico...");
  Serial.println(buffer);

  if (mqttCliente.publish(topico, buffer) == true) {
    Serial.println("Envío de mensaje exitoso");
  } else {
    Serial.println("Error en el envío del mensaje");
  }

  mqttCliente.loop();
  Serial.println("-------------");
}
