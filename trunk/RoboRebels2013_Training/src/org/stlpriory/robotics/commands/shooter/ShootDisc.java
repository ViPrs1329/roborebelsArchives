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

    private static final double DEFAULT_TIME_OUT = 1;
    private double timeout;

    public ShootDisc() {
        this(DEFAULT_TIME_OUT);
    }

    public ShootDisc(double timeout) {
        super("ShootDisc");
        requires(shooter);
        this.timeout = timeout;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + getName() + "] initialize");
        Debug.print("\tTimeout: " + timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       shooter.startShooter(Constants.SHOOTER_WHEEL_MOTOR_SPEED);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        return false;
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
        Debug.print("[" + getName() + "] end");
        shooter.stopShooter();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
    }
}
