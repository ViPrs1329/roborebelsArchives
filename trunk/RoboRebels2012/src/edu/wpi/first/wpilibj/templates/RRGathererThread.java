/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author dmw
 */
public class RRGathererThread extends Thread
{
    private     RRGatherer      gatherer;
    
    private     boolean         run = true;
    private     boolean         isGatherable = false;
    
    public RRGathererThread(int sc, int bcc, int bbsc, int mbsc, int tbsc)
    {
        if ( gatherer == null )
            gatherer = new RRGatherer( sc, bcc, bbsc, mbsc, tbsc);
    }
    
    public void run()
    {
        while (run)
        {
//            System.out.println("RRGathererThread::run() Running...");
            if ( isGatherable )
            {
//                System.out.println("RRGathererThread::run() Gathering...");
                gatherer.gather();
            }
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
