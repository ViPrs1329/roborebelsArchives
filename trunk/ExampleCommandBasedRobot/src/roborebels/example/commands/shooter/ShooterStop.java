/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.commands.shooter;

import roborebels.example.commands.CommandBase;
import roborebels.example.misc.Debug;

/**
 *
 */
public class ShooterStop extends CommandBase {

    public ShooterStop() {
        super("ShooterStop");
        requires(shooter);
    }

    protected void initialize() {
        Debug.println("[" + this.getName() + "]");
    }

    protected void execute() {
        shooter.stop();
    }

    protected boolean isFinished() {
        return true; // This will call end() and will stop it anyways. Will also kill the thread that way.
    }

    protected void end() {
        shooter.stop();
    }

    protected void interrupted() {
        Debug.println("[interrupted] " + getName());
        shooter.stop();
    }
}
