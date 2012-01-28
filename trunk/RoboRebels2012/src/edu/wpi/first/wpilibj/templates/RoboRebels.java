
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
 */
/*
y4
x3  b2
a1


 * NOTES:
 *
 * - Watchdog is no longer required, however, we may want to use it
 *   if we feel that need for a that bit of safety.
 *
 * - There is code within the RobotDrive class which can handle
 *   Mecanum wheels!  This will help us out a lot.
 *
 * - Just for the sake of cleanliness, I think that we should
 *   delete the following modules:  RRKicker, RRGRabber, RRPullup and RRSpinner
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {

    TrackerDashboard trackerDashboard = new TrackerDashboard();
    // Declare a variable to use to access the driver station object
    DriverStation m_ds;                   // driver station object
    DriverStationLCD m_dsLCD;                // driver station LCD object
    AxisCamera cam;                    // camera object
    CriteriaCollection cc;      // the criteria for doing the particle filter operation
    double autonomousStartTime;    // holds the start time for autonomous mode
    Joystick m_rightStick;		// joystick 1 (arcade stick or right tank stick)
    Joystick m_leftStick;		// joystick 2 (tank left stick)
    Joystick m_xboxStick;
    static final int NUM_JOYSTICK_BUTTONS = 16;  // how many joystick buttons exist?
    static boolean disabledStateBroadcasted = false;
    static boolean teleopStateBroadcasted = false;
    static boolean autonomousStateBroadcasted = false;
    double kScoreThreshold = .01;      // used in circle tracking code; default = 0.01
    double targetTolerance = 1.0;      // used for target tracking
    boolean foundTarget = false, // have we found the target yet?
            autoDrive = false;                  // are we in autonomous drive mode?
    double lastZValue;                         // last Z value for the dial on the joystick
    double robotDriveSensitivity = 0.25;       // sensitivity of the RobotDrive object
    boolean releasedPin = false;
    Ultrasonic ultraSonicSensor;
    double distanceInInches = -1;

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


        //Watchdog.getInstance().setExpiration(0.75);



        // Camera init code
        Timer.delay(5.0);
        cam = AxisCamera.getInstance();


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

        m_leftStick = new Joystick(2);
        m_rightStick = new Joystick(3);
        m_xboxStick = new Joystick(1);//TODO test, check if problem is solved

        int pingChannel = 0;
        int echoChannel = 0;
        try {
            ultraSonicSensor = new Ultrasonic(pingChannel, echoChannel);
        } catch (Exception e) {
            System.out.println("WARNING: UltraSonic Sensor not connected.");
        }

        cam = AxisCamera.getInstance();  // get an instance ofthe camera
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);

        System.out.println("Robot Ready");
    }

    public void disabledInit() {
        teleopStateBroadcasted = false;
        autonomousStateBroadcasted = false;
    }

    public void autonomousInit() {
        System.out.println("autonomousInit()");

        disabledStateBroadcasted = false;
        teleopStateBroadcasted = false;

        // Get the time that the autonomous mode starts
        //autonomousStartTime = Timer.getUsClock();
        autonomousStartTime = Timer.getFPGATimestamp();
    }

    public void teleopInit() {
        System.out.println("teleopInit()");

        disabledStateBroadcasted = false;
        autonomousStateBroadcasted = false;

//        m_rightStick = new Joystick(2);
//        m_leftStick = new Joystick(1);

        ultraSonicSensor.setEnabled(true);




        /* Drive station code */
        m_ds = DriverStation.getInstance();
        m_dsLCD = DriverStationLCD.getInstance();

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
       try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "10ft2.jpg"
             *
             */
            ColorImage image = cam.getImage();     // comment if using stored images
//
            try {
                image.write("/hey.jpg");
            } catch (Exception e) {
                System.out.println("error saving image");
            }
            System.out.println("WROTE IMAGE");

            BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                System.out.println("Particle: " + i + ":  Center of mass x: " + r.center_mass_x);
            }
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */

            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This function is called periodically during operator control
     *
     * ---------------------
     * This is the most important method in this class
     * ---------------------
     */
    public void teleopPeriodic() {
        //nothing right now
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
        updateDSLCD();
        if (ultraSonicSensor.isEnabled() && ultraSonicSensor.isRangeValid()) {
            distanceInInches = ultraSonicSensor.getRangeInches();
            System.out.println("Ultrasonic sensor:  distance (inches) = " + distanceInInches);
        }
    }

    /**
     * The VM will try to call this function as often as possible during the disbabled state
     */
    public void disabledContinuous() {
    }

    /*
     * This method checks buttons and sets states accordingly
     */
    public void checkButtons() {
        //System.out.println( "checkButtons()" );

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
}
