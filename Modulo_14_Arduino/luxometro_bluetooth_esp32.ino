#include "BluetoothSerial.h"
#include <BH1750.h>
#include <Wire.h>

// para manejar bluetooth
#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

// para manejar JSON
#include <ArduinoJson.h>
#include <ArduinoJson.hpp>

BH1750 lightMeter(0x23);
BluetoothSerial SerialBT;
StaticJsonDocument<300> doc; // 300 bytes
uint16_t valor;
int tiempo;        // en ms
int periodo = 100; // en ms

void setup() {
  Serial.begin(115200);
  SerialBT.begin("PhysicsESP32"); // nombre del dispsoitivo Bluetooth
  Serial.println(
      "El dispositivo empezó, ahora puede emparejarlo con bluetooth!");
  // envía el mensaje de texto a través del serial
  SerialBT.println(
      "LED encendido"); // Envía el mensaje de texto a través de BT Serial

  // Initiacializa el bus I2C (la librería BH1750 no lo hace automáticamente)
  Wire.begin();
  if (lightMeter.begin()) {
    Serial.println(F("BH1750 inicializado"));
  } else {
    Serial.println(F("Error inicializando BH1750"));
  }
  Serial.println(F("BH1750 Test begin"));
}

void loop() {
  valor = lightMeter.readLightLevel();
  SerializeObject();
  Serial.println();

  // cada segundo
  delay(periodo);
  tiempo = tiempo + periodo;
}

void SerializeObject() {
  doc["unidad"] = "lux";
  doc["periodo"] = periodo;
  doc["tiempo"] = tiempo;
  doc["valor"] = valor;
  char buffer[200];
  serializeJsonPretty(doc, buffer);
  SerialBT.println(buffer);
  // Espera a que se complete la transmisión de datos en serie salientes
  SerialBT.flush();
}
