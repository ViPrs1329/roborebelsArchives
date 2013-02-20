/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
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


//        Debug.println("[Shooter] Initializing shooter motor encoder to channels "
//                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
//                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
//        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
//                1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true, EncodingType.k1X);
//        shooterEncoder.setDistancePerPulse(1.0);
//        shooterEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);
//
//        Debug.println("[Shooter] Initializing shooter proximity sensor to I/O channel "
//                + RobotMap.PROXY_SENSOR_IO_CHANNEL);
//        shooterProximitySensor = new DigitalInput(1, RobotMap.PROXY_SENSOR_IO_CHANNEL);

        Debug.println("[Shooter] Instantiation complete.");
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
            while ( !stopLimitSwitch.get() ) {
                loaderVictor.set(-speed);
            }
            printLimitSwitchValues();
            loaderVictor.set(0);
        }
    }

    public boolean isLoadDiscFinished() {
        Debug.println("isLoadDiscFinished");
        return stopLimitSwitch.get();
    }

    public void resetLoader(double speed) {
        // Until the start position limit switch is triggered
        // the value returned will be false
        while (!startLimitSwitch.get()) {
            loaderVictor.set(speed);
        }
        printLimitSwitchValues();
        loaderVictor.set(0);
    }

    public boolean isResetLoaderFinished() {
        Debug.println("isResetLoaderFinished");
        return startLimitSwitch.get();
    }

    public void printLimitSwitchValues() {
        Debug.println("start switch = " + startLimitSwitch.get() + ", stop switch = "+ stopLimitSwitch.get());
    }

    public void startEncoder() {
//        shooterEncoder.start();
//        Debug.println("[Shooter] encoder started.");
    }

    public void stopEncoder() {
//        shooterEncoder.stop();
//        Debug.println("[Shooter] encoder stopped.");
    }

    public void startShooter(double speed) {
        shooterVictor.set(speed);
    }

    public void stopShooter() {
        shooterVictor.set(0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
