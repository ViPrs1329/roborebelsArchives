/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2011
 *
 * This class raises and lowers the elevator
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;


public class RRElevator {
    private     Jaguar      liftMotor;

    private     Joystick    liftStick;

    private     Encoder     liftEncoder;

    private     boolean     snap_to = false;

    private     double[]    heightMap = {0, 1, 2.2, 4.3, 5};

    private     double      currentHeight;


    public RRElevator(int motorChannel) {
        liftMotor = new Jaguar(motorChannel);
        //liftEncoder = new Encoder();
    }
    
    public void assignJoystick(Joystick s) {
        liftStick = s;
    }

    public void lift() {
        if (snap_to){

        }
        else {
            liftMotor.set(liftStick.getRawAxis(2));//Note: currently no protection against strain at top and bottom
        }
    }
}
