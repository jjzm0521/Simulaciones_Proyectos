#include "BluetoothSerial.h"

// para manejar JSON
#include <ArduinoJson.h>
#include <ArduinoJson.hpp>

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

StaticJsonDocument<300> doc; // 300 bytes

int pinRed = 23;
int pinGreen = 22;
int pinBlue = 21;
int frecuencia = 5000;
int canal_1 = 0;
int canal_2 = 1;
int canal_3 = 2;
int resolucion = 8;

BluetoothSerial SerialBT;

int r, g, b;
int valor;

int tiempo;        // en ms
int periodo = 100; // en ms

// SENSOR  HC-SR04
// define los números de los pines
// del sensor ultrasónico HC-SR04
const int trigPin = 5;
const int echoPin = 18;

void setup() {
  // Asigna el pin trigPin como de salida
  pinMode(trigPin, OUTPUT);
  // Asigna el pin echoPin como de entrada
  pinMode(echoPin, INPUT);

  Serial.begin(115200);
  SerialBT.begin("PhysicsESP32"); // nombre del dispsoitivo Bluetooth
  Serial.println(
      "El dispositivo empezó, ahora puede emparejarlo con bluetooth!");
  configurarPines();
}

void loop() {
  // retardo de 50 ms entre las ondas generadas.
  delay(50);
  distancia();
  while (SerialBT.available() >
         0) { // Revisamos si hay datos en el puerto serial
    deserializarJSON();
  }

  // Se envía el valor en forma de cadena por bluetooth
  // bluetooth aquí hace el papel de puerto serial
  SerializarObject();
  Serial.println();

  // cada periodo
  delay(periodo);
  tiempo = tiempo + periodo;
}

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

void distancia() {

  // Borra el trigPin
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);

  digitalWrite(trigPin, HIGH);
  // envía pulso de 10 us
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);

  // tiempode viaje del sonido en microsegundos
  float duracion = pulseIn(echoPin, HIGH);

  // velocidad del sonido en el aire en m/s
  // sin corrección de temperatura y humedad
  float velocidad_sonido = 340;

  // cálculo de distancia en cm
  valor = ((duracion * velocidad_sonido) / 2) * 0.0001;
}

void establecerColor(int R, int G, int B) {
  ledcWrite(canal_1, R);
  ledcWrite(canal_2, G);
  ledcWrite(canal_3, B);
}

void deserializarJSON() {
  deserializeJson(doc, SerialBT);
  r = doc["r"];
  g = doc["g"];
  b = doc["b"];
  establecerColor(r, g, b);
}

void SerializarObject() {
  doc["unidad"] = "cm";
  doc["periodo"] = periodo;
  doc["tiempo"] = tiempo;
  doc["valor"] = valor;

  char buffer[200];
  serializeJsonPretty(doc, buffer);
  SerialBT.println(buffer);
  // Espera a que se complete la transmisión de datos en serie salientes
  SerialBT.flush();
}
