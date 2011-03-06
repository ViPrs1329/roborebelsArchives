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
 */



public class RRAutonomous {
    RRDipSwitch         dipSwitch;
    Gyro gyro;
    RRElevator elevator;
    double angle;

    double xmov = 0;
    double ymov = 0;
    double rot = 0;
    double speed = -.2;//-.45 is good, at least for straight\\
    double strafeSpeed = 0;
    double smallStrafeCorrection = 0;
    double largeStrafeCorrection = 0;
    double large_correction = .1;
    double small_correction = .05;
    double lost_line_timer = 100;
    double hunt_time = .2/speed;
    boolean     isDriving;
    boolean     yIsDetected = false;
    boolean     stopCondition = false;
    boolean     strafeToTheEnd = false;
    boolean     goingDownCenter = false;
    boolean     highPeg = false;
    boolean     goFast = false;

    boolean lift;

    public  final   int     LEFT_STRAIGHT = 1;
    public  final   int     LEFT_FORK = 2;
    public  final   int     RIGHT_FORK = 3;
    public  final   int     RIGHT_STRAIGHT = 4;

    public          int     dipMode;



    RRMecanumDrive mecanumDrive;
    RRLineTracker lineTracker;

    Timer timer;
    Timer yCounter;
    boolean expectY;
    boolean YLINE;
    int seconds;
    boolean inPosition;



    public RRAutonomous(RRMecanumDrive m_drive, RRElevator elevator, RRDipSwitch dSwitch, RRLineTracker lineT, Gyro n_gyro){
        dipSwitch = dSwitch;

        if (dipSwitch.getState(0)){
          dipMode = LEFT_FORK;
        }
        else if(dipSwitch.getState(1))
        {
          dipMode = LEFT_STRAIGHT;
        }
        else if(dipSwitch.getState(2))
        {
          dipMode = RIGHT_STRAIGHT;
        }
        else if(dipSwitch.getState(3))
        {
          dipMode = RIGHT_FORK;
        }





        String output = new String();
        switch (dipMode){
            case LEFT_STRAIGHT:
                output = ("LEFT_STRAIGHT");
                break;
            case LEFT_FORK:
                output = ("LEFT_FORK");
                strafeSpeed = .2;
                smallStrafeCorrection = +.1;
                largeStrafeCorrection = +.2;
                break;
            case RIGHT_FORK:
                output = ("RIGHT_FORK");
                strafeSpeed = -.2;
                smallStrafeCorrection = -.1;
                largeStrafeCorrection = -.2;
                break;
            case RIGHT_STRAIGHT:
                output = ("RIGHT_STRAIGHT");
                break;
        }
       System.out.println("Dip Mode: "+output + ", Number: " + dipMode);



        gyro = n_gyro;
        gyro.setSensitivity(.007);
        mecanumDrive = m_drive;
        this.elevator = elevator;
        lineTracker = lineT;
        timer = new Timer();
        YLINE = true;
        yCounter = new Timer();
        expectY = false;
        //seconds = 1000;
        isDriving = false;


        //Set the dipswitch enumeration value once at the beginning
       // if (dipSwitch.)

        timer.start();
    }

    /*
     * Get state of switch array.
     *
     * Bit #    T           F
     *
     * 1      Center    Outside
     * 2      Peg (M)   Peg (H)
     * 3     Speed (M)  Speed (F)
     * 4     Unused     Unused
     *
     */

    public void reset(){

    }

    void getSwitchState()
    {
        
    }

    /*
     *
     * One idea for dealing with "Y" line is that if we are traveling at a
     * constant speed then if we don't encounter all three line sensors
     * activating after a certain amount of time then we must be on a straight
     * line.
     *
     */

    boolean doneOpening = false;
    boolean open = false;
    boolean winchBegun = false;
    Timer winchTimer = new Timer();

    boolean backUpBegin = false;
    
    Timer backUpTimer = new Timer();

    Timer clawTimer = new Timer();
    public void lineAuton(boolean right, boolean mid, boolean left) {
        //Sees if the sensors are over the tape and moves if they are, stops if they aren't

        if (inPosition){
               
            }
 else {
        if ((left) && (mid) && (right)) {       // off the line
             
            if (lost_line_timer == 100){
                lost_line_timer = timer.get();
                System.out.println("Lost_Line_Timer = " + lost_line_timer );
            }
            if(timer.get() < lost_line_timer + hunt_time) {
                                                //keeps the program going for 20 seconds
                //System.out.println("Still going Lost_Line_Timer = " + lost_line_timer );
                ymov = speed;
                xmov = 0;                            //look for the line for 1 second then stop
                rot = 0;
            }
             else{
                
                System.out.println("Stopping Lost_Line_Timer = " + lost_line_timer );
                 ymov = 0;
                 xmov = 0;
                 rot = 0;
                 
             }
        }
        else if((left) && (mid) && (!right)) {  // right sensor on the line
            System.out.println("Moving right");
            if ( strafeToTheEnd )
            {
                ymov = speed * 0.5;
                xmov =strafeSpeed+largeStrafeCorrection;
                rot = 0;
                lost_line_timer = 100;              // strafe to the forward/right
            }
            else
            {
               ymov = speed;
               xmov = 0;                            // move right
               rot = - large_correction;
               lost_line_timer = 100;
            }
        }
        else if((left) && (!mid) && (right)) {  // middle sensor on the line
            System.out.println("Moving forward");
            if (strafeToTheEnd){
                ymov = speed*.25;
                xmov = strafeSpeed;
                rot = 0;
            }
            else {
           ymov = speed;
           xmov = 0;                            // move forward
           rot = 0;
           lost_line_timer = 100;
            }
        }
        else if((left) && (!mid) && (!right)) { // middle and right sensors on the line
            System.out.println("Moving slightly to the right");

            if ( strafeToTheEnd )
            {
                ymov = speed * 0.5;
                xmov = strafeSpeed+smallStrafeCorrection;
                rot = 0;
                lost_line_timer = 100;
            }
            else
            {
               ymov = speed;
               xmov = 0;                            // move forward and slightly right
               rot = - small_correction;
               lost_line_timer = 100;
            }
        }
        else if((!left) && (mid) && (right)) {  // left sensor on the line
            System.out.println("Moving to the left");
            if ( strafeToTheEnd )
            {
                ymov = speed * 0.5;
                xmov = strafeSpeed-largeStrafeCorrection;
                rot = 0;
                lost_line_timer = 100;
            }
            else
            {
               ymov = speed;
               xmov = 0;                            // move left
               rot = large_correction;
               lost_line_timer = 100;
            }
        }
        else if ((!left) && (mid) && (!right)) {// right and left sensors on the line (split)
          System.out.println("Found a split");
          if (expectY){
               System.out.println("Found a split");
              yIsDetected = true;
          }
 else {
              System.out.println("Found a split before a Y, ?!?!?");
 }
        }
        else if ((!left) && (!mid) && (right)) {// left and right sensors on the line
            System.out.println("Moving slightly to the left");
            if ( strafeToTheEnd )
            {
                ymov = speed * 0.5;
                xmov = strafeSpeed-smallStrafeCorrection;
                rot = 0;
                lost_line_timer = 100;
            }
            else
            {
               ymov = speed;
               xmov = 0;                            // move forward and slightly left
               rot = small_correction;
               lost_line_timer = 100;
            }
        }
        else if((!left) && (!mid) && (!right)) {   // end of line (all three tripped)
            System.out.println("End of line detected, could be split though");
           // if (!all3){
           //     all3 = true;
           // }
           // else{

            //TODO here change what the robot does based on dipSwitch
            //if a straight line, just do inPosition stuff
            //if expecting a Y, strafe

            if (dipMode == RIGHT_STRAIGHT || dipMode == LEFT_STRAIGHT){

                inPosition = true;
                    System.out.println("Stopping!");
                    mecanumDrive.stop();//TODO UNTESTED, ideally will stop the robot faster when it hits the end
                    ymov = 0;
                    xmov = 0;                            // stop
                    rot = 0;
                
            }
            else{


                if (yIsDetected){
                   inPosition = true;
                    System.out.println("Stopping!");
                    mecanumDrive.stop();//TODO UNTESTED, ideally will stop the robot faster when it hits the end
                    ymov = 0;
                    xmov = 0;                            // stop
                    rot = 0;
                }
                else {
                   expectY = true;
                }

            }
                 
              
            //}
        }

        if (yIsDetected){
            /*
            ymov = speed * 0.4;
            xmov = 0;
            rot = 0.3;


            System.out.println("Y detected, hunting!");
            */

            System.out.println("Y detected, enabling strafe mode");

            strafeToTheEnd = true;
            //yIsDetected = false;



           // ymov = speed * 0.5;
            
            rot = 0;
            

            /*
            if((left) && (!mid) && (right)) {  // middle sensor on the line
                System.out.println("Found a line!  Going back to normal");
                stopCondition = true;
                yIsDetected = false;
            }
             *
             */



            
        }
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

/*    public void printGyro(){

        if (lineTracker.activeSensor().equals("none")){
            mecanumDrive.drive(0,.04);
        }
        else {
            if (lineTracker.activeSensor().equals("middle")){
                mecanumDrive.drive(0,.04);
            }
        }

            angle = gyro.pidGet();
            System.out.println(angle%(360));

    }*/
    
    
}
