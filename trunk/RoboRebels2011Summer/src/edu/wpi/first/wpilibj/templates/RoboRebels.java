
/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
// roy test
/*
 * Joystick (xbox) button map
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Stick Click
 */
/*
   y4
 x3  b2
   a1


 */



package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare custom object vars
    RRMecanumDrive                  mecanumDrive;
    RRAuton                         auton;

    // Declare objects needed for the robot that might be used
    // in more than one location
    
    Joystick                        m_xboxStick;

    // Declare a variable to use to access the driver station object
    DriverStation       m_ds;                   // driver station object
    DriverStationLCD    m_dsLCD;                // driver station LCD object

    // Misc variable declarations 
    static final int    NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean      disabledStateBroadcasted = false;
    static boolean      teleopStateBroadcasted = false;
    static boolean      autonomousStateBroadcasted = false;


    /**
     * Constructor
     * 
     * NOTE: This should not be modified!
     * 
     */
    
    public void RoboRebels()
    {
        System.out.println( "RoboRebels()" );
      
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     * 
     * NOTE:  You should only modify one line code in this method!
     * 
     */
    
    public void robotInit()
    {
        System.out.println( "robotInit()" );

        // front left, rear left, front right, rear right

        /*
         *
         *
         *     ^
         *     |
         * +-------+
         * |J2   J4|
         *L|J1   J3|R
         * |       |
         * |       |
         * +-------+
         *
         * Match jag numbers with the dig i/o ports
         *
         */

        //                              FL, FR, BL, BR

        
        // Initialize tangible objects here
        m_xboxStick = new Joystick(1);
        mecanumDrive = new RRMecanumDrive(3, 4, 1,2);
        mecanumDrive.assignJoystick(m_xboxStick);
        
        
        // --------------------------
        // Only edit the line below with the RRAuton class 
        // that you will be testing
        
        auton = new RRAutonSimple(mecanumDrive);
        
        // --------------------------

        /* Drive station code */
        m_ds = DriverStation.getInstance();
        m_dsLCD = DriverStationLCD.getInstance();

        System.out.println( "Robot Ready" );
    }

    /**
     * Put initialization code for the disabled state in this method.
     * 
     * NOTE: This should not be modified!
     * 
     */
    
    public void disabledInit()
    {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;

    }

    /**
     * Put initialization code for the autonomous state in this method.
     * 
     * NOTE: This should not be modified!
     * 
     */
    
    public void autonomousInit()
    {
        System.out.println( "autonomousInit()" );

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // reset the autonomous code
        auton.reset();

        // Initialize the autonomous code
        auton.init();
    }

    /**
     * Put initialization code for the teleop state in this method.
     * 
     * NOTE: This should not be modified!
     * 
     */
    
    public void teleopInit()
    {
        System.out.println( "teleopInit()" );

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;

        

    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *  - This class is used for processing code which does not
     *    need to be fast, but predictable.  
     * 
     * 
     * NOTE: This should not be modified!
     *
     */
    
    public void autonomousPeriodic()
    {
        // The code below prevents a spam of "Autonomous State" messages on the console
        if ( autonomousStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            autonomousStateBroadcasted = false;
        }

        
    }

    /**
     * This function is called periodically during operator control
     *
     * ---
     * This is the most important method in this class
     * ---
     * 
     * 
     * NOTE: This should not be modified!
     * 
     */

    public void teleopPeriodic()
    {
        // The code below prevents a spam of "Teleop State" messages on the console
        if ( teleopStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            teleopStateBroadcasted = false;
        }

        // This method needs to be called often to grab input from the 
        // joystick and drive the mecanum system
        mecanumDrive.drive();
    }

    /**
     * This function is called periodically during the disabled state
     *
     * NOTE: This should not be modified!
     *
     */
    
    public void disabledPeriodic()
    {
        // The code below prevents a spam of "Disabled State" messages on the console
        if ( disabledStateBroadcasted == true )
        {
            System.out.println( "Disabled State" );
            disabledStateBroadcasted = false;
        }
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     * NOTE:  Autonomous drive code works very well in this method.
     * 
     * NOTE: This should not be modified!
     */

    public void autonomousContinuous()
    {
        // Continuously run the autonomous code
        auton.run();

        updateDSLCD();
    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     * 
     * NOTE: This should not be modified!
     *
     */

    public void teleopContinuous()
    {
         updateDSLCD();
    }

    /**
     * The VM will try to call this function as often as possible during the disabled state
     * 
     * NOTE: This should not be modified!
     * 
     */

    public void disabledContinuous()
    {
        
    }

    
    /*
     * Sends useful information to the LCD on the DriverStation
     * 
     * NOTE: This should not be modified!
     * 
     */

    public void updateDSLCD()
    {
        m_dsLCD.println(DriverStationLCD.Line.kUser2, 1, "DCM: "+ mecanumDrive.getControlModeName() + "      ");
        m_dsLCD.println(DriverStationLCD.Line.kUser3, 1, "En: " + auton.getCount() + "      ");
        m_dsLCD.println(DriverStationLCD.Line.kUser4, 1, "Tm: " + auton.getTime() + "      ");
        m_dsLCD.println(DriverStationLCD.Line.kUser5, 1, "Gy: " + auton.getAngle() + "      ");
        m_dsLCD.updateLCD();

    }
}
