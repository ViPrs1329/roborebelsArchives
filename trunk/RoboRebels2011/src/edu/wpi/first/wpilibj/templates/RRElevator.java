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

    double      INITIAL_ENCODER = 0;

    public RRElevator(int motorChannel,int motorChannel2, int motorChannel3, Encoder nencoder) {
        liftMotor = new Victor(motorChannel);
        flipUpMotor = new Victor(motorChannel2);
        pincherMotor = new Victor(motorChannel3);
        liftEncoder = nencoder;
        liftEncoder.start();

        INITIAL_ENCODER = liftEncoder.getDistance();

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
        //System.out.println("Raw encoder value: "+liftEncoder.getDistance());

        if (snap_to){

        }
        else {
            //liftMotor.set(liftStick.getRawAxis(2));//Note: currently no protection against strain at top and bottom
        }
/*
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
        else if(xboxStick.getRawButton(2))
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
*/

         //don't move the arm if joystick is being used for deployment
        if (!(armStick.getRawButton(6))){

//JRH: NOTE: COMPETITION BOT WAS SET TO .45 (larger motor)
            //Pincher OPEN
            if (armStick.getRawButton(1)){
                pincherMotor.set(.5);
            }
            //PINCHER CLOSES
            else if (armStick.getRawButton(3)){
                pincherMotor.set(-.35);
            }
            else {
                pincherMotor.set(0);
            }

           

            flipUpMotor.set(armStick.getRawAxis(2));

        }

        double encoderDist = -(liftEncoder.getDistance());//-INITIAL_ENCODER

        if (liftStick.getRawButton(2)){
            liftTo(875);
        }
        else {
            double liftSpeed = -liftStick.getRawAxis(2);

            //manual override of cushion
            if (!liftStick.getRawButton(6)){
            if (encoderDist < 30 && liftSpeed < 0){

               if (Math.abs(liftSpeed) > .1){
                   System.out.println("Breaking");
                    liftSpeed = -.15;//TODO needs to be tested
                }
            }
            }
            
            System.out.println("Encoder: "+encoderDist);
            //System.out.println("Lift Speed: " + liftSpeed);
            
             liftMotor.set(liftSpeed);
        }

        //System.out.println("Lift Encoder: "+encoderDist);
    }
        //for autonomous
        public void lift(double lift, double winch, double grip) {
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



        
        pincherMotor.set(grip);
       


        flipUpMotor.set(winch);

        liftMotor.set(lift);

    }

    public void stop(){
        pincherMotor.set(0);
        flipUpMotor.set(0);
        liftMotor.set(0);
    }
    //2nd peg - 871.5
    public void liftTo(double dest_height){
        double speed = 0;
        double height = -liftEncoder.getDistance();
        System.out.println("encoder height: "+height);
        if (Math.abs(dest_height-height) > 50){
            speed = .8*(Math.abs(dest_height-height)/(dest_height-height));
        }
        lift(speed, 0, 0);

    }

    public double getHeight(){
        return -liftEncoder.getDistance();
    }




}
