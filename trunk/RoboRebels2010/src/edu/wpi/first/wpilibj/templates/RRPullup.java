/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.Victor;
//import edu.wpi.first.wpilibj.Timer;

/*
 * Controls the pullup device.
 */

public class RRPullup
{
    private     Victor      armMotor,
                            winchMotor;
    /*
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
                            pullupStartTime,
                            unwindStartTime;
    */
    private     double      armSpeed,
                            winchSpeed;

    /*
     * Pass in the arm and winch motor victor channel
     */

    public RRPullup( int armMotorChannel, int winchMotorChannel, double aS, double wS )
    {
        armMotor = new Victor(armMotorChannel);
        winchMotor = new Victor(winchMotorChannel);

        /*
        armExtending = armExtended = armRetracting = armRetracted = false;
        winchWinding = winchWound = winchUnwinding = winchUnwound = false;
        extendStartTime = retractStartTime = pullupStartTime = 0;
        */
        if ( aS >= 0.0 && aS <= 1.0 )
            armSpeed = aS;
        else
            armSpeed = 1.0;

        if ( wS >= 0.0 && wS <= 1.0 )
            winchSpeed = wS;
        else
            winchSpeed = 1.0;
    }

    /*
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
    */
    
    public void extendArmStart()
    {
        System.out.println(armSpeed);
        armMotor.set(armSpeed);
        //armExtending = true;
    }

    public void extendArmStop()
    {
        //extendStartTime = 0;
        armMotor.set(0.0);
        //armExtending = false;
    }

    /*
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
    */

    public void retractArmStart()
    {
        System.out.println(-1.0 * armSpeed);
        armMotor.set(-1.0 * armSpeed);
        //armRetracting = true;
    }

    public void retractArmStop()
    {
        armMotor.set(0.0);
        //armRetracting = false;
    }

    /*
    public boolean pullUp(long windTime)
    {
        // if we are not in the process of winding our winch and
        // we are not already wound save the start time and
        // start the motor
        if ( winchWinding == false && winchWound == false )
        {
            pullupStartTime = Timer.getUsClock();
            windWinchStart();
        }

        // if we are in the process of winding our winch
        // and it is not already wound then check the time
        if ( winchWinding == true && winchWound == false )
        {
            // if the time between from when we started and what it is
            // now stop the motor and set the Retracted state to true
            if ( Timer.getUsClock() - pullupStartTime >= windTime )
            {
                windWinchStop();
                winchWound = true;
            }
        }

        // if the winch has been wound return true, otherwise return false
        if (winchWound == true)
            return true;
        else
            return false;
    }
    */

    public void windWinchStart()
    {
        winchMotor.set(winchSpeed);
        //winchWinding = true;
    }

    public void windWinchStop()
    {
        winchMotor.set(0.0);
        //winchWinding = false;
    }

    /*
    public boolean unwind(long windTime)
    {
        // if we are not in the process of Unwinding our winch and
        // we are not already wound save the start time and
        // start the motor
        if ( winchUnwinding == false && winchUnwound == false )
        {
            unwindStartTime = Timer.getUsClock();
            unwindWinchStart();
        }

        // if we are in the process of Unwinding our winch
        // and it is not already wound then check the time
        if ( winchUnwinding == true && winchUnwound == false )
        {
            // if the time between from when we started and what it is
            // now stop the motor and set the Retracted state to true
            if ( Timer.getUsClock() - unwindStartTime >= windTime )
            {
                unwindWinchStop();
                winchUnwound = true;
            }
        }

        // if the winch has been wound return true, otherwise return false
        if (winchUnwound == true)
            return true;
        else
            return false;
    }
    */

    public void unwindWinchStart()
    {
        winchMotor.set(-1.0 * winchSpeed);
        //winchUnwinding = true;
    }

    public void unwindWinchStop()
    {
        winchMotor.set(0.0);
        //winchUnwinding = false;
    }
}
