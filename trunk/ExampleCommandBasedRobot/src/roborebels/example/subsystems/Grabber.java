/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import roborebels.example.robot.RobotMap;
import roborebels.example.misc.Constants;
import roborebels.example.misc.Debug;

/**
 *
 */
public class Grabber extends Subsystem implements Constants {

    private Jaguar grabberJag;

    public Grabber() {
        super("Grabber");
        Debug.println("[Grabber] Initializing ball grabber jaguar on channel " + RobotMap.kGrabberChannel);
        grabberJag = new Jaguar(RobotMap.kGrabberChannel);
    }

    protected void initDefaultCommand() {
    }

    public double getSpeed() {
        return grabberJag.get();
    }

    public void start() {
        grabberJag.set(1.0);
    }

    public void reverse() {
        grabberJag.set(-1.0);
    }

    public void stop() {
        grabberJag.set(0.0);
    }
}
