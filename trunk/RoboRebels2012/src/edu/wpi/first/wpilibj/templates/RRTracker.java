


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
    
    RRShooter       shooter;
    RRDIPSwitch     dipSwitch;

    public RRTracker(ADXL345_I2C a, RRDIPSwitch the_dipswitch)
    {
        Timer.delay(10.0);       // This delay is recommended as the camera takes some time to start up
        cam = AxisCamera.getInstance();
        System.out.println("Camera");

        cc = new CriteriaCollection();      // create the criteria for the particle filter
        //System.out.println("Criteria Collection ownage");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        //System.out.println("Criteria Collection ownage 2");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        //System.out.println("Criteria Collection ownage 3");
        targets = new Target[4];
        
        dipSwitch = the_dipswitch;
        
        System.out.println("Targets");
        //accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        accel = a;
        System.out.println("accel");

    }
    
    
    public void setShooter(RRShooter s)
    {
        if ( s != null )
            shooter = s;
        else
            throw new NullPointerException("RRTracker was passed a null RRShooter object!");
    }
    
    

    public void trackTarget()
    {
        double start = Timer.getFPGATimestamp();
        double angle;
        try
        {
           
            RoboRebels.tilt_angle = accelAngle();
            
            ColorImage image = cam.getImage();     // comment if using stored images

           // TODO: This image write section should be commented out for the production code
//            try {
//                image.write("/raw.jpg");
//            } catch (Exception e) {
//                System.out.println("error saving image 1");
//            }
//            System.out.println("WROTE IMAGE1");

            //BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects

            // TODO:  The white object threshold value needs to be tested to get an optimal number
            BinaryImage thresholdImage = image.thresholdRGB(225, 255, 225, 255, 175, 255);   // keep only White objects

            // blue value was adjusted (above) because of ambient sunlight
           
            
            // TODO: This image write section should be commented out for the production code
//            try {
//                thresholdImage.write("/after_thresh.bmp");    // this seems to work well
//            } catch (Exception e) {
//                System.out.println("error saving image 2");
//            }
//            System.out.println("WROTE IMAGE2");


          //BinaryImage bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts
          //BinaryImage convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage convexHullImage = thresholdImage.convexHull(false);          // fill in occluded rectangles
            BinaryImage filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles
          // BinaryImage filteredImage = thresholdImage.particleFilter(cc);           

            
            // TODO: This image write section should be commented out for the production code
            try {
                //filteredImage.write("/processed.bmp");     // This seems to work well.
            } catch (Exception e) {
                System.out.println("error saving image 3");
            }
            //System.out.println("WROTE IMAGE3");

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();
            
            int center_target_index = 0;    // initialize to zero 
            int distance_from_center = 160; // set to maximum
            int dist;                       // distance of center of target from the center of the image
            int potential_targets = Math.min(reports.length, 4);    // number of potential targets from image processing
            boolean lowest = true;          // true if center target is the lowest target in the image
          
              if ((reports != null) && (reports.length > 0)) 
              {     
                for (int i = 0; i < potential_targets; i++) 
                {
                    ParticleAnalysisReport r = reports[i];
                    if ((dist = Math.abs(x(r.center_mass_x, 0))) < distance_from_center)  // This does not have camera offset calculation built in
                    {
                        distance_from_center = dist;
                        center_target_index = i;
                    }        
                } 
                               
                if (potential_targets == 1)
                    lowest = true;
                else if (potential_targets == 2)
                {
                    int other_index;
                    if (center_target_index == 0)
                        other_index = 1;
                    else
                        other_index = 0;
                       
                    ParticleAnalysisReport center_target_r = reports[center_target_index];
                    ParticleAnalysisReport other_target_r = reports[other_index];
                    
                    if (y(center_target_r.center_mass_y) <= y(other_target_r.center_mass_y)) 
                        lowest = true;
                    else
                        lowest = false; 
                }
                if (potential_targets == 3)
                {
                    int other_index;
                    int another_index;
                    
                    if (center_target_index == 0)
                    {
                        other_index = 1;
                        another_index = 2;
                    }
                    else if (center_target_index == 1)
                    {  
                        other_index = 0;
                        another_index = 2;
                    }
                    else
                    {
                        other_index = 0;
                        another_index = 1;
                    }
                       
                    ParticleAnalysisReport center_target_r = reports[center_target_index];
                    ParticleAnalysisReport other_target_r = reports[other_index];
                    ParticleAnalysisReport another_target_r = reports[another_index];
                    
                    if ((y(center_target_r.center_mass_y) < y(other_target_r.center_mass_y)) && 
                            (y(center_target_r.center_mass_y) < y(another_target_r.center_mass_y))) 
                        lowest = true;
                    else
                        lowest = false; 
  
                }
                if (potential_targets == 4)
                {
                    int other_index;
                    int another_index;
                    int yet_another_index;
                    
                    if (center_target_index == 0)
                    {
                        other_index = 1;
                        another_index = 2;
                        yet_another_index = 3;
                    }
                    else if (center_target_index == 1)
                    {  
                        other_index = 0;
                        another_index = 2;
                        yet_another_index = 3;
                    }
                    else if (center_target_index == 2)
                    {
                        other_index = 0;
                        another_index = 1;
                        yet_another_index = 3;
                    }
                    else   // center_target_index == 3
                    {
                        other_index = 0;
                        another_index = 1;
                        yet_another_index = 2;
                    }
                       
                    ParticleAnalysisReport center_target_r = reports[center_target_index];
                    ParticleAnalysisReport other_target_r = reports[other_index];
                    ParticleAnalysisReport another_target_r = reports[another_index];
                    ParticleAnalysisReport yet_another_target_r = reports[yet_another_index];
                    
                    if ((y(center_target_r.center_mass_y) <= y(other_target_r.center_mass_y)) && 
                            (y(center_target_r.center_mass_y) <= y(another_target_r.center_mass_y)) &&
                            (y(center_target_r.center_mass_y) <= y(yet_another_target_r.center_mass_y))) 
                        lowest = true;
                    else
                        lowest = false; 
  
                }   
                
                ParticleAnalysisReport r = reports[center_target_index];
                
                double distance = 801.4/r.boundingRectWidth;  // Calculate distance to target based on rectangle width
                
                System.out.println("Target " + center_target_index + "/" + potential_targets + " Center: (x,y)  (" +
                        x(r.center_mass_x, r.boundingRectWidth) + "," + y(r.center_mass_y) + ") Width: " + r.boundingRectWidth + 
                        " Height: " + r.boundingRectHeight + 
                        " Aspect: " + round2((double)r.boundingRectWidth/r.boundingRectHeight) + 
                        " Distance: " + round(distance));
                
                int     targetID = 0;
                
                RoboRebels.going_for_highest = dipSwitch.getState(0);                   // Read first DIP Switch
                System.out.println("DIP Switch 0: " + RoboRebels.going_for_highest);
                    
       //       double display_distance = ((int)(distance * 100))/100.0;
                
                double display_distance = round(distance);
                
                if (lowest && RoboRebels.going_for_highest)
                {
                    targetID = RoboRebels.HIGHEST;
                    RoboRebels.printLCD(5, "Dist: " + display_distance + " to Highest" );
                }
                else if (lowest && !RoboRebels.going_for_highest)
                {
                    targetID = RoboRebels.LOWEST;
                    RoboRebels.printLCD(5, "Dist: " + display_distance + " to Lowest" );
                }
                else if (!lowest)
                {
                    targetID = RoboRebels.MIDDLE;
                    RoboRebels.printLCD(5, "Dist: " + display_distance + " to Middle" );
                }
                   
                // TODO:  Probably only need to do the rest of this if active tracking is happening.
                
                angle = RRShooter.determineAngle(distance, RoboRebels.muzzle_velocity, targetID);
                
                angle = 55.0;
                    
                System.out.println("Muzzle Velocity: " + round(RoboRebels.muzzle_velocity) +
                        " Theta: " + round(angle) + " Tilt_angle: " + round(RoboRebels.tilt_angle));
                
                if (x(r.center_mass_x, r.boundingRectWidth) > RoboRebels.PIXEL_ACCURACY/2)      
                {   if (x(r.center_mass_x, r.boundingRectWidth) > RoboRebels.PIXEL_ACCURACY*3)      
                        RoboRebels.target_azimuth = RoboRebels.FAR_LEFT;  // LazySusan needs to move far to left
                    else
                        RoboRebels.target_azimuth = RoboRebels.LEFT;  // LazySusan needs to move to left
                }
                else if(x(r.center_mass_x, r.boundingRectWidth) < -RoboRebels.PIXEL_ACCURACY/2)
                {
                    if(x(r.center_mass_x, r.boundingRectWidth) < -RoboRebels.PIXEL_ACCURACY*3)
                        RoboRebels.target_azimuth = RoboRebels.FAR_RIGHT;   // LazySusan meeds to move far to left
                    else
                        RoboRebels.target_azimuth = RoboRebels.RIGHT;   // LazySusan meeds to move to left
                }
                else
                {
                    RoboRebels.target_azimuth = RoboRebels.LOCK;   // Don't move, we are facing target!
                    shooter.stopLazySusan();                     // Immediately stop LazySusan to prevent overshoot
                }
                if (angle == 0.0)  // Check to see if there is a valid angle
                {
                    RoboRebels.target_muzzle_velocity = RoboRebels.FASTER; // Muzzle velocity needs to be faster
                    RoboRebels.target_elevation = RoboRebels.HOLD;         // Wait for correct muzzle velocity before tilting
                }
                else if (angle > 80.0)
                {
                    RoboRebels.target_muzzle_velocity = RoboRebels.SLOWER; // Muzzel velocity needs to be slower
                    RoboRebels.target_elevation = RoboRebels.HOLD;         // Wait for correct muzzle velocity before tilting
                }
                else  // The angle is valid, so adjust tilt angle
                {
                    RoboRebels.target_muzzle_velocity = RoboRebels.LOCK; // Muzzle velocity is fine.
                    
                    if (RoboRebels.tilt_angle < (angle - RoboRebels.ANGLE_ACCURACY/2))
                    {
                        if (RoboRebels.tilt_angle < (angle - RoboRebels.ANGLE_ACCURACY*3))
                            RoboRebels.target_elevation = RoboRebels.FAR_UP;  // Shooter needs to tilt far up
                        else
                            RoboRebels.target_elevation = RoboRebels.UP;  // Shooter needs to tilt up
                    }
                    else if (RoboRebels.tilt_angle > (angle + RoboRebels.ANGLE_ACCURACY/2))    
                    {
                        if (RoboRebels.tilt_angle > (angle + RoboRebels.ANGLE_ACCURACY*3))
                            RoboRebels.target_elevation = RoboRebels.FAR_DOWN;
                        else
                             RoboRebels.target_elevation = RoboRebels.DOWN;                        
                    }
                    else
                        RoboRebels.target_elevation = RoboRebels.LOCK;  // Don't move, we are at right angle!          
                }       
            }
                
            
       //     System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());

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

        //    System.out.println("accel angle: " + accelAngle());

        } catch (Exception ex) {
            System.err.println("There was an error while tracking a target!");
            ex.printStackTrace();
        }
        System.out.println(Timer.getFPGATimestamp() - start);
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
    
        public static Target lowestTarget() {
        Target lowest = null;

        for (int i = 0; i < targets.length; i++) {
            if (lowest == null || targets[i].posY() < lowest.posY()) {
                lowest = targets[i];
            }
        }

        return lowest;
    }

    public static Target[] targets() {
        return targets;
    }


    public double accelAngle() {
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
 //       System.out.println("X Accel: " + axes.XAxis);
        System.out.println("Y Accel: " + axes.YAxis);
        System.out.println("Z Accel: " + axes.ZAxis);
        
        // Probably should not get reading from accelerometer if if > 1 or < -1 as it is moving too fast.
        
        double yAxis = Math.min(1, axes.YAxis);
        yAxis = Math.max(-1, yAxis);
        
        double zAxis = Math.min(1, axes.ZAxis);
        zAxis = Math.max(-1, zAxis);
        
        double another_angle = (180.0 * MathUtils.asin(zAxis) / Math.PI);  // Use this angle if angle is greater than 70 degrees
        
        System.out.println("Accel Angle from Z Axis:" + round(another_angle));
        
        // Need to subtract 90 degrees to return correct angle when
        // accelerometer is mounted on back of shooter

        double current_angle = 90.0 - (180.0 * MathUtils.asin(yAxis) / Math.PI);
        
        // Updates current moving average sum by subtracing oldest entry and adding in current entry
        
        RoboRebels.current_angle_sum = RoboRebels.current_angle_sum - RoboRebels.previous_angles[RoboRebels.curent_angle_index] + current_angle;
        
        // Replaces oldest entry with current entry
        
        RoboRebels.previous_angles[RoboRebels.curent_angle_index] = current_angle;
        
        // Increment index, modulo the size of the moving average
        
        RoboRebels.curent_angle_index = (RoboRebels.curent_angle_index + 1) % RoboRebels.NUMBER_OF_PREVIOUS;  // this might need a - 1 here
        
        // Compute the current moving average value
                
        double moving_average_angle = RoboRebels.current_angle_sum / RoboRebels.NUMBER_OF_PREVIOUS;
        
        RoboRebels.printLCD(4, "Tilt ang.: " + round(moving_average_angle));

        return moving_average_angle;
        
    }
    
        public double new_accelAngle() {
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
 //       System.out.println("X Accel: " + axes.XAxis);
        System.out.println("Y Accel: " + axes.YAxis);
        System.out.println("Z Accel: " + axes.ZAxis);
        
        // Probably should not get reading from accelerometer if if > 1 or < -1 as it is moving too fast.
        
        double yAxis = axes.YAxis;
        double zAxis = axes.ZAxis;
        boolean update_moving_average = false;
        double angle_from_y = 45.0;
        double angle_from_z = 45.0;
        double current_angle = 45.0;
        
        if ((yAxis < 1.0) && (yAxis > -1.0))  // Make sure in range for inverse sin operation
        {
               
            // Need to subtract 90 degrees to return correct angle when
            // accelerometer is mounted on back of shooter
            
            angle_from_y = 90.0 - (180.0 * MathUtils.asin(yAxis) / Math.PI);

            System.out.println("Accel Angle from X Axis:" + round(angle_from_y));
 
            update_moving_average = true;
        }
        
                if ((zAxis < 1.0) && (zAxis > -1.0))  // Make sure in range for inverse sin operation
        {           
            angle_from_z = (180.0 * MathUtils.asin(zAxis) / Math.PI);  // Use this angle if angle is greater than 70 degrees

            System.out.println("Accel Angle from Z Axis:" + round(angle_from_z));

            update_moving_average = true;
        }
                
        if ((angle_from_y > 60.0) && (angle_from_z > 60.0))
            current_angle = angle_from_z;                         // choose Z as Y is unreliable
        else if ((angle_from_y < 60.0) && (angle_from_z < 60.0))
            current_angle = (angle_from_y + angle_from_z) / 2.0;  // average Y and Z readings
        else if ((angle_from_y > 0) && (angle_from_y < 90))
            current_angle = angle_from_y;                         // choose Y if valid
        else
            update_moving_average = false;                        // otherwise, no valid angle - don't update MA
        
        if (update_moving_average)
        {
            // Updates current moving average sum by subtracing oldest entry and adding in current entry

            RoboRebels.current_angle_sum = RoboRebels.current_angle_sum - RoboRebels.previous_angles[RoboRebels.curent_angle_index] + current_angle;

            // Replaces oldest entry with current entry

            RoboRebels.previous_angles[RoboRebels.curent_angle_index] = current_angle;

            // Increment index, modulo the size of the moving average

            RoboRebels.curent_angle_index = (RoboRebels.curent_angle_index + 1) % RoboRebels.NUMBER_OF_PREVIOUS;  // this might need a - 1 here

        }
        
        // Compute and return the current moving average value
                
        double moving_average_angle = RoboRebels.current_angle_sum / RoboRebels.NUMBER_OF_PREVIOUS;
        
        RoboRebels.printLCD(4, "Tilt ang.: " + round(moving_average_angle));

        return moving_average_angle;
        
    }
    
    public int x(int raw_x, int target_image_width)
    {   
        double camera_offset = 10.0;   // camera offset from center of robot in inches.  Positive is to the right side of robot
        double target_width = 24.0;     // width of backboard target in inches   
        int correction;
        
        correction = (int)(((camera_offset/target_width) * target_image_width) + 0.5);
        
        System.out.println("x: " + raw_x + "correction: " + correction);
        
        return (raw_x - 160 + correction);
    }
    
    public int y(int raw_y)
    {   
         return (280 - raw_y);
    }
    
    
    static public double round(double x)  // Rounds double X to one decimal place
    {
        double z = ((int)((x + 0.5) * 10.0))/10.0;
                       
        return z;
                
    }
    
    static public double round2(double x)  // Rounds double X to two decimal places
    {
        double z = ((int)((x + 0.05) * 100.0))/100.0;
                       
        return z;
                
    }
   
  }
