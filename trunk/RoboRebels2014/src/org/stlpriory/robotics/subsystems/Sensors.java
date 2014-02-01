/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;

/**
 *
 * @author William
 */
public class Sensors extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static I2C i2c;
    DigitalModule digitalSidecar;
    public static byte dataReceived[] = {0, 0, 0, 0, 0, 0, 0};
    private static byte dataToSend[] = {0, 0, 0, 0, 0, 0, 0};

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        digitalSidecar = DigitalModule.getInstance(RobotMap.DIGITAL_SIDECAR_MODULE_NUMBER);
        i2c = digitalSidecar.getI2C(RobotMap.I2C_ADRESS_NUMBER);
        i2c.setCompatabilityMode(true);
    }
    public void arduino_write(byte newData[], byte length)
{
  int i;

  // Maximum 6 bytes to send in addition to the "command" byte.  Place all the data into
  // the byte array.

  if (length > 6) {
    length = 6;
  }

  dataToSend[0] = 2;

  for (i=0; i<length; i++) {
    dataToSend[i+1] = newData[i];
  }

  // Send the data to the Arduino.  Do not request any return bytes or this function
  // will fail

  if (i2c.transaction(dataToSend, length + 1, dataReceived, 0) == false)
  {
    // After successfully sending the data, perform a data read.  Since the last
    // transaction was a write with a "Command" value of 2, the Arduino will assume
    // this is the data to return.

    if (i2c.transaction(dataToSend, 0, dataReceived, 7) == false)
    {
      if (dataReceived[0] != 2) {
               System.out.println("Invalid data returned from Arduino - command 2");   
      }
    }
    else{
      System.out.println("Failure to read from Arduino - command 2");
    }
  }
  else{
    System.out.println("Failure to send data to Arduino");
  }
}
    
}
