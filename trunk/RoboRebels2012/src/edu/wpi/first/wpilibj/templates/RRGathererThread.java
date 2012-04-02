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
public class RRGathererThread extends Thread
{
    private     RRGatherer      gatherer;
    
    private     boolean         run = true;
    private     boolean         isGatherable = false;
    
    private     double          lastTickTime = Timer.getFPGATimestamp();
    
    public RRGathererThread(int sc, int bcc, int bbsc, int mbsc, int tbsc)
    {
        if ( gatherer == null )
            gatherer = new RRGatherer( sc, bcc, bbsc, mbsc, tbsc);
    }
    
    public void run()
    {
        long cycleDuration = 0;
        
        while (run)
        {
            lastTickTime = Timer.getFPGATimestamp();
            
//            System.out.println("RRGathererThread::run() Running...");
            if ( isGatherable )
            {
//                System.out.println("RRGathererThread::run() Gathering...");
                gatherer.gather();
            }
            
            cycleDuration = (long) Math.ceil(1000 * (Timer.getFPGATimestamp() - lastTickTime));
            
            
            
            try {
                RRGathererThread.sleep(20 - cycleDuration);
            } catch (InterruptedException ex) {
                System.err.println("RRGathererThread::run() - Interrupted Exception!");
            }
            
            System.out.println("RRGathererThread::run() time(ms) " + cycleDuration);
        }
    }
    
    public void free()
    {
        run = false;
    }
    
    public void enableGatherer()
    {
        isGatherable = true;
    }
    
    public void disableGatherer()
    {
        isGatherable = false;
    }
    
    public RRGatherer getGatherer()
    {
        return gatherer;
    }
}
