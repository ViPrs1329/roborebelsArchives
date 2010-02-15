/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Timer;

/*
 * Controls the pullup device.
 */

public class RRPullup
{
    private     Victor      armMotor,
                            winchMotor;
    private     boolean     armExtending,
                            armExtended,
                            armRetracting,
                            armRetracted,
                            winchWinding,
                            winchWound,
                            winchUnwinding,
                            winchUnwound;
    private     long        extendStartTime,
                            retractStartTime,
                            pullupStartTime;
    private     double      armSpeed;

    /*
     * Pass in the arm and winch motor victor channel
     */

    public RRPullup( int armMotorChannel, int winchMotorChannel, double aS )
    {
        armMotor = new Victor(armMotorChannel);
        winchMotor = new Victor(winchMotorChannel);

        armExtending = armExtended = armRetracting = armRetracted = false;
        winchWinding = winchWound = winchUnwinding = winchUnwound = false;
        extendStartTime = retractStartTime = pullupStartTime = 0;
        if ( aS >= 0.0 && aS <= 1.0 )
            armSpeed = aS;
        else
            armSpeed = 1.0;
    }

    public boolean extendArm(long armExtendTime)
    {
        // if we are not in the process of extending our arm and
        // we are not already extended save the start time and
        // start the motor
        if ( armExtending == false && armExtended == false )
        {
            extendStartTime = Timer.getUsClock();
            extendArmStart();
        }

        // if we are in the process of extending our arm
        // and it is not already extended then check the time
        if ( armExtending == true && armExtended == false )
        {
            // if the time between from when we started and what it is
            // now stop the motor and set the extended state to true
            if ( Timer.getUsClock() - extendStartTime >= armExtendTime )
            {
                extendArmStop();
                armExtended = true;
            }
        }

        // if the arm has been extended return true, otherwise return false
        if (armExtended == true)
            return true;
        else
            return false;
    }

    public void extendArmStart()
    {
        armMotor.set(armSpeed);
        armExtending = true;
    }

    public void extendArmStop()
    {
        extendStartTime = 0;
        armMotor.set(0.0);
        armExtending = false;
    }

    public boolean retractArm(long armRetractTime)
    {
        // if we are not in the process of Retracting our arm and
        // we are not already Retracted save the start time and
        // start the motor
        if ( armRetracting == false && armRetracted == false )
        {
            retractStartTime = Timer.getUsClock();
            retractArmStart();
        }

        // if we are in the process of Retracting our arm
        // and it is not already Retracted then check the time
        if ( armRetracting == true && armRetracted == false )
        {
            // if the time between from when we started and what it is
            // now stop the motor and set the Retracted state to true
            if ( Timer.getUsClock() - retractStartTime >= armRetractTime )
            {
                retractArmStop();
                armRetracted = true;
            }
        }

        // if the arm has been Retracted return true, otherwise return false
        if (armRetracted == true)
            return true;
        else
            return false;
    }

    public void retractArmStart()
    {
        armMotor.set(-1.0 * armSpeed);
        armRetracting = true;
    }

    public void retractArmStop()
    {
        armMotor.set(0.0);
        armRetracting = false;
    }

    public void pullUp()
    {

    }

    public void windWinchStart()
    {

    }

    public void windWinchStop()
    {

    }

    public void unwindWinchStart()
    {

    }

    public void unwindWinchStop()
    {

    }
}
