/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    private static SpeedController shooterVictor = null;
    private static SpeedController loaderVictor = null;
    // Quadrature encoder used to measure shoot motor speed
    private static Encoder shooterEncoder;

    private static DigitalInput shooterProximitySensor;

    private static DigitalInput startLimitSwitch;
    private static DigitalInput stopLimitSwitch;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Instantiating...");

        Debug.println("[Shooter] Initializing shooter wheel motor speed controller to PWM channel "
                + RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_WHEEL_MOTOR_PWM_CHANNEL);


        Debug.println("[Shooter] Initializing loader motor speed controller to PWM channel "
                + RobotMap.LOADER_MOTOR_PWM_CHANNEL + " on the digital module.");
        loaderVictor = new Victor(RobotMap.LOADER_MOTOR_PWM_CHANNEL);


        Debug.println("[Shooter] Initializing loader start position limit switch to I/O channel "
                + RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        startLimitSwitch = new DigitalInput(1, RobotMap.LOADER_START_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        Debug.println("[Shooter] Initializing loader stop position limit switch to I/O channel "
                + RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);
        stopLimitSwitch = new DigitalInput(1, RobotMap.LOADER_STOP_POSITION_LIMIT_SWITCH_DIGITAL_IO_CHANNEL);


        Debug.println("[PIDShooter] Initializing shooter motor encoder to channels "
                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
                                     1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true,
                                     CounterBase.EncodingType.k1X);
        shooterEncoder.setDistancePerPulse(1);
        shooterEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
        shooterEncoder.start();

        Debug.println("[Shooter] Instantiation complete.");
    }

    private boolean canLoadDisc() {
        // If the start position limit switch is triggered then
        // the loader arm is retracted and ready to load another disc
        return true;
        //return startLimitSwitch.get();
    }

    public void loadDisc(double speed) {
        // Until the stop position limit switch is triggered
        // the value returned will be false
        while (!stopLimitSwitch.get()) {
            loaderVictor.set(-speed);
        }
        loaderVictor.set(0);
        printLimitSwitchValues();
    }

    public void resetLoader(double speed) {
        // Until the start position limit switch is triggered
        // the value returned will be false
        while (!startLimitSwitch.get()) {
            loaderVictor.set(speed);
        }
        loaderVictor.set(0);
        printLimitSwitchValues();
    }

    public void loadDisc2(double speed) {
        // Create a timer to measure the execution time
        // for attempting to load the disc.  If we exceed
        // the timeout value then stop.
        Timer timer = new Timer();
        double timeOut = Constants.LOAD_DISC_TIMEOUT_IN_SECS * 1000000.0;

        // Until the stop position limit switch is triggered
        // the value returned will be false
        timer.start();
        while (!stopLimitSwitch.get()) {
            Debug.println("timer = "+timer.get()+", timeOut ="+timeOut);
            if (timer.get() < timeOut) {
                Debug.println("Load disc timed out");
                break;
            }
            loaderVictor.set(-speed);
        }
        timer.stop();
        loaderVictor.set(0);
    }

    public void resetLoader2(double speed) {
        // Create a timer to measure the execution time
        // for attempting to reset the loader arm.  If we
        // exceed the timeout value then stop.
        Timer timer = new Timer();
        double timeOut = Constants.RESET_LOADER_TIMEOUT_IN_SECS * 1000000.0;

        // Until the start position limit switch is triggered
        // the value returned will be false
        timer.start();
        while (!startLimitSwitch.get()) {
            Debug.println("timer = "+timer.get()+", timeOut ="+timeOut);
            if (timer.get() < timeOut) {
                Debug.println("Reset loader is timed out");
                break;
            }
            loaderVictor.set(speed);
        }
        timer.stop();
        loaderVictor.set(0);
    }

    public boolean isLoadDiscFinished() {
        boolean isFinished = stopLimitSwitch.get();
        if (isFinished) Debug.println("Load disc is finished!");
        return isFinished;
    }

    public boolean isResetLoaderFinished() {
        boolean isFinished = startLimitSwitch.get();
        if (isFinished)  Debug.println("Reset loader is finished!");
        return isFinished;
    }

    public void printLimitSwitchValues() {
        Debug.println("start switch = " + startLimitSwitch.get() + ", stop switch = "+ stopLimitSwitch.get());
    }

    public void printEncoderValues() {
        System.out.println("encoder raw = " + shooterEncoder.getRaw() + ", rate = " + shooterEncoder.getRate() + ", pidGet = " + shooterEncoder.pidGet());
    }

    public void startShooter(double speed) {
//      shooterEncoder.start();
        shooterVictor.set(speed);
//        printEncoderValues();
    }

    public void stopShooter() {
        shooterVictor.set(0);
//      shooterEncoder.stop();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
