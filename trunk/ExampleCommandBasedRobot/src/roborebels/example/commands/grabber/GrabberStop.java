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
public class GrabberStop extends CommandBase {

    public GrabberStop() {
        super("GrabberStop");
        requires(grabber);
    }

    protected void initialize() {
        Debug.print("[" + this.getName() + "]");
        grabber.stop();
    }

    protected void execute() {
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
        Debug.println("\t\tDONE");
    }

    protected void interrupted() {
        Debug.println("[interrupted] " + getName());
    }
}
