#include <SPI.h>
#include <Client.h>
#include <Configuration.h>
#include <Debug.h>
#include <ParsedStream.h>
#include <Server.h>
#include <SpiUart.h>
#include <WiFly.h>
#include <WiFlyDevice.h>
#include <_Spi.h>
#include "Ethernet.h"
#include "WiFly.h"

int PORT_SERVER = 8888;
WiFlyServer server(PORT_SERVER);

// set pin numbers:
const int buttonPin2 = 2;     // the number of the pushbutton pin
const int buttonPin3 = 3;
const int buttonPin4 = 4;
const int buttonPin5 = 5;
const int buttonPin6 = 6;
const int buttonPin7 = 7;
const int buttonPin8 = 8;
const int buttonPin9 = 9;
const int buttonPin10 = 10;
const int buttonPin11 = 11;
const int buttonPin12 = 12;
const int buttonPin13 = 13;

// variables will change:
int buttonState2 = 0;         // variable for reading the pushbutton status
int buttonState3 = 0;
int buttonState4 = 0;
int buttonState5 = 0;
int buttonState6 = 0;

int buttonState22 = 0;         // variable for reading the pushbutton status
int buttonState33 = 0;
int buttonState44 = 0;
int buttonState55 = 0;
int buttonState66 = 0;

//how long each associated button has been open:
unsigned long buttonTime2 = 0;
unsigned long buttonTime3 = 0;
unsigned long buttonTime4 = 0;
unsigned long buttonTime5 = 0;
unsigned long buttonTime6 = 0;

unsigned long buttonTime22 = 0;
unsigned long buttonTime33 = 0;
unsigned long buttonTime44 = 0;
unsigned long buttonTime55 = 0;
unsigned long buttonTime66 = 0;

boolean button2Change = false;
boolean button3Change = false;
boolean button4Change = false;
boolean button5Change = false;
boolean button6Change = false;

//current time
long currentTime = 0;

void setup() {
    // initialize the pushbutton pin as an input:
  pinMode(buttonPin2, INPUT); 
  pinMode(buttonPin3, INPUT);   
  pinMode(buttonPin4, INPUT);  
  pinMode(buttonPin5, INPUT); 
  pinMode(buttonPin6, INPUT); 

  Serial.begin(9600);
  
  Serial.println("Starting...");
  WiFly.begin();
  Serial.println("WiFly.begin");
  //Hard-coded login credentials
  if (!WiFly.join("me202wifi", "passphrase")) {
    while (1) {
      Serial.println("Failed to join WiFi network");
      // Hang on failure.
    }
  }
  

  Serial.print("IP: ");
  Serial.println(WiFly.ip());
  
  server.begin();
  Serial.println("server.begin");


}

void loop(){
  currentTime = millis();
  
  // read the state of the pushbutton value:
  buttonState2 = digitalRead(buttonPin2);
  buttonState3 = digitalRead(buttonPin3);
  buttonState4 = digitalRead(buttonPin4);
  buttonState5 = digitalRead(buttonPin5);
  buttonState6 = digitalRead(buttonPin6);

  // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
  if (buttonState2 == HIGH) {    
    buttonTime2 = 0;
    buttonTime22 = 0;
    buttonState22 = HIGH;
  } 
  else {
    if(buttonState22== HIGH)
    {
      button2Change = true;
      buttonState22 = LOW;
    }
    if(button2Change == true)
    {
      buttonTime22 = currentTime;
      button2Change = false;
    }
    buttonTime2 = currentTime - buttonTime22;
  }
  
  if (buttonState3 == HIGH) {    
    Serial.println("3 ON");
  } 
  else {
    Serial.println("3 OFF");
  }
  
  if (buttonState4 == HIGH) {    
    Serial.println("4 ON");
  } 
  else {
    Serial.println("4 OFF");
  }
  
  if (buttonState5 == HIGH) {    
    Serial.println("5 ON");
  } 
  else {
    Serial.println("5 OFF");
  }
  
  if (buttonState6 == HIGH) {    
    Serial.println("6 ON");
  } 
  else {
    Serial.println("6 OFF");
  } 
  
}
