/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.commands.drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author dfuglsan
 */
public class DriveInASquare extends CommandGroup {

    public DriveInASquare() {
        addSequential(new DriveStraight(1.0, 1.0));
        addSequential(new Turn(1.0,1.0));
        addSequential(new DriveStraight(1.0, 1.0));
        addSequential(new Turn(1.0, 1.0));
        addSequential(new DriveStraight(1.0, 1.0));
        addSequential(new Turn(1.0, 1.0));
        addSequential(new DriveStraight(1.0, 1.0));
    }
}
