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
        boolean driveStraight;






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
                break;
            case LEFT_FORK:
                output = ("LEFT_FORK");
                //strafeSpeed = .2;
               // smallStrafeCorrection = +.1;
               // largeStrafeCorrection = +.2;
                break;
            case RIGHT_FORK:
                output = ("RIGHT_FORK");
               // strafeSpeed = -.2;
               // smallStrafeCorrection = -.1;
               // largeStrafeCorrection = -.2;
                break;
            case RIGHT_STRAIGHT:
                output = ("RIGHT_STRAIGHT");
                break;
            }
            System.out.println("Dip Mode: "+output);


            //Set up line following
            timer = new Timer();







            timer.start();

        }
        
        public void lineAuton(boolean right, boolean mid, boolean left) {
            //NOTE: A true value means the sensor sees the line
            
            if ((!left) && (!mid) && (!right)){//None Detect Line
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
            else if ((left) && (!mid) && (right)){//Outside Detect Line (Y)
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
            else if ((left) && (mid) && (right)){//All Three Detect Line
                
            }
        }


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

           // elevator.liftTo(1000);
            // negative is forward!
            mecanumDrive.drive(xmov, ymov, rot);
/*
            if (inPosition){
                 double clawSpeed = 0;
                double winchSpeed = 0;
                double driveSpeed = 0;
                System.out.println("in Position");
              // lift = true;

                //if within 10 units from 875, begin releasing
                if (Math.abs(elevator.getHeight()) > 900){

                        if (winchBegun == false){
                            winchTimer.start();
                            winchBegun = true;
                        }
                        if ( winchTimer.get() < 2){
                            winchSpeed = 1;
                        }

                        if (open == false && winchTimer.get() > 2){
                            clawTimer.start();
                            open = true;
                        }
                       if (clawTimer.get() < 1){
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

                }

                elevator.lift(0, winchSpeed, clawSpeed);
            }
        */
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
