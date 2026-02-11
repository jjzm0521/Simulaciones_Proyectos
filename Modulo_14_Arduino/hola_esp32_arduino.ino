/*
* Este método se encarga de la
* configuración. Inicializa
* y se ejecuta una sola vez
*/
void setup() {
/*
* Empezar la comunicación serial.
*/
Serial.begin(115200);
}
/*
* Este método contiene las instrucciones
* que se repetirán continuamente: lecturas
* de entrada, activación de salidas,...
*/
void loop() {
/*
* Imprime la cadena y hace salto de línea
*/
Serial.println("Hola ESP32 desde IDE Arduino");
//Espera 1 segundo para repetir el ciclo
delay(1000);
}
