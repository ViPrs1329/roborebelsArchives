package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.stlpriory.robotics.OI;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.subsystems.Claw;
import org.stlpriory.robotics.subsystems.DriveTrain;
import org.stlpriory.robotics.subsystems.Launcher;
import org.stlpriory.robotics.subsystems.Sensors;
import org.stlpriory.robotics.subsystems.Tank;
import org.stlpriory.robotics.subsystems.Vision;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    // Create a single static instance of the Operator Interface
    public static OI oi;
    
    // Create a single static instance of all of your subsystems
    public static DriveTrain drivetrain = new DriveTrain();
    public static Launcher launcher = new Launcher();
    public static Claw claw = new Claw();
    public static Tank tank = new Tank();
    public static Vision vision = new Vision();
    public static Sensors sensors = new Sensors();

    public static void init() {
        Debug.println("[CommandBase.init()] Initializing...");
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = OI.getInstance();

        // Show what command your subsystem is running on the SmartDashboard
 //       SmartDashboard.putData(drivetrain);

        Debug.println("[CommandBase.init()] Done.");
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
