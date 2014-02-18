package org.stlpriory.robotics.commands;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.command.Command;
import org.stlpriory.robotics.OI;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.subsystems.CANDriveTrain;
import org.stlpriory.robotics.subsystems.Claw;
import org.stlpriory.robotics.subsystems.Launcher;
import org.stlpriory.robotics.subsystems.NoOpDriveTrain;
import org.stlpriory.robotics.subsystems.Sensors;
import org.stlpriory.robotics.subsystems.Tank;
import org.stlpriory.robotics.subsystems.Vision;

/**
 * The base for all commands. All atomic commands should subclass CommandBase. CommandBase stores creates and stores
 * each control system. To access a subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 *
 * @author Author
 */
public abstract class CommandBase extends Command {

    // Create a single static instance of the Operator Interface
    public static OI oi;
    
    // Create a single static instance of all of your subsystems
    //p;blic static DriveTrain drivetrain = new DriveTrain();
    //public static CANDriveTrain drivetrain = new CANDriveTrain();
    public static NoOpDriveTrain drivetrain = new NoOpDriveTrain();
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

    /**
     * @param lineNumber The line on the LCD to print to (range of values is 1-6).
     * @param startingColumn The column to start printing to. This is a 1-based number.
     * @param the text to print
     */
    public static void updateDriverStationLCD(int lineNumber, int startingColumn, String text) {
        switch (lineNumber) {
            case 1:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, startingColumn, text);
                break;
            case 2:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser2, startingColumn, text);
                break;
            case 3:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser3, startingColumn, text);
                break;
            case 4:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser4, startingColumn, text);
                break;
            case 5:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser5, startingColumn, text);
                break;
            case 6:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser6, startingColumn, text);
                break;
            default:
                DriverStationLCD.getInstance().println(DriverStationLCD.Line.kUser1, startingColumn, text);
                break;
        }
        DriverStationLCD.getInstance().updateLCD();
    }
}
