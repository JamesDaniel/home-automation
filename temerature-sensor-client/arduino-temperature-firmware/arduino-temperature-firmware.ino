#include <dht.h>

dht DHT;

#define DHT11_PIN 5

void setup()
{
  Serial.begin(9500);
}

void loop()
{
  int chk = DHT.read11(DHT11_PIN);
  Serial.println(DHT.temperature, 1);
  delay(5000);
}
