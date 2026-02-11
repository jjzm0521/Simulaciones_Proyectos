#include "BluetoothSerial.h"


//para manejar JSON
#include <ArduinoJson.hpp>
#include <ArduinoJson.h>


#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif


StaticJsonDocument<300> doc;//300 bytes


int pinRed=23;    
int pinGreen= 22; 
int pinBlue= 21; 
int frecuencia = 5000;
int canal_1 = 0; 
int canal_2 = 1;
int canal_3 = 2;
int resolucion = 8;


int r,g,b;
BluetoothSerial SerialBT;


void setup() {
  
  Serial.begin(115200);
  SerialBT.begin("PhysicsESP32"); //nombre del dispsoitivo Bluetooth 
  Serial.println("El dispositivo empezó, ahora puede emparejarlo con bluetooth!");
  
  configurarPines();


}


void loop() {
  delay(250); 
    while (SerialBT.available() > 0){//Revisamos si hay datos en el puerto serial
    deserializarJSON();  
  
  } 
  
} 


void configurarPines(){
  //configuración de los canales
  //rojo
  ledcSetup(canal_1, frecuencia, resolucion); 
  ledcAttachPin(pinRed,canal_1);


  //azul
  ledcSetup(canal_2, frecuencia, resolucion); 
  ledcAttachPin(pinGreen,canal_2); 


  //verde
  ledcSetup(canal_3, frecuencia, resolucion); 
  ledcAttachPin(pinBlue,canal_3);
 
}


void establecerColor(int R, int G, int B) {
   ledcWrite(canal_1,R);
   ledcWrite(canal_2,G);
   ledcWrite(canal_3,B);
  
}




void deserializarJSON(){
  deserializeJson(doc, SerialBT);
  
  r = doc["r"];
  g = doc["g"]; 
  b = doc["b"];


  establecerColor(r, g, b); 
   
}
