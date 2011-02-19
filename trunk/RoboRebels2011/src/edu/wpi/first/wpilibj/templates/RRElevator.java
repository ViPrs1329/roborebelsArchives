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

    private     Joystick    liftStick;

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
    
    public void assignJoystick(Joystick s) {
        liftStick = s;
    }

    public void lift() {
        if (snap_to){

        }
        else {
            //liftMotor.set(liftStick.getRawAxis(2));//Note: currently no protection against strain at top and bottom
        }

        if ( liftStick.getRawButton(3) )
        {
            if(shift==false){
                shift=!shift;
            }
            else{
                shift=!shift;
            }
        }
        else if ( liftStick.getRawButton(4) )
        {
            if(shift==true){
                liftMotor.set(0.4);
            }
            else{
                liftMotor.set(-0.4);
            }
        }
        else if(liftStick.getRawButton(1))
        {
            if(shift==true){
                flipUpMotor.set(0.4);
            }
            else{
                flipUpMotor.set(0.4);
            }
        }
        else if (liftStick.getRawButton(2))
        {
            if(shift==true){
                pincherMotor.set(0.4);
            }
            else{
                pincherMotor.set(-0.4);
            }
        }



    }
}
