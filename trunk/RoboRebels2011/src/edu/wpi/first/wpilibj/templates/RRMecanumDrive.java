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

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Jaguar;
import java.lang.Math;
import edu.wpi.first.wpilibj.Joystick;
import java.util.Enumeration;
import com.sun.squawk.util.MathUtils;


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

    double                  l_angle, l_magnitude, rotation;
    double                  r_angle, r_magnitude;


    private     boolean     controlModeSwitched = false;

    public static final int DRIVE_MECANUM = 0;
    public static final int DRIVE_TANK = 1;
    public static final int DRIVE_CARTESIAN_TEST = 2;

    private     int         controlMode = DRIVE_MECANUM;




    public RRMecanumDrive( int frontLeftMotorChannel,
                             int frontRightMotorChannel,
                             int backLeftMotorChannel,
                             int backRightMotorChannel)
    {

          frontLeftMotor = new Jaguar(frontLeftMotorChannel);
          frontRightMotor = new Jaguar(frontRightMotorChannel);
          backLeftMotor = new Jaguar(backLeftMotorChannel);
          backRightMotor = new Jaguar(backRightMotorChannel);        
    }

    public void assignJoystick(Joystick s) {
          m_xboxStick = s;
    }


    public void drive() {
          l_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(1),-m_xboxStick.getRawAxis(2)));
          l_magnitude = Math.sqrt((m_xboxStick.getRawAxis(1)*m_xboxStick.getRawAxis(1))+(m_xboxStick.getRawAxis(2)*m_xboxStick.getRawAxis(2)));
          r_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(4),-m_xboxStick.getRawAxis(5)));
          r_magnitude = Math.sqrt((m_xboxStick.getRawAxis(4)*m_xboxStick.getRawAxis(4))+(m_xboxStick.getRawAxis(5)*m_xboxStick.getRawAxis(5)));

          rotation = m_xboxStick.getRawAxis(3);

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
          if (l_magnitude < -1)
              l_magnitude = -1;

          if(l_magnitude > 1)
              l_magnitude = 1;

          if (r_magnitude < -1)
              r_magnitude = -1;

          if(r_magnitude > 1)
              r_magnitude = 1;


          //decrease magnitude for precise mode
          if (m_xboxStick.getRawButton(5)){
              if (m_xboxStick.getRawButton(6)){
                  l_magnitude*=.4;
                  r_magnitude*=.4;
                  rotation*=.4;
              }
                else {
                  l_magnitude*=.7;
                  r_magnitude*=.7;
                  rotation*=.7;
                }
          }

          /*
           * Thanks -Matt
           */
          
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
                      controlMode = DRIVE_MECANUM;
                      break;
              }

              controlModeSwitched = true;
          }
          else if (!m_xboxStick.getRawButton(8)){
              controlModeSwitched = false;
          }


          switch (controlMode){
              case DRIVE_MECANUM:
                  driveMecanum();
                  break;
              case DRIVE_TANK:
                  driveTank();
                  break;
              case DRIVE_CARTESIAN_TEST:
                  driveCartesianTest();
          }
    }

      
    /*
     * requires a magnitude between -1 and 1 inclusive:
     * assumes that the angle is in degrees
     * calculates and sets the motor speeds for a given polar vector
     * allows for rotation while driving [-1,1]
     */

    private void driveMecanum() {
          //convert the angle into speeds and set each motor's speed
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

    private void driveCartesianTest(){
        //TODO implementation

    }

    public void stop() {
          frontLeftMotor.set(0);
          frontRightMotor.set(0);
          backLeftMotor.set(0);
          backRightMotor.set(0);
    }

    public String getControlModeName() {
        String controlOut = new String();
       switch (controlMode){
           case DRIVE_MECANUM:
               controlOut = "Mecanum";
               break;
           case DRIVE_TANK:
               controlOut = "Tank";
               break;
           case DRIVE_CARTESIAN_TEST:
               controlOut = "Cartesian Test";
               break;
       }
          return controlOut;
    }


}
