/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVisionException;

/**
 * 
 * Thought that this would be a good implementation of putting the 
 * camera control into it's own thread, however, when I tried this
 * I kept getting "Running out of memory" exceptions and the cRIO
 * would actually crash and become unresponsive (ie. it needed a 
 * hard reboot).  I even tried slowing down the capture rate to 500
 * milliseconds and the out of memory exception still occurred.  
 * I'm not sure what is going on....   
 *
 * *hand to forehead*  collected images must be freed!!!!
 * 
 * !!!!!!!!!!!!!!!!!!!!!!!!!
 * !!  N O T E 
 * !!!!!!!!!!!!!!!!!!!!!!!!!
 * 
 * This threading concept for the camera doesn't make sense (at least
 * the way that it's currently implemented).  The image object is 
 * collected and freed before it can ever be used.  And, if it's not freed
 * we will crash the cRIO via a memory leak.  Not sure if this is useful.
 *
 * @author Derek Ward
 */
public class RRCameraThread extends Thread
{
    AxisCamera      cam = null;
    ColorImage      currentImage = null;
    boolean         collectImages;
    final int       IMAGE_CAPTURE_RATE = 100;
    
    public RRCameraThread()
    {
        Timer.delay(10.0);
        
        cam = AxisCamera.getInstance();
        cam.writeMaxFPS(5);
        cam.writeResolution(AxisCamera.ResolutionT.k320x240);
        cam.writeExposurePriority(AxisCamera.ExposurePriorityT.imageQuality);
        cam.writeColorLevel(100);       // Sets the camera to black and white
        
        collectImages = false;
    }
    
    public void run()
    {
        while(true)
        {
            if ( cam != null && cam.freshImage() && collectImages )
            {
                if ( currentImage != null )
                {
                    try {
                        currentImage.free();
                        currentImage = null;
                    } catch (NIVisionException ex) {
                        ex.printStackTrace();
                    }
                }
                
//                System.out.println("RRCameraThread::run() Trying to collect an image!");
                try {
                    currentImage = cam.getImage();
                } catch (AxisCameraException ex) {
                    ex.printStackTrace();
                    System.err.println("RRCameraThread::run() There was an AxisCameraException thrown!");
                    System.exit(1);
                } catch (NIVisionException ex) {
                    ex.printStackTrace();
                    System.err.println("RRCameraThread::run() There was a NIVisionException thrown!");
                    System.exit(1);
                }
            }
            
//            System.out.println("RRCameraThread::run() cam: " + cam + " | " + currentImage + " | " + cam.freshImage() + " | " + collectImages);
            
            
            try {
                RRCameraThread.sleep(IMAGE_CAPTURE_RATE);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        }
    }
    
    public ColorImage getImage()
    {
        return currentImage;
    }
    
    public void collectImages()
    {
        collectImages = true;
    }
    
    public void stopCollectingImages()
    {
        collectImages = false;
    }
}
