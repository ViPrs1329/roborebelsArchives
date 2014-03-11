/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.launcher.Launch;
import org.stlpriory.robotics.commands.launcher.Retract;
import org.stlpriory.robotics.misc.Debug;
import org.stlpriory.robotics.subsystems.Vision;

/**
 *
 * @author William
 */
public class AutonomousCommand extends CommandGroup {
    
    // measured in seconds
    public static final double IDEAL_SHOOT_TIME_IF_NO_HOT_GOAL = 7.0;
    
    // used to keep track of the amount of time that autonoumous phase
    // has been running
    private final Timer timer = new Timer();;
    
    public AutonomousCommand() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.

        addSequential(new AutonomousFirstPart());
        addSequential(new ShootingStrategy());
        
    }
     
    protected void initialize() {
        timer.start();
    }
    
    /**
     * This command group will not have any child commands, but during 
     * initialize it will examine the status of the hot goal and 
     * start an appropriate sequence of commands in order
     * to execute the shooting strategy.
     */
    private class ShootingStrategy extends CommandGroup {
        
        public void initialize() {

            double autonoumousTimeTakenSoFar = timer.get();
            
            CommandGroup commandGroup = new CommandGroup();
            
            if (Vision.getInstance().getHotGoalNormalizedX() == null) {
                double timeRemaining = IDEAL_SHOOT_TIME_IF_NO_HOT_GOAL - autonoumousTimeTakenSoFar;
                if (timeRemaining > 0) {
                    Debug.println("No hot goal detected, so waiting to shoot for " + timeRemaining + " sec");
                    commandGroup.addSequential(new WaitCommand(timeRemaining));
                }
            }
            
            // shoot the ball
            commandGroup.addSequential(new Launch());
            
            // after short delay, prepare for loading ball
            commandGroup.addSequential(new WaitCommand(0.5));
            commandGroup.addSequential(new Retract());
            commandGroup.start();
        }
        
       
    }
    
}
