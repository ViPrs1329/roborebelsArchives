/*
 * St. Louis Priory School Robotics Club
 * RoboRebels 2010
 *
 * This class implements the robot drive object which is
 * in charge of encapsulating all driving functionality.
 *
 * Maintainer:  David Sescleifer
 *
 * ;;;
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;

public class RRDrive implements MotorSafety {

    private MotorSafetyHelper motorSafetyHelper;
    private Jaguar leftMotor;
    private Jaguar rightMotor;
    private RRXboxController xboxController;
    static final double kDefaultExpirationTime = 0.1;

    /**
     * @param xbc XBox controller
     * @param leftChannel
     * @param rightChannel 
     */
    RRDrive(RRXboxController xbc, int leftChannel, int rightChannel) {
        if (xbc == null || leftChannel <= 0 || rightChannel <= 0) {
            throw new NullPointerException("Invalid argument provided to RRDrive constructor");
        }

        RRLogger.logDebug(this.getClass(), "RRDrive()", "" + leftChannel + " " + rightChannel);
        xboxController = xbc;
        leftMotor  = new Jaguar(leftChannel);
        rightMotor = new Jaguar(rightChannel);
        setupMotorSafety();
    }

    /**
     * 
     * @param tankDrive 
     */
    public void drive(boolean tankDrive) {
        double l_xVal = xboxController.getXAxisState_LeftStick();
        double l_yVal = xboxController.getYAxisState_LeftStick();

        RRLogger.logDebug(this.getClass(), "drive()", "xVal=" + l_xVal + ", yVal=" + l_yVal);
        if (Math.abs(l_xVal) < .13) {
            l_xVal = 0;
        }

        if (Math.abs(l_yVal) < .13) {
            l_yVal = 0;
        }

        //Change the range of the joystick values to account for the dead zone
        if (l_xVal > 0) {
            l_xVal = (l_xVal - .13) / (1 - .13);
        } else if (l_xVal < 0) {
            l_xVal = (l_xVal + .13) / (1 - .13);
        }

        if (l_yVal > 0) {
            l_yVal = (l_yVal - .13) / (1 - .13);
        } else if (l_yVal < 0) {
            l_yVal = (l_yVal + .13) / (1 - .13);
        }

        if (!tankDrive) {
            arcadeDrive(l_yVal, l_xVal);
        } else {
            RRLogger.logDebug(this.getClass(), "drive()", "Tank drive has not been implimented yet!!!");
        }
    }

    /**
     * 
     * @param moveValue
     * @param rotateValue 
     */
    public void arcadeDrive(double moveValue, double rotateValue) {
        double rightMotorSpeed;
        double leftMotorSpeed;

        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }

        setLeftRightMotorValue(leftMotorSpeed, rightMotorSpeed);
    }

    /**
     * 
     * @param left
     * @param right 
     */
    public void setLeftRightMotorValue(double left, double right) {
        System.out.println("RRDrive() Regular mode " + left + " | " + right);
        leftMotor.set(left);
        rightMotor.set(right);

        motorSafetyHelper.feed();
    }

    /**
     * 
     * @param timeout 
     */
    public void setExpiration(double timeout) {
        motorSafetyHelper.setExpiration(timeout);
    }

    /**
     * 
     * @return 
     */
    public double getExpiration() {
        return motorSafetyHelper.getExpiration();
    }

    /**
     * 
     * @return 
     */
    public boolean isAlive() {
        return motorSafetyHelper.isAlive();
    }

    /**
     * 
     * @return 
     */
    public boolean isSafetyEnabled() {
        return motorSafetyHelper.isSafetyEnabled();
    }

    /**
     * 
     * @param enabled 
     */
    public void setSafetyEnabled(boolean enabled) {
        motorSafetyHelper.setSafetyEnabled(enabled);
    }

    /**
     * 
     * @return 
     */
    public String getDescription() {
        return "RR Robot Drive";
    }

    /**
     * 
     */
    public void stopMotor() {
        if (leftMotor != null) {
            leftMotor.set(0.0);
        }
        if (rightMotor != null) {
            rightMotor.set(0.0);
        }

    }

    /**
     * 
     */
    private void setupMotorSafety() {
        motorSafetyHelper = new MotorSafetyHelper(this);
        motorSafetyHelper.setExpiration(kDefaultExpirationTime);
        motorSafetyHelper.setSafetyEnabled(false);
    }
}
