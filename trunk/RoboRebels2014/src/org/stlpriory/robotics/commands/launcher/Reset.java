/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.launcher;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 * @author admin
 */
public class Reset extends CommandBase {

    public Reset() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(launcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (tank.isCompressorStarted()) {
            tank.stopCompressor();
        }
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
        if (switchHit) {
            launcher.stopWindingLauncher();
            CommandBase.updateDriverStationLCD(2, 1, "Hit limit switch");
        }
        return switchHit;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}