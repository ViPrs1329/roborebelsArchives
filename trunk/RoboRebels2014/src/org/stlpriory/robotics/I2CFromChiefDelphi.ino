#include <Wire.h>

byte dataToSend = 0;
unsigned long counter = 0;
byte data2[6] = {11, 12, 13, 14, 15, 16};

void setup()
{
  Wire.begin(2);                                            // Start I2C interface (address 2)
  Wire.onReceive(i2cReceive);                               // Receive ISR routine
  Wire.onRequest(i2cRequest);                               // Request (send) ISR routine
  Serial.begin(9600);
  Serial.println("Starting up!");
}

void loop()
{
  delay(10);                                                // 10 milliseond delay
  
  // Increment a counter every loop.  Since the ISR may interrupt this code at any time,
  // we need to disable interrupts during the increment operation so the ISR can't a
  // partially updated value.
  
  noInterrupts();
  counter++;
  interrupts();
}

// This routine is called by the ISR once a complete transmission is received from the master

void i2cReceive(int count)
{
  byte i2cCount;                                            // Loop/Array counter
  byte tmp;                                                 // temporary byte holder
  byte i2cDataR[7];                                         // 7 Bytes in length as that is the
                                                            // maximum that can be sent by the
                                                            // cRIO

  Serial.println("Data received from cRIO!");

  for (i2cCount = 0; i2cCount < count; i2cCount++)          // Read data into local buffer
  {                                                         // looping through the entire message
    tmp = Wire.read();

    if (i2cCount < 7)                                       // If more than 7 bytes in length,
      i2cDataR[i2cCount] = tmp;                             // discard remaining data as somthing
  }                                                         // is wrong.

  // the first byte read is typically what is called the register to be read/written.  This
  // example utilizes it as a "command" either to be executed or the type of data to be returned
  // when a "request" is received.

  switch (i2cDataR[0])                                      // Perform action based on command
  {
    // Command 1: Return the counter value when the "request" occurs
    case 1:
      dataToSend = 1;                                       // Set a "global" variable for use by
      break;                                                // the i2cRequest routine

    // Command 2: Update data 2 array with what ever data is sent (if any) and allow this data
    //            to be returned to the master if it is requested
    case 2:
      dataToSend = 2;                                       // Communication for i2cRequest

      if (i2cCount > 1)                                     // More than just the command byte
        memcpy(&data2[0], &i2cDataR[1], i2cCount < 7 ?      // Limit the data to copy to 6 max
          i2cCount - 1 : 6);                                // so as to not overflow the array
  }
}

// This routine is called when the master requests the slave send data.  A full transaction
// typically includes a Receive envent (register to send) followed by a Request event where the
// slave data is actually transmitted to the master.

void i2cRequest()
{ 
  byte i2cDataW[7];
  byte length;
  
  Serial.println("Data sent to cRIO!");
  
  // I use the first byte of the data array to send a comfirmation of the information that follows.
  // This allows the cRIO a way to check what "message" is being sent from the Arduino and interpret
  // it correctly.  While this does limit the actual data part of the message to 6 bytes, the extra
  // verification, I feel, is worth it.
  
  switch (dataToSend)                                        // variable set in the i2cReceive event
  {    
    // Return the value of the counter
    
    case 1:
      i2cDataW[0] = 1;
      memcpy(&i2cDataW[1], &counter, 4);
      length = 5;
      break;
    
    // Return the contents of the cRIO read/write array
    
    case 2:
      i2cDataW[0] = 2;
      memcpy(&i2cDataW[1], &data2[0], 6);
      length = 7;
      break;
    
    // Unknown or invalid command.  Send an error byte back
    
    default:
      i2cDataW[0] = 0xFF;
      length = 1;
  }

  Wire.write((uint8_t *) &i2cDataW[0], length);
}

