// LED BLINK de ESP32 está conectado al GPIO 2
int LED_BLINK = 2;
void setup() { pinMode(LED_BLINK, OUTPUT); }
void loop() {
  digitalWrite(LED_BLINK, LOW);
  delay(1000);
  digitalWrite(LED_BLINK, HIGH);
  delay(2000);
}
