/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.image.*;

/**
 *
 * @author deeek
 */
public class RRTracker {
    
        AxisCamera cam;                    // camera object
        CriteriaCollection cc;             // the criteria for doing the particle filter operation
        
    public RRTracker(){
        Timer.delay(5.0);
        cam = AxisCamera.getInstance();
        
        cc = new CriteriaCollection();      // create the criteria for the particle filter
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
    }
    
    public void trackTarget(){
        try {
            /**
             * Do the image capture with the camera and apply the algorithm described above. This
             * sample will either get images from the camera or from an image file stored in the top
             * level directory in the flash memory on the cRIO. The file name in this case is "10ft2.jpg"
             *
  
  */
            
           System.out.println("Starting Auto mode");
           ColorImage image = cam.getImage();     // comment if using stored images
//
            try {
                image.write("/raw.jpg");
            } catch (Exception e) {
                System.out.println("error saving image");
            }
            System.out.println("WROTE IMAGE1");

            //BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects
            
            BinaryImage thresholdImage = image.thresholdRGB(225, 255, 225, 255, 225, 255);   // keep only White objects
            
            try {
                thresholdImage.write("/after_thresh.bmp");    // this seems to work well
            } catch (Exception e) {
                System.out.println("error saving image");
            }
            System.out.println("WROTE IMAGE2");
            BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
            BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles

            try {
                filteredImage.write("/processed.bmp");     // This seems to work well.
            } catch (Exception e) {
                System.out.println("error saving image");
            }
            System.out.println("WROTE IMAGE3");
            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();  // get list of results
            for (int i = 0; i < reports.length; i++) {                                // print results
                ParticleAnalysisReport r = reports[i];
                double distance = 18.27 - r.boundingRectWidth/11.0;  // distance to target based on rectangle width
                System.out.println("Particle: " + i + ":  Center x: " + r.center_mass_x + ":  Center y: " + r.center_mass_y + " Width: " + r.boundingRectWidth+ " Height: "
                         + r.boundingRectHeight + " Distance: " + distance);
            }
            System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             */

            filteredImage.free();
            convexHullImage.free();
            bigObjectsImage.free();
            thresholdImage.free();
            image.free();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
