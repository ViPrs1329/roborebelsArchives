/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import roborebels.example.robot.RobotMap;
import roborebels.example.commands.shooter.ShooterMove;
import roborebels.example.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    private Jaguar flyWheel;
    private Jaguar feeder;
    private Relay angler;
    private double speed = 1.0;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Initializing FlyWheel jaguar on channel " + RobotMap.kShooterFlyWheelChannel);
        flyWheel = new Jaguar(RobotMap.kShooterFlyWheelChannel);
        Debug.println("[Shooter] Initializing Feeder jaguar on channel " + RobotMap.kShooterFeederChannel);
        feeder = new Jaguar(RobotMap.kShooterFeederChannel);
        Debug.println("[Shooter] Initializing Angler relay on channel " + RobotMap.kShooterAnglerChannel);
        angler = new Relay(RobotMap.kShooterAnglerChannel);
        Debug.println("[Shooter] Initializing gyro on channel " + RobotMap.kGyroChannel);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new ShooterMove());
    }

// Set manual speed for the flyWheel
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public double getActualSpeed() {
        return flyWheel.get();
    }

    public void run() {
// flyWheel.set(this.speed);
//limit is the amount of change you will allow every iteration
//limitedJoystick is the rate-limited joystick value you use to control your motors.
        double limit = 0.075;
        double joystick = this.speed;
        double limitedJoystick = flyWheel.getSpeed();
        double change = joystick - limitedJoystick;
        if (change > limit) {
            change = limit;
        } else if (change < -limit) {
            change = -limit;
        }
        limitedJoystick += change;
        flyWheel.set(limitedJoystick);
    }

    public void run(double speed) {
        setSpeed(speed);
        this.run();
    }

    public void stop() {
        this.run(0.0);
    }

    public void setFeederForward() {
        feeder.set(-1.0);
    }

    public void setFeederReverse() {
        feeder.set(1.0);
    }

    public void stopFeeder() {
        feeder.set(0.0);
    }

    public void moveUp() {
        SmartDashboard.putString("ShooterMove", "Up");
        angler.set(Relay.Value.kForward);
    }

    public void moveDown() {
        SmartDashboard.putString("ShooterMove", "Down");
        angler.set(Relay.Value.kReverse);
    }

    public void moveStop() {
        SmartDashboard.putString("ShooterMove", "Stop");
        angler.set(Relay.Value.kOff);
    }
}
