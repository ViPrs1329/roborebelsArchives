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
    private static final double DRIVE_SPEED = 0.1;
    private static final double TURN_SPEED = 0.1;

    public DriveInASquare() {
        addSequential(new DriveStraight(DRIVE_SPEED, 1.0));
        addSequential(new Turn(TURN_SPEED,1.0));
        addSequential(new DriveStraight(DRIVE_SPEED, 1.0));
        addSequential(new Turn(TURN_SPEED, 1.0));
        addSequential(new DriveStraight(DRIVE_SPEED, 1.0));
        addSequential(new Turn(TURN_SPEED, 1.0));
        addSequential(new DriveStraight(DRIVE_SPEED, 1.0));
    }
}
