/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.misc.Constants;

/**
 * Command use to unload Frisbee disc into shooter
 */
public class ResetLoadDisc extends CommandBase {
    private double timeout = Constants.RESET_LOADER_TIMEOUT_IN_SECS;

    public ResetLoadDisc() {
        super("ResetLoadDisc");
        requires(shooter);
    }

    /**
     * Called just before this Command runs the first time
     */
    protected void initialize() {
        setTimeout(timeout);
        Debug.print("[" + getName() + "] initialize");
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    protected void execute() {
        shooter.resetLoader2(Constants.LOADER_MOTOR_SPEED);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     *
     * @return finished state
     */
    protected boolean isFinished() {
//        return (shooter.isResetLoaderFinished() || isTimedOut());
        return (shooter.isResetLoaderFinished());
    }

    /**
     * Called once after isFinished returns true
     */
    protected void end() {
        Debug.print("[" + getName() + "] end");
    }

    /**
     * Called when another command which requires one or more of the same subsystems is scheduled to run
     */
    protected void interrupted() {
        Debug.print("[" + getName() + "] interrupted");
        shooter.resetLoader(Constants.LOADER_MOTOR_SPEED);
    }
}
