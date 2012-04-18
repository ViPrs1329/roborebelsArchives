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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.MotorSafety;
import edu.wpi.first.wpilibj.MotorSafetyHelper;

public class RRDrive implements MotorSafety {

    private MotorSafetyHelper motorSafetyHelper;
    private boolean arcade;
    private Jaguar leftMotor,
            rightMotor;
    private boolean backbuttonpressed;
    public static final double kDefaultExpirationTime = 0.1;

    /**
     * 
     * @param js
     * @param leftChannel
     * @param rightChannel 
     */
    public RRDrive(int leftChannel, int rightChannel) {
        //RRLogger.logDebug(this.getClass(),"RRDrive()","" + leftChannel + " " + rightChannel);

        setupDrive(leftChannel, rightChannel);

        setupMotorSafety();
    }

    /**
     * 
     * @param lc
     * @param rc 
     */
    private void setupDrive(int lc, int rc) {
        leftMotor = new Jaguar(lc);
        rightMotor = new Jaguar(rc);
    }

    /**
     * 
     * @param tankDrive 
     */
    public void drive(boolean tankDrive) {
//        double l_xVal  = leftJoystick.getRawAxis(1);
//        double l_yVal  = leftJoystick.getRawAxis(2);
//
//        double r_xVal  = leftJoystick.getRawAxis(4);
//        double r_yVal  = leftJoystick.getRawAxis(5);

        RRAction aoX = RRButtonMap.getActionObject(RRButtonMap.ARCADE_STICK_X),
                aoY = RRButtonMap.getActionObject(RRButtonMap.ARCADE_STICK_Y);

        double l_xVal = aoX.getAxisState();
        double l_yVal = aoY.getAxisState();

        //RRLogger.logDebug(this.getClass(),"drive()","");


        if (Math.abs(l_xVal) < .13) {
            l_xVal = 0;
        }

        if (Math.abs(l_yVal) < .13) {
            l_yVal = 0;
        }

//        if (Math.abs(r_xVal) < .13)
//        {
//            r_xVal = 0;
//        }
//
//        if (Math.abs(r_yVal) < .13)
//        {
//            r_yVal = 0;
//        }




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

//        if (r_xVal > 0)
//        {
//            r_xVal = (r_xVal-.13)/(1-.13);
//        }
//        else if (r_xVal < 0)
//        {
//            r_xVal = (r_xVal+.13)/(1-.13);
//        }
//
//        if (r_yVal > 0)
//        {
//            r_yVal = (r_yVal-.13)/(1-.13);
//        }
//        else if (r_yVal < 0)
//        {
//            r_yVal = (r_yVal+.13)/(1-.13);
//        }


        if (!tankDrive) {
            arcadeDrive(l_yVal, l_xVal);
        } else {
            //tank mode
            //RRLogger.logDebug(this.getClass(),"drive()","Tank drive has not been implimented yet!!!");
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

        if ((moveValue != 0.0) || (rotateValue != 0.0)) {
            RoboRebels.elevation_lock = false;                      // remove lock if robot is driven.
        }
        //      RRLogger.logDebug(this.getClass(),"arcadeDrive()","mv: " + RRTracker.round2(moveValue) + " rv: " + RRTracker.round2(rotateValue) + " lms: " + RRTracker.round2(leftMotorSpeed) + " rms: " + RRTracker.round2(rightMotorSpeed));

//        RRLogger.logDebug(this.getClass(),"arcadeDrive()","Left Motor Speed: " + leftMotorSpeed + "RMS: " + rightMotorSpeed);

        setLeftRightMotorValue(leftMotorSpeed, rightMotorSpeed);
    }

    /**
     * 
     * @param left
     * @param right 
     */
    public void setLeftRightMotorValue(double left, double right) {
        if (leftMotor == null || rightMotor == null) {
            throw new NullPointerException("Null motor provided");
        }
        RRAction me = RRButtonMap.getActionObject(RRButtonMap.TOGGLE_SPEED);
        boolean toggledspeed = me.getButtonState();
        if (toggledspeed) {
            left = left * 0.5;
            right = right * 0.5;
            backbuttonpressed = true;
        } else {
            /*if (backbuttonpressed=false){
            left = left*2;
            right= right*2;
            }*/
        }
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
