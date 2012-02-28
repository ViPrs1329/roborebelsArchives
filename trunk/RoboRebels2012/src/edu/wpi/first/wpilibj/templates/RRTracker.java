


package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.*;
import edu.wpi.first.wpilibj.ADXL345_I2C;
import com.sun.squawk.util.MathUtils;

/**
 *
 * @author deeek
 */
public class RRTracker
{
    AxisCamera cam;                    // camera object
    CriteriaCollection cc;             // the criteria for doing the particle filter operation
    private static Target[] targets;
    ADXL345_I2C accel;

    public RRTracker()
    {
        Timer.delay(5.0);       // This delay is recommended as the camera takes some time to start up
        cam = AxisCamera.getInstance();

        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        RRTracker.targets = new Target[4];
        accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number

    }
    
    

    public void trackTarget()
    {
        try
        {
           ColorImage image = cam.getImage();     // comment if using stored images

           // TODO: This image write section should be commented out for the production code
            try {
                image.write("/raw.jpg");
            } catch (Exception e) {
                System.out.println("error saving image 1");
            }
            System.out.println("WROTE IMAGE1");

            //BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects

            // TODO:  The white object threshold value needs to be tested to get an optimal number
            BinaryImage thresholdImage = image.thresholdRGB(225, 255, 225, 255, 175, 255);   // keep only White objects

            // blue value was adjusted (above) because of ambient sunlight
           
            
            // TODO: This image write section should be commented out for the production code
            try {
                thresholdImage.write("/after_thresh.bmp");    // this seems to work well
            } catch (Exception e) {
                System.out.println("error saving image 2");
            }
            System.out.println("WROTE IMAGE2");


          //BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
          //BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage convexHullImage = thresholdImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles
          // BinaryImage filteredImage = thresholdImage.particleFilter(cc);           

            
            // TODO: This image write section should be commented out for the production code
            try {
                filteredImage.write("/processed.bmp");     // This seems to work well.
            } catch (Exception e) {
                System.out.println("error saving image 3");
            }
            System.out.println("WROTE IMAGE3");

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();
            for (int i = 0; i < Math.min(reports.length, 4); i++) {
		RRTracker.targets[i] = new Target(reports[i]);
                ParticleAnalysisReport r = reports[i];
                double distance = 18.27 - r.boundingRectWidth/11.0;  // distance to target based on rectangle width
                System.out.println("Target: " + i + "Center: (x,y)  (" + (r.center_mass_x - 160) + "," + (280 - r.center_mass_y) + ") Width: " + r.boundingRectWidth+ " Height: " + r.boundingRectHeight + "Aspect: " + r.boundingRectWidth/r.boundingRectHeight + " Distance: " + distance);
                
                int     targetID = 2;
                
                double angle = RRShooter.determineAngle(distance,targetID);
 //               double angle = 1.5;
                System.out.println("Theta: " + angle);
            }
            
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */

            filteredImage.free();
            convexHullImage.free();
        //  bigObjectsImage.free();
            thresholdImage.free();
            image.free();

            System.out.println("accel angle: " + accelAngle());

        } catch (Exception ex) {
            System.err.println("There was an error while tracking a target!");
            ex.printStackTrace();
        }
    }

    public static Target highestTarget() {
        Target highest = null;

        for (int i = 0; i < targets.length; i++) {
            if (highest == null || targets[i].posY() > highest.posY()) {
                highest = targets[i];
            }
        }

        return highest;
    }

    public static Target[] targets() {
        return targets;
    }


    public double accelAngle() {
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
 //       System.out.println("X Accel: " + axes.XAxis);
 //       System.out.println("Y Accel: " + axes.YAxis);
        double yAxis = Math.min(1, axes.YAxis);
        yAxis = Math.max(-1, yAxis);

        // Need to subtract 90 degrees to return correct angle when
        // accelerometer is mounted on back of shooter

        double angle = 90.0 - (180.0 * MathUtils.asin(yAxis) / Math.PI);

        return angle;
        
    }
  }
