/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.commands.vision;

import com.sun.cldc.jna.Pointer;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
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

    // measured in degrees from horizontal
    public static final double FILTER_HORIZONTAL_VARIANCE = 10;
    
    // measured in pixels ^ 2
    public static final double FILTER_MIN_AREA = 50;
    
    // particle area / particle bounding rectangle area
    public static final double FILTER_MIN_COMPACTNESS = 0.9;
    
    // horizontal retroreflective tape is 4" wide X 1' 11.5" long
    // so the ideal aspect ratio is 5.875
    public static final double FILTER_ASPECT_RATION_IDEAL = 5.875;
    public static final double FILTER_ASPECT_RATIO_VARIANCE = 1;
    
    private boolean isFinished = false;
    private StringBuffer logs;
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
        requires(vision);
        Debug.println("DetermineHotGoal constructor finished"); 
    }
    
    protected void initialize ( ) {
        Debug.println("DetermineHotGoal initialize finished");
    }
    
    protected void execute ( ) {
        isFinished = false;
        logs = new StringBuffer();
        long startTime = System.currentTimeMillis();
        log("DetermineHotGoal execute start");
        ColorImage image = null;
        BinaryImage thresholdImage = null;
        try {
            image = new RGBImage("/center.jpg");
            
            // 0-255 min/max values for hue, saturation, and value
            // optimized to pick up green LED reflection from retro reflective tape
            thresholdImage = image.thresholdHSV(99, 163, 0, 255, 230, 255); 
            // thresholdImage.write("threshold.bmp");
            
            // free memory
            image.free();
            image = null;
            
            log("Forming images took " + (System.currentTimeMillis() - startTime) + " msec");
            
            Vector passingParticles = filterParticles(thresholdImage);
            
            // free memory
            thresholdImage.free();
            thresholdImage = null;
            
            // look at the particles passing criteria and see if can determine which one is
            // the horizontal tape of the hot goal
            int numberPassingParticles = passingParticles.size();
            log("DetermineHotGoal " + numberPassingParticles + " passed filtering");
            for ( int i = 0; i < numberPassingParticles; i++ ) {
                PassingParticle pp = (PassingParticle) passingParticles.elementAt(i);

                log("Particle " + (i + 1) + " at ("
                    + pp.relativeX + "," + pp.relativeY + ")\n"
                    + "\tOrientation: " + pp.orientation + "\n"
                    + "\tCompactness: " + pp.compactness + "\n"
                    + "\tAspectRatio: " + pp.aspectRatio);
            }
            
            // TODO update the Vision subsystem with the results of the image analysis
            
        } catch (Exception e) {
            Debug.err("Error in DetermineHotGoal execute " + e.getMessage());
        } finally {
            if ( image != null ) {
                try {
                    image.free();
                } catch ( NIVisionException e ) {
                    Debug.err("Exception while trying to free image " + e.getMessage());
                }
            }
            if ( thresholdImage != null ) {
                try {
                    thresholdImage.free();
                } catch ( NIVisionException e ) {
                    Debug.err("Exception while trying to free threshold image " + e.getMessage());
                }
            }

            log("DetermineHotGoal execute finished in " + (System.currentTimeMillis() - startTime) + " msec"); 
            String logString = logs.toString();
            Debug.println(logString);
            Debug.println(logString);
            Debug.println(logString);
            
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
    
    protected void log ( String msg ) {
        logs.append(msg + "\n");
    }
    
    protected Vector filterParticles ( BinaryImage image ) throws NIVisionException {
 
        int particleCount = image.getNumberParticles();
        log("DetermineHotGoal: There are " + particleCount + " particles");
        Pointer rawImage = image.image;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        log("Image width/height is " + imageWidth + "/" + imageHeight);

        Vector passingParticles = new Vector();
        
        for ( int particleNumber = 0; particleNumber < particleCount; particleNumber++ ) {
            log("DetermineHotGoal: Analyzing particle number " + (particleNumber + 1));
            
            double area = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_AREA);
            if ( area < FILTER_MIN_AREA ) {
                // skip this particle since it is too small
                log("DetermineHotGoal: Skipping particle due to small area " + area);
                continue;
            }

            // use to distinquish between vertical and horizontal tape
            double orientation = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_ORIENTATION);
            
            if ( !(orientation < FILTER_HORIZONTAL_VARIANCE) && !(orientation > (180 - FILTER_HORIZONTAL_VARIANCE)) ) {
                // skip this particle since not horizontal
                log("DetermineHotGoal: Skipping particle due to orientation " + orientation);
                continue;
            }

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
            
            // filter by aspect ratio
            if ( calculatedAspectRatio < FILTER_ASPECT_RATION_IDEAL - FILTER_ASPECT_RATIO_VARIANCE || 
                    calculatedAspectRatio > FILTER_ASPECT_RATION_IDEAL + FILTER_ASPECT_RATIO_VARIANCE ) {
                // skip this particle since not of correct aspect ratio
                log("DetermineHotGoal: Skipping particle due to aspect ratio " + calculatedAspectRatio);
                continue;
            }
            
            // measure the compactness of the particle by dividing the area of the particle
            // by the area of the bounding rectangle.  A perfectly rectangular particle with
            // perfectly level camera image would have a value of 1
            double compactness = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_COMPACTNESS_FACTOR);
            
            if ( compactness < FILTER_MIN_COMPACTNESS ) {
                log("DetermineHotGoal: Skipping particle due to compactness " + compactness);
                continue;
            }
            
            // where is the particle
            double particleCenterOfMassX = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_X);
            double particleCenterOfMassY = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_Y);

            log("DetermineHotGoal: Particle passed all constraints. Adding to vector");
            // passed all criteria, so add to the vector
            PassingParticle pp = new PassingParticle();
            pp.relativeX = -1 + 2 * (particleCenterOfMassX / imageWidth);
            pp.relativeY = -1 + 2 * (particleCenterOfMassY / imageHeight);
            pp.orientation = orientation;
            pp.aspectRatio = calculatedAspectRatio;
            pp.compactness = compactness;

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
        
        // value from 0 to 180 0=horizontal,90=verticle, 180=horizontal
        double orientation;
        
        // long dimension / short dimension
        double aspectRatio;
        
        // area of particle/area of bounding rectangle - max value is 1.0
        // This is a measure of the rectangularity of the particle
        double compactness;
    }
    
}

