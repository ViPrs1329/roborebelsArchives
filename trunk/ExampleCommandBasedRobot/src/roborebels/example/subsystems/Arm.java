/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import roborebels.example.robot.RobotMap;
import roborebels.example.misc.Debug;

/**
 *
 */
public class Arm extends Subsystem {

    private double speed = 0.1;
    private Jaguar arm;
    private Jaguar latcher;
    private DigitalInput sensorExtracted;
    private DigitalInput sensorRetracted;
    private DigitalInput sensorLatch;

    public Arm() {
        super("Arm");
        arm = new Jaguar(RobotMap.kArmChannel);
        latcher = new Jaguar(RobotMap.kArmLatchChannel);
        Debug.println("[Arm] Initializing arm motor on channel " + RobotMap.kArmChannel);
        Debug.println("[Arm] Initializing arm latch motor on relay " + RobotMap.kArmLatchChannel);
        sensorExtracted = new DigitalInput(RobotMap.kArmSensorExtracted);
        sensorRetracted = new DigitalInput(RobotMap.kArmSensorRetracted);
        sensorLatch = new DigitalInput(RobotMap.kArmSensorLatch);
    }

    public void initDefaultCommand() {
    }

    public void latch() {
        latcher.set(-0.8);
    }

    public void unlatch() {
        latcher.set(0.8);
    }

    public void stopLatch() {
        latcher.set(0.0);
    }

    public double getSpeed() {
        return this.speed;
    }

    public void runLatch(double speed) {
        latcher.set(speed);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void extract() {
        arm.set(this.speed * -1);
    }

    public void retract() {
        arm.set(this.speed * 1);
    }

    public void stop() {
        arm.set(0.0);
    }

    public boolean getSensorRetracted() {
        return !sensorRetracted.get();
    }

    public boolean getSensorExtracted() {
        return !sensorExtracted.get();
    }

    public boolean getSensorLatch() {
        return sensorLatch.get();
    }
}
