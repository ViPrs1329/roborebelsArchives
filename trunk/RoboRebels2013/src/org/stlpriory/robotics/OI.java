
package org.stlpriory.robotics;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import org.stlpriory.robotics.commands.shooter.LoadDisc;
import org.stlpriory.robotics.commands.shooter.ResetLoadDisc;
import org.stlpriory.robotics.commands.shooter.Shoot;
import org.stlpriory.robotics.commands.shooter.StartShooting;
import org.stlpriory.robotics.commands.shooter.StopShooting;
import org.stlpriory.robotics.misc.Debug;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());

    /*
     * The bottons on the XBox controller follow this mapping
     * 1:  A
     * 2:  B
     * 3:  X
     * 4:  Y
     * 5:  Left Bumper
     * 6:  Right Bumper
     * 7:  Back
     * 8:  Start
     * 9:  Left thumbstick
     * 10: Right thumbstick
     *
     * The axis on the controller follow this mapping
     * (all output is between -1 to 1)
     * 1:  Left stick X axis  (left:negative, right:positve)
     * 2:  Left stick Y axis  (up:negative, down:positive)
     * 3:  Triggers           (left:positive, right:negative)
     * 4:  Right stick X axis (left:negative, right:positive)
     * 5:  Right stick Y axis (up:negative, down:positive)
     * 6:  Directional pad
     */

    private static OI instance = null;
    private Joystick joystick;
    private JoystickButton loaderButton;
    private JoystickButton shooterStartButton;
    private JoystickButton shooterStopButton;

    public OI() {
        Debug.println("[OI] Instantiating ...");

        Debug.println("[OI] Initializing gamepad to Drivers station USB port " + RobotMap.DRIVER_STATION_USB_PORT1);
        this.joystick = new Joystick(RobotMap.DRIVER_STATION_USB_PORT1);

        Debug.println("[OI] Initializing gamepad to reset disc loader when the left bumper pressed");
        this.loaderButton = new JoystickButton(this.joystick, 5);
        this.loaderButton.whenPressed(new ResetLoadDisc());

        Debug.println("[OI] Initializing gamepad to load a disc when the right bumper pressed");
        this.loaderButton = new JoystickButton(this.joystick, 6);
        this.loaderButton.whenPressed(new LoadDisc());

        Debug.println("[OI] Initializing gamepad to start the shooter motor when the start button is pressed");
        this.shooterStartButton = new JoystickButton(this.joystick, 8);
        this.shooterStartButton.whenPressed(new StartShooting());

        Debug.println("[OI] Initializing gamepad to stop the shooter motor when the stop button is pressed");
        this.shooterStopButton = new JoystickButton(this.joystick, 7);
        this.shooterStopButton.whenPressed(new StopShooting());

        Debug.println("[OI] Initializing gamepad to execute the shooting sequence when the A button is pressed");
        this.shooterStopButton = new JoystickButton(this.joystick, 1);
        this.shooterStopButton.whenPressed(new Shoot());

        // SmartDashboard Buttons
//        SmartDashboard.putData("Autonomous Command", new Auton1());
//        SmartDashboard.putData("DriveInASquare", new DriveInASquare());
//        SmartDashboard.putData("DriveWithJoystick", new DriveWithJoystick());
//        SmartDashboard.putData("DriveWithGamepad", new DriveWithGamepad());
//        SmartDashboard.putData("LoadDisc", new LoadDisc());
//        SmartDashboard.putData("ShootDisc", new ShootDisc());

        // associate the DriveInSquare command group with the
        // trigger button on the right joystick. Whenever the
        // joystick is pressed, the robot will drive
        // in a square pattern. When the command is completed,
        // the default command for the DriveLine subsystem will
        // run - DriveViaJoysticks
 //       this.trigger.whenPressed(new DriveInASquare());

        Debug.println("[OI] Instantiation complete.");
    }

    public static OI getInstance() {
        if (instance == null) {
            instance = new OI();
        }
        return instance;
    }

    public Joystick getJoystick() {
        return this.joystick;
    }

}
