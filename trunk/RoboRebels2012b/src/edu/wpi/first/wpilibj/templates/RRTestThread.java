/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 * @author dmw
 */
public class RRTestThread extends Thread 
{
    private RRDrive drive;
    
    private double lastTickTime;
    
    public RRTestThread(RRDrive d)
    {
        drive = d;
    }
    
    public RRTestThread()
    {
        drive = new RRDrive(2, 1);
        
        lastTickTime = Timer.getFPGATimestamp();
    }
    
    public void run()
    {
        double currentTime = 0.0;
        
        while (true)
        {
            currentTime = Timer.getFPGATimestamp();
            
            if ( currentTime - lastTickTime >= 0.02 )
            {
                drive.drive(false);
                lastTickTime = Timer.getFPGATimestamp();
                System.out.println("+++ driving from thread... " + currentTime);
            }
            else
            {
                System.out.println("*** not driving at this moment!");
            }
        }
    }
}
