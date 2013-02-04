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
public class ShootDisc extends CommandBase {

    private double timeout;

    public ShootDisc() {
        this(0.5);
    }

    public ShootDisc(double timeout) {
        requires(shooter);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(timeout);
        shooter.startEncoder();
        Debug.print("[" + getName() + "]");
        Debug.print("\tTimeout: " + timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       shooter.startShooter(Constants.SHOOTER_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
        //return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        shooter.stopEncoder();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
