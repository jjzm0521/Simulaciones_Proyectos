// para comunicación WiFi
#include <WiFi.h>
// para el protocolo MQTT de IoT
#include <PubSubClient.h>
// para manejar JSON
#include <ArduinoJson.h>
#include <ArduinoJson.hpp>

StaticJsonDocument<300> doc; // 300 bytes

int pinRed = 23;   // GPIO 19
int pinGreen = 22; // GPIO 21
int pinBlue = 21;  // GPIO 22
int frecuencia = 5000;
int canal_1 = 0;
int canal_2 = 1;
int canal_3 = 2;
int resolucion = 8;

int r, g, b;

// Variables para conexiones WiFi
const char *ssid = "xxxxx";     // reemplazar SSID;
const char *password = "xxxxx"; // reemplazar pasword;

// URL del Broker MQTT de ESCUELA DE FÍSICA UNALMED
const char *mqtt_server = "168.176.136.61";
const int mqttPort = 1883;
const char *mqttUser = "fisica";        // reemplazar usuario
const char *mqttPassword = "iotfisica"; // reemplazar pasword
const char *topico = " ";               // reemplazar topico

WiFiClient espCliente;
PubSubClient mqttCliente(espCliente);

// forward declaration
void callback(char *topic, byte *payload, unsigned int length);
void DeserializeObject(byte *payload);
void establecerColor(int R, int G, int B);
void configurarPines();
void setupMQTT();
void conectarToWiFi();
void reconnect();

void setup() {
  Serial.begin(115200);
  configurarPines();
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
      // suscripción al tópico: el que determinen
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

  // se recibe el mensaje
  for (int i = 0; i < length; i++) {
    (char)payload[i];
  }

  DeserializeObject(payload);
}

void loop() {

  if (!mqttCliente.connected())
    reconnect();
  mqttCliente.loop();

  establecerColor(r, g, b);

} // fin loop

void configurarPines() {

  // configuración de los canales
  // rojo
  ledcSetup(canal_1, frecuencia, resolucion);
  ledcAttachPin(pinRed, canal_1);

  // azul
  ledcSetup(canal_2, frecuencia, resolucion);
  ledcAttachPin(pinGreen, canal_2);

  // verde
  ledcSetup(canal_3, frecuencia, resolucion);
  ledcAttachPin(pinBlue, canal_3);
}

void establecerColor(int R, int G, int B) {

  ledcWrite(canal_1, R);
  ledcWrite(canal_2, G);
  ledcWrite(canal_3, B);
}

void DeserializeObject(byte *payload) {

  // void DeserializeObject(String mensajeRGB){
  // char buffer[200];
  deserializeJson(doc, payload);

  r = doc["r"];
  g = doc["g"];
  b = doc["b"];
}
