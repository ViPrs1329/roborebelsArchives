/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;

/**
 *
 * 
 */
public class RRSimplifiedAutonomous {
        RRMecanumDrive mecanumDrive;
        RRElevator elevator;
        RRLineTracker lineTracker;
        RRDipSwitch dipSwitch;
        Gyro gyro;//Unused

        //Enumeration for dipSwitch
        public  final   int     LEFT_STRAIGHT = 1;
        public  final   int     LEFT_FORK = 2;
        public  final   int     RIGHT_FORK = 3;
        public  final   int     RIGHT_STRAIGHT = 4;

        public int switchMode;

        Timer timer;

        //Drive Speeds
        double xmov = 0;
        double ymov = 0;
        double rot = 0;

        //Variables for Line Tracking
        boolean isFork;

        boolean driveStraight;

        double speed = -.3;
        double speed2 = -.4;
        double strafe_speed2 = .4;

        double small_correction = .05;
        double large_correction = .1;

        double smallStrafeCorrection = .2;
        double largeStrafeCorrection = .4;

        int direction = 1;//one or negative one, to specify the fork direction

        Timer yPeriod;

        Timer lostLineTimer;

        int lost_line_Timer = 0;

        //Tube Placement Variables


        //TODO
        /*
         * Link up direction to fork dir
         */



        public RRSimplifiedAutonomous(RRMecanumDrive m_drive, RRElevator n_elevator, RRDipSwitch dSwitch, RRLineTracker lineT, Gyro n_gyro){
            mecanumDrive = m_drive;
            elevator = n_elevator;
            lineTracker = lineT;
            dipSwitch = dSwitch;
            gyro = n_gyro;//Unused
            gyro.setSensitivity(.007);

            setSwitchMode();

            //Assign Strafing Values Based on switch state
            String output = new String();
            switch (switchMode){
            case LEFT_STRAIGHT:
                output = ("LEFT_STRAIGHT");
                isFork = false;
                break;
            case LEFT_FORK:
                output = ("LEFT_FORK");
                isFork = true;
                direction = -1;//TODO change one to -1, selects direction
                strafe_speed2 = -.5;
                //strafeSpeed = .2;
               // smallStrafeCorrection = +.1;
               // largeStrafeCorrection = +.2;
                break;
            case RIGHT_FORK:
                output = ("RIGHT_FORK");
                isFork = true;
                direction = 1;//TODO change one to -1, selects direction
                strafe_speed2 = .4;
               // strafeSpeed = -.2;
               // smallStrafeCorrection = -.1;
               // largeStrafeCorrection = -.2;
                break;
            case RIGHT_STRAIGHT:
                output = ("RIGHT_STRAIGHT");
                isFork = false;
                break;
            }
            System.out.println("Dip Mode: "+output);


            //Set up line following
            timer = new Timer();

            lostLineTimer = new Timer();

            yPeriod = new Timer();
            yPeriod.start();

            driveStraight = true;

           // strafe_speed2 *= direction;
            //smallStrafeCorrection *= direction;
            //largeStrafeCorrection *= direction;


            timer.start();
            lostLineTimer.start();

        }
        
        boolean resetTimer = false;

        public void lineAuton(boolean right, boolean mid, boolean left) {
            //NOTE: A true value means the sensor sees the line
            
            if ((!left) && (!mid) && (!right)){//None Detect Line
                if (driveStraight){
                    if (resetTimer == false){
                        lostLineTimer.reset();
                        resetTimer = true;
                    }
                    else{
                        if (lostLineTimer.get() <= .2){
                             if (driveStraight){
                                ymov = speed;
                                xmov = 0;
                                rot = 0;
                            }
                            else {
                                ymov = speed2;
                                xmov = strafe_speed2;
                                rot = 0;
                            }
                        }else {
                            ymov = 0;
                            xmov = 0;
                            rot = 0;
                        }
                    }
                }
                else {
                    if (yPeriod.get()>.9){
                        System.out.println("Stopping");
                        ymov = 0;
                        xmov = 0;
                        rot = 0;
                    }
                    else {
                        System.out.println("ATTEMPTING TO STRAFE WITH STRAFE VALUE: " +strafe_speed2);
                        ymov = 0;
                        xmov = strafe_speed2;
                        rot = 0;
                    }
                }
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                System.out.println("ALL 3 Detected");
                if (driveStraight){
                    System.out.println("isFork: "+isFork);
                    if (isFork){
                        System.out.println("Timer Counting, Switched to Strafe");
                        driveStraight = false;
                        yPeriod.reset();
                    }
                    else{

                    ymov = 0;
                    xmov = 0;
                    rot = 0;
                    }


                    inPosition = true;
                    //Begin winch and claw stuff
                }
                else {
                    //Begin Strafing to specified side (treat as middle detection
                    //ymov = speed2;
                   if (yPeriod.get()<.8){
                    ymov = 0;
                    xmov = strafe_speed2;
                    rot = 0;
                    }
                    else {
                       inPosition = true;
                       ymov = 0;
                       xmov = 0;
                       rot = 0;
                     }
                }
            }
            else if ((left) && (!mid) && (right)){//Outside Detect Line (Y)
                if (driveStraight){



                    System.out.println("Detected Split In Straight Mode, Error.");
                     if (isFork){
                        System.out.println("Timer Counting, Switched to Strafe");
                        driveStraight = false;
                        yPeriod.reset();
                    }


                }
                else {
                    ymov = 0;
                    xmov = strafe_speed2;
                    rot = 0;
                }
            }
            else if ((!left) && (mid) && (!right)){//Only Middle Detects Line (ideal)
                if (driveStraight){
                    ymov = speed;
                    xmov = 0;
                    rot = 0;
                }
                else {
                    ymov = speed2;
                    xmov = strafe_speed2;
                    rot = 0;
                }
            }
            else if ((left) && (mid) && (!right)){//Off Slightly, needs to correct Right
                if (driveStraight){
                    ymov = speed;
                    xmov = 0;
                    rot = small_correction;
                }
                else {
                    ymov = speed2;
                    xmov = strafe_speed2-smallStrafeCorrection;//May need to invert sign
                    rot = 0;
                }
            }
            else if ((left) && (!mid) && (!right)){//Off a lot, needs to correct a lot Right
                if (driveStraight){
                    ymov = speed;
                    xmov = 0;
                    rot = large_correction;
                }
                else {
                    ymov = speed2;
                    xmov = strafe_speed2-largeStrafeCorrection;//May need to invert sign
                    rot = 0;
                }
            }
            else if ((!left) && (mid) && (right)){//Off Slightly, needs to correct Left
                if (driveStraight){
                     ymov = speed;
                    xmov = 0;
                    rot = - small_correction;
                }
                else {
                    ymov = speed2;
                    xmov = strafe_speed2+smallStrafeCorrection;//May need to invert sign
                    rot = 0;
                }
            }
            else if ((!left) && (!mid) && (right)){//Off a lot, needs to correct a lot Left
                if (driveStraight){
                     ymov = speed;
                    xmov = 0;
                    rot = -large_correction;
                }
                else {
                    ymov = speed2;
                    xmov = strafe_speed2+largeStrafeCorrection;//May need to invert sign
                    rot = 0;
                }
            }
        }

        boolean inPosition;

        boolean doneOpening = false;
    boolean open = false;
    boolean winchBegun = false;
    Timer winchTimer = new Timer();

    boolean backUpBegin = false;

    Timer backUpTimer = new Timer();

    Timer clawTimer = new Timer();

    Timer winchTimer2 = new Timer();

    double winchSpeed = 0;

    boolean firstRun = false;
        public void drive(){


        //gets the values of the line sensors NOTE: Inverted so true is now on line
        boolean testL = !lineTracker.getL();
        boolean testM = !lineTracker.getM();
        boolean testR = !lineTracker.getR();
        
        //sets the value of ymov and xmov
        lineAuton(testR,testM,testL);




        if(timer.get() < 20) {
            //keeps the program going for 20 seconds

            //System.out.println("Timer = " + timer.get() );


            if (!firstRun){
                winchTimer2.reset();

                firstRun = true;
            }
 else {
                if (winchTimer2.get() < 2){
                    //winchSpeed = .4;
                    elevator.lift(0, .45, 0);
                }
 }

            //elevator.liftTo(1000);

            // negative is forward!
            mecanumDrive.drive(xmov, ymov, rot);

            if (inPosition){
                 double clawSpeed = 0;
                
                double driveSpeed = 0;
                System.out.println("in Position");
              // lift = true;

                //if within 10 units from 875, begin releasing
             //   if (Math.abs(elevator.getHeight()) > 800){

                        if (winchBegun == false){
                            winchTimer.start();
                            winchBegun = true;
                        }
                        if ( winchTimer.get() < 2){
                           // winchSpeed = 1;
                        }

                        if (open == false && winchTimer.get() > 2){
                            clawTimer.start();
                            open = true;
                        }
                       if (clawTimer.get() < .55){
                            clawSpeed = -.45;
                        }
                        else {

                            if (backUpBegin == false){
                                backUpTimer.start();
                                backUpBegin = true;
                            }

                            if (backUpTimer.get() < 1){
                                driveSpeed = .3;
                            }

                            mecanumDrive.drive(0, driveSpeed, 0);
                        }

               // }

                elevator.lift(0, winchSpeed, clawSpeed);
            }
        
        }
        else
        {
            mecanumDrive.drive(0, 0, 0);
        }






        }


        private void setSwitchMode(){
             if (dipSwitch.getState(0)){
          switchMode = LEFT_FORK;
        }
        else if(dipSwitch.getState(1))
        {
          switchMode = LEFT_STRAIGHT;
        }
        else if(dipSwitch.getState(2))
        {
          switchMode = RIGHT_STRAIGHT;
        }
        else if(dipSwitch.getState(3))
        {
          switchMode = RIGHT_FORK;
        }
        }
}
