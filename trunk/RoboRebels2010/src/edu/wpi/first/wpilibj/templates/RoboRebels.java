/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/


// test

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Watchdog;

/**
 * This "BuiltinDefaultCode" provides the "default code" functionality as used in the "Benchtop Test."
 *
 * The BuiltinDefaultCode extends the IterativeRobot base class to provide the "default code"
 * functionality to confirm the operation and usage of the core control system components, as
 * used in the "Benchtop Test" described in Chapter 2 of the 2009 FRC Control System Manual.
 *
 * This program provides features in the Disabled, Autonomous, and Teleop modes as described
 * in the benchtop test directions, including "once-a-second" debugging printouts when disabled,
 * a "KITT light show" on the solenoid lights when in autonomous, and elementary driving
 * capabilities and "button mapping" of joysticks when teleoperated.  This demonstration
 * program also shows the use of the user watchdog timer.
 *
 * This demonstration is not intended to serve as a "starting template" for development of
 * robot code for a team, as there are better templates and examples created specifically
 * for that purpose.  However, teams may find the techniques used in this program to be
 * interesting possibilities for use in their own robot code.
 *
 * The details of the behavior provided by this demonstration are summarized below:
 *
 * Disabled Mode:
 * - Once per second, print (on the console) the number of seconds the robot has been disabled.
 *
 * Autonomous Mode:
 * - Flash the solenoid lights like KITT in Knight Rider
 * - Example code (commented out by default) to drive forward at half-speed for 2 seconds
 *
 * Teleop Mode:
 * - Select between two different drive options depending upon Z-location of Joystick1
 * - When "Z-Up" (on Joystick1) provide "arcade drive" on Joystick1
 * - When "Z-Down" (on Joystick1) provide "tank drive" on Joystick1 and Joystick2
 * - Use Joystick buttons (on Joystick1 or Joystick2) to display the button number in binary on
 *   the solenoid LEDs (Note that this feature can be used to easily "map out" the buttons on a
 *   Joystick.  Note also that if multiple buttons are pressed simultaneously, a "15" is displayed
 *   on the solenoid LEDs to indicate that multiple buttons are pressed.)
 *
 * This code assumes the following connections:
 * - Driver Station:
 *   - USB 1 - The "right" joystick.  Used for either "arcade drive" or "right" stick for tank drive
 *   - USB 2 - The "left" joystick.  Used as the "left" stick for tank drive
 *
 * - Robot:
 *   - Digital Sidecar 1:
 *     - PWM 1/3 - Connected to "left" drive motor(s)
 *     - PWM 2/4 - Connected to "right" drive motor(s)
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RoboRebels extends IterativeRobot {
	// Declare variable for the robot drive system
	RobotDrive m_robotDrive;		// robot will use PWM 1-4 for drive motors

	// Declare a variable to use to access the driver station object
	DriverStation m_ds;                     // driver station object
	int m_priorPacketNumber;                // keep track of the most recent packet number from the DS
	int m_dsPacketsReceivedInCurrentSecond;	// keep track of the ds packets received in the current second

	// Declare variables for the two joysticks being used
	Joystick m_rightStick;			// joystick 1 (arcade stick or right tank stick)
	Joystick m_leftStick;			// joystick 2 (tank left stick)

	
							// drive mode selection
	static final int UNINITIALIZED_DRIVE = 0;
	static final int ARCADE_DRIVE = 1;
	static final int TANK_DRIVE = 2;
	int m_driveMode;

        int m_autoPeriodicLoops = 0;

	

    /**
     * Constructor for this "BuiltinDefaultCode" Class.
     *
     * The constructor creates all of the objects used for the different inputs and outputs of
     * the robot.  Essentially, the constructor defines the input/output mapping for the robot,
     * providing named objects for each of the robot interfaces.
     */
    public RoboRebels() {
        System.out.println("BuiltinDefaultCode Constructor Started\n");

		// Create a robot using standard right/left robot drive on PWMS 1, 2, 3, and #4
		m_robotDrive = new RobotDrive(1, 3, 2, 4);

		// Acquire the Driver Station object
		m_ds = DriverStation.getInstance();
		m_priorPacketNumber = 0;
		m_dsPacketsReceivedInCurrentSecond = 0;

		// Define joysticks being used at USB port #1 and USB port #2 on the Drivers Station
		m_rightStick = new Joystick(1);
		m_leftStick = new Joystick(2);

		

		

		// Set drive mode to uninitialized
		m_driveMode = UNINITIALIZED_DRIVE;

		

		System.out.println("BuiltinDefaultCode Constructor Completed\n");
	}


	/********************************** Init Routines *************************************/

	public void robotInit() {
		// Actions which would be performed once (and only once) upon initialization of the
		// robot would be put here.

		System.out.println("RobotInit() completed.\n");
	}

	public void disabledInit() {
		
		
	}

	public void autonomousInit() {
		
	}

	public void teleopInit() {
		
	}

	/********************************** Periodic Routines *************************************/
	static int printSec = (int)((Timer.getUsClock() / 1000000.0) + 1.0);
	static final int startSec = (int)(Timer.getUsClock() / 1000000.0);

	public void disabledPeriodic()  {
		// feed the user watchdog at every period when disabled
		Watchdog.getInstance().feed();

		

		// while disabled, printout the duration of current disabled mode in seconds
		if ((Timer.getUsClock() / 1000000.0) > printSec) {
			// Move the cursor back to the previous line and clear it.
			//System.out.println("\x1b[1A\x1b[2K");
			System.out.println("Disabled seconds: " + (printSec - startSec) + "\r\n");
			printSec++;
		}
	}

	public void autonomousPeriodic() {
		// feed the user watchdog at every period when in autonomous
		Watchdog.getInstance().feed();

                if (m_autoPeriodicLoops == 1) {
			// When on the first periodic loop in autonomous mode, start driving forwards at half speed
			m_robotDrive.drive(0.5, 0.0);			// drive forwards at half speed
		}
		if (m_autoPeriodicLoops == (2 * GetLoopsPerSec())) {
			// After 2 seconds, stop the robot
			m_robotDrive.drive(0.0, 0.0);			// stop robot
		}
		m_autoPeriodicLoops++;
	}


	public void teleopPeriodic() {
            // feed the user watchdog at every period
            Watchdog.getInstance().feed();


            m_dsPacketsReceivedInCurrentSecond++;					// increment DS packets received



            m_robotDrive.tankDrive(m_leftStick, m_rightStick);	// drive with tank style
            if (m_driveMode != TANK_DRIVE) {
                // if newly entered tank drive, print out a message
                System.out.println("Tank Drive\n");
                m_driveMode = TANK_DRIVE;
            }


	}

	

	

        int GetLoopsPerSec() {
            return 10000;
        }

	


}
