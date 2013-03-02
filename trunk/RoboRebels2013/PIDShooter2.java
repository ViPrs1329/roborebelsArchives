/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class PIDShooter2 extends PIDSubsystem {
    private static SpeedController loaderVictor = null;
    private static Encoder shooterEncoder;

    private static SpeedController shooterVictor = null;
    private static DigitalInput startLimitSwitch;
    private static DigitalInput stopLimitSwitch;
    private static final double Kp = 0.4;
    private static final double Ki = 0.01;
    private static final double Kd = 0.0;
    private static final double Kf = Constants.SHOOTER_WHEEL_MOTOR_SPEED;

    // Initialize your subsystem here
    public PIDShooter2() {
        super("PIDShooter2", Kp, Ki, Kd, Kf);
        Debug.println("[PIDShooter2] Instantiating...");

        Debug.println("[PIDShooter2] Initializing loader motor speed controller to PWM channel "
                + RobotMap.LOADER_MOTOR_PWM_CHANNEL + " on the digital module.");
        loaderVictor = new Victor(RobotMap.LOADER_MOTOR_PWM_CHANNEL);


        Debug.println("[PIDShooter2] Initializing loader start position limit switch to I/O channel "
                + RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        startLimitSwitch = new DigitalInput(1, RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        Debug.println("[PIDShooter2] Initializing loader stop position limit switch to I/O channel "
                + RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        stopLimitSwitch = new DigitalInput(1, RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);


        Debug.println("[PIDShooter2] Initializing shooter wheel motor speed controller to PWM channel "
                + RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL);


        Debug.println("[PIDShooter2] Initializing shooter motor encoder to channels "
                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
                                     1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true, CounterBase.EncodingType.k1X);
        shooterEncoder.setDistancePerPulse(1);
        shooterEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        shooterEncoder.start();

        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.

        Debug.println("[PIDShooter2] Instantiation complete.");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;
        return shooterEncoder.getRate();
    }

    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        shooterVictor.pidWrite(output);
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

    public void printEncoderValues() {
        System.out.println("encoder raw = " + shooterEncoder.getRaw() + ", rate = " + shooterEncoder.getRate() + ", pidGet = " + shooterEncoder.pidGet());
    }

    public void startShooter(double speed) {
        shooterEncoder.start();
        //shooterVictor.set(speed);
    }

    public void stopShooter() {
        //shooterVictor.set(0);
        shooterEncoder.stop();
    }

}
