/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.launcher;

import edu.wpi.first.wpilibj.Timer;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class ResetWithTimeout extends CommandBase {
    private static final double TIME_OUT = 0.5;
    private Timer timer;

    public ResetWithTimeout() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(launcher);
        timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!launcher.isEngagedForLoad()) {
            launcher.engageForLoad();
        }
        launcher.startWindingLauncher();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean switchHit = launcher.isPunterLimitReached();
        boolean timedOut  = timer.get() > TIME_OUT;
        if (switchHit || timedOut) {
            launcher.stopWindingLauncher();
            CommandBase.updateDriverStationLCD(2, 1, "Hit limit switch or timed out");
        }
        return switchHit || timedOut;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}