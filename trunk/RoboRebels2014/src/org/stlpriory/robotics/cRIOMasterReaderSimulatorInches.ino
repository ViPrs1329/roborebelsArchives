//
//
// FRC Team 1329 Roborebels
//
// I2C Bus Master to simulate cRIO for RangeFinderI2CTestInches sketch
//
//  based on:

// Wire Master Reader
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Reads data from an I2C/TWI slave device
// Refer to the "Wire Slave Sender" example for use with this

// Created 29 March 2006

// This example code is in the public domain.


#include <Wire.h>

void setup()
{
  Wire.begin();        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
}

void loop()
{
  Wire.requestFrom(2, 1);    // request 1 byte from slave device #2

  while(Wire.available())    // slave may send less than requested
  { 
    int c = Wire.read(); // receive a byte as character
    
    Serial.print("Distance is: ");
/*    
    char d = 48 + c;
     
    if (c > 9)
    {
      d = 45;    // if more than one digit, dislay as "-"
    }   
    
*/
    Serial.print(c);         // print the character
    
    Serial.println(" inches.");
        
  }

  delay(500);
}
