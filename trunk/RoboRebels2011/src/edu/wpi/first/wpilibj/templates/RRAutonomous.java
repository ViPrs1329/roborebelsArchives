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

    boolean     isDriving;


    RRMecanumDrive mecanumDrive;
    RRLineTracker lineTracker;
    Timer timer;
    int seconds;



    public RRAutonomous(RRMecanumDrive m_drive){
        gyro = new Gyro(1);
        gyro.setSensitivity(.007);
        mecanumDrive = m_drive;
        lineTracker = new RRLineTracker(4,5,6);
        timer = new Timer();
        //seconds = 1000;
        isDriving = false;

        timer.start();
    }
    double xmov = 0;
    double ymov = 0;
    public void lineAuton(boolean righter, boolean midler, boolean lefter) {
        //Sees if the sensors are over the tape and moves if they are, stops if they aren't
        if (( !lefter) ||(!midler) || (!righter)) {
           ymov = -0.2;
        }
        else {
            xmov = 0;
            ymov = 0;
        }

    }

    public void drive(){
        //gets the values of the line sensors
        boolean testL = lineTracker.getL();
        boolean testM = lineTracker.getM();
        boolean testR = lineTracker.getR();
        //sets the value of ymov and xmov
        lineAuton(testR,testM,testL);

        if(timer.get() < 20) {
            //keeps the program going for 20 seconds

            System.out.println("Timer = " + timer.get() );

            // negative is forward!
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
