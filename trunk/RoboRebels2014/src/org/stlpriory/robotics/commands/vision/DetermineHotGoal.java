/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.stlpriory.robotics.commands.vision;

import com.sun.cldc.jna.Pointer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.NIVision;
import edu.wpi.first.wpilibj.image.NIVisionException;
import java.util.Vector;
import org.stlpriory.robotics.commands.CommandBase;
import org.stlpriory.robotics.misc.Debug;

/**
 * Will use the camera system and attempt to determine which goal is the "hot"
 * goal during autonomous phase.  The "hot" goal will have horizontal retroreflective
 * tape which should reflect the green LED light back to the camera.
 * 
 * The strategy to determine which "particle" is the horizontal strip is to
 * measure the orientation, aspect ratio, and rectangularity of the particle and 
 * if within the expected values, then this is the single horizontal strip particle.  
 * The hope is that at most, a single particle will survive the filtering process,
 * but may need to improve algorithm to choose best match particle if multiple are
 * found.
 * 
 * The center of mass relative X value ( -1 to 1 value range ) of the single 
 * particle is then used to determine if the hot goal is left or right goal.
 * 
 * Performance goal is to execute in less than 1 second for processing of
 * a 640X480 pixel image from the Axis camera.
 * 
 */
public class DetermineHotGoal extends CommandBase {

    // measured in degrees from horizontal
    public static final double FILTER_HORIZONTAL_VARIANCE = 10;
    
    // measured in pixels ^ 2
    public static final double FILTER_MIN_AREA = 50;
    
    // particle width / equivalent rectangle long dimension or
    // vice versa so ratio <= 1.
    public static final double FILTER_MIN_RECTANGULARITY = 0.9;
    
    // horizontal retroreflective tape is 4" wide X 1' 11.5" long
    // so the ideal aspect ratio is 5.875
    public static final double FILTER_ASPECT_RATION_IDEAL = 5.875;
    public static final double FILTER_ASPECT_RATIO_VARIANCE = 1;
    
    // TODO determine ideal value during testing
    public static final double IDEAL_PARTICLE_RELATIVE_Y = 0.1;
    
    // used internally to keep track of whether command is running
    private boolean executing = false;
    
    private AxisCamera camera;
    
    public DetermineHotGoal ( ) {
        super("DetermineHotGoal");
        requires(vision);
        setInterruptible(false);
        log("constructor finished"); 
    }
    
    protected void initialize ( ) {
        log("initialize started");
        log("getting instance of AxisCamera");
        camera = AxisCamera.getInstance();
        if ( camera == null ) {
            logError("Could not get singleton instance of AxisCamera");
        } else {
            log("Successfully retrieved singleton instance of AxisCamera");
        }
        executing = false;
        log("initialize finished");
    }
    
    protected void execute ( ) {
        if ( executing ) {
            // already executing, so don't restart
            log("execute called but already executing");
            return;
        }
        executing = true;
        long startTime = System.currentTimeMillis();
        log("execute start");
        ColorImage image = null;
        BinaryImage thresholdImage = null;
        try {
            image = camera.getImage();
            // image = new RGBImage("/center.jpg");
            
            // 0-255 min/max values for hue, saturation, and value
            // just looking for bright spots on the image and will rely on 
            // particle filtering to ensure only find right shape
            thresholdImage = image.thresholdHSV(0, 255, 0, 255, 230, 255); 
            // thresholdImage.write("threshold.bmp");
            
            // free memory
            image.free();
            image = null;
            
            log("Creating binary image took " + (System.currentTimeMillis() - startTime) + " msec");
            
            Vector passingParticles = filterParticles(thresholdImage);
            
            // free memory
            thresholdImage.free();
            thresholdImage = null;
            
            // look at the particles passing criteria and see if can determine which one is
            // the horizontal tape of the hot goal
            int numberPassingParticles = passingParticles.size();
            log(numberPassingParticles + " passed filtering");
            for ( int i = 0; i < numberPassingParticles; i++ ) {
                PassingParticle pp = (PassingParticle) passingParticles.elementAt(i);

                log("Particle " + (i + 1) + " at ("
                    + pp.relativeX + "," + pp.relativeY + ")\n"
                    + "\tOrientation: " + pp.orientation + "\n"
                    + "\tRectangularity: " + pp.rectangluarity + "\n"
                    + "\tAspectRatio: " + pp.aspectRatio);
            }
            
            if ( numberPassingParticles == 0 ) {
                // could not find the hot goal pattern
                log("No particles passed filtering, so setting null for vision system hot goal relative X");
                vision.setHotGoalRelativeX(null);
            } else if ( numberPassingParticles == 1 ) {
                PassingParticle singleParticle = (PassingParticle) passingParticles.elementAt(0);
                log("Single particle passed filtering, so setting " + singleParticle.relativeX +
                        " for vision system hot goal relative X");
                vision.setHotGoalRelativeX(new Double(singleParticle.relativeX));
            } else {
                PassingParticle bestParticle = determineBestParticle(passingParticles);
                log("Multiple particles passed filtering, but best particle found, so setting " + 
                        bestParticle.relativeX + " for vision system hot goal relative X");
                vision.setHotGoalRelativeX(new Double(bestParticle.relativeX));
            }
            
        } catch (Exception e) {
            logError("Error in execute " + e.getMessage());
        } finally {
            if ( image != null ) {
                try {
                    image.free();
                } catch ( NIVisionException e ) {
                    logError("Exception while trying to free image " + e.getMessage());
                }
            }
            if ( thresholdImage != null ) {
                try {
                    thresholdImage.free();
                } catch ( NIVisionException e ) {
                    logError("Exception while trying to free threshold image " + e.getMessage());
                }
            }

            log("execute finished in " + (System.currentTimeMillis() - startTime) + " msec"); 
            
            executing = false;
        }
    }
    
    protected boolean isFinished ( ) {
        log("isFinished called returning " + !executing);
        return !executing;
    }
    
    protected void end ( ) {
        log("end started");
        // free memory 
        camera = null;
        executing = false;
        log("end finished");
    }
    
    protected void interrupted ( ) {
        // should never be called because this command is not interruptable
    }
    
    private void log ( String msg ) {
        Debug.println("DetermineHotGoal: " + msg );
    }
    
    private void logError ( String msg ) {
        Debug.err("DetermineHotGoal: " + msg);
    }
    
    /**
     * This method will be called only if multiple particles pass the filtering process
     * and we need to determine which is the best particle.  Since the relative Y of
     * the particle should be fairly consistent for the hot goal, the best particle
     * is determined to be the one with the relative Y closest to the ideal relative Y
     * @param particles
     * @return 
     */
    private PassingParticle determineBestParticle ( Vector particles ) {
        PassingParticle bestParticle = null;
        double bestParticleRelativeYDiffFromIdeal = 0;
        for ( int i = 0; i < particles.size(); i++ ) {
            PassingParticle thisParticle = (PassingParticle) particles.elementAt(i);
            if ( bestParticle == null ) {
                // initialize the values with the first element of the vector
                bestParticle = thisParticle;
                bestParticleRelativeYDiffFromIdeal = 
                        Math.abs(IDEAL_PARTICLE_RELATIVE_Y - thisParticle.relativeY);
            } else {
                double thisParticleRelativeYDiffFromIdeal = 
                        Math.abs(IDEAL_PARTICLE_RELATIVE_Y - thisParticle.relativeY);
                if ( thisParticleRelativeYDiffFromIdeal < bestParticleRelativeYDiffFromIdeal ) {
                    // this particle is closer to ideal, so use it as the best particle
                    bestParticle = thisParticle;
                    bestParticleRelativeYDiffFromIdeal = thisParticleRelativeYDiffFromIdeal;
                }
            }
        }
        
        return bestParticle;
    }
    
    private Vector filterParticles ( BinaryImage image ) throws NIVisionException {
 
        int particleCount = image.getNumberParticles();
        log("DetermineHotGoal: There are " + particleCount + " particles");
        Pointer rawImage = image.image;
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        log("DetermineHotGoal: Image width/height is " + imageWidth + "/" + imageHeight);

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
            
            // where is the particle
            double particleCenterOfMassX = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_X);
            double particleCenterOfMassY = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_CENTER_OF_MASS_Y);

            // use to distinquish between vertical and horizontal tape
            double orientation = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_ORIENTATION);
            
            if ( !(orientation < FILTER_HORIZONTAL_VARIANCE) && !(orientation > (180 - FILTER_HORIZONTAL_VARIANCE)) ) {
                // skip this particle since not horizontal
                log("DetermineHotGoal: Skipping particle at (" + particleCenterOfMassX + ", " +
                    particleCenterOfMassY + ") due to orientation " + orientation);
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
                log("DetermineHotGoal: Skipping particle at (" + particleCenterOfMassX + ", " +
                    particleCenterOfMassY + ") due to aspect ratio " + calculatedAspectRatio);
                continue;
            }
            
            // Even a 3 degree camera tilt will decrease the ratio of particle area 
            // to bounding rectangle area to 0.76 for a particle with 5.875 aspect ration, 
            // so it is too sensitive to camera tilt to use as a measure of rectangularity.  
            // Instead we compare the particle length to equivalent rectangle ( same area and perimiter ) length.
            double equivalentRectLength = NIVision.MeasureParticle(rawImage, particleNumber,
                    false, NIVision.MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
            double rectangularity = equivalentRectLength < particleWidth ?
                    equivalentRectLength / particleWidth :
                    particleWidth / equivalentRectLength;
            
            if ( rectangularity < FILTER_MIN_RECTANGULARITY ) {
                log("DetermineHotGoal: Skipping particle at (" + particleCenterOfMassX + ", " +
                    particleCenterOfMassY + ") due to rectangularity " + rectangularity);
                continue;
            }

            log("DetermineHotGoal: Particle passed all constraints. Adding to vector");
            // passed all criteria, so add to the vector
            PassingParticle pp = new PassingParticle();
            pp.relativeX = -1 + 2 * (particleCenterOfMassX / imageWidth);
            pp.relativeY = -1 + 2 * (particleCenterOfMassY / imageHeight);
            pp.orientation = orientation;
            pp.aspectRatio = calculatedAspectRatio;
            pp.rectangluarity = rectangularity;

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
        
        // value in degrees from 0 to 180 0=horizontal,90=verticle, 180=horizontal
        double orientation;
        
        // long dimension / short dimension
        double aspectRatio;
        
        // compares particle width with equivalent rectangle long dimension - max value is 1.0
        // This is a measure of the rectangularity of the particle
        double rectangluarity;
    }
    
}

