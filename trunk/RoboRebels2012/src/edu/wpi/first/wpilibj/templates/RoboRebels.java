/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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
 * 10 R Stick Click
 * 
 * Axis
 * 
 *  ´1: Left Stick X Axis
        -Left:Negative ; Right: Positive
    ´2: Left Stick Y Axis
        -Up: Negative ; Down: Positive
    ´3: Triggers
        -Left: Positive ; Right: Negative
    ´4: Right Stick X Axis
        -Left: Negative ; Right: Positive
    ´5: Right Stick Y Axis
        -Up: Negative ; Down: Positive
    ´6: Directional Pad (Not recommended, buggy)

 * 
 */

/*
 * 


 * NOTES:
 *
 *
 * - I have decided to clean up our code base a bit. - Derek W.
 * 
 * 
 * TODO:
 * 
 * - Migrate code out of RoboRebels.java into their respective classes
 * 
 * - All classees that depend on a Joystick should be passed joystick object(s)
 *   and handled within their classes
 * 
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.PWM;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    // Declare a variable to use to access the driver station object
    DriverStation       m_ds;                   // driver station object
    static DriverStationLCD    m_dsLCD;                // driver station LCD object
    Joystick            m_rightStick;           // joystick 1 (arcade stick or right tank stick)
    Joystick            m_leftStick;            // joystick 2 (tank left stick)
    Joystick            m_xboxStick;
    PWM                 currentPWM;
    RRDrive             drive;
    RRShooter           shooter;
    RRGatherer          gatherer;
    RRBridgeArm         arm;
    ADXL345_I2C         accel;
    RobotDrive          m_robotDrive;
    RRTracker           tracker;
    RRBallSensor        sensor;
    RRDIPSwitch         dipSwitch;
    RRButtonMap         buttonMap;
    RRAutonomous        autonomous;
    
    //RRTracker tracker = new RRTracker();   // New objects shouldn't be created outside of a method.
    
    double              lastZValue;                         // last Z value for the dial on the joystick
    double              autonomousStartTime;    // holds the start time for autonomous mode
    double              robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object
    boolean             tankDrive = false;
    
    // PWM Channel constants
    final static int    LEFT_DRIVE_CHANNEL = 1;
    final static int    RIGHT_DRIVE_CHANNEL = 2;
    final static int    SHOOTER_CHANNEL = 3;
    final static int    TILT_CHANNEL = 7;
    final static int    LAZY_SUSAN_CHANNEL = 8;
    final static int    LOADER_CHANNEL = 5;
    final static int    SPINNER_CHANNEL = 4;
    final static int    BRIDGE_ARM_CHANNEL = 6;
    
    // Digital I/O constants
    final static int    BOTTOM_BALL_SENSOR_CHANNEL = 1;
    final static int    MIDDLE_BALL_SENSOR_CHANNEL = 2;
    final static int    TOP_BALL_SENSOR_CHANNEL = 3;
    final static int    TILT_LIMIT_SWITCH_CHANNEL = 4;
    
    
    static final int    NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean      disabledStateBroadcasted = false;
    static boolean      teleopStateBroadcasted = false;
    static boolean      autonomousStateBroadcasted = false;
    
    int                 pwmTest = 0;
    boolean             btnPressed = false;
    double              launcher_speed = 0.0;
    boolean             launcher_button_pressed = false;
    
    final static int    CLOSE_LEFT = -1;
    final static int    CLOSE_RIGHT = 1;
    final static int    LEFT = -2;
    final static int    RIGHT = -2;
    final static int    FAR_LEFT = -3;
    final static int    FAR_RIGHT = 3;
    final static int    LOCK = 0;
    final static int    UP = 1;
    final static int    DOWN = -1;
    final static int    FASTER = 1;
    final static int    SLOWER = -1;
    final static int    HOLD = -3; 
    final static int    MIN_TILT_ANGLE = 46; 
    
    final static int    AT_RIGHT_LIMIT = 1;
    final static int    AT_LEFT_LIMIT = -1;
    final static int    OK = 0;
    
    static double       angle_position = 0.0;     // Initial position of LS.
    static double       time_last_update = 0;     // Timestamp of last position update       
    
    final static int    PIXEL_ACCURACY = 16;      // Used by RRTRacker to determne when Locked.
    final static int    ANGLE_ACCURACY = 6;       // Used by RRTRacker to determne when Locked.
    
    final static int    LOWEST_TARGET  = 0;     // Lowest basket target
    //    final static int    MIDDLE = 1;     // Middle basket target
    final static int    HIGHEST_TARGET = 2;    // Highest basket target
    final static int    LEFT_TARGET = 3;       // Left Middle target
    final static int    RIGHT_TARGET = 4;      // Right Middle target
    final static int    AUTO_TARGET = 5;       // trackTarget chooses target automatically
    
    static boolean      going_for_highest = false;   // Don't change this as we calibrated this this set to false!!
    
    static int          target_azimuth = HOLD;  // -1 if target is to left, 0 if on target, 1 if target is the right
    static int          target_elevation = HOLD;  // elevation direction of target:  UP, DOWN, LOCK
    static int          target_muzzle_velocity = HOLD; //muzzle velocity in meters per second
    
    static double       muzzle_velocity = 7.5;  // Actual muzzle velocity 8.5 meters per second

    final static int    NUMBER_OF_PREVIOUS = 10;    // Moving average values
    static double       previous_angles[] = new double [NUMBER_OF_PREVIOUS];   
    static int          curent_angle_index = 0;
    static double       current_angle_sum = 0;
                                                  
    static boolean      azimuth_lock = false;  //  azimuth (left/right) target lock acquired
    static boolean      elevation_lock = false; // elevation (up/down) target lock acquired
    static boolean      muzzle_velocity_lock = false;  // muzzle velocity is correct
    
    static  boolean     isFinishedShooting = false;  // True when a ball has just been shot
    static  boolean     isShooting = false;         // True when ball is in process of being shot
    static  boolean     delay_between_balls = false;  // True when waiting between shooting balls
    static  boolean     delay_after_two_balls = false;
    static  boolean     shot_first_ball = false;
    static  boolean     shot_second_ball = false;
//    final static double TICKS_FOR_3_SECONDS = 3.0;  // Used for delay between shots
    static  boolean     driving_to_bridge = false;
    static  boolean     no_balls_shot = true;
    static  boolean     second_ball_started_shoot = false;
    static  boolean     autonomous_complete = false;
    static  boolean     autonomous_mode_tracking = false;
    static  boolean     autonomous_tracking_failed = false;    
    static  boolean     shooter_motor_running = false;
    
    static  boolean     save_camera_image_file = true;
    
    static  double      time_started_waiting;           // Time variables
    static  double      time_started_shooting;
    static  double      time_started_shooter_motor;
    static  double      time_started_tracking;
    static  double      time_started_driving;
    static  double      time_after_shooting;
    static  double      time_delivered_ball;
    
    static  boolean      remove_small_objects_from_image = false;    // Set true to try adding back in image processing step
    
      // During match setup, save image file to set image thresholds

    final   static  double      MAX_TRACKING_TIME = 3.0;    // Time before tracking is given up if no lock obtained
    
    final   static  double      DELAY_AT_START_OF_AUTON = 3.0;  // Delay set by DIP switch in shooting
    final   static  double      MAX_SHOOTING_TIME = 9.0;  // Total time for shooter to give ball to basket
    final   static  double      DELAY_BETWEEN_SHOTS = 3.0;  // Used for delay between shots in autonomous
    final   static  double      DRIVE_TIME_TO_BRIDGE = 2.0; // Drive to bridge for 2 seconds
    
    final   static  double      SHOOTER_SPINUP_TIME = 1.0;  // Time taken for shooter to get up to speed before we send ball
    final   static  double      SHOOTER_SPINDOWN_TIME = 2.0;  // Time to wait for motor to spin down
    
    static  boolean     autonomous_mode = true;
    
    static  boolean     troubleshooting = false;  // true;
    
    static double       tilt_angle = 90;        // tilt angle (elevation)

    /*
     *          (\_/)
     *          (O.0)
     *           =o=
     *         (    ) <--- bunny ;)
     *          (  )
     *
     */


    /**
     * Constructor
     */
    public void RoboRebels() {
        System.out.println("RoboRebels()");

    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        System.out.println("robotInit()");
        
        m_dsLCD = DriverStationLCD.getInstance();

        m_leftStick     = new Joystick(1);
        m_rightStick    = new Joystick(2);
        m_xboxStick     = new Joystick(3);
        System.out.println("Joysticks set");

        buttonMap = new RRButtonMap(m_leftStick, m_rightStick, m_xboxStick);
        buttonMap.setControllers();
        System.out.println("Button map");   
        
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        System.out.println("accel");
        
        drive = new RRDrive(2, 1);
        System.out.println("Drive");
        
        gatherer = new RRGatherer(SPINNER_CHANNEL, LOADER_CHANNEL, BOTTOM_BALL_SENSOR_CHANNEL, MIDDLE_BALL_SENSOR_CHANNEL, TOP_BALL_SENSOR_CHANNEL);
        System.out.println("Gatherer");
        
        
        dipSwitch = new RRDIPSwitch(7, 10);  // These are the values from last year.
        
        tracker = new RRTracker(accel, dipSwitch);
        System.out.println("Tracker");
        
        arm = new RRBridgeArm(BRIDGE_ARM_CHANNEL, tracker);
        System.out.println("Arm");
        
        sensor = new RRBallSensor();
        sensor.ballSensorInit(5, 4); // These are the values from last year.
        
        shooter = new RRShooter(SHOOTER_CHANNEL, LAZY_SUSAN_CHANNEL, TILT_CHANNEL, 
                                TILT_LIMIT_SWITCH_CHANNEL, tracker, sensor, dipSwitch, gatherer);
        
        tracker.setShooter(shooter);
        
        autonomous = new RRAutonomous(dipSwitch, tracker, shooter, sensor, gatherer);
        
        isFinishedShooting = true;  
        
        time_last_update = Timer.getFPGATimestamp();
         
        System.out.println("Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
        
        time_last_update = Timer.getFPGATimestamp();
        azimuth_lock = false;  //  azimuth (left/right) target lock acquired
        elevation_lock = false; // elevation (up/down) target lock acquired
        muzzle_velocity_lock = false;
        angle_position = 0.0;
        
        shooter.reset();
    }

    public void autonomousInit() {
        System.out.println("autonomousInit()");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        autonomousStartTime = Timer.getFPGATimestamp();
        
        isFinishedShooting = false;  
        isShooting = false;  
        delay_between_balls = false;
        delay_after_two_balls = false;
        shot_first_ball = false;
        shot_second_ball = false;
        driving_to_bridge = false;
        shooter_motor_running = false;
        
        autonomous_mode = true;
        autonomous_complete = false;
        autonomous_tracking_failed = false;
        no_balls_shot = true;
        second_ball_started_shoot = false;
        
        for (int i = 0; i < NUMBER_OF_PREVIOUS; i++)    // initialize Moving Average values
        {
            previous_angles[i] = 90.0;
        }
        current_angle_sum = 90.0 * NUMBER_OF_PREVIOUS;  // initialize MA sum
        
        autonomous.auton_init();
        
  
    }

    public void teleopInit() {
        System.out.println("teleopInit()");

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;
        tankDrive = false;
        autonomous_mode = false;

        // Need to fix this!!
        
        gatherer.stop();
        
        isFinishedShooting = false;  
        isShooting = false; 
        // shooter_motor_running = false;
        
        /* Drive station code */
        //m_ds = DriverStation.getInstance();
        //m_dsLCD = DriverStationLCD.getInstance();

    }

    /**
     * This function is called periodically during autonomous
     *
     * Notes:
     *
     *
     *
     */
    public void autonomousPeriodic() {
//        tracker.trackTarget(RoboRebels.AUTO_TARGET);
        
          autonomous.auton_periodic();      
        
        //System.out.println(getAngle());
    }

    /**
     * This function is called periodically during operator control
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic() 
    {
        
       
        if ( teleopStateBroadcasted == true )
        {
            System.out.println( "Teleop State" );
            teleopStateBroadcasted = false;
        }
        
        if ( tankDrive == true ) {
            drive.drive(true);
            //System.out.println("Tank Drive");
        }
        else{
            drive.drive(false);
            //System.out.println("Arcade Drive");
        }
        
      tracker.trackTarget(RoboRebels.AUTO_TARGET);   
      shooter.shoot();
      gatherer.gather();
      arm.arm();
    }

    /**
     * This function is called periodically during the disabled state
     *
     * What it needs to do:
     *
     *
     */
    public void disabledPeriodic() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the autonomous state
     *
     */
   
    
    public void autonomousContinuous() {
        //nothing right now
    }

    /**
     * The VM will try to call this function as often as possible during the teleop state
     *
     */
    public void teleopContinuous() {
        
        

    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous() {
    }

    /*
     * This method checks buttons and sets states accordingly
     * 
     * NOTE:  Input checking should be put into their respective classes.  For 
     * reference, see RRShooter.
     */
    public void checkButtons() {
        //System.out.println( "checkButtons()" );
        /*
        if (m_rightStick.getZ() <= 0)
        {    // Logitech Attack3 has z-polarity reversed; up is negative
            // arcade mode
            tankDrive = false;
        }
        else
        {
            // tank drive
            tankDrive = true;
        }
        * */
        /*
        if (m_leftStick.getRawButton(1)) {
            launcher.set(launcher_speed);
            if (!launcher_button_pressed)  // if the shooter button is pressed then this adds .2 to its speed
            
           {
                launcher_speed += -0.2;     
                launcher_button_pressed = true;
                if (launcher_speed < -1.0)  // once it gets past speed of -1
                {
                    launcher_speed  = 0.0; // it turns itself off
                }
     
            System.out.println("Increasing launcher_speed to "+ launcher_speed);
            }
        }
        else
            launcher_button_pressed = false;
      // else {
      //      launcher.set(0);
      //      System.out.println("Launch cim off");
      //  }
        
        if(m_leftStick.getRawButton(3)) { //when button 3 is presssssssed the up/down aiming increases
            elevation.set(.3);
        System.out.println("elevation increase");
        }
        else if (m_leftStick.getRawButton(2)) { //when button 2 is pressed the up/down aiming decreases
            elevation.set(-.3);
        System.out.println("elevation decrease");
        }
        else {
            elevation.set(0);
        System.out.println("elevation standstill");//otherwise it stops moving
        }
        

        if (m_leftStick.getRawButton(4)) { //when button 4, move susan left
            lazySusan.set(-.3);
            System.out.println("Lazysusan Left");
         }
       else if (m_leftStick.getRawButton(5)) {// when but 5, move suzie right
            lazySusan.set(.3);
           System.out.println("Lazysusan Right");
          }
        else { //otherwise dont do anything
             lazySusan.set(0);
            System.out.println("Lazysusan STOPPPPP!!!!");
            }

         if (m_leftStick.getRawButton(6)) { //if 6, suck ball in
            loader.set(-.75);
            System.out.println("loader up =D");
         }
       else if (m_leftStick.getRawButton(7)) { //if 7, drop (or de-suck) ball
            loader.set(.75);
           System.out.println("loader down :?");
          }
        else {
             loader.set(0); //otherwise, dont move(aka stoop) at all
            System.out.println("loader STOOP");
            }
            * 
            */

/*
        if (m_leftStick.getRawButton(6) && btnPressed == false) {
            btnPressed = true;
            pwmTest++;

            if (currentPWM != null)
                currentPWM.setRaw(0);

            if (pwmTest == 8)
                pwmTest = 1;

            System.out.println("PWM #" + pwmTest);

            currentPWM = new PWM(pwmTest);
            currentPWM.setRaw(128);
            System.out.println("Pwm Test done with channel");
        }

        if (!m_leftStick.getRawButton(6) && btnPressed == true) {
            btnPressed = false;
        }
 *
 */



        /*
        System.out.println( "LX: " + m_xboxStick.getRawAxis(1));
        System.out.flush();
        System.out.println( "LY: " + m_xboxStick.getRawAxis(2));
        System.out.flush();
        System.out.println( "RX: " + m_xboxStick.getRawAxis(4));
        System.out.flush();
        System.out.println( "RY: " + m_xboxStick.getRawAxis(5));
        System.out.flush();
         */
    }
    
    

    public static void printLCD(int lineNum, String value) {
        DriverStationLCD.Line line = null;
        switch (lineNum) {
            case 2:
                line = DriverStationLCD.Line.kUser2;
                break;
            case 3:
                line = DriverStationLCD.Line.kUser3;
                break;
            case 4:
                line = DriverStationLCD.Line.kUser4;
                break;
            case 5:
                line = DriverStationLCD.Line.kUser5;
                break;
            case 6:
                line = DriverStationLCD.Line.kMain6;
                break;
        }
        m_dsLCD.println(line, 1, value);
        m_dsLCD.updateLCD();
    }
 /*   
    public double getAngle() {
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
        System.out.println("X Accel: " + axes.XAxis);
        System.out.println("Y Accel: " + axes.YAxis);
        System.out.println("Z Accel: " + axes.ZAxis); 
        double yAxis = Math.min(1, axes.YAxis);
        yAxis = Math.max(-1, yAxis);
        return 180.0 * MathUtils.asin(yAxis) / 3.14159;
    }
    *
    */
}
