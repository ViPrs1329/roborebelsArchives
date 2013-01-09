package roborebels.example.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import roborebels.example.robot.OI;
import roborebels.example.subsystems.Arm;
import roborebels.example.subsystems.DriveTrain;
import roborebels.example.subsystems.Grabber;
import roborebels.example.subsystems.Shooter;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use
 * CommandBase.exampleSubsystem
 *
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
// Create a single static instance of all of your subsystems
    public static DriveTrain drivetrain = new DriveTrain();
    public static Shooter shooter = new Shooter();
    public static Grabber grabber = new Grabber();
    public static Arm arm = new Arm();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(drivetrain);
        SmartDashboard.putData(shooter);
        SmartDashboard.putData(grabber);
        SmartDashboard.putData(arm);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
}
