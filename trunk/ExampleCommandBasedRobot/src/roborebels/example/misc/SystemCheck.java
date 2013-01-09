/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.misc;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import edu.wpi.first.wpilibj.command.WaitCommand;
import roborebels.example.commands.arm.ArmRun;
import roborebels.example.commands.grabber.GrabberReverse;
import roborebels.example.commands.grabber.GrabberRun;
import roborebels.example.commands.grabber.GrabberStop;
import roborebels.example.commands.shooter.ShooterFeed;
import roborebels.example.commands.shooter.ShooterMove;
import roborebels.example.commands.shooter.ShooterRun;
import roborebels.example.commands.shooter.ShooterStop;

/**
 *
 */
public class SystemCheck extends CommandGroup implements Constants {

    public SystemCheck() {

        addSequential(new PrintCommand("[SystemCheck] --- SYSTEM CHECK! ---"));

        // Grabber
        addSequential(new PrintCommand("[SystemCheck] Grabber Run"));
        addSequential(new GrabberRun(1.0));
        addSequential(new PrintCommand("[SystemCheck] Grabber Reverse"));
        addSequential(new GrabberReverse(1.0));
        addSequential(new PrintCommand("[SystemCheck] Grabber Stop"));
        addSequential(new GrabberStop());
        addSequential(new WaitCommand(1.0));

        // Shooter
        addSequential(new PrintCommand("[SystemCheck] Shooter Run"));
        addSequential(new ShooterRun(0.384), 3.0);
        addSequential(new PrintCommand("[SystemCheck] Shooter Stop"));
        addSequential(new ShooterStop());
        addSequential(new WaitCommand(1.0));

        // Feeder
        addSequential(new PrintCommand("[SystemCheck] Feeder Run"));
        addSequential(new ShooterFeed(kTimeFeedOneBall, kShooterForceFeed));
        addSequential(new WaitCommand(1.0));

        // Angler
        addSequential(new PrintCommand("[SystemCheck] Shooter Up"));
        addSequential(new ShooterMove(kShooterUp, 1.0));
        addSequential(new PrintCommand("[SystemCheck] Shooter Down"));
        addSequential(new ShooterMove(kShooterDown, 1.0));
        addSequential(new WaitCommand(1.0));

        // Arm
        addSequential(new PrintCommand("[SystemCheck] Arm Run"));
        addSequential(new ArmRun());
        addSequential(new WaitCommand(1.0));

    }
}
