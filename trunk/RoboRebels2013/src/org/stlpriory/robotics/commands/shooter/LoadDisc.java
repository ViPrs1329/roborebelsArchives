/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Constants;

/**
 *  Command use to load frisbee disc into shooter
 */
public class LoadDisc extends CommandBase {

    private double timeout;

    public LoadDisc() {
        this(0.5);
    }

    public LoadDisc(double timeout) {
        requires(shooter);
        this.timeout = timeout;
    }

    /**
     * Called just before this Command runs the first time
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + getName() + "]");
        Debug.print("\tTimeout: " + timeout);
        shooter.setAngle(Constants.MIN_LOADER_SERVO_ANGLE);
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    protected void execute() {
        shooter.loadDisc();
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     * @return finished state
     */
    protected boolean isFinished() {
        return isTimedOut();
    }

    /**
     * Called once after isFinished returns true
     */
    protected void end() {
        Debug.println("\t\tDONE");
        shooter.resetLoader();
    }

    /**
     * Called when another command which requires one or more of
     * the same subsystems is scheduled to run
     */
    protected void interrupted() {
        Debug.println("\t[interrupted] " + getName());
        shooter.resetLoader();
    }
}
