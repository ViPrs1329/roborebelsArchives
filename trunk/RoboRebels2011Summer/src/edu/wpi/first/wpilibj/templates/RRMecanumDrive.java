/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2011
 *
 * This class manually drives each mecanum wheel motor.  It calculates the
 * wheel output based on polar input.
 *
 * Contains all methods for drive control actions.
 *
 * NOTE:  I know we are on a tight schedule, but please,
 * can we make sure that we organize the code a little
 * bit more (ie. keep members declared at the top, line up
 * braces, etc.)?  Thanks.  - Mr. Ward
 */

/*
 * THINGS TO TEST:
 *
 * Functionality of switching drive mode
 *      drivestation mode indicator
 *
 * Is the joystick object being passed properly to RRMecanumDrive?
 *
 * New Cartesian Mode
 *
 * New Cartesian Tank Mode
 *
 * Adding global sensitivity variable to control sensitivity and precise mode
 *      in cart Tank
 *      in all modes (still need to do once tank test passes)
 *
 *
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import java.lang.Math;
import edu.wpi.first.wpilibj.Joystick;
import java.util.Enumeration;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.Encoder;


/**
 *
 * @author Matt
 */

public class RRMecanumDrive {
    
    private     Jaguar      frontLeftMotor,
                            frontRightMotor,
                            backLeftMotor,
                            backRightMotor;

    private     double      frontLeftSpeed,
                            frontRightSpeed,
                            backLeftSpeed,
                            backRightSpeed;


    private     Joystick    m_xboxStick;

    private     double      l_angle, l_magnitude, rotation;
    private     double      r_angle, r_magnitude;

    private     double      forward, right, clockwise;
    private     double      right_forward, right_right;

    private     double      sensitivity = 1;


    private     boolean     controlModeSwitched = false;

    public static final int DRIVE_MECANUM = 0;
    public static final int DRIVE_TANK = 1;
    public static final int DRIVE_CARTESIAN_TEST = 2;
    public static final int DRIVE_CARTESIAN_TANK = 3;

    private     int         controlMode = DRIVE_CARTESIAN_TEST;

   // private     Encoder     fl_Encoder;
    //private     Encoder     fr_Encoder;
    //private     Encoder     bl_Encoder;
   // private     Encoder     br_Encoder;


    /**
     * Constructor for RRMecanumDrive
     * 
     * @param frontLeftMotorChannel Front-left channel port number
     * @param frontRightMotorChannel Front-right channel port number
     * @param backLeftMotorChannel Back-left channel port number
     * @param backRightMotorChannel Back-right channel port number
     */

    public RRMecanumDrive( int frontLeftMotorChannel,
                           int frontRightMotorChannel,
                           int backLeftMotorChannel,
                           int backRightMotorChannel )
    {

          frontLeftMotor = new Jaguar(frontLeftMotorChannel);
          frontRightMotor = new Jaguar(frontRightMotorChannel);
          backLeftMotor = new Jaguar(backLeftMotorChannel);
          backRightMotor = new Jaguar(backRightMotorChannel);

          //fl_Encoder = new Encoder(4,7,4,8,false);
          //fr_Encoder = new Encoder(4,9,4,10,false);
          //bl_Encoder = new Encoder(4,11,4,12,false);
          //br_Encoder = new Encoder(4,13,4,14,false);

          //fl_Encoder.start();
          //fr_Encoder.start();
          //bl_Encoder.start();
         // br_Encoder.start();

    }

    /**
     * Assign a joystick that this object will grab input from
     * 
     * @param j Passed in joystick object.  Should already be instantiated
     */
    
    public void assignJoystick(Joystick j) {
          m_xboxStick = j;
    }

    /**
     * This method should be the only method that needs to be called
     * to actually drive a mecanum system.  This should be called 
     * from the TeleopPeriodic function.
     * 
     * TODO:  Have we tried this call from the TeloepContinuous method?
     */
    
    public void drive() {

          /*
           * Matt, I found some incredibly simple code to drive a mecanum drive
           * system with two joysticks (one for x, y cartesian coordinates and
           * one for rotation here: http://www.chiefdelphi.com/forums/showthread.php?t=89205&highlight=mecanum+normalize
           * I have a suspicion that your code implements this in a roundabout
           * way by calculating angles, etc.  I am not sure if this works with
           * a tank drive control scheme, but I can imagine that it can be
           * adopted if it works out.
           *
           *
                // 3-axis joystick interface to a mecanum or omni drive


                // first define your driver interface,
                // in this case a 3-axis joystick:


                forward = -Y;   // push joystick forward to go forward
                right = X;      // push joystick to the right to strafe right
                clockwise = Z;  // twist joystick clockwise turn clockwise


                // here is where you would put any special shaping of the joystick
                // response curve, such as deadband or gain adjustment


                // now apply the inverse kinematic tranformation
                // to convert your vehicle motion command
                // to 4 wheel speed commands:

                // NOTE: you can introduce some tuning parameters for forward,
                // rotate and strafing to dial in the correct feel.  See example
                // below.

                front_left = forward + clockwise + right;
                front_right = forward - clockwise - right;
                rear_left = forward + clockwise - right;
                rear_right = forward - clockwise + right;


                // finally, normalize the wheel speed commands
                // so that no wheel speed command exceeds magnitude of 1:

                max = abs(front_left);
                if (abs(front_right)>max) max = abs(front_right);
                if (abs(rear_left)>max) max=abs(rear_left);
                if (abs(rear_right)>max) max=abs(rear_right);

                if (max>1)
                  {front_left/=max; front_right/=max; rear_left/=max; rear_right/=max;}


                // you're done. send these four wheel commands to their respective wheels
           *
           *
           *
           * 
           * Found some tank drive psuedo code which is similar:
           *
                Here's a way to program TANK DRIVE on a mecanum bot so that you 
                can tune the joystick sensitivity to all three motions (fwd/rev,
                turn, stafe) independently:

                Let Kf, Kt, and Ks be the tuning parameters (0 to +1) for the
                forward/reverse, turn, and strafe motions, respectively.

                Let X1 and Y1 represent the joystick outputs for the driver's
                left-hand joystick (-1 to +1);

                Let Y2 represent the joystick outputs for the driver's
                right-hand joystick (-1 to +1).

                When each joystick is pushed forward, its Y output should be
                positive. When the joystick is pushed to the right, its X output
                should be positive. If not, add code to invert the sign if
                necessary.

                Let W1, W2, W3, and W4 be the front left, front right, rear 
                left, and rear right wheels, respectively. ("left" and "right"
                in this context means "port" and "starboard", respectively)



                Calculate the following:

                Yf = (Y1 + Y2)/2

                Yt = (Y1 - Y2)/2


                Now calculate the four wheel speed commands:

                W1 = Kf*Yf + Kt*Yt + Ks*X1

                W2 = Kf*Yf - Kt*Yt - Ks*X1

                W3 = Kf*Yf + Kt*Yt - Ks*X1

                W4 = Kf*Yf - Kt*Yt + Ks*X1



                Now normalize the wheel speed commands:

                Let Wmax be the maximum absolute value of the four wheel speed 
                commands. If Wmax is greater than 1, then divide each of the four
                wheel speed commands by Wmax.


                Finally, send each of the four normalized wheel speed commands
                to the respective wheels (-1 means 100% reverse, +1 means 100% forward).

                The Y1 and Y2 axes act like tank drive.  The X1 axis commands
                strafe left and right.  The X2 axis is not used.

                Tune Kf, Kt, and Ks (from 0 to +1) to get the desired joystick
                sensitivity to each of the three motions.


           *
           * Could you try this out in your simulation?  Thanks.
           *
           * - Mr. Ward
           */


          /*
           * NOTE: To those of you who don't understand what is happening here
           * allow me to explain:
           *
           * We are computing the angle and maginitude of each joystick position
           * by utilizing trigonometric properties, specifically this one:
           *
           *       /|
           *      / |
           *hyp  /  |
           *    /   | opposite, y or axis 1
           *   /    |
           *  / i   |
           * /______|
           *   adjacent, x or axis 2
           *
           * SOA CAH TOA
           *
           * sin(i) = opposite / hypotenous
           * cos(i) = adjacent / hypotenous
           * tan(i) = opposite / adjacent
           *
           * i = arctan( opposite / adacent )
           *
           * With the magnitude we are simply calculating the hypotenous via the
           * formula:
           *
           * hyp^2 = x^2 + y^2
           * hyp = sqrt( x^2 + y^2 )
           * 
           */

           double l_xVal  = m_xboxStick.getRawAxis(1);
           double l_yVal  = m_xboxStick.getRawAxis(2);

           double r_xVal  = m_xboxStick.getRawAxis(4);
           double r_yVal  = m_xboxStick.getRawAxis(5);

           if (Math.abs(l_xVal) < .13){
               l_xVal = 0;
           }

           if (Math.abs(l_yVal)< .13){
               l_yVal = 0;
           }

           if (Math.abs(r_xVal) < .13){
               r_xVal = 0;
           }

           if (Math.abs(r_yVal)< .13){
               r_yVal = 0;
           }

           //Change the range of the joystick values to account for the dead zone
           if (l_xVal > 0){
               l_xVal = (l_xVal-.13)/(1-.13);
           }
            else if (l_xVal < 0){
               l_xVal = (l_xVal+.13)/(1-.13);
            }

            if (l_yVal > 0){
               l_yVal = (l_yVal-.13)/(1-.13);
           }
            else if (l_yVal < 0){
               l_yVal = (l_yVal+.13)/(1-.13);
            }

            if (r_xVal > 0){
               r_xVal = (r_xVal-.13)/(1-.13);
           }
            else if (r_xVal < 0){
               r_xVal = (r_xVal+.13)/(1-.13);
            }

            if (r_yVal > 0){
               r_yVal = (r_yVal-.13)/(1-.13);
           }
            else if (r_yVal < 0){
               r_yVal = (r_yVal+.13)/(1-.13);
            }
           

          l_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(1),-m_xboxStick.getRawAxis(2)));
          l_magnitude = Math.sqrt((m_xboxStick.getRawAxis(1)*m_xboxStick.getRawAxis(1))+(m_xboxStick.getRawAxis(2)*m_xboxStick.getRawAxis(2)));
          r_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(4),-m_xboxStick.getRawAxis(5)));
          r_magnitude = Math.sqrt((m_xboxStick.getRawAxis(4)*m_xboxStick.getRawAxis(4))+(m_xboxStick.getRawAxis(5)*m_xboxStick.getRawAxis(5)));




          if (l_magnitude < .28){
              l_magnitude = 0;
          }

          if (r_magnitude <.28){
              r_magnitude = 0;
          }

          rotation = -m_xboxStick.getRawAxis(3);


          forward = l_yVal;
          right = -l_xVal;
          clockwise = m_xboxStick.getRawAxis(3);

          right_forward = r_yVal;
          right_right = -r_xVal;

          //make sure angles are in the expected range
          l_angle %= 360;
          r_angle %= 360;

          if (l_angle < 0){
              l_angle = 360+l_angle;
          }

          if (r_angle < 0){
              r_angle = 360+r_angle;
          }

          //make sure magnitudes are in range
          l_magnitude = Math.max(-1, l_magnitude);
          l_magnitude = Math.min(1, l_magnitude);
          r_magnitude = Math.max(-1, r_magnitude);
          r_magnitude = Math.min(1, r_magnitude);

          //decrease magnitude for precise mode
          if (m_xboxStick.getRawButton(5)){
              if (m_xboxStick.getRawButton(6)){
                  l_magnitude*=.4;
                  r_magnitude*=.4;
                  rotation *= .4;

                  forward *= .4;
                  right *= .4;
                  clockwise *= .4;

                  right_forward *= .4;
                  right_right *= .4;

                  sensitivity = .4;

              }
                else {
                  l_magnitude*=.7;
                  r_magnitude*=.7;
                  rotation*=.7;

                  forward *= .7;
                  right *= .7;
                  clockwise *= .7;

                  right_forward *= .7;
                  right_right *= .7;

                  sensitivity = .7;
                }

          }




          
          // Toggle Through Drive Modes with Start Button
          if (m_xboxStick.getRawButton(8) && !controlModeSwitched) {
              switch (controlMode){
                  case DRIVE_MECANUM:
                      controlMode = DRIVE_TANK;
                      break;
                  case DRIVE_TANK:
                      controlMode = DRIVE_CARTESIAN_TEST;
                      break;
                  case DRIVE_CARTESIAN_TEST:
                      controlMode = DRIVE_CARTESIAN_TANK;
                      break;
                  case DRIVE_CARTESIAN_TANK:
                      controlMode = DRIVE_MECANUM;
                      break;
              }

              controlModeSwitched = true;
          }
          else if (!m_xboxStick.getRawButton(8)){
              controlModeSwitched = false;
          }


          //drive in correct mode
          switch (controlMode){
              case DRIVE_MECANUM:
                  driveMecanum();
                  break;
              case DRIVE_TANK:
                  driveTank();
                  break;
              case DRIVE_CARTESIAN_TEST:
                  driveCartesianTest();
                  break;
              case DRIVE_CARTESIAN_TANK:
                  driveCartesianTank();
                  break;
          }

          /*
          System.out.println("FLE Distance: "+ fl_Encoder.get());
          System.out.println("FRE Distance: "+ fr_Encoder.get());
          System.out.println("BLE Distance: "+ bl_Encoder.get());
          System.out.println("BRE Distance: "+ br_Encoder.get());
          */
    }

    
    /**
     * Drive method used for autonomous motion.  Range of all variables
     * is from -1.0 - 1.0
     * 
     * @param x X component of the direction vector
     * @param y Y component of the direction vector
     * @param rot Speed of rotation 
     */
    
    public void drive(double x, double y, double rot) {
          l_angle = Math.toDegrees(MathUtils.atan2(-x,-y));
          l_magnitude = Math.sqrt((x*x)+(y*y));

          forward = y;
          right = -x;
          clockwise = rot;

          //make sure angles are in the expected range
          l_angle %= 360;
          r_angle %= 360;

          if (l_angle < 0){
              l_angle = 360+l_angle;
          }

          if (r_angle < 0){
              r_angle = 360+r_angle;
          }

          //make sure magnitudes are in range
          l_magnitude = Math.max(-1, l_magnitude);
          l_magnitude = Math.min(1, l_magnitude);
          r_magnitude = Math.max(-1, r_magnitude);
          r_magnitude = Math.min(1, r_magnitude);

          driveCartesianTest();
 }
      
    /*
     * requires a magnitude between -1 and 1 inclusive:
     * assumes that the angle is in degrees
     * calculates and sets the motor speeds for a given polar vector
     * allows for rotation while driving [-1,1]
     */

    private void driveMecanum() {

          /*
           * Does the computation of each motor speed value ever exceed the
           * range of -1.0, 1.0?  Just curious.
           *
           * - Mr. Ward
           */

           //System.out.println( "driveMecanum()" );

          frontLeftMotor.set(-(l_magnitude+rotation)*Math.cos(Math.toRadians((l_angle+45))));
          frontRightMotor.set((l_magnitude-rotation)* Math.sin(Math.toRadians(l_angle+45)));
          backLeftMotor.set(-(l_magnitude+rotation)*Math.sin(Math.toRadians(l_angle+45)));
          backRightMotor.set((l_magnitude-rotation)*Math.cos(Math.toRadians(l_angle+45)));

    }

    /*
     * Drives the left and right wheels separately, with its repsective xbox stick
     * Allows for more precise rotation
     */

    private void driveTank() {
          frontLeftMotor.set(-(l_magnitude+rotation)*Math.cos(Math.toRadians((l_angle+45))));
          frontRightMotor.set((r_magnitude-rotation)* Math.sin(Math.toRadians(r_angle+45)));
          backLeftMotor.set(-(l_magnitude+rotation)*Math.sin(Math.toRadians(l_angle+45)));
          backRightMotor.set((r_magnitude-rotation)*Math.cos(Math.toRadians(r_angle+45)));
    }

    /*
     * Uses a different or modified computation to resolve motor speeds
     * without trigonometric functions
     * Hopefully this will provide increased efficiency as well as
     * more precise driving
     */

    private void driveCartesianTest() {
            double front_left = forward + clockwise + right;
            double front_right = forward - clockwise - right;
            double rear_left = forward + clockwise - right;
            double rear_right = forward - clockwise + right;

            double max = Math.abs(front_left);

            max = Math.max(Math.abs(front_right), max);
            max = Math.max(Math.abs(rear_left), max);
            max = Math.max(Math.abs(rear_right), max);

            if (max > 1) {
                front_left /= max;
                front_right /= max;
                rear_left /= max;
                rear_right /= max;
            }
            
            frontLeftMotor.set(front_left);
            frontRightMotor.set(-front_right);
            backLeftMotor.set(rear_left);
            backRightMotor.set(-rear_right);
    }


    /**
     * Cartesian tank drive method.  (ie. no trig functions present 
     * in this code)
     * 
     */

    private void driveCartesianTank() {
        //sensitivity
        double tuneForward = 1*sensitivity;
        double tuneRight = 1*sensitivity;
        double tuneClockwise = .75*sensitivity;



        double Yf = (forward + right_forward)/2;
        double Yt = (forward - right_forward)/2;

        //can change this to also use the average of X sticks
        //currently does not use right x value
        double front_left = tuneForward * Yf + tuneClockwise * Yt + tuneRight * right;
        double front_right = tuneForward * Yf - tuneClockwise * Yt - tuneRight * right;
        double rear_left = tuneForward * Yf + tuneClockwise * Yt - tuneRight * right;
        double rear_right = tuneForward * Yf - tuneClockwise * Yt + tuneRight * right;


        //Normalize
        double max = Math.abs(front_left);

            if (Math.abs(front_right) > max) max = Math.abs(front_right);
            if (Math.abs(rear_left) > max) max = Math.abs(rear_left);
            if (Math.abs(rear_right) > max) max = Math.abs(rear_right);

            if (max > 1) {
                front_left /= max;
                front_right /= max;
                rear_left /= max;
                rear_right /= max;
            }

        frontLeftMotor.set(front_left);
        frontRightMotor.set(-front_right);
        backLeftMotor.set(rear_left);
        backRightMotor.set(-rear_right);
    }


    /**
     * Sets all motor speeds to zero, stopping the drive system.
     */

    public void stop() {
          frontLeftMotor.set(0);
          frontRightMotor.set(0);
          backLeftMotor.set(0);
          backRightMotor.set(0);
    }


    /**
     * Returns a string value of the current drive system that we're in.
     * 
     * @return Returns a string representation of the drive mode.
     */

    public String getControlModeName() {
        String controlOut = new String();
       switch (controlMode){
           case DRIVE_MECANUM:
               controlOut = "Mecanum       ";
               break;
           case DRIVE_TANK:
               controlOut = "Tank         ";
               break;
           case DRIVE_CARTESIAN_TEST:
               controlOut = "Cart Mec        ";
               break;
           case DRIVE_CARTESIAN_TANK:
               controlOut = "Cart Tank        ";
               break;
       }
          return controlOut;
    }


}
