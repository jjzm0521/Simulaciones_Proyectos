int LED_aparpadear = 5;

void setup() { pinMode(LED_aparpadear, OUTPUT); }
void loop() {
  digitalWrite(LED_aparpadear, HIGH);
  delay(1000);
  digitalWrite(LED_aparpadear, LOW);
  delay(2000);
}
