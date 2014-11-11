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

// variables will change:
int buttonCurrentState2 = 0;         // variable for reading the pushbutton status
int buttonCurrentState3 = 0;
int buttonCurrentState4 = 0;
int buttonCurrentState5 = 0;
int buttonCurrentState6 = 0;

int buttonLastState2 = 0;         // variable for reading the pushbutton status
int buttonLastState3 = 0;
int buttonLastState4 = 0;
int buttonLastState5 = 0;
int buttonLastState6 = 0;

//how long each associated button has been open:
unsigned long buttonStartTime2 = 0;
unsigned long buttonStartTime3 = 0;
unsigned long buttonStartTime4 = 0;
unsigned long buttonStartTime5 = 0;
unsigned long buttonStartTime6 = 0;

static String msg;

void setup() {
    // initialize the pushbutton pin as an input:
  pinMode(buttonPin2, INPUT); 
  pinMode(buttonPin3, INPUT);   
  pinMode(buttonPin4, INPUT);  
  pinMode(buttonPin5, INPUT); 
  pinMode(buttonPin6, INPUT); 
  
  // initialize to high for removal
  buttonLastState2 = HIGH;         
  buttonLastState3 = HIGH;
  buttonLastState4 = HIGH;
  buttonLastState5 = HIGH;
  buttonLastState6 = HIGH;

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
  

 
  WiFlyClient client = server.available();
  if (client) {
      client.println("HTTP/1.1 200 OK");
      client.println("Content-Type: text/html");   
    
    //Serial.println("in client loop");
    // A http request ends with a blank line
    boolean current_line_is_blank = true;
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        
        CreateButtonStateMsg();
        
        // Send Msg
        int strLen = msg.length();
        for (int index = 0; index < strLen; index++) {     
          client.write(msg.charAt(index));
        }
  
      }
      char incomingByte;
      while (Serial.available() > 0) {
        // Read the incoming byte:
        //incomingByte = Serial.read();
        //Serial.print(incomingByte);
        //client.write(incomingByte);
       }       
     
      ReadButtonStates();
      CheckTrackerRemoved();
      CheckTrackerReturned();  
 
      CreateButtonStateMsg(); // Comment Out 
     
      client.println();
      client.println("<html>");   
      client.println("<h2>TEST</h2>");
      CreateButtonStateMsg();
      client.println("<p>");
      client.println(msg);
      client.println("<p/>");
      
      
      client.flush();
      
       
    }
    Serial.println("Client disconnected.");
    // Give the web browser time to receive the data
    
    delay(100);
    client.stop();
  }
  
  
  delay(5000);
  
}

void ReadButtonStates() {
  // read the state of the pushbutton value:
  //Serial.println("Read Button States");
  buttonCurrentState2 = digitalRead(buttonPin2);
  buttonCurrentState3 = digitalRead(buttonPin3);
  buttonCurrentState4 = digitalRead(buttonPin4);
  buttonCurrentState5 = digitalRead(buttonPin5);
  buttonCurrentState6 = digitalRead(buttonPin6);
  
}

void CheckTrackerRemoved() {
  //Previous button state was high and now is low - signaling removal
  //Serial.println("Check Tracker Removed");
  
  if(buttonCurrentState2 == LOW && buttonLastState2 == HIGH) {
    Serial.println("Button 2: REMOVED");
    buttonStartTime2 = millis();
    buttonLastState2 = buttonCurrentState2;
  }
  
  if(buttonCurrentState3 == LOW && buttonLastState3 == HIGH) {
    Serial.println("Button 3: REMOVED");
    buttonStartTime3 = millis();
    buttonLastState3 = buttonCurrentState3;
  }
  
  if(buttonCurrentState4 == LOW && buttonLastState4 == HIGH) {
    Serial.println("Button 4: REMOVED");
    buttonStartTime4 = millis();
    buttonLastState4 = buttonCurrentState4;
  }
  
  if(buttonCurrentState5 == LOW && buttonLastState5 == HIGH) {
    Serial.println("Button 5: REMOVED");
    buttonStartTime5 = millis();
    buttonLastState5 = buttonCurrentState5;
  }
  
  if(buttonCurrentState6 == LOW && buttonLastState6 == HIGH) {
    Serial.println("Button 6: REMOVED");
    buttonStartTime6 = millis();
    buttonLastState6 = buttonCurrentState6;
  }
  
}

void CheckTrackerReturned() {
  //Previous button state was low and now is high - signaling return
  //Serial.println("Check Tracker Returned");
  
  if(buttonCurrentState2 == HIGH && buttonLastState2 == LOW) {
    Serial.println("Button 2: RETURNED");
    buttonStartTime2 = 0;
    buttonLastState2 = buttonCurrentState2;
  }
  
  if(buttonCurrentState3 == HIGH && buttonLastState3 == LOW) {
    Serial.println("Button 3: RETURNED");
    buttonStartTime3 = 0;
    buttonLastState3 = buttonCurrentState3;
  }
  
  if(buttonCurrentState4 == HIGH && buttonLastState4 == LOW) {
    Serial.println("Button 4: RETURNED");
    buttonStartTime4 = 0;
    buttonLastState4 = buttonCurrentState4;
  }
  
  if(buttonCurrentState5 == HIGH && buttonLastState5 == LOW) {
    Serial.println("Button 5: RETURNED");
    buttonStartTime5 = 0;
    buttonLastState5 = buttonCurrentState5;
  }
  
  if(buttonCurrentState6 == HIGH && buttonLastState6 == LOW) {
    Serial.println("Button 6: RETURNED");
    buttonStartTime6 = 0;
    buttonLastState6 = buttonCurrentState6;
  }
  
}

void CreateButtonStateMsg() {
  //Serial.println("Create Button State Msg");
  
  unsigned long currentTime = millis();
  unsigned long buttonElapsedTime2;
  unsigned long buttonElapsedTime3;
  unsigned long buttonElapsedTime4;
  unsigned long buttonElapsedTime5;
  unsigned long buttonElapsedTime6;
  
  // Assumes no overflow of millis timer
  // For each button, calculates elapsed time or else 0
  if (buttonStartTime2 != 0) {
    buttonElapsedTime2 = currentTime - buttonStartTime2;
  } else {
    buttonElapsedTime2 = buttonStartTime2;
  }
  
  if (buttonStartTime3 != 0) {
    buttonElapsedTime3 = currentTime - buttonStartTime3;
  } else {
    buttonElapsedTime3 = buttonStartTime3;
  }
  
  if (buttonStartTime4 != 0) {
    buttonElapsedTime4 = currentTime - buttonStartTime4;
  } else {
    buttonElapsedTime4 = buttonStartTime4;
  }
  
  if (buttonStartTime5 != 0) {
    buttonElapsedTime5 = currentTime - buttonStartTime5;
  } else {
    buttonElapsedTime5 = buttonStartTime5;
  }
  
  if (buttonStartTime6 != 0) {
    buttonElapsedTime6 = currentTime - buttonStartTime6;
  } else {
    buttonElapsedTime6 = buttonStartTime6;
  }
  
  msg = String((unsigned long) buttonElapsedTime2) + ":2\n";
  msg += String((unsigned long) buttonElapsedTime3) + ":3\n";
  msg += String((unsigned long) buttonElapsedTime4) + ":4\n";
  msg += String((unsigned long) buttonElapsedTime5) + ":5\n";  
  msg += String((unsigned long) buttonElapsedTime6) + ":6\n";  
    
  Serial.println(msg);
    
}

