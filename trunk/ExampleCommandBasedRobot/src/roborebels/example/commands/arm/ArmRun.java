/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.arm;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;
import roborebels.example.misc.Constants;

/**
 *
 */
public class ArmRun extends CommandGroup implements Constants {

    private double ARM_EXTRACT_TIMEOUT = 3.5;
    private double ARM_LATCH_TIMEOUT = 0.5;
    private double DELAY_TIME = 7.0;
    private double ARM_UNLATCH_TIMEOUT = 0.4;
    private double ARM_RETRACT_TIMEOUT = 2.0;
    private double ARM_EXTRACT2_TIMEOUT = 0.17;

    public ArmRun() {
        super("ArmRun");
        /*
         * Extract the arm all the way until sensor
         */
        addSequential(new ArmExtract(kArmSpeed, ARM_EXTRACT_TIMEOUT));
        /*
         * Latch the arm until sensor, with a timeout of 0.5 seconds
         * in case the sensor doesn't work.
         */
        addSequential(new WaitCommand(0.5));
        addSequential(new ArmLatch(ARM_LATCH_TIMEOUT));
        /*
         * Wait for 7 seconds; give time to get on the bridge
         */
        addSequential(new WaitCommand(DELAY_TIME));
        /*
         * Unlatch the arm for 0.4 seconds
         */
        addSequential(new ArmUnlatch(ARM_UNLATCH_TIMEOUT));
        /*
         * Retract back the arm all the way until sensor
         */
        addSequential(new ArmRetract(kArmSpeed, ARM_RETRACT_TIMEOUT));
        /*
         * Extract the arm for 0.3 seconds at slow speed
         */
        addSequential(new ArmExtract(kArmSlowSpeed, ARM_EXTRACT2_TIMEOUT));
    }
}
