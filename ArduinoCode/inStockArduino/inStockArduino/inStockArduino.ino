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
int buttonState4= 0;
int buttonState5 = 0;
int buttonState6 = 0;
int buttonState7 = 0;
int buttonState8 = 0;
int buttonState9 = 0;
int buttonState10 = 0;
int buttonState11 = 0;
int buttonState12 = 0;
int buttonState13 = 0;

void setup() {
    // initialize the pushbutton pin as an input:
  pinMode(buttonPin2, INPUT); 
  pinMode(buttonPin3, INPUT);   
  pinMode(buttonPin4, INPUT);  
  pinMode(buttonPin5, INPUT); 
  pinMode(buttonPin6, INPUT); 
  pinMode(buttonPin7, INPUT); 
  pinMode(buttonPin8, INPUT); 
  pinMode(buttonPin9, INPUT); 
  pinMode(buttonPin10, INPUT); 
  pinMode(buttonPin11, INPUT); 
  pinMode(buttonPin12, INPUT); 
  pinMode(buttonPin13, INPUT); 

  Serial.begin(9600);

}

void loop(){
  // read the state of the pushbutton value:
  buttonState2 = digitalRead(buttonPin2);
  buttonState3 = digitalRead(buttonPin3);
  buttonState4 = digitalRead(buttonPin4);
  buttonState5 = digitalRead(buttonPin5);
  buttonState6 = digitalRead(buttonPin6);
  buttonState7 = digitalRead(buttonPin7);
  buttonState8 = digitalRead(buttonPin8);
  buttonState9 = digitalRead(buttonPin9);
  buttonState10 = digitalRead(buttonPin10);
  buttonState11 = digitalRead(buttonPin11);
  buttonState12 = digitalRead(buttonPin12);
  buttonState13 = digitalRead(buttonPin13);

  // check if the pushbutton is pressed.
  // if it is, the buttonState is HIGH:
  if (buttonState2 == HIGH) {    
    Serial.println("2 ON");
  } 
  else {
    Serial.println("2 OFF");
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
  
  if (buttonState7 == HIGH) {    
    Serial.println("7 ON");
  } 
  else {
    Serial.println("7 OFF");
  }
  
  if (buttonState8 == HIGH) {    
    Serial.println("8 ON");
  } 
  else {
    Serial.println("8 OFF");
  }
  
  if (buttonState9 == HIGH) {    
    Serial.println("9 ON");
  } 
  else {
    Serial.println("9 OFF");
  }
  
  if (buttonState10 == HIGH) {    
    Serial.println("10 ON");
  } 
  else {
    Serial.println("10 OFF");
  }
  
  if (buttonState11 == HIGH) {    
    Serial.println("11 ON");
  } 
  else {
    Serial.println("11 OFF");
  }
  
  if (buttonState12 == HIGH) {    
    Serial.println("12 ON");
    Serial.println("testCommit");
  } 
  else {
    Serial.println("12 OFF");
  }
  
  if (buttonState13 == HIGH) {    
    Serial.println("13 ON");
  } 
  else {
    Serial.println("13 OFF");
  }
}
