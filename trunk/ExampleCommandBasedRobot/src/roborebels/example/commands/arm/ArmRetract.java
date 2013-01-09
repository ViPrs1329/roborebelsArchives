/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.arm;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Constants;
import roborebels.example.misc.Debug;

/**
 *
 */
public class ArmRetract extends CommandBase implements Constants {

    private boolean hasFinished = false;
    private double speed = 0.0;
    private double timeout;

    public ArmRetract(double speed, double timeout) {
        super("ArmRetract");
        requires(arm);
        this.speed = speed;
        this.timeout = timeout;
    }

    protected void initialize() {
        setTimeout(timeout);
        hasFinished = false;
        arm.setSpeed(this.speed);
        Debug.print("[" + this.getName() + "] Speed: " + this.speed);
        Debug.print("\tTimeout: " + timeout);
        Debug.print("\tSensorExtracted: " + arm.getSensorExtracted());
        Debug.print("\tSensorRetracted: " + arm.getSensorRetracted());
    }

    protected void execute() {
        arm.retract();
        if (arm.getSensorRetracted()) {
            hasFinished = true;
        }
    }

    protected boolean isFinished() {
        return isTimedOut() || hasFinished;
    }

    protected void end() {
        Debug.println("\tSensorRetracted: " + arm.getSensorRetracted());
        arm.stop();
    }

    protected void interrupted() {
        Debug.println("\tSensorRetracted: " + arm.getSensorRetracted());
        Debug.println("[interrupted] " + getName());
        arm.stop();
    }
}
