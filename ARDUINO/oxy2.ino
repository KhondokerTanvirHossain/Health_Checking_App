#include <Arduino.h>
#include <math.h>
#include <Wire.h>
#include "MAX30100.h"


MAX30100* pulseOxymeter;

void setup() {
  Wire.begin();
  Serial.begin(115200);
  Serial1.begin(115200);
  Serial.println("Pulse oxymeter test!");

  pulseOxymeter = new MAX30100();
  pinMode(2, OUTPUT);

}

void loop() {
int temp = analogRead(A0);
int temparature=(5.0*temp*1000.0)/(1024*10);
  
  pulseoxymeter_t result = pulseOxymeter->update();
  

  if( result.pulseDetected == true )
  {
    Serial1.println(" BEAT");
    
    Serial1.print( " BPM " );
    Serial1.print( result.heartBPM );
    Serial1.print( " | " );
  
    Serial1.print( " SO2 " );
    Serial1.print( result.SaO2 );
    Serial1.println( "%" );
    Serial1.println(" TEMP ");
    Serial1.println(temparature);
  }
    {
    Serial.println(" BEAT");
    
    Serial.print( " BPM " );
    Serial.print( result.heartBPM );
    Serial.print( " | " );
  
    Serial.print( " SO2 " );
    Serial.print( result.SaO2 );
    Serial.println( "%" );
    Serial.println(" TEMP ");
    Serial.println(temparature);
    Serial.println(digitalRead(2));
    
  }  
   
  delay(2000);
  digitalWrite( 2, !digitalRead(2) );
}
