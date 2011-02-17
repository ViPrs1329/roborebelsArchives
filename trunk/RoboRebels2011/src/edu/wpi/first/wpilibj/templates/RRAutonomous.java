/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author Matt
 */
public class RRAutonomous {
    Gyro gyro;
    double angle;


    RRMecanumDrive mecanumDrive;
    RRLineTracker lineTracker;
    Timer timer;
    int seconds;



    public RRAutonomous(){
        gyro = new Gyro(1);
        gyro.setSensitivity(.007);
        mecanumDrive = new RRMecanumDrive(4, 1,2,3);
        lineTracker = new RRLineTracker(4,5,6);
        timer = new Timer();
        seconds = 1000;
    }

    public void drive(){
        timer.start();
        if(timer.get() < 2 * seconds) {
            double xmov = .5;
            double ymov = .5;

            //use the gyro to line up the robot with the field

            //detect line, and adjust strafing movement appropriately

            //drive forward until robot is in front of wall

            //stop driving and start arm movement

            mecanumDrive.drive(xmov, ymov);
        }
        else
        {
            mecanumDrive.drive(0, 0);
        }
    }

/*    public void printGyro(){

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

    }*/
}
