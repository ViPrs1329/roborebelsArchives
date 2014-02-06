/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Sensors extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    private static I2C i2c;
    DigitalModule digitalSidecar;
    public static byte dataReceived[] = {0, 0, 0, 0, 0, 0, 0};
    private static byte dataToSend[] = {0, 0, 0, 0, 0, 0, 0};

    public Sensors() {
        super("Sensors");
        Debug.println("[Sensors Subsystem] Instantiating...");

        digitalSidecar = DigitalModule.getInstance(1);
        i2c = digitalSidecar.getI2C(RobotMap.I2C_ADRESS_NUMBER);
        // Put the i2c device into CompatibilityMode, this ensures compliance with the entire i2c spec)
        i2c.setCompatabilityMode(true);

        
        Debug.println("[Sensors Subsystem] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /*
     * This routine retrieves the incrementing counter value from the Arduino and reconstitutes
     * it back into a 32 bit integer.  This is a rather convoluted way of doing it as JAVA does
     * not support an unsigned single byte (bytes are signed)
     */
    public int arduino_read() {
        Debug.println("[Sensors Subsystem] arduino_read()");
        // Request Command #1 - Return Counter Value ("Register" 1)
        i2c.write(1, 0);
        Debug.println("i2c.write(1,0)");

        // Read 5 bytes of data, with 0 bytes to send.  A false return value indicates success
//        boolean aborted = i2c.read(0, 5, dataReceived);
        boolean aborted = i2c.transaction(dataToSend, 0, dataReceived, 1);
        Debug.println("transaction result = "+aborted);
//        if (!aborted) {
            // If the data returned is indeed the counter, the first byte should be a 1 - identical
            // to the value we sent above
            if (dataReceived[0] != 1) {
                Debug.println("Invalid data returned from Arduino - command 1");
            } else {
                Debug.println("[Sensors Subsystem] arduino_read()");
                return (((int) dataReceived[4] * 16777216)
                        + (((int) dataReceived[3] & 0x000000ff) * 65536)
                        + (((int) dataReceived[2] & 0x000000ff) * 256)
                        + ((int) dataReceived[1] & 0x000000ff));
            }
//        } else {
//            Debug.println("Failure to read from Arduino - command 1");
//        }

        return 0;
    }
    /*
     * This routine sends up to 6 bytes to place in the Arduino's "read/write" array and
     * then reads it back into the public byte array "dataReceived" for verification
     */

    public void arduino_write(byte newData[], byte length) {
        int i;

        // Maximum 6 bytes to send in addition to the "command" byte.  Place all the data into
        // the byte array.
        if (length > 6) {
            length = 6;
        }

        dataToSend[0] = 2;
        for (i = 0; i < length; i++) {
            dataToSend[i + 1] = newData[i];
        }

        // Send the data to the Arduino.  Do not request any return bytes or this function
        // will fail
        if (i2c.transaction(dataToSend, length + 1, dataReceived, 0) == false) {
            // After successfully sending the data, perform a data read.  Since the last
            // transaction was a write with a "Command" value of 2, the Arduino will assume
            // this is the data to return.

            if (i2c.transaction(dataToSend, 0, dataReceived, 7) == false) {
                if (dataReceived[0] != 2) {
                    Debug.println("Invalid data returned from Arduino - command 2");
                }
            } else {
                Debug.println("Failure to read from Arduino - command 2");
            }
        } else {
            Debug.println("Failure to send data to Arduino");
        }
    }
}
