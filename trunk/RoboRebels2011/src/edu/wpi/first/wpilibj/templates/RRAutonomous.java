/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;

/**
 *
 * @author Matt
 */
public class RRAutonomous {
    Gyro gyro;
    double angle;


    RRMecanumDrive mecanumDrive;

    RRLineTracker lineTracker;



    public RRAutonomous(){
    gyro = new Gyro(1);
    gyro.setSensitivity(.007);
    mecanumDrive = new RRMecanumDrive(4, 1,2,3);
    lineTracker = new RRLineTracker(4,5,6);
    
    }

    public void drive(){
        double xmov = 0;
        double ymov = 0;

        //use the gyro to line up the robot with the field

        //detect line, and adjust strafing movement appropriately

        //drive forward until robot is in front of wall

        //stop driving and start arm movement

        mecanumDrive.drive(xmov, ymov);
    }

    public void printGyro(){

        if (lineTracker.activeSensor().equals("none")){
            mecanumDrive.drive(0,.04);
        }
        else {
            if (lineTracker.activeSensor().equals("middle")){
                mecanumDrive.drive(,.04);
            }
        }

            angle = gyro.pidGet();
            System.out.println(angle%(360));

    }
}
