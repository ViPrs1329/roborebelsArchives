/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2011
 *
 * This class raises and lowers the elevator
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;


public class RRElevator {
    private     Victor      liftMotor;

    private     Victor      pincherMotor;

    private     Victor      flipUpMotor;

    private     Joystick    xboxStick;

    private     Joystick    liftStick;

    private     Joystick    armStick;

    private     Encoder     liftEncoder;

    private     boolean     snap_to = false;

    private     double[]    heightMap = {0, 1, 2.2, 4.3, 5};

    private     double      currentHeight;

    private     boolean     shift= false;


    public RRElevator(int motorChannel,int motorChannel2, int motorChannel3) {
        liftMotor = new Victor(motorChannel);
        flipUpMotor = new Victor(motorChannel2);
        pincherMotor = new Victor(motorChannel3);
        //liftEncoder = new Encoder();
    }
    
    public void assignXboxJoystick(Joystick s) {
        xboxStick = s;
    }
    public void assignLiftJoystick(Joystick s) {
        liftStick = s;
    }
    public void assignArmJoystick(Joystick s) {
        armStick = s;
    }

    public void lift() {
        if (snap_to){

        }
        else {
            //liftMotor.set(liftStick.getRawAxis(2));//Note: currently no protection against strain at top and bottom
        }

        if ( xboxStick.getRawButton(3) )
        {
            shift = !shift;
        }
        else if ( xboxStick.getRawButton(4) )
        {
            if(shift){
                liftMotor.set(0.1);
            }
            else{
                liftMotor.set(-0.1);
            }
        }
        else if(xboxStick.getRawButton(1))
        {
            if(shift){
                flipUpMotor.set(0.1);
            }
            else{
                flipUpMotor.set(0.1);
            }
        }
        else if (xboxStick.getRawButton(2))
        {
            if(shift){
                pincherMotor.set(0.1);
            }
            else{
                pincherMotor.set(-0.1);
            }
        }


        if (armStick.getRawButton(0)){
            pincherMotor.set(.01);
        }
        else if (armStick.getRawButton(3)){
            pincherMotor.set(-.01);
        }
        else {
            pincherMotor.set(0);
        }


        pincherMotor.set(armStick.getRawAxis(2));



    }
}
