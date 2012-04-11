


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
//    RRDIPSwitch     dipSwitch;
    RRCameraThread  cameraThread;
    
//    public RRTracker(ADXL345_I2C a, RRDIPSwitch the_dipswitch)
    public RRTracker(ADXL345_I2C a)
    {
        
        Timer.delay(10.0);       // This delay is recommended as the camera takes some time to start up
        cam = AxisCamera.getInstance();
        
        
//        cameraThread = new RRCameraThread();
//        cameraThread.collectImages();
//        cameraThread.start();
        System.out.println("Camera");

        cc = new CriteriaCollection();      // create the criteria for the particle filter
        //System.out.println("Criteria Collection ownage");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        //System.out.println("Criteria Collection ownage 2");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        //System.out.println("Criteria Collection ownage 3");
        targets = new Target[4];
        
 //       dipSwitch = the_dipswitch;
        
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
    
    

    public void trackTarget(int target_selected)   // Target selected is HIGEST, LOWEST, LEFT, RIGHT, or AUTO
    {
        double start = Timer.getFPGATimestamp();
        double angle;
        boolean TTState;
        
        try
        {
             RoboRebels.tilt_angle = accelAngle();
             RoboRebels.printLCD(4, "Tilt: " + round(RoboRebels.tilt_angle) + "    ");
             
             
             if (shooter.tracking)
             {
                 
                if (RoboRebels.DEBUG_ON)
                    System.out.println("Tracker: Tracking target...");
         
                ColorImage image = cam.getImage();     // comment if using stored images
                 
//                 ColorImage image = cameraThread.getImage();
                 
//                 System.out.println("RRTracker::trackTarget() " + image);
                 
            if (RoboRebels.save_camera_image_file)  // Only do this durng initial competition setup to adjust levels
            {
                try {
                    if ( image != null )
                        image.write("/raw.jpg");
                    else
                    {
                        System.out.println("Image is null!!!");
                    }
                } catch (Exception e) {
                    System.out.println("error saving image 1");
                }
                System.out.println("Saved raw camera image to cRIO");
                
                RoboRebels.save_camera_image_file = false;      // Only save one image at start of match.
            }
                
  //              RoboRebels.save_camera_image_file = false;      // Only save one image at start of match.
  //          }

            /*  
             *   Choose thresholds for white by setting save_camer_image_file variable and FTPing the image off cRIO and examining in PhotoShop
             */
            
            //BinaryImage thresholdImage = image.thresholdRGB(25, 255, 0, 45, 0, 47);   // keep only red objects

            // TODO:  The white object threshold value needs to be tested to get an optimal number
//            BinaryImage thresholdImage = image.thresholdRGB(225, 255, 225, 255, 175, 255);   // keep only White objects
//            BinaryImage thresholdImage = image.thresholdRGB(225, 255, 225, 255, 205, 255);   // keep only White objects when there is sunlight
//             BinaryImage thresholdImage = image.thresholdRGB(235, 255, 235, 255, 235, 255);   // Adjusted for Chaivitz Arena lighting
              BinaryImage thresholdImage = image.thresholdRGB(0, 0, 85, 255, 0, 0);        // Green LED mask test

            // blue value was adjusted (above) because of ambient sunlight
           
            
            // TODO: This image write section should be commented out for the production code
//            try {
//                if ( thresholdImage != null )
//                    thresholdImage.write("/after_thresh.bmp");    // this seems to work well
//                else
//                {
//                    System.out.println("ThresholdImage is null!!!");
//                }
//            } catch (Exception e) {
//                System.out.println("error saving image 2");
//            }
//            System.out.println("WROTE IMAGE2");

                BinaryImage filteredImage;
                BinaryImage convexHullImage;
 //               BinaryImage bigObjectsImage;

 //               bigObjectsImage = thresholdImage.removeSmallObjects(false, 2);  // remove small artifacts 
 
 //               if (RoboRebels.remove_small_objects_from_image)     // We commented out the removeSmallObjects step very early on.  Now should try it back in again
 //               {
 //                   convexHullImage = bigObjectsImage.convexHull(false);          // fill in occluded rectangles after removing small objects   
 //               }
 //               else
 //               {
                    convexHullImage = thresholdImage.convexHull(false);            // fill in occluded rectangles without removing small objects
 //               }
                
                filteredImage = convexHullImage.particleFilter(cc);           // find filled in rectangles

            // TODO: This image write section should be commented out for the production code
   //         try {
   //             filteredImage.write("/processed.bmp");     // This seems to work well.
   //         } catch (Exception e) {
   //             System.out.println("error saving image 3");
   //         }
   //         System.out.println("WROTE IMAGE3");

            ParticleAnalysisReport[] reports = filteredImage.getOrderedParticleAnalysisReports();
            
            int selected_target_index = 0;    // initialize to zero 
            int potential_targets = Math.min(reports.length, 4);    // number of potential targets from image processing
          
            if ((reports != null) && (reports.length > 0))   // Only do tracking if there is at least one target
            {
                
//            if ((target_selected == RoboRebels.AUTO_TARGET) ||
//                    (target_selected == RoboRebels.HIGHEST_TARGET)
//                      || (target_selected == RoboRebels.LOWEST_TARGET))
//            {
                int distance_from_center = 160; // set to maximum
                boolean lowest = true;          // true if center target is the lowest target in the image
                int dist;                       // distance of center of target from the center of the image
 
                for (int i = 0; i < potential_targets; i++) 
                {
                    ParticleAnalysisReport r = reports[i];
                    if ((dist = Math.abs(x(r.center_mass_x, 0))) < distance_from_center)  // This does not have camera offset calculation built in
                    {
                        distance_from_center = dist;
                        selected_target_index = i;
                    }        
                } 
//
//      Removed determination if lowest or highest target
//                
//                if (potential_targets == 1)
//                    lowest = true;
//                else if (potential_targets == 2)
//                {
//                    int other_index;
//                    if (selected_target_index == 0)
//                        other_index = 1;
//                    else
//                        other_index = 0;
//                       
//                    ParticleAnalysisReport center_target_r = reports[selected_target_index];
//                    ParticleAnalysisReport other_target_r = reports[other_index];
//                    
//                    if (y(center_target_r.center_mass_y) <= y(other_target_r.center_mass_y)) 
//                        lowest = true;
//                    else
//                        lowest = false; 
//                }
//                if (potential_targets == 3)
//                {
//                    int other_index;
//                    int another_index;
//                    
//                    if (selected_target_index == 0)
//                    {
//                        other_index = 1;
//                        another_index = 2;
//                    }
//                    else if (selected_target_index == 1)
//                    {  
//                        other_index = 0;
//                        another_index = 2;
//                    }
//                    else
//                    {
//                        other_index = 0;
//                        another_index = 1;
//                    }
//                       
//                    ParticleAnalysisReport center_target_r = reports[selected_target_index];
//                    ParticleAnalysisReport other_target_r = reports[other_index];
//                    ParticleAnalysisReport another_target_r = reports[another_index];
//                    
//                    if ((y(center_target_r.center_mass_y) < y(other_target_r.center_mass_y)) && 
//                            (y(center_target_r.center_mass_y) < y(another_target_r.center_mass_y))) 
//                        lowest = true;
//                    else
//                        lowest = false; 
//  
//                }
//                if (potential_targets == 4)
//                {
//                    int other_index;
//                    int another_index;
//                    int yet_another_index;
//                    
//                    if (selected_target_index == 0)
//                    {
//                        other_index = 1;
//                        another_index = 2;
//                        yet_another_index = 3;
//                    }
//                    else if (selected_target_index == 1)
//                    {  
//                        other_index = 0;
//                        another_index = 2;
//                        yet_another_index = 3;
//                    }
//                    else if (selected_target_index == 2)
//                    {
//                        other_index = 0;
//                        another_index = 1;
//                        yet_another_index = 3;
//                    }
//                    else   // selected_target_index == 3
//                    {
//                        other_index = 0;
//                        another_index = 1;
//                        yet_another_index = 2;
//                    }
//                       
//                    ParticleAnalysisReport center_target_r = reports[selected_target_index];
//                    ParticleAnalysisReport other_target_r = reports[other_index];
//                    ParticleAnalysisReport another_target_r = reports[another_index];
//                    ParticleAnalysisReport yet_another_target_r = reports[yet_another_index];
//                    
//                    if ((y(center_target_r.center_mass_y) <= y(other_target_r.center_mass_y)) && 
//                            (y(center_target_r.center_mass_y) <= y(another_target_r.center_mass_y)) &&
//                            (y(center_target_r.center_mass_y) <= y(yet_another_target_r.center_mass_y))) 
//                        lowest = true;
//                    else
//                        lowest = false; 
//  
//                }
                        // Put it here!
//              }

//
//         Removed Left and Right Targeting            
//            else if ((target_selected == RoboRebels.RIGHT_TARGET) || (target_selected == RoboRebels.LEFT_TARGET))
//              {
//                 selected_target_index = 0; 
//                 int left_target_index = 0;
//                 int right_target_index = 0;
//                 int left_most_target = 160;
//                 int right_most_target = -160;
//                 
//                 for (int i = 0; ((i < targets.length) && (i < reports.length)); i++) {
//                     
//                     ParticleAnalysisReport r = reports[i];
//                     
//                     if (y(r.center_mass_y) < left_most_target)
//                     {
//                         left_most_target = y(r.center_mass_y);
//                         left_target_index = i;
//                     }
//                     if (y(r.center_mass_y) > right_most_target)
//                     {
//                         right_most_target = y(r.center_mass_y);
//                         right_target_index = i;
//                     }  
//                  }
//                 
//                 if (target_selected == RoboRebels.RIGHT_TARGET)
//                     selected_target_index = right_target_index;
//                 else if (target_selected == RoboRebels.LEFT_TARGET)
//                     selected_target_index = left_target_index;
//               }   
              
              // Targeting image processing is now done.
              
              ParticleAnalysisReport r = reports[selected_target_index];
           
              double aspect_ratio = ((double)r.boundingRectWidth)/((double)r.boundingRectHeight);
              
              double distance = 801.4/r.boundingRectWidth;  // Calculate distance to target based on rectangle width
           
              if (RoboRebels.DEBUG_ON)
              {
               System.out.println("Tracker: Target " + selected_target_index + "/" + potential_targets + " Center: (x,y)  (" +
                        x(r.center_mass_x, r.boundingRectWidth) + "," + y(r.center_mass_y) + ") Width: " + r.boundingRectWidth + 
                        " Height: " + r.boundingRectHeight + 
                        " Aspect: " + round2(aspect_ratio) + 
                        " Distance: " + round(distance));
             
              }   
               if ((aspect_ratio < 1.5) && (aspect_ratio > 1.1))  // Check aspect ratio of target to make sure it is valid.
               {
                               
                
            
 //               int     targetID = 0;
                
 //               RoboRebels.going_for_highest = dipSwitch.getState(0);                   // Read first DIP Switch
 //               System.out.println("DIP Switch 0: " + RoboRebels.going_for_highest);
                    
       //       double display_distance = ((int)(distance * 100))/100.0;
                
                double display_distance = round(distance);
                
//                if (target_selected == RoboRebels.HIGHEST_TARGET)
//                {
//                     RoboRebels.printLCD(2, "Dist: " + display_distance + " to Highest" );
//                }
//                else if ( target_selected == RoboRebels.LEFT_TARGET)
//                {
//                    RoboRebels.printLCD(2, "Dist: " + display_distance + " to Left" );
//                }             
//                else if ( target_selected == RoboRebels.LOWEST_TARGET)
//                {
//                    RoboRebels.printLCD(2, "Dist: " + display_distance + " to Lowest" );  
//                }    
//                 else if ( target_selected == RoboRebels.RIGHT_TARGET)
//                {
//                    RoboRebels.printLCD(2, "Dist: " + display_distance + " to Right" ); 
//                }
//                 else if ( target_selected == RoboRebels.AUTO_TARGET)
//                {
                     RoboRebels.printLCD(2, "Dist: " + display_distance + " to Target" ); 
//                }
//                else
//                {
//                    RoboRebels.printLCD(2, "Dist: No Target        " ); 
//                }
                                 
                // TODO:  Probably only need to do the rest of this if active tracking is happening.
                
                angle = RRShooter.determineAngle(distance, RoboRebels.muzzle_velocity, target_selected);
                
                
//               if (RoboRebels.autonomous_mode == true)
//               {
                angle = angle + correction(distance) - 10.0;      // Correction, i.e. fudge factor based on data.
//               }
//               else
//               {
//                    angle = angle + correction(distance) - 10.0;      // Correction, i.e. fudge factor based on data.
//                
//               }
                // angle = 55;
                    
//                System.out.println("Muzzle Velocity: " + round(RoboRebels.muzzle_velocity) +
//                        " Theta: " + round(angle) + " Tilt_angle: " + round(RoboRebels.tilt_angle));
//                
                
                RoboRebels.printLCD(5, "d: " +  round(distance) + " C: " + round2(angle - RoboRebels.tilt_angle) + "              ");

                int x_accuracy;
                
//                if ((distance > 6.0) && (distance < 25.0))                                    // Adjust pixel accuracy with distance.
//                    x_accuracy = (int)(RoboRebels.PIXEL_ACCURACY * (12.0/distance) + 0.5);    // Further away from target, smaller pixel accuracy must be
//                else
                    x_accuracy = RoboRebels.PIXEL_ACCURACY;

                
                int x = x(r.center_mass_x, r.boundingRectWidth);
                
//                if ((x > x_accuracy/2) && (x < x_accuracy*4))      
//                {
//                        RoboRebels.target_azimuth = RoboRebels.CLOSE_LEFT;  // LazySusan needs to only a little to left
//                }
//                else if ((x >= x_accuracy*4) && (x < x_accuracy*6))
//                {
//                        RoboRebels.target_azimuth = RoboRebels.LEFT;  // LazySusan needs to move left
//                }  
//                else if (x >= x_accuracy*6)
//                {
//                        RoboRebels.target_azimuth = RoboRebels.FAR_LEFT;  // LazySusan needs to move far left
//                }
//                else if ((x < -x_accuracy/2) && (x > -x_accuracy*4))      
//                {
//                        RoboRebels.target_azimuth = RoboRebels.CLOSE_RIGHT;  // LazySusan needs to only a little to right
//                }
//                else if ((x <= -x_accuracy*4) && (x > -x_accuracy*6))
//                {
//                        RoboRebels.target_azimuth = RoboRebels.RIGHT;  // LazySusan needs to move right
//                }  
//                else if (x <= -x_accuracy*6)
//                {
//                        RoboRebels.target_azimuth = RoboRebels.FAR_RIGHT;  // LazySusan needs to move far right
//                }
                
                if (x >= x_accuracy/2)
                {
                        RoboRebels.target_azimuth = RoboRebels.LEFT;  // LazySusan needs to move left
                }
                else if (x <= -x_accuracy/2)      
                {
                        RoboRebels.target_azimuth = RoboRebels.RIGHT;  // LazySusan needs to move right
                }
               else
                {
                    RoboRebels.target_azimuth = RoboRebels.LOCK; 
                    RoboRebels.azimuth_lock = true;                 // Don't move, we are facing target!
                    shooter.stopLazySusan();                        // Immediately stop LazySusan to prevent overshoot
                }
                
                if (angle <= 45.0)  // Check to see if there is a valid angle
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
                        RoboRebels.target_elevation = RoboRebels.UP;  // Shooter needs to tilt up
                    }
                    else if (RoboRebels.tilt_angle > (angle + RoboRebels.ANGLE_ACCURACY/2))    
                    {
                        RoboRebels.target_elevation = RoboRebels.DOWN;                        
                    }
                    else
                        RoboRebels.target_elevation = RoboRebels.LOCK;  // Don't move, we are at right angle!          
                }                      
       //     System.out.println(filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());
             } else
              {
                  RoboRebels.printLCD(2, "Target Not Valid!          " );
                  
          //        System.out.println("Target Not Valid!         ");
                  
                  RoboRebels.target_azimuth = RoboRebels.HOLD;
                  RoboRebels.target_elevation = RoboRebels.HOLD;
                  RoboRebels.target_elevation = RoboRebels.HOLD;
              }             
            }
            else
            {
                  RoboRebels.printLCD(2, "No Target Detected!          " );
    //              System.out.println("No Target Detected!         ");
                  
                  RoboRebels.target_azimuth = RoboRebels.HOLD;
                  RoboRebels.target_elevation = RoboRebels.HOLD;
                  RoboRebels.target_elevation = RoboRebels.HOLD;

            }
            /**
             * all images in Java must be freed after they are used since they are allocated out
             * of C data structures. Not calling free() will cause the memory to accumulate over
             * each pass of this loop.
             **/
            
            filteredImage.free();
            convexHullImage.free();       
//            bigObjectsImage.free();     // Still free up even if not used.
            thresholdImage.free();
            image.free();
            
            // Moved from RRShooter
            
            TTState = RRButtonMap.getActionObject(RRButtonMap.TRACK_TARGET).getButtonState();
        //if (shootingJoystick.getRawButton(RRButtonMap.TRACK_TARGET))  // Target Tracking when joystick button 11 is pressed and held 
            if ( TTState || RoboRebels.autonomous_mode_tracking)
            {
            if (!shooter.tracking)      // Starting tracking - first time through this path
            {
                shooter.tracking = true;
                RoboRebels.time_started_tracking = Timer.getFPGATimestamp();
   //             System.out.println("Beginning Target Tracking");
                
   //             drive.stopMotor();        // Need to stop drive motors when button is pressed since user input will not be polled.
   //               gatherer.disableGatherer();   // Stop gathering
            }
            
            double time = RoboRebels.MAX_TRACKING_TIME - (Timer.getFPGATimestamp() - RoboRebels.time_started_tracking);
            
            if (RoboRebels.DEBUG_ON)
                System.out.println("Tracking time left: " + round2(time));
            
            if ((time > 0))          // If timeout, stop auto tracking
            {          
                if (!RoboRebels.troubleshooting) // If troubleshooting, don't auto track target.  Lock is determined by delay DIP Switch
                {                   
                    if (TTState) 
     //                   System.out.println("Track Target Button Pressed");

                    if (RoboRebels.target_azimuth == RoboRebels.LOCK)
                    {
                        if (RoboRebels.DEBUG_ON)
                            System.out.println("Shooter: Auto Lazy susan Lock"); 
                        shooter.lazySusanSpeed = 0.0;
                        
                        shooter.stopLazySusan();
                        
                        RoboRebels.azimuth_lock = true;         // Indicate azimuth target lock
                    } 
                    else if (RoboRebels.target_azimuth == RoboRebels.LEFT)       // Left normal
                    {
                        if (RoboRebels.DEBUG_ON)
                            System.out.println("Shooter: Auto Lazy susan left"); 
                        shooter.lazySusanSpeed = -0.15;         //-0.20;  // was -0.75 * LS_SPEED
                        RoboRebels.azimuth_lock = false;         // No azimuth target lock
                    }
                    else if (RoboRebels.target_azimuth == RoboRebels.RIGHT)     // Right normal
                    {
                        if (RoboRebels.DEBUG_ON)
                            System.out.println("Shooter: Auto Lazy susan right"); 
                        shooter.lazySusanSpeed = 0.15;  //* 1.2;      //  0.20  * 1.2;           // Added 20% due to motor slowness
                        RoboRebels.azimuth_lock = false;         // No azimuth target lock
                    }
                    else              // Must be set to HOLD
                    {
                        if (RoboRebels.DEBUG_ON)
                            System.out.println("Shooter: Auto Lazy susan Hold"); 
                        shooter.lazySusanSpeed = 0.0;
                        RoboRebels.azimuth_lock = false;         // No azimuth target lock
                    }

                    if (RoboRebels.dont_track_azimuth)
                    {
                            RoboRebels.azimuth_lock = true;
                            shooter.lazySusanSpeed = 0.0;
                    }
         //           System.out.println("Tilt value:" + RoboRebels.target_elevation); 

                    if (RoboRebels.target_elevation == RoboRebels.LOCK)
                    {
                        shooter.tiltSpeed = 0.0;                      // Stop Tilting
                        RoboRebels.elevation_lock = true;     // Indicate elevation target lock
          //              System.out.println("Auto Tilt Lock"); 
                    }
                    else if (RoboRebels.target_elevation == RoboRebels.UP)  // Track target elevation (up/down)
                    {
           //             System.out.println("Auto Tilt Up"); 
                        shooter.tiltSpeed = -0.2;            //  -1.0 * TILT_SPEED (.4)  * 0.5 
                        RoboRebels.elevation_lock = false;         // No elevation target lock
                    }
                    else if (RoboRebels.target_elevation == RoboRebels.DOWN)
                    {
        //                System.out.println("Auto Tilt Down"); 
                        shooter.tiltSpeed = 0.2;            // 1.0 * TILT_SPEED * 0.5;
                        RoboRebels.elevation_lock = false;         // No elevation target lock
                    }
                    else             // is HOLD
                    {
                    shooter.tiltSpeed = 0.0;
                    RoboRebels.elevation_lock = false;         // No elevation target lock
    //                System.out.println("Auto Tilt Hold"); 
                    }

                    if (RoboRebels.target_muzzle_velocity == RoboRebels.FASTER)
                    {
      //                  System.out.println("Auto Shooting Speed Up"); 
                        RoboRebels.muzzle_velocity = 8.0;
                        RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
                    }
                    else if (RoboRebels.target_muzzle_velocity == RoboRebels.SLOWER)
                    {
        //                System.out.println("Auto Shooting Speed Down"); 
                        RoboRebels.muzzle_velocity = 7.5;
                        RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
                    }
                    else if (RoboRebels.target_muzzle_velocity == RoboRebels.LOCK)
                    {
                        // Muzzle velocity is fine - don't change
                        RoboRebels.muzzle_velocity_lock = true;     // muzzle velocity target lock
       //                 System.out.println("Auto Shooting Lock"); 
                    }
                    else
                    {
                        RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
       //                 System.out.println("Auto Shooting Hold"); 
                    }
                }
                else
                {
      //              System.out.println("Auto no tracking due to troubleshooting mode.  Lock by DIP Switch: " + locked());
                }
            }  
            else  // Tracking timeout
            {
                shooter.tracking = false;
                
                shooter.lazySusanSpeed = 0.0;      // Stop azimuth tracking
                shooter.tiltSpeed = 0.0;           // Stop elevation tracking
                                        
                RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
                RoboRebels.target_elevation = RoboRebels.HOLD;
                RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;
                
      //          System.out.println("Tracking Timeout!"); 
                RoboRebels.printLCD(6, "Tracking Timeout!                ");
                
                if (RoboRebels.autonomous_mode)
                {
                    RoboRebels.autonomous_tracking_failed = true;
        //            System.out.println("End autonomous tracking"); 
                    
                }

            }
        } 
        else if (shooter.tracking)   // Was tracking target, but now no longer tracking
        {
             shooter.tracking = false;
             
     //        System.out.println("End of tracking"); 
             
             shooter.lazySusanSpeed = 0.0;      // Stop azimuth tracking
             shooter.tiltSpeed = 0.0;           // Stop elevation tracking
                                        
             RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
             RoboRebels.target_elevation = RoboRebels.HOLD;
             RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;
             
//             gatherer.enableGatherer();           // Enable gathering again
//             drive.enableDrive();                  // Enable driving again
        }
            
       if (shooter.locked())
       {
            RoboRebels.printLCD(6, "All Locked!                "); 
       }
       else if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock)
            RoboRebels.printLCD(6, "LS & Speed Locked!          "); 
       else if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock)
            RoboRebels.printLCD(6, "LS & Angle Locked!          "); 
       else if (RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock)
            RoboRebels.printLCD(6, "Angle & Speed Locked!       "); 
       else if (RoboRebels.elevation_lock)
            RoboRebels.printLCD(6, "Angle Locked!               ");
       else if (RoboRebels.muzzle_velocity_lock)
            RoboRebels.printLCD(6, "Speed Locked!               ");
       else if (RoboRebels.azimuth_lock)
            RoboRebels.printLCD(6, "LS Locked!                  ");
       else if (shooter.tracking) 
            RoboRebels.printLCD(6, "Tracking Target...          "); 
       else 
            RoboRebels.printLCD(6, "                            ");   // Clear the display       
            
        }
             
        shooter.setTrackerSpeeds();

        //    System.out.println("accel angle: " + accelAngle());

        } catch (Exception ex) {
            System.err.println("There was an error while tracking a target!");
            ex.printStackTrace();
        }
  //      System.out.println("Track Target Execution Time: " + round((Timer.getFPGATimestamp() - start)*1000.0) + " milliseconds");
    }
//}       // Just added this now
    
/*  These two are not currently used. 
 * 
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
    * */

    public static Target[] targets() {
        return targets;
    }


    public double accelAngle() {
        ADXL345_I2C.AllAxes axes = accel.getAccelerations();
 //       System.out.println("X Accel: " + axes.XAxis);
 //       System.out.println("Y Accel: " + axes.YAxis);
 //       System.out.println("Z Accel: " + axes.ZAxis);
        
        // Probably should not get reading from accelerometer if if > 1 or < -1 as it is moving too fast.
        
        double yAxis = Math.min(1, axes.YAxis);
        yAxis = Math.max(-1, yAxis);
        
 //       double zAxis = Math.min(1, axes.ZAxis);
 //       zAxis = Math.max(-1, zAxis);
        
 //       double another_angle = (-180.0 * MathUtils.acos(zAxis) / Math.PI);  // Use this angle if angle is greater than 70 degrees
        
 //       System.out.println("Accel Angle from Z Axis:" + round(another_angle));
        
        // Need to subtract 90 degrees to return correct angle when
        // accelerometer is mounted on back of shooter

        double current_angle = 90.0 - (180.0 * MathUtils.asin(yAxis) / Math.PI);
        
 //       System.out.println("Accel Angle from Y Axis:" + round(current_angle));
      
        // Updates current moving average sum by subtracing oldest entry and adding in current entry
        
        RoboRebels.current_angle_sum = RoboRebels.current_angle_sum - RoboRebels.previous_angles[RoboRebels.curent_angle_index] + current_angle;
        
        // Replaces oldest entry with current entry
        
        RoboRebels.previous_angles[RoboRebels.curent_angle_index] = current_angle;
        
        // Increment index, modulo the size of the moving average
        
        RoboRebels.curent_angle_index = (RoboRebels.curent_angle_index + 1) % RoboRebels.NUMBER_OF_PREVIOUS;  // this might need a - 1 here
        
        // Compute the current moving average value
                
        double moving_average_angle = RoboRebels.current_angle_sum / RoboRebels.NUMBER_OF_PREVIOUS;
        
//        RoboRebels.printLCD(4, "Tilt: " + round(moving_average_angle) + "|Act: " + round(current_angle));

        return moving_average_angle;
        
    }
    
      
    public int x(int raw_x, int target_image_width)
    {   
        double camera_offset = 9.0;      // 10.0 on practice bot;   // camera offset from center of robot in inches.  Positive is to the right side of robot
        double target_width = 24.0;     // width of backboard target in inches   
        int correction;
        
        correction = (int)(((camera_offset/target_width) * target_image_width) + 0.5);
        
    //    System.out.println("x: " + raw_x + "correction: " + correction);
        
        return (raw_x - 160 + correction);
    }
    
    public int y(int raw_y)
    {   
         return (280 - raw_y);
    }
    
    
    static public double round(double x)  // Rounds double X to one decimal place
    {
        double z = ((int)((x + 0.05) * 10.0))/10.0;
                       
        return z;
                
    }
    
    static public double round2(double x)  // Rounds double X to two decimal places
    {
        double z = ((int)((x + 0.005) * 100.0))/100.0;
                       
        return z;
                
    }
    
    static public double correction(double x)
                 
{
        double temp; 
        temp = 0.0;
        
        // coefficients from http://zunzun.com/Equation/2/Polynomial/Quadratic/
        double a = -1.4456094367262210E+01; 
        double b = 1.7038026632980896E+00; 
        double c = -4.6058642266421534E-02;
        
        temp += a + b * x + c * x * x; 
        
        return temp;

    }
            
            
   
  }
