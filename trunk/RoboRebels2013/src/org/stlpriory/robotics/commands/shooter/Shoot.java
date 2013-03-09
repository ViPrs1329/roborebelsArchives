/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.shooter;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 * @author William
 */
public class Shoot extends CommandGroup {

    public Shoot() {
        super("Shoot");
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        double waitBeforeLoad  = 2;
        double waitBeforeReset = 0.5;

        addSequential(new StartShooting());
        addSequential(new WaitCommand(waitBeforeLoad));

        for (int i = 0; i < 5; i++) {
            addSequential(new LoadDisc());
            addSequential(new WaitCommand(waitBeforeReset));
            addSequential(new ResetLoadDisc());
            addSequential(new WaitCommand(waitBeforeLoad));
        }

        addSequential(new StopShooting());

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
    }
}
