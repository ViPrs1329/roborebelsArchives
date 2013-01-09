/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.drivetrain;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Constants;
import roborebels.example.misc.Debug;

/**
 *
 */
public class DriveWithJoystick extends CommandBase implements Constants {

    public DriveWithJoystick() {
        super("DriveWithJoystick");
        requires(drivetrain);
    }

    protected void initialize() {
        Debug.println("[" + this.getName() + "] initialized");
    }

    protected void execute() {
        if (oi.getDS().getDS().getDigitalIn(kDSDigitalInputArcadeDrive)) {
            drivetrain.arcadeDrive(oi.getJoystick().getAxis(kJoystickAxisY), oi.getJoystick().getAxis(kJoystickAxisX));
        } else {
            if (oi.getDS().getDS().getDigitalIn(kDSDigitalInputSlowDrive)) {
                drivetrain.tankDrive(oi.getGamePad().getAxis(kGamepadAxisLeftStickY) * 0.8, oi.getGamePad().getAxis(kGamepadAxisRightStickY) * 0.8);
            } else {
                drivetrain.tankDrive(oi.getGamePad().getAxis(kGamepadAxisLeftStickY), oi.getGamePad().getAxis(kGamepadAxisRightStickY));
            }
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        drivetrain.stop();
    }

    protected void interrupted() {
        Debug.println("[interrupted] " + getName());
    }
}
