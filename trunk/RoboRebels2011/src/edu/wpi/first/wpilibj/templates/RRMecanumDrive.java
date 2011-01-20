/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2011
 *
 * This class manually drives each mecanum wheel motor.  It calculates the
 * wheel output based on polar input.
 *
 * Contains all methods for drive control actions.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Jaguar;

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



      public RRMecanumDrive( int frontLeftMotorChannel,
                                    int frontRightMotorChannel,
                                    int backLeftMotorChannel,
                                    int backRightMotorChannel){
          frontLeftMotor = new Jaguar(frontLeftMotorChannel);
          frontRightMotor = new Jaguar(frontRightMotorChannel);
          backLeftMotor = new Jaguar(backLeftMotorChannel);
          backRightMotor = new Jaguar(backRightMotorChannel);
      }

      /*
       *requires a magnitude between -1 and 1 inclusive:
       * assumes that the angle is in degrees
       * calculates and sets the motor speeds for a given polar vector
       */
      public void drivePolar(double angle, double magnitude){
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
          frontLeftMotor.set(magnitude*Math.cos(Math.toRadians((angle+45))));
          frontRightMotor.set(magnitude* Math.sin(Math.toRadians(angle+45)));
          backLeftMotor.set(magnitude*Math.sin(Math.toRadians(angle+45)));
          backRightMotor.set(magnitude*Math.cos(Math.toRadians(angle+45)));

      }


      /*
       *requires a speed between -1 and 1 inclusive
       *a positive speed rotates clockwise, negative counterclockwise
       * set speed to 0 while driving
       *if the speed is 0, it does nothing
       */
      public void rotate(double speed){
            if (speed != 0){
             frontLeftMotor.set(speed);
             frontRightMotor.set(-speed);
             backLeftMotor.set(speed);
             backRightMotor.set(-speed);
          }

      }


}
