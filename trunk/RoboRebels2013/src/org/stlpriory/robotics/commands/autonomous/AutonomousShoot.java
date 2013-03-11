/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.autonomous;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import org.stlpriory.robotics.commands.drivetrain.DriveStraight;
import org.stlpriory.robotics.commands.drivetrain.Turn;
import org.stlpriory.robotics.commands.shooter.LoadDisc;
import org.stlpriory.robotics.commands.shooter.ResetLoadDisc;
import org.stlpriory.robotics.commands.shooter.StartShooting;
import org.stlpriory.robotics.commands.shooter.StopShooting;

/**
 *
 */
public class AutonomousShoot extends CommandGroup {
    private static final double DRIVING_SPEED = -0.4;
    private static final double DRIVE_STAIGHT_TIMEOUT = 0.4;

    private static final double WAIT_BEFORE_LOAD  = 2;
    private static final double WAIT_BEFORE_RESET = 0.5;

    public AutonomousShoot() {
        super("AutonomousShoot");

        addSequential(new StartShooting());
        addSequential(new DriveStraight(DRIVING_SPEED,DRIVE_STAIGHT_TIMEOUT));
        addSequential(new WaitCommand(WAIT_BEFORE_LOAD));

        for (int i = 0; i < 5; i++) {
            addSequential(new LoadDisc());
            addSequential(new WaitCommand(WAIT_BEFORE_RESET));
            addSequential(new ResetLoadDisc());
            addSequential(new WaitCommand(WAIT_BEFORE_LOAD));
        }

        addSequential(new StopShooting());

    }
}
