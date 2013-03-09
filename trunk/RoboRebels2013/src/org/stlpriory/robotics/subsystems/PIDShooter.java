/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *  Implementation of Shooter with PID feedback control of the shooter wheel speed
 */
public class PIDShooter extends Shooter {

    private static Encoder shooterEncoder;
    private final PIDController shooterPID;

    private static final double Kp = 0.0;
    private static final double Ki = 0.0;
    private static final double Kd = 0.0;
    private static final double Kf = 0.9;///Constants.SHOOTER_WHEEL_MOTER_RATE;
//  private static final double Kf = Constants.SHOOTER_WHEEL_MOTOR_SPEED;

    // The period is the loop time for doing PID calculations. This particularly effects
    // calculations of the integral and differential terms. The default is 50ms.
    private static final double PERIOD = 200;

    // Initialize your subsystem here
    public PIDShooter() {
        super();
        Debug.println("[PIDShooter] Instantiating...");

        Debug.println("[PIDShooter] Initializing shooter motor encoder to channels "
                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
                                     1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true, EncodingType.k1X);
        shooterEncoder.setDistancePerPulse(1);
        shooterEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);

        // Initialize the shooter PID Controller
        shooterPID = new PIDController(Kp, Ki, Kd, Kf, shooterEncoder, shooterVictor);
        shooterPID.setContinuous();
        shooterPID.setPercentTolerance(5);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.

        Debug.println("[PIDShooter] Instantiation complete.");
    }

    public void startShooter(double speed) {
        shooterVictor.set(speed);

        shooterEncoder.start();
        shooterPID.setSetpoint(Constants.SHOOTER_WHEEL_MOTER_RATE);
        shooterPID.enable();
        printPIDControllerError();
        printEncoderValues();
        Debug.println("[PIDShooter.startShooter] pidGet(rate) " + shooterEncoder.pidGet());
    }

    public void loadDiscWithTimeout(double speed) {
        printPIDControllerError();
        printEncoderValues();
        super.loadDiscWithTimeout(speed);
    }

    public void stopShooter() {
        shooterVictor.set(0);

        shooterPID.setSetpoint(0);
        shooterEncoder.stop();
        Debug.println("[PIDShooter.stopShooter] pidGet(rate) " + shooterEncoder.pidGet());
    }

    public void printEncoderValues() {
        Debug.println("encoder raw = " + shooterEncoder.getRaw() + ", rate = " + shooterEncoder.getRate());
    }

    public void printPIDControllerError() {
        Debug.println("PID controller error = "+shooterPID.getError()+", pidGet = "+shooterEncoder.pidGet());
    }

}
