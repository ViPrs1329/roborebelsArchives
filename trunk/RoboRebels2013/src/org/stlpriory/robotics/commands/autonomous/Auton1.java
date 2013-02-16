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

/**
 *
 */
public class Auton1 extends CommandGroup {
    private static final double DRIVING_SPEED = .01;
    private static final double TURNING_SPEED = .01;
    
    private static double forwards = -1;
    private static double backwards = 1;
    private double DRIVE_SPEED = DRIVING_SPEED;
    private double DRIVE_TIMEOUT = 3.0;
    private double TURN1_SPEED = TURNING_SPEED;
    private double TURN1_TIMEOUT = 1.0;
    private double WAIT_TIMEOUT = 5.0;
    private double TURN2_SPEED = TURNING_SPEED;
    private double TURN2_TIMEOUT = 1.0;
    private double DRIVE_BACK_SPEED = DRIVING_SPEED;
    private double DRIVE_BACK_TIMEOUT = 3.0;
    private double WAIT_ONE_SECOND = 1.0;

    public Auton1() {
        super("Auton1");
        /*
         * Drive forwards to the fender at 0.7 speed for 3 seconds
         */
        
        addSequential(new PrintCommand("[auton1] Driving straight at speed: " + DRIVE_SPEED + " and timeout: " + DRIVE_TIMEOUT));
        addSequential(new DriveStraight(forwards * DRIVE_SPEED, DRIVE_TIMEOUT));
        /*
         * Turn to the left at 0.5 speed for 1 seconds
         */
        addSequential(new PrintCommand("[auton1] Turning left at speed: " + TURN1_SPEED + " and timeout: " + TURN1_TIMEOUT));
        addParallel(new Turn(forwards * TURN1_SPEED, TURN1_TIMEOUT));
        /*
         * Wait for 5 seconds for something else to happen
         */
        addSequential(new PrintCommand("[auton1] Waiting " + WAIT_TIMEOUT + " seconds for the robot"));
        addSequential(new WaitCommand(WAIT_TIMEOUT));
        /*
         * Turn to the right at 0.5 speed for 2 seconds
         */
        addSequential(new PrintCommand("[auton1] Turning right at speed: " + TURN2_SPEED + " and timeout: " + TURN2_TIMEOUT));
        addParallel(new Turn(backwards * TURN2_SPEED, TURN2_TIMEOUT));
        /*
         * Drive back as far as it can (time up)
         * TODO: Why doesn't the DriveStraight command stop after timeout?
         */
        addSequential(new PrintCommand("[auton1] Driving back at speed: " + DRIVE_BACK_SPEED + " and timeout: " + DRIVE_BACK_TIMEOUT));
        addSequential(new DriveStraight(backwards * DRIVE_BACK_SPEED, DRIVE_BACK_TIMEOUT));
        /*
         * Maybe helps stop the previous command?
         */
        addSequential(new PrintCommand("[auton1] Waiting " + WAIT_ONE_SECOND + " second(s)"));
        addSequential(new WaitCommand(WAIT_ONE_SECOND));
    }
}
