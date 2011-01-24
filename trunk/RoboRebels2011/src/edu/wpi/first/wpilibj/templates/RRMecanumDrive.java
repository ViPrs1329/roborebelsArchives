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

    boolean                 bdone = false;      // ? access modifier ?
    boolean                 fdone = false;      // ? access modifier ?

    //TODO why wont enum work?
    int                     controlMode = 0;



      public RRMecanumDrive( int frontLeftMotorChannel,
                             int frontRightMotorChannel,
                             int backLeftMotorChannel,
                             int backRightMotorChannel, 
                             Joystick s)
      {

          frontLeftMotor = new Jaguar(frontLeftMotorChannel);
          frontRightMotor = new Jaguar(frontRightMotorChannel);
          backLeftMotor = new Jaguar(backLeftMotorChannel);
          backRightMotor = new Jaguar(backRightMotorChannel);

          m_xboxStick = s;          // TODO do we need to accept a joystick here
                                    // if we accept it again in the drive method
      }


      public void drive(Joystick n) {
          m_xboxStick = n;
          
          l_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(1),-m_xboxStick.getRawAxis(2)));
          l_magnitude = Math.sqrt((m_xboxStick.getRawAxis(1)*m_xboxStick.getRawAxis(1))+(m_xboxStick.getRawAxis(2)*m_xboxStick.getRawAxis(2)));
          r_angle = Math.toDegrees(MathUtils.atan2(-m_xboxStick.getRawAxis(4),-m_xboxStick.getRawAxis(5)));
          r_magnitude = Math.sqrt((m_xboxStick.getRawAxis(4)*m_xboxStick.getRawAxis(4))+(m_xboxStick.getRawAxis(5)*m_xboxStick.getRawAxis(5)));

          rotation = m_xboxStick.getRawAxis(3);
          //TODO fix trigger input - test actual output values

           //make sure angle is in the expected range
          l_angle %= 360;
          r_angle %= 360;

          if (l_angle < 0){
              l_angle = 360+l_angle;
          }

          if (r_angle < 0){
              r_angle = 360+r_angle;
          }

          //make sure magnitude is in range
          if (l_magnitude < -1)
              l_magnitude = -1;

          if(l_magnitude > 1)
              l_magnitude = 1;


          //make sure magnitude is in range
          if (r_magnitude < -1)
              r_magnitude = -1;

          if(r_magnitude > 1)
              r_magnitude = 1;





          //decrease magnitude for precise mode
          if (m_xboxStick.getRawButton(5)){//TODO change to raw button of left bumper
              if (m_xboxStick.getRawButton(6)){//TODO change to raw button of right bumper
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


          if (m_xboxStick.getRawButton(8)){
              if (!m_xboxStick.getRawButton(8)){
                controlMode = 1;
              }
          }


          if (controlMode == 0){
              
          
          driveMecanum();
          }
          else if (controlMode == 1){
              driveTank();
          }
          //driveTank();




      }

      
       /*
        * Note: will probably require debugging
        *requires a magnitude between -1 and 1 inclusive:
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

       private void driveTank() {
          frontLeftMotor.set(-(l_magnitude+rotation)*Math.cos(Math.toRadians((l_angle+45))));
          frontRightMotor.set((r_magnitude-rotation)* Math.sin(Math.toRadians(r_angle+45)));
          backLeftMotor.set(-(l_magnitude+rotation)*Math.sin(Math.toRadians(l_angle+45)));
          backRightMotor.set((r_magnitude-rotation)*Math.cos(Math.toRadians(r_angle+45)));
       }

       private void driveArcade() {
           double forwardSpeed;
           double rotationSpeed;

           //TODO add ability to strafe left and right with triggers
           frontLeftMotor.set((l_magnitude+(Math.cos(Math.toRadians(l_angle)))));
          frontRightMotor.set((l_magnitude-(Math.cos(Math.toRadians(l_angle)))));
          backLeftMotor.set((l_magnitude+(Math.cos(Math.toRadians(l_angle)))));
          backRightMotor.set((l_magnitude-(Math.cos(Math.toRadians(l_angle)))));

       }



      public void stop() {
          frontLeftMotor.set(0);
          frontRightMotor.set(0);
          backLeftMotor.set(0);
          backRightMotor.set(0);
      }




      /*
       *requires a magnitude between -1 and 1 inclusive:
       * assumes that the angle is in degrees
       * calculates and sets the motor speeds for a given polar vector
       */

      public void drivePolar(double angle, double magnitude) {
          //make sure angle is in the expected range
          angle%=360;

          if (angle < 0){
              angle = 360+angle;
          }

          //make sure magnitude is in range
          if (magnitude < -1)
              magnitude = -1;

          if(magnitude > 1)
              magnitude = 1;


          //convert the angle into speeds and set each motor's speed
          frontLeftMotor.set(-(magnitude)*Math.cos(Math.toRadians((angle+45))));
          frontRightMotor.set((magnitude)* Math.sin(Math.toRadians(angle+45)));
          backLeftMotor.set(-(magnitude)*Math.sin(Math.toRadians(angle+45)));
          backRightMotor.set((magnitude)*Math.cos(Math.toRadians(angle+45)));
          System.out.println("front Left Speed: "+frontLeftMotor.getSpeed());
      }


      public int getControlMode() {
          return controlMode;
      }


}
