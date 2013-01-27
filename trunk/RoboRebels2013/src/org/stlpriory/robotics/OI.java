
package org.stlpriory.robotics;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.stlpriory.robotics.commands.autonomous.Auton1;
import org.stlpriory.robotics.commands.drivetrain.DriveInASquare;
import org.stlpriory.robotics.commands.drivetrain.DriveWithJoystick;
import org.stlpriory.robotics.commands.shooter.LoadDisc;
import org.stlpriory.robotics.commands.shooter.ShootDisc;
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

    private static OI instance = null;
    private Joystick joystick;
    private JoystickButton shooterButton;

    public OI() {
        Debug.println("[OI] Instantiating ...");

        Debug.println("[OI] Initializing Joystick to port " + RobotMap.JOYSTICK1);
        this.joystick = new Joystick(RobotMap.JOYSTICK1);

        Debug.println("[OI] Initializing Joystick button to load disc when trigger is pressed ");
        this.shooterButton = new JoystickButton(this.joystick, Joystick.ButtonType.kTrigger.value);
        this.shooterButton.whenPressed(new LoadDisc(0.5));

        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Command", new Auton1());
        SmartDashboard.putData("DriveInASquare", new DriveInASquare());
        SmartDashboard.putData("DriveWithJoystick", new DriveWithJoystick());
        SmartDashboard.putData("LoadDisc", new LoadDisc());
        SmartDashboard.putData("ShootDisc", new ShootDisc());

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
