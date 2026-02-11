// pines de las componentes del LED
int red = 23;
int green = 22;
int blue = 21;
void setup() {
  // iniciilaizar los pines de salida.
  pinMode(red, OUTPUT);
  pinMode(green, OUTPUT);
  pinMode(blue, OUTPUT);
  digitalWrite(red, HIGH);
  digitalWrite(green, HIGH);
  digitalWrite(blue, HIGH);
}
void loop() {
  digitalWrite(red, LOW);  // apaga componente red
  delay(1000);             // espera 1 s
  digitalWrite(red, HIGH); // prende componente red
  delay(1000);
  digitalWrite(green, LOW);
  delay(1000);
  digitalWrite(green, HIGH);
  delay(1000);
  digitalWrite(blue, LOW);
  delay(1000);
  digitalWrite(blue, HIGH);
  delay(1000);
}
