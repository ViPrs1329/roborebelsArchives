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
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.RGBImage;
import java.util.Vector;
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
        requires(vision);
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
            
            Vector passingParticles = collectPassingParticles(largeParticleImage);
            int numberPassingParticles = passingParticles.size();
            Debug.println("DetermineHotGoal " + numberPassingParticles + " passed filtering");
            
            // TODO update the Vision subsystem with the results of the image analysis
            
            
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
    
    private Vector collectPassingParticles ( BinaryImage image ) throws NIVisionException {
 
        int particleCount = image.getNumberParticles();
        Debug.println("DetermineHotGoal: There are " + particleCount + " particles");
        Pointer rawImage = image.image;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        Debug.println("Image width/height is " + imageWidth + "/" + imageHeight);

        Vector passingParticles = new Vector();
        for (int particleNumber = 0; particleNumber < particleCount; particleNumber++) {

            // use to distinquish between vertical and horizontal tape
            double orientation = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_ORIENTATION);

                // TODO if the orientation is not reasonably horizontal, then don't take any
            // further measurements on the particle
                // what is the aspect ratio of the equivalent rectangle which will
            // divide the long dimension over the short dimension.  The equivalent
            // rectangle is defined as one with the same area and perimeter as the particle
            double equivalentRectAspectRatio = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_RATIO_OF_EQUIVALENT_RECT_SIDES);

                // for horizontal tape, in order to make the particle width/height less sensitive
            // to any camera angle in the roll axis, use the bounding rectangle for width but use
            // the average vertical segment length for the height
            double particleWidth = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH);
            double particleHeight = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_AVERAGE_VERT_SEGMENT_LENGTH);
            double calculatedAspectRatio = particleWidth > particleHeight
                    ? particleWidth / particleHeight
                    : particleHeight / particleWidth;

                // TODO determine whether equivalentRectAspectRatio or calculatedAspectRatio should be
            // used to determine the aspect ratio
                // TODO if the aspect ratio is not within limits, then don't take any further
            // measurements
            // where is the particle
            double particleCenterOfMassX = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_X);
            double particleCenterOfMassY = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_Y);

            // log the particle measurements
            Debug.println("Particle " + (particleNumber + 1) + " at ("
                    + particleCenterOfMassX + "," + particleCenterOfMassY + ")\n"
                    + "\tOrientation: " + orientation + "\n"
                    + "\tWidth: " + particleWidth + "\n"
                    + "\tHeight: " + particleHeight + "\n"
                    + "\tEqivRectAspectRatio: " + equivalentRectAspectRatio + "\n"
                    + "\tCalcAspectRatio: " + calculatedAspectRatio + "\n");

            // passed all criteria, so add to the vector
            PassingParticle pp = new PassingParticle();
            pp.relativeX = -1 + 2 * (particleCenterOfMassX / imageWidth);
            pp.relativeY = -1 + 2 * (particleCenterOfMassY / imageHeight);
            pp.orientation = orientation;
            pp.aspectRatio = calculatedAspectRatio;  // TODO which aspect ratio method to use?

            passingParticles.addElement(pp);
        }

        return passingParticles;
    }
    
    /**
     * Used internally to store the critical measurements for a particle
     */
    class PassingParticle {
        // normalized value from -1 to 1 with -1=far left, 0=center, 1=far right
        double relativeX;
        // normalized value from -1 to 1 with -1=far top, 0=center, 1=far bottom
        double relativeY;
        // value from 0 to 180 0=horizontal,90=verticle ????  // TODO confirm this
        double orientation;
        // long dimension / short dimension
        double aspectRatio;
    }
    
}

