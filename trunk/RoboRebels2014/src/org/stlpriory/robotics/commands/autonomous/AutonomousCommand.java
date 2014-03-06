/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.commands.launcher.Launch;
import org.stlpriory.robotics.commands.launcher.Retract;
import org.stlpriory.robotics.subsystems.Vision;

/**
 *
 * @author William
 */
public class AutonomousCommand extends CommandGroup {
    
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
    
    private class ShootingStrategy extends CommandBase {
        
        CommandGroup shootingSequence;
        
        public void initialize() {
            shootingSequence = new CommandGroup();
        }
        
        public void execute() {
            if (vision.getHotGoalNormalizedX() == null) {
                shootingSequence.addSequential(new WaitCommand(5));
            }
            shootingSequence.addSequential(new Launch());
            shootingSequence.addSequential(new Retract());
            // start will schedule the command to run with the Scheduler singleton
            shootingSequence.start();
        }
        
        public boolean isFinished() {
            return true;
        }
        
        public void interrupted() {
            
        }
        
        public void end() {
            
        }
        
    }
    
    // TODO use this as an alternative to extending CommandBase
    private class ShootingStrategyAlternative extends CommandGroup {
        private boolean executed = false;
        
        // we don't need to call super.execute since CommandGroup's _execute
        // method handles the internal command group execution logic
        public void execute() {
            if ( executed ) {
                return;
            }
            
            if (Vision.getInstance().getHotGoalNormalizedX() == null) {
                addSequential(new WaitCommand(5));
            }
            addSequential(new Launch());
            addSequential(new Retract());
            
            executed = true;
        }
    }
}
