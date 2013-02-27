/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class StartShooting extends CommandBase {

    public StartShooting() {
        super("StartShooting");
        requires(shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        Debug.print("[" + getName() + "] initialize");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       shooter.startShooter(Constants.SHOOTER_WHEEL_MOTOR_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
        Debug.print("[" + getName() + "] end");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
    }
}
