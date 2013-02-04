/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.subsystems;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Encoder.PIDSourceParameter;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.stlpriory.robotics.RobotMap;
import org.stlpriory.robotics.misc.Constants;
import org.stlpriory.robotics.misc.Debug;

/**
 *
 */
public class Shooter extends Subsystem {

    private static Servo shooterServo = null;
    private static SpeedController shooterVictor = null;
    // Quadrature encoder used to measure shoot motor speed
    private static Encoder shooterEncoder;

    private static DigitalInput shooterDigitalInput1;

    public Shooter() {
        super("Shooter");
        Debug.println("[Shooter] Instantiating...");

        // Set the servo and victor to the PWM channels on the digital module
        // to which the servo is attached.
        Debug.println("[Shooter] Initializing shooter motor speed controller to PWM channel "
                + RobotMap.SHOOTER_VICTOR_PWM_CHANNEL + " on the digital module.");
        shooterVictor = new Victor(RobotMap.SHOOTER_VICTOR_PWM_CHANNEL);

        Debug.println("[Shooter] Initializing shooter disc loader servo to PWM channel "
                + RobotMap.SHOOTER_SERVO_PWM_CHANNEL + " on the digital module.");
        shooterServo = new Servo(RobotMap.SHOOTER_SERVO_PWM_CHANNEL);

//        Debug.println("[Shooter] Initializing shooter motor encoder to channels "
//                + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A
//                + " and " + RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B);
//        shooterEncoder = new Encoder(1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_A,
//                1, RobotMap.SHOOTER_ENCODER_DIGITAL_IO_CHANNEL_B, true, EncodingType.k1X);
//        shooterEncoder.setDistancePerPulse(1.0);
//        shooterEncoder.setPIDSourceParameter(PIDSourceParameter.kRate);

        Debug.println("[Shooter] Initializing shooter proximity sensor to channel "
                + RobotMap.PROXY_SENSOR_IO_CHANNEL);
        shooterDigitalInput1 = new DigitalInput(1, RobotMap.PROXY_SENSOR_IO_CHANNEL);

        Debug.println("[Shooter] Instantiation complete.");
    }

    private boolean canLoadDisc() {
        return true;
    }

    public void loadDisc() {
        if (canLoadDisc()) {
            setAngle(Constants.MAX_LOADER_SERVO_ANGLE);
        }
    }

    public void resetLoader() {
        setAngle(Constants.MIN_LOADER_SERVO_ANGLE);
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
        //System.out.println(shooterEncoder.getRate());
        System.out.println(shooterDigitalInput1.get());
    }

    public void stopShooter() {
        shooterVictor.set(0);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    public void setAngle(double angle) {
        shooterServo.setAngle(angle);
    }
}
