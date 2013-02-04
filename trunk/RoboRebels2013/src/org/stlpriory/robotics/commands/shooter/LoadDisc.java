/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Constants;

/**
 * Command use to load frisbee disc into shooter
 */
public class LoadDisc extends CommandBase {

    private static final double DEFAULT_TIME_OUT = 2;
    private double timeout;

    public LoadDisc() {
        this(DEFAULT_TIME_OUT);
    }

    public LoadDisc(double timeout) {
        super("LoadDisc");
        requires(shooter);
        this.timeout = timeout;
    }

    /**
     * Called just before this Command runs the first time
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + getName() + "] initialize");
        Debug.print("\tTimeout: " + timeout);
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    protected void execute() {
        shooter.loadDisc(Constants.LOADER_MOTOR_SPEED);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     *
     * @return finished state
     */
    protected boolean isFinished() {
        return isTimedOut();
    }

    /**
     * Called once after isFinished returns true
     */
    protected void end() {
        Debug.print("[" + getName() + "] end");
        shooter.resetLoader(Constants.LOADER_MOTOR_SPEED);
    }

    /**
     * Called when another command which requires one or more of the same subsystems is scheduled to run
     */
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
        shooter.resetLoader(Constants.LOADER_MOTOR_SPEED);
    }
}
