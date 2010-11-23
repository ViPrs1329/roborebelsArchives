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
    
    private     double      armSpeed,
                            armRetractSpeed,
                            winchSpeed;
    


    /*
     * Pass in the arm and winch motor victor channel, arm extend speed,
     * arm retract speed and wench wind and unwind speed
     */

    public RRPullup( int armMotorChannel, int winchMotorChannel, double aS, double aRS, double wS )
    {
        armMotor = new Victor(armMotorChannel);
        winchMotor = new Victor(winchMotorChannel);

        /*
        armExtending = armExtended = armRetracting = armRetracted = false;
        winchWinding = winchWound = winchUnwinding = winchUnwound = false;
        extendStartTime = retractStartTime = pullupStartTime = 0;
        */
        if ( aS >= 0.0 && aS <= 1.0 )
            armSpeed = -1.0 * aS;
        else
            armSpeed = -1.0;

        if ( aRS >= 0.0 && aRS <= 1.0 )
            armRetractSpeed = aRS;
        else
            armRetractSpeed = 0.5;

        if ( wS >= 0.0 && wS <= 1.0 )
            winchSpeed = wS;
        else
            winchSpeed = 1.0;
    }

    
    
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

    

    public void retractArmStart()
    {
        //System.out.println(armRetractSpeed);
        armMotor.set(armRetractSpeed);
        //armRetracting = true;
    }

    public void retractArmStop()
    {
        armMotor.set(0.0);
        //armRetracting = false;
    }

    

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
