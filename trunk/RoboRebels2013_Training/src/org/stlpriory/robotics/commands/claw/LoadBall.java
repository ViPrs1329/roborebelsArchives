/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//package org.stlpriory.robotics.commands.shooter;
package org.stlpriory.robotics.commands.claw;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.subsystems.Claw;
import org.stlpriory.robotics.commands.CommandBase;
/**
 *
 * @author John
 */
public class LoadBall extends CommandBase{
    private double timeout = Constants.LOAD_DISC_TIMEOUT_IN_SECS;
    protected static SpeedController loaderVictor;

    public LoadBall() {
        //super("LoadBall");
        //requires(shooter);
        Debug.println("[Shooter] Initializing loader motor speed controller to PWM channel "
                + RobotMap.LOADER_MOTOR_PWM_CHANNEL + " on the digital module.");
        loaderVictor = new Victor(RobotMap.LOADER_MOTOR_PWM_CHANNEL);
    }

    /**
     * Called just before this Command runs the first time
     */
    protected void initialize() {
        //setTimeout(timeout);
        //Debug.print("[" + getName() + "] initialize");
    }

    /**
     * Called repeatedly when this Command is scheduled to run
     */
    protected void execute() {
        //shooter.loadDiscWithTimeout(Constants.LOADER_MOTOR_SPEED);
        claw.loadBall(Constants.LOADER_MOTOR_SPEED);
    }

    /**
     * Make this return true when this Command no longer needs to run execute()
     *
     * @return finished state
     */
    protected boolean isFinished() {
//        return (shooter.isLoadDiscFinished() || isTimedOut());
        //return (shooter.isLoadDiscFinished());
        return(Claw.isLoadBallFinished());
    }

    /**
     * Called once after isFinished returns true
     */
    protected void end() {
        //Debug.print("[" + getName() + "] end");
    }

    /**
     * Called when another command which requires one or more of the same subsystems is scheduled to run
     */
    protected void interrupted() {
        //Debug.print("[" + getName() + "] interrupted");
        //shooter.resetLoader(Constants.LOADER_MOTOR_SPEED);
    }
    private void loadBall(){
        
    }
}
