/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Encoder.PIDSourceParameter;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class PIDShooter extends PIDSubsystem {

    private static SpeedController shooterVictor = null;
    private static SpeedController loaderVictor = null;
    // Quadrature encoder used to measure shoot motor speed
    private static Encoder shooterEncoder;
    private static DigitalInput startLimitSwitch;
    private static DigitalInput stopLimitSwitch;
    private static final double Kp = 1.0;
    private static final double Ki = 0.2;
    private static final double Kd = 0.0;

    // Initialize your subsystem here
    public PIDShooter() {
        super("PIDShooter", Kp, Ki, Kd);

        Debug.println("[PIDShooter] Instantiating...");

        Debug.println("[PIDShooter] Initializing shooter wheel motor speed controller to PWM channel "
                + RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL);


        Debug.println("[PIDShooter] Initializing loader motor speed controller to PWM channel "
                + RobotMap.LOADER_MOTOR_PWM_CHANNEL + " on the digital module.");
        loaderVictor = new Victor(RobotMap.LOADER_MOTOR_PWM_CHANNEL);


        Debug.println("[PIDShooter] Initializing loader start position limit switch to I/O channel "
                + RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        startLimitSwitch = new DigitalInput(1, RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        Debug.println("[PIDShooter] Initializing loader stop position limit switch to I/O channel "
                + RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        stopLimitSwitch = new DigitalInput(1, RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);


        Debug.println("[PIDShooter] Initializing shooter motor encoder to channels "
                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
                                     1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true, EncodingType.k1X);
        shooterEncoder.setDistancePerPulse(1.0);
        shooterEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.

        Debug.println("[PIDShooter] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * @return The value of the encoder used as the PID input device.
     *         These values correspond to your PID setpoint.
     */
    protected double returnPIDInput() {
        return shooterEncoder.getRate();
    }

    /**
     * @param output The value to set the output to as determined by the PID
     *               algorithm. This gets called each time through the PID loop
     *               to update the output to the motor.
     */
    protected void usePIDOutput(double output) {
        shooterVictor.set(output);
    }

    private boolean canLoadDisc() {
        // If the start position limit switch is triggered then
        // the loader arm is retracted and ready to load another disc
        return true;
        //return startLimitSwitch.get();
    }

    public void loadDisc(double speed) {
        if (canLoadDisc()) {
            // Until the stop position limit switch is triggered
            // the value returned will be false
            while (!stopLimitSwitch.get()) {
                loaderVictor.set(-speed);
            }
            loaderVictor.set(0);
        }
    }

    public boolean isLoadDiscFinished() {
        return stopLimitSwitch.get();
    }

    public void resetLoader(double speed) {
        // Until the start position limit switch is triggered
        // the value returned will be false
        while (!startLimitSwitch.get()) {
            loaderVictor.set(speed);
        }
        loaderVictor.set(0);
    }

    public boolean isResetLoaderFinished() {
        return startLimitSwitch.get();
    }

    public void printLimitSwitchValues() {
        System.out.println("start switch = " + startLimitSwitch.get() + ", stop switch = " + stopLimitSwitch.get());
    }

    public void startEncoder() {
        shooterEncoder.start();
        Debug.println("[PIDShooter] encoder started.");
    }

    public void stopEncoder() {
        shooterEncoder.stop();
        Debug.println("[PIDShooter] encoder stopped.");
    }

    public void startShooter(double speed) {
        shooterEncoder.start();
        shooterVictor.set(speed);
    }

    public void stopShooter() {
        shooterVictor.set(0);
        shooterEncoder.stop();
    }
}
