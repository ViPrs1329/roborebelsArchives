/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.grabber;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Debug;

/**
 *
 */
public class GrabberRun extends CommandBase {

    private boolean hasTimeout = false;
    private double timeout;

    public GrabberRun() {
        super("GrabberRun");
        requires(grabber);
    }

    public GrabberRun(double timeout) {
        this();
        this.hasTimeout = true;
        this.timeout = timeout;
    }

    protected void initialize() {
        Debug.print("[" + this.getName() + "] initialize");
        if (hasTimeout) {
            Debug.print("\tTimeout: " + timeout);
            setTimeout(timeout);
        }
    }

    protected void execute() {
        grabber.start();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        Debug.println("\t\tDONE");
        grabber.stop();
    }

    protected void interrupted() {
        Debug.println("[interrupted] " + getName());
        grabber.stop();
    }
}
