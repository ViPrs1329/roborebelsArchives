/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.arm;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Debug;
import roborebels.example.misc.Utils;

/**
 *
 */
public class ArmLatch extends CommandBase {

    private boolean hasFinished = false;
    private boolean hasTimeout = false;
    private double timeout;

    public ArmLatch() {
        super("ArmLatch");
        requires(arm);
    }

    public ArmLatch(double timeout) {
        this();
        this.hasTimeout = true;
        this.timeout = timeout;
    }

    protected void initialize() {
        hasFinished = false;
        Debug.print("[ArmLatch] initialize");
        if (hasTimeout) {
            Debug.print("\tTimeout: " + timeout);
            setTimeout(timeout);
        }
        Debug.print("\tTimeStarted: " + Utils.roundDecimals(timeSinceInitialized(), 5));
        if (!arm.getSensorExtracted()) {
            hasFinished = true;
            Debug.print("\t[ERROR] ARM NOT EXTRACTED!");
        }
    }

    protected void execute() {
        arm.latch();
        if (arm.getSensorLatch()) {
            hasFinished = true;
        }
    }

    protected boolean isFinished() {
        return isTimedOut() || hasFinished;
    }

    protected void end() {
        Debug.println("\tTimeEnded: " + Utils.roundDecimals(timeSinceInitialized(), 5));
        arm.stopLatch();
    }

    protected void interrupted() {
        Debug.print("\tTimeEnded: " + Utils.roundDecimals(timeSinceInitialized(), 5));
        Debug.println("\t[interrupted] " + getName());
        arm.stopLatch();
    }
}
