/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.commands.vision;

import com.sun.cldc.jna.Pointer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.RGBImage;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 * Will use the camera system and attempt to determine which goal is the "hot"
 * goal during autonomous phase.  The "hot" goal will have horizontal retroreflective
 * tape which should reflect the green LED light back to the camera.
 * 
 * The strategy to determine which "particle" is the horizontal strip is to
 * measure the orientation and aspect ratio of the particle and if within the
 * expected values, then this is the single horizontal strip particle.  The hope
 * is that at most, a single particle will survive the filtering process
 * 
 * The center of mass relative X value ( -1 to 1 value range ) of the single 
 * particle is then used to determine if the hot goal is left or right goal.
 * 
 */
public class DetermineHotGoal extends CommandBase {
    
    private boolean isFinished = false;
    private static final boolean isDebug = true;
    private CriteriaCollection cc;
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
//        requires(vision);
        Debug.println("DetermineHotGoal constructor finished"); 
    }
    
    protected void initialize ( ) {
        cc = new CriteriaCollection();
        // only the lower range is important because the upper range is just
        // intended to be a really big number.  We are filtering out the
        // small particles and the units are pixels squared
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_AREA, 50, 65535, false);
        Debug.println("DetermineHotGoal initialize finished");
    }
    
    protected void execute ( ) {
        isFinished = false;
        long startTime = System.currentTimeMillis();
        Debug.println("DetermineHotGoal execute start");
        ColorImage image = null;
        BinaryImage thresholdImage = null;
        BinaryImage convexHullImage = null;
        BinaryImage largeParticleImage = null;
        try {
            image = new RGBImage("/center.jpg");
            // 0-255 min/max values for hue, saturation, and value
            // optimized to pick up green LED reflection from retro reflective tape
            thresholdImage = image.thresholdHSV(99, 163, 0, 255, 230, 255); 
            image.free();
            image = null;
            if ( isDebug ) {
                thresholdImage.write("/threshold.bmp");
            }
            convexHullImage = thresholdImage.convexHull(false);
            thresholdImage.free();
            thresholdImage = null;
            if ( isDebug ) {
                convexHullImage.write("/convexHull.bmp");
            }
            // filter out small particles
            largeParticleImage = convexHullImage.particleFilter(cc);  
            convexHullImage.free();
            convexHullImage = null;
            if ( isDebug ) {
                largeParticleImage.write("/largeParticles.bmp");
            }
            
            int particleCount = largeParticleImage.getNumberParticles();
            Debug.println("DetermineHotGoal: There are " + particleCount + " large particles");
            Pointer rawImage = largeParticleImage.image;
            int imageWidth = largeParticleImage.getWidth();
            int imageHeight = largeParticleImage.getHeight();
            Debug.println("Image width/height is " + imageWidth + "/" + imageHeight);
            for ( int particleNumber = 0; particleNumber < particleCount; particleNumber++ ) {
                double orientation = NIVision.MeasureParticle(rawImage, particleNumber, 
                        false, NIVision.MeasurementType.IMAQ_MT_ORIENTATION);
                double particleWidth = NIVision.MeasureParticle(rawImage, particleNumber, 
                        false, NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
                double particleHeight = NIVision.MeasureParticle(rawImage, particleNumber, 
                        false, NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT);
                double particleCenterOfMassX = NIVision.MeasureParticle(rawImage, particleNumber, 
                        false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_X);
                double particleCenterOfMassY = NIVision.MeasureParticle(rawImage, particleNumber, 
                        false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_Y);
                
                Debug.println("Particle " + (particleNumber + 1) + " at (" +
                    particleCenterOfMassX + "," + particleCenterOfMassY + ")\n" +
                    "\tOrientation: " + orientation + "\n" +
                    "\tWidth: " + particleWidth + "\n" +
                    "\tHeight: " + particleHeight + "\n" +
                    "\tWidth/Height: " + particleWidth / particleHeight + "\n");
            }
            
        } catch (Exception e) {
            Debug.err("Error in DetermineHotGoal execute " + e.getMessage());
        } finally {
            if ( image != null ) {
                try {
                    image.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free image " + e.getMessage());
                }
            }
            if ( thresholdImage != null ) {
                try {
                    thresholdImage.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free threshold image " + e.getMessage());
                }
            }
            if ( convexHullImage != null ) {
                try {
                    convexHullImage.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free convex hull image " + e.getMessage());
                }
            }
            if ( largeParticleImage != null ) {
                try {
                    largeParticleImage.free();
                } catch ( Exception e ) {
                    Debug.err("Exception while trying to free large particle image " + e.getMessage());
                }
            }
            long stopTime = System.currentTimeMillis();
            Debug.println("DetermineHotGoal execute finished in " + (stopTime - startTime) + " msec");
            isFinished = true;
        }
    }
    
    protected boolean isFinished ( ) {
        return isFinished;
    }
    
    protected void end ( ) {
        
    }
    
    protected void interrupted ( ) {
        
    }
    
}

