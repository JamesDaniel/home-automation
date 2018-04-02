// This code should be run on an arduino.
int incomingByte = 0;
int in1 = 7;

void setup() {
    Serial.begin(9600);
    Serial.print("I started");
    pinMode(in1, OUTPUT);
}

void loop() {
    if (Serial.available() > 0) {
        incomingByte = Serial.read();
        char myChar = incomingByte;

        if (myChar == '0') {
            digitalWrite(in1, HIGH);
        }
        if (myChar == '1') {
            digitalWrite(in1, LOW);
        }
        Serial.print("I received: ");
        Serial.println(myChar);
    }
}