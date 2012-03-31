/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author dmw
 */
public class RRBridgeArmThread extends Thread
{
    private     RRBridgeArm         bridgeArm;
    
    private     boolean             run = true;
    private     boolean             enableArm = false;
    
    public RRBridgeArmThread(int c)
    {
        bridgeArm = new RRBridgeArm(c);
    }
    
    public void run()
    {
        while (run)
        {
//            System.out.println("RRBridgeArmThread::run() Running...");
            if ( enableArm )
            {
//                System.out.println("RRBridgeArmThread::run() Arming...");
                bridgeArm.arm();
            }
        }
    }
    
    public void enableArm()
    {
        enableArm = true;
    }
    
    public void disableArm()
    {
        enableArm = false;
    }
    
    public void free()
    {
        run = false;
    }
}
