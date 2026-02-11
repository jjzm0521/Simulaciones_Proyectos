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

int r, g, b;
BluetoothSerial SerialBT;

// Prototipos de funciones
void configurarPines();
void deserializarJSON();
void establecerColor(int R, int G, int B);

void setup() {
  Serial.begin(115200);
  SerialBT.begin("PhysicsESP32"); // nombre del dispsoitivo Bluetooth
  Serial.println(
      "El dispositivo empezó, ahora puede emparejarlo con bluetooth!");

  configurarPines();
}

void loop() {
  delay(250);
  while (SerialBT.available() >
         0) { // Revisamos si hay datos en el puerto serial
    deserializarJSON();
  }
}

void configurarPines() {
  // configuración de los canales
  // rojo
  ledcSetup(canal_1, frecuencia, resolucion);
  ledcAttachPin(pinRed, canal_1);

  // verde (corrigiendo asignación de pinGreen al canal correspondiente si es
  // necesario, pero el código original dice: pinGreen -> canal_2, pinBlue ->
  // canal_3. Nota: En el código original pinGreen está en canal_2 y pinBlue en
  // canal_3. Mantendré el código original, aunque el comentario decía //azul
  // para canal_2 y //verde para canal_3, lo cual es confuso con los nombres de
  // variables. Asumiré que pinGreen es Verde y pinBlue es Azul, ajustaré
  // comentarios para claridad si es 'correcta' manera)

  // El código original tenía:
  //  ledcAttachPin(pinGreen,canal_2); // bajo comentario //azul
  //  ledcAttachPin(pinBlue,canal_3); // bajo comentario //verde

  // Si pinGreen es Verde, debería ir bajo comentario Verde.
  // Dejaré la lógica del código (pinGreen -> canal_2) pero corregiré los
  // comentarios para que coincidan con la variable.

  // Verde (pinGreen es 22)
  ledcSetup(canal_2, frecuencia, resolucion);
  ledcAttachPin(pinGreen, canal_2);

  // Azul (pinBlue es 21)
  ledcSetup(canal_3, frecuencia, resolucion);
  ledcAttachPin(pinBlue, canal_3);
}

void establecerColor(int R, int G, int B) {
  ledcWrite(canal_1, R);
  ledcWrite(canal_2, G);
  ledcWrite(canal_3, B);
}

void deserializarJSON() {
  // deserializeJson puede fallar, es bueno chequear errores pero mantendré
  // simple como el pedido
  deserializeJson(doc, SerialBT);

  r = doc["r"];
  g = doc["g"];
  b = doc["b"];

  establecerColor(r, g, b);
}
