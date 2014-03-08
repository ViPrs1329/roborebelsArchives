/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.launcher.Launch;
import org.stlpriory.robotics.commands.launcher.Reset;
import org.stlpriory.robotics.commands.launcher.Retract;
import org.stlpriory.robotics.commands.launcher.Stop;
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
    
    /**
     * This command group will not have any child commands until it is
     * initialized, at which point it will examine the status
     * of the hot goal and add an appropriate sequence of commands in order
     * to execute the shooting strategy.
     */
    private class ShootingStrategy extends CommandGroup {
        
        public void initialize() {
            
            CommandGroup commandGroup = new CommandGroup();
            
            if (Vision.getInstance().getHotGoalNormalizedX() == null) {
                commandGroup.addSequential(new WaitCommand(5) {

                    protected void execute() {
                        System.out.println("ShootingStrategy Wait at " + System.currentTimeMillis());
                        super.execute();
                    }
                    
                    protected void end() {
                        System.out.println("ShootingStrategy Wait ending at " + System.currentTimeMillis());
                        super.end();
                    }
                    
                });
            }
            
            commandGroup.addSequential(new Reset());
            commandGroup.addSequential(new WaitCommand(.4));
            commandGroup.addSequential(new Stop());
            commandGroup.addSequential(new Launch() {
                protected void execute() {
                    System.out.println("ShootingStrategy Launch at " + System.currentTimeMillis());
                    super.execute();
                }
                protected void end() {
                    System.out.println("ShootingStrategy Launch ending at " + System.currentTimeMillis());
                    super.end();
                }
            });
            
            commandGroup.addSequential(new WaitCommand(1));
            commandGroup.addSequential(new Retract());
            commandGroup.start();
        }
        
        protected void execute() {
            System.out.println("ShootingStrategy at " + System.currentTimeMillis());
            super.execute();
        }
        
        protected void end() {
            System.out.println("ShootingStrategy ending at " + System.currentTimeMillis());
            super.end();
        }
    }
}
