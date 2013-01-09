/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package roborebels.example.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import roborebels.example.commands.CommandBase;
import roborebels.example.commands.autonomous.Auton1;
import roborebels.example.misc.Constants;
import roborebels.example.misc.Debug;
import roborebels.example.misc.Utils;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends IterativeRobot implements Constants {

    private Command autonomousCommand;
    private Timer timer = new Timer();
    private boolean firstTime = true;

    /**
     * Robot-wide initialization code which will be called when the robot is
     * first powered on. It will be called exactly 1 time.
     */
    public void robotInit() {
        Debug.println("[robotInit] Initializing...");
        autonomousCommand = new Auton1();
        timer.start();
        CommandBase.init();
        timer.stop();
        SmartDashboard.putData(Scheduler.getInstance());
        Debug.println("[robotInit] Done in " + timer.get() * 1e6 + " ms");
    }

    /**
     * Initialization code for autonomous mode which will be called each time
     * the robot enters autonomous mode.
     */
    public void autonomousInit() {
        Debug.println("[mode] Autonomous");
        commonInit();

// Don't want autonomous unless at competition, to prevent accidental usage.
//autonomousCommand.start();
    }

    /**
     * Periodic code for autonomous mode which will be called periodically at a
     * regular rate while the robot is in autonomous mode.
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    /**
     * Continuous code for autonomous mode which will be called repeatedly as
     * frequently as possible while the robot is in autonomous mode.
     */
    public void autonomousContinuous() {
    }

    /**
     * Initialization code for teleop mode which will be called each time the
     * robot enters teleop mode.
     */
    public void teleopInit() {
        Debug.println("[mode] Operator control");
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        commonInit();
    }

    /**
     * Periodic code for teleop mode which will be called periodically at a
     * regular rate while the robot is in teleop mode.
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        updateDashboard();
    }

    /**
     * Continuous code for teleop mode which will be called repeatedly as
     * frequently as possible while the robot is in teleop mode.
     */
    public void teleopContinuous() {
    }

    /**
     * Initialization code for disabled mode which will be called each time the
     * robot enters disabled mode.
     */
    public void disabledInit() {
        Debug.println("[mode] Disabled");
        commonInit();
    }

    /**
     * Periodic code for disabled mode which will be called periodically at a
     * regular rate while the robot is in disabled mode.
     */
    public void disabledPeriodic() {
    }

    /**
     * Continuous code for disabled mode which will be called repeatedly as
     * frequently as possible while the robot is in disabled mode.
     */
    public void disabledContinuous() {
    }

    /**
     * A common initialization code for all modes which will be called each time
     * disabledInit(), autonomousInit(), or teleopInit() is called.
     */
    public void commonInit() {
        if (firstTime) {
            CommandBase.oi.getDS().print(1, "Robot Ready!");
            firstTime = false;
        }
    }

    /**
     * Update the SmartDashboard, DriverStation and the UserMessages from one
     * place to avoid confusion.
     */
    public void updateDashboard() {
        SmartDashboard.putDouble("Battery Percent", Utils.scaleBatteryVoltage(CommandBase.oi.getDS().getDS().getBatteryVoltage()));
        SmartDashboard.putDouble("Sonar", CommandBase.drivetrain.getSonarDistance());
        SmartDashboard.putDouble("Shooter Speed", CommandBase.shooter.getSpeed());
        SmartDashboard.putDouble("ShooterSpeed", CommandBase.shooter.getSpeed());
        SmartDashboard.putBoolean("SystemActive", isSystemActive());
        SmartDashboard.putBoolean("SensorExtracted", CommandBase.arm.getSensorExtracted());
        SmartDashboard.putBoolean("SensorRetracted", CommandBase.arm.getSensorRetracted());
        SmartDashboard.putBoolean("SensorLatch", CommandBase.arm.getSensorLatch());
        CommandBase.oi.getDS().getDS().setDigitalOut(kDSDigitalOutputSensorExtracted, CommandBase.arm.getSensorExtracted());
        CommandBase.oi.getDS().getDS().setDigitalOut(kDSDigitalOutputSensorRetracted, CommandBase.arm.getSensorRetracted());
        CommandBase.oi.getDS().getDS().setDigitalOut(kDSDigitalOutputSensorLatch, CommandBase.arm.getSensorLatch());
    }
}
