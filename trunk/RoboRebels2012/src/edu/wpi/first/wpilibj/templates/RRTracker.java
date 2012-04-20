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
public class RRTracker {

    AxisCamera cam;                    // camera object
    CriteriaCollection cc;             // the criteria for doing the particle filter operation
    private static Target[] targets;
    ADXL345_I2C accel;
    RRShooter shooter;
//    RRDIPSwitch     dipSwitch;
    RRCameraThread cameraThread;
    RRDriveThread driveThread;
    RRDrive drive;
    int i = 0;

    public RRTracker(ADXL345_I2C a, RRDIPSwitch the_dipswitch, RRDriveThread dr, RRDrive d) //    public RRTracker(ADXL345_I2C a)
    {

        Timer.delay(10.0);       // This delay is recommended as the camera takes some time to start up
        cam = AxisCamera.getInstance();

        driveThread = dr;

        drive = d;


//        cameraThread = new RRCameraThread();
//        cameraThread.collectImages();
//        cameraThread.start();
        RRLogger.logDebug(this.getClass(),"","Camera");

        cc = new CriteriaCollection();      // create the criteria for the particle filter
        //RRLogger.logDebug(this.getClass(),"RRTracker()","Criteria Collection ownage");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 30, 400, false);
        //RRLogger.logDebug(this.getClass(),"RRTracker()","Criteria Collection ownage 2");
        cc.addCriteria(NIVision.MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 40, 400, false);
        //RRLogger.logDebug(this.getClass(),"RRTracker()","Criteria Collection ownage 3");
        targets = new Target[4];

        //       dipSwitch = the_dipswitch;

        RRLogger.logDebug(this.getClass(),"","Targets");
        //accel = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G); // slot number is actually module number
        accel = a;
        RRLogger.logDebug(this.getClass(),"","accel");

    }

    public void setShooter(RRShooter s) {
        if (s != null) {
            shooter = s;
        } else {
            throw new NullPointerException("RRTracker was passed a null RRShooter object!");
        }
    }

    public void trackTarget(int target_selected) // Target selected is HIGEST, LOWEST, LEFT, RIGHT, or AUTO
    {
        double start = Timer.getFPGATimestamp();
        double angle;
        boolean TTState;
        ParticleAnalysisReport r = null;
        double aspect_ratio = 1.0;
//        double distance = 12.0;
        ParticleAnalysisReport[] reports;
        int selected_target_index = 0;
        int potential_targets = 0;

        try {
            RoboRebels.tilt_angle = accelAngle();
            RoboRebels.printLCD(4, "Tilt: " + round(RoboRebels.tilt_angle) + " Calc Tilt: " + round(RoboRebels.calc_angle) +
                    " c: " + round(RoboRebels.calc_angle - RoboRebels.tilt_angle) + " d: " + round(RoboRebels.distance));

//                 RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Tilt Angle "+ round(RoboRebels.tilt_angle));
//                 RRLogger.logDebug(this.getClass(),"trackTarget()","Tracking: tracker " + shooter.tracking + " complete " +
//                         shooter.tracking_complete + " timeout " + shooter.tracking_timeout);                

            //RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: tracking:" + shooter.tracking);

            if (shooter.tracking) {

//                    RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Tracking target...");

                ColorImage image = cam.getImage();     // comment if using stored images

//                 ColorImage image = cameraThread.getImage();

//                 RRLogger.logDebug(this.getClass(),"trackTarget()","RRTracker::trackTarget() " + image);

                if (RoboRebels.save_camera_image_file) // Only do this durng initial competition setup to adjust levels
                //               if (i < 10)
                {
                    try {
                        if (image != null) {
                            image.write("/raw" + i + ".jpg");
                        } else {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Image is null!!!");
                        }
                    } catch (Exception e) {
                        RRLogger.logDebug(this.getClass(),"trackTarget()","error saving image 1");
                    }
                    RRLogger.logDebug(this.getClass(),"trackTarget()","Saved raw camera image to cRIO");

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
                BinaryImage thresholdImage = image.thresholdRGB(0, 10, 65, 255, 0, 10);        // Green LED mask test

                // blue value was adjusted (above) because of ambient sunlight


                // TODO: This image write section should be commented out for the production code
//            if (i < 10) {
//                try {
//                    if ( thresholdImage != null ) {
//                        thresholdImage.write("/after_thresh" + i + ".bmp");    // this seems to work well
//                    } else {
//                        RRLogger.logDebug(this.getClass(),"trackTarget()","ThresholdImage is null!!!");
//                    }
//                } catch (Exception e) {
//                    RRLogger.logError(this.getClass(),"trackTarget()",e);
//                }
//                RRLogger.logDebug(this.getClass(),"trackTarget()","WROTE IMAGE 2");
//            }
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

//            if (i < 10) {
//              try {
//                  filteredImage.write("/processed" + i + ".bmp");     // This seems to work well.
//              } catch (Exception e) {
//                  RRLogger.logDebug(this.getClass(),"trackTarget()","error saving image 3");
//              }
//              RRLogger.logDebug(this.getClass(),"trackTarget()","WROTE IMAGE 3");
//            }
//            
//            i++;

                reports = filteredImage.getOrderedParticleAnalysisReports();

                selected_target_index = 0;    // initialize to zero
                potential_targets = Math.min(reports.length, 4);    // number of potential targets from image processing

                if ((reports != null) && (reports.length > 0)) // Only do tracking if there is at least one target
                {

//            if ((target_selected == RoboRebels.AUTO_TARGET) ||
//                    (target_selected == RoboRebels.HIGHEST_TARGET)
//                      || (target_selected == RoboRebels.LOWEST_TARGET))
//            {
                    int distance_from_center = 160; // set to maximum
                    boolean lowest = true;          // true if center target is the lowest target in the image
                    int dist;                       // distance of center of target from the center of the image

                    for (int i = 0; i < potential_targets; i++) {
                        ParticleAnalysisReport rp = reports[i];
                        if ((dist = Math.abs(x(rp.center_mass_x, 0))) < distance_from_center) // This does not have camera offset calculation built in
                        {
                            distance_from_center = dist;
                            selected_target_index = i;
                        }
                    }
//
//      Removed determination if lowest or highest target
//                
//                if (potential_targets == 1) {
//                    lowest = true;
//                } else if (potential_targets == 2) {
//                    int other_index;
//                    if (selected_target_index == 0) {
//                        other_index = 1;
//                    } else {
//                        other_index = 0;
//                    }
//                       
//                    ParticleAnalysisReport center_target_r = reports[selected_target_index];
//                    ParticleAnalysisReport other_target_r = reports[other_index];
//                    
//                    if (y(center_target_r.center_mass_y) <= y(other_target_r.center_mass_y)) {
//                        lowest = true;
//                    } else {
//                        lowest = false;
//                    }
//                }
//                if (potential_targets == 3) {
//                    int other_index;
//                    int another_index;
//                    
//                    if (selected_target_index == 0) {
//                        other_index = 1;
//                        another_index = 2;
//                    } else if (selected_target_index == 1) {  
//                        other_index = 0;
//                        another_index = 2;
//                    } else {
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

                    r = reports[selected_target_index];

                    aspect_ratio = ((double) r.boundingRectWidth) / ((double) r.boundingRectHeight);

                    RoboRebels.distance = 801.4 / r.boundingRectWidth;  // Calculate distance to target based on rectangle width

                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Target " + (selected_target_index + 1) + "/" + potential_targets
                                + " Center: (x,y) (" + x(r.center_mass_x, r.boundingRectWidth) + "," + y(r.center_mass_y)
                                + ") Raw x: " + (r.center_mass_x - 160) + " Width: " + r.boundingRectWidth + " Height: " + r.boundingRectHeight
                                + " Aspect: " + round2(aspect_ratio) + " Distance: " + round(RoboRebels.distance) + " Tilt: " + round(RoboRebels.tilt_angle)
                                + " Speed: " + RoboRebels.muzzle_velocity);
                    }
                    if ((aspect_ratio < 1.5) && (aspect_ratio > 1.1)) // Check aspect ratio of target to make sure it is valid.
                    {



                        //               int     targetID = 0;

                        //               RoboRebels.going_for_highest = dipSwitch.getState(0);                   // Read first DIP Switch
                        //               RRLogger.logDebug(this.getClass(),"","DIP Switch 0: " + RoboRebels.going_for_highest);

                        //       double display_distance = ((int)(distance * 100))/100.0;

                        double display_distance = round(RoboRebels.distance);

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
                        RoboRebels.printLCD(2, "Dist: " + display_distance + " to Target");
//                }
//                else
//                {
//                    RoboRebels.printLCD(2, "Dist: No Target        " ); 
//                }

                        // TODO:  Probably only need to do the rest of this if active tracking is happening.

                        RoboRebels.calc_angle = RRShooter.determineAngle(RoboRebels.distance, RoboRebels.muzzle_velocity, target_selected);


//               if (RoboRebels.autonomous_mode == true)
//               {
                        angle = RoboRebels.calc_angle + correction(RoboRebels.distance) - 10.0;      // Correction, i.e. fudge factor based on data.
//               }
//               else
//               {
//                    angle = angle + correction(distance) - 10.0;      // Correction, i.e. fudge factor based on data.
//                
//               }
                        // angle = 55;

//                RRLogger.logDebug(this.getClass(),"","Muzzle Velocity: " + round(RoboRebels.muzzle_velocity) +
//                        " Theta: " + round(angle) + " Tilt_angle: " + round(RoboRebels.tilt_angle));
//                

                        RoboRebels.printLCD(5, "d: " + round(RoboRebels.distance) + " C: " + round2(RoboRebels.calc_angle - RoboRebels.tilt_angle) + "              ");

                        if (RoboRebels.DEBUG_ON) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","distance: " + round(RoboRebels.distance) + " Correction: " + round2(RoboRebels.calc_angle - RoboRebels.tilt_angle));
                        }

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
//                if (x >= x_accuracy*6)
//                {
//                        RoboRebels.target_azimuth = RoboRebels.FAR_LEFT;  // LazySusan needs to move far left
//                }
//                else if ((x < -x_accuracy/2) && (x > -x_accuracy*4))      
//                {
//                        RoboRebels.target_azimuth = RoboRebels.CLOSE_RIGHT;  // LazySusan needs to only a little to right
//                }
//                else if (x <= -x_accuracy*6)
//                {
//                        RoboRebels.target_azimuth = RoboRebels.FAR_RIGHT;  // LazySusan needs to move far right
//                }  
//                else if (x <= -x_accuracy*6)
//                {
//                        RoboRebels.target_azimuth = RoboRebels.FAR_RIGHT;  // LazySusan needs to move far right
//                }

                        if (x >= x_accuracy / 2) // need to add back in else if add FAR_LEFT and FAR_RIGHT
                        {
                            RoboRebels.target_azimuth = RoboRebels.LEFT;  // LazySusan needs to move left
                        } else if (x <= -x_accuracy / 2) {
                            RoboRebels.target_azimuth = RoboRebels.RIGHT;  // LazySusan needs to move right
                        } else {
                            RoboRebels.target_azimuth = RoboRebels.LOCK;
                            RoboRebels.azimuth_lock = true;                 // Don't move, we are facing target!
                            shooter.stopLazySusan();                        // Immediately stop LazySusan to prevent overshoot
                        }

                        if (angle <= 45.0) // Check to see if there is a valid angle
                        {
                            RoboRebels.target_muzzle_velocity = RoboRebels.FASTER; // Muzzle velocity needs to be faster
                            RoboRebels.target_elevation = RoboRebels.HOLD;         // Wait for correct muzzle velocity before tilting
                        } else if (angle > 80.0) {
                            RoboRebels.target_muzzle_velocity = RoboRebels.SLOWER; // Muzzel velocity needs to be slower
                            RoboRebels.target_elevation = RoboRebels.HOLD;         // Wait for correct muzzle velocity before tilting
                        } else // The angle is valid, so adjust tilt angle
                        {
                            RoboRebels.target_muzzle_velocity = RoboRebels.LOCK; // Muzzle velocity is fine.

                            if (RoboRebels.tilt_angle < (angle - RoboRebels.ANGLE_ACCURACY / 2)) {
                                RoboRebels.target_elevation = RoboRebels.UP;  // Shooter needs to tilt up
                            } else if (RoboRebels.tilt_angle > (angle + RoboRebels.ANGLE_ACCURACY / 2)) {
                                RoboRebels.target_elevation = RoboRebels.DOWN;
                            } else {
                                RoboRebels.target_elevation = RoboRebels.LOCK;  // Don't move, we are at right angle!
                            }
                        }
                        //     RRLogger.logDebug(this.getClass(),"",filteredImage.getNumberParticles() + "  " + Timer.getFPGATimestamp());
                    } else {
                        if (RoboRebels.DEBUG_ON) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Target Not Valid!");
                        }
                        RoboRebels.printLCD(2, "Target Not Valid!          ");

                        //        RRLogger.logDebug(this.getClass(),"","Target Not Valid!         ");

                        RoboRebels.target_azimuth = RoboRebels.HOLD;
                        RoboRebels.target_elevation = RoboRebels.HOLD;
                        RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;
                    }
                } else {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: No Target Detected!");
                    }
                    RoboRebels.printLCD(2, "No Target Detected!          ");
                    //              RRLogger.logDebug(this.getClass(),"","No Target Detected!         ");

                    RoboRebels.target_azimuth = RoboRebels.HOLD;
                    RoboRebels.target_elevation = RoboRebels.HOLD;
                    RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;

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
            }
            TTState = RRButtonMap.getActionObject(RRButtonMap.TRACK_TARGET).getButtonState();

//            RRLogger.logDebug(this.getClass(),"","Tracker: TTState " + TTState);

            //if (shootingJoystick.getRawButton(RRButtonMap.TRACK_TARGET))  // Target Tracking when joystick button 11 is pressed and held
            if (TTState || RoboRebels.autonomous_mode_tracking) {
                if (!shooter.tracking && !shooter.tracking_complete && !shooter.tracking_timeout) // Starting tracking - first time through this path
                {
                    shooter.tracking = true;
                    RoboRebels.time_started_tracking = Timer.getFPGATimestamp();
                    RRLogger.logDebug(this.getClass(),"trackTarget()","Beginning Target Tracking");

                    stopThreads();


//                drive.disableDrive();
                    //             gatherer.disableGatherer();   // Stop gathering
                }

                if (shooter.tracking) {

                    double time = RoboRebels.MAX_TRACKING_TIME - (Timer.getFPGATimestamp() - RoboRebels.time_started_tracking);

                    RRLogger.logDebug(this.getClass(),"trackTarget()","Tracking time left: " + round2(time));

                    if ((time > 0)) // If timeout, stop auto tracking
                    {
                        //               if (!RoboRebels.troubleshooting) // If troubleshooting, don't auto track target.  Lock is determined by delay DIP Switch
                        //               {
                        //                      RRLogger.logDebug(this.getClass(),"","Track Target Button Pressed");

                        if (RoboRebels.target_azimuth == RoboRebels.LOCK) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Lazy Susan Lock");
                            shooter.lazySusanSpeed = 0.0;

                            shooter.stopLazySusan();

                            RoboRebels.azimuth_lock = true;         // Indicate azimuth target lock
                        } else if (RoboRebels.target_azimuth == RoboRebels.LEFT) { // Left normal
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Lazy Susan Left");
                            shooter.lazySusanSpeed = -0.15;         //-0.20;  // was -0.75 * LS_SPEED
                            RoboRebels.azimuth_lock = false;         // No azimuth target lock
                        //                   } else if (RoboRebels.target_azimuth == RoboRebels.FAR_LEFT) {      // Left fast
                        //                        RRLogger.logDebug(this.getClass(),"","Tracker: Auto Lazy susan left fast");
                        //                        shooter.lazySusanSpeed = -0.2;         //-0.20;  // was -0.75 * LS_SPEED
                        //                        RoboRebels.azimuth_lock = false;         // No azimuth target lock
                        }  else if (RoboRebels.target_azimuth == RoboRebels.RIGHT) { // Right normal
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Lazy Susan Right");
                            shooter.lazySusanSpeed = 0.15;  //* 1.2;      //  0.20  * 1.2;           // Added 20% due to motor slowness
                            RoboRebels.azimuth_lock = false;         // No azimuth target lock
                        //                   } else if (RoboRebels.target_azimuth == RoboRebels.FAR_RIGHT) {    // Right fast
                        //                        RRLogger.logDebug(this.getClass(),"","Tracker: Auto Lazy susan right fast");
                        //                        shooter.lazySusanSpeed = 0.2;  //* 1.2;      //  0.20  * 1.2;           // Added 20% due to motor slowness
                        //                        RoboRebels.azimuth_lock = false;         // No azimuth target lock
                        } else {// Must be set to HOLD
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Lazy Susan Hold");
                            shooter.lazySusanSpeed = 0.0;
                            RoboRebels.azimuth_lock = false;         // No azimuth target lock
                        }

                        if (RoboRebels.dont_track_azimuth) {
                            RoboRebels.azimuth_lock = true;
                            shooter.lazySusanSpeed = 0.0;
                        }

//                        RRLogger.logDebug(this.getClass(),"","Tilt value:" + RoboRebels.target_elevation);

                        if (RoboRebels.target_elevation == RoboRebels.LOCK) {
                            shooter.tiltSpeed = 0.0;                      // Stop Tilting
                            RoboRebels.elevation_lock = true;     // Indicate elevation target lock
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Tilt Lock");
                        } else if (RoboRebels.target_elevation == RoboRebels.UP) { // Track target elevation (up/down)
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Tilt Up");
                            shooter.tiltSpeed = -0.2;            //  -1.0 * TILT_SPEED (.4)  * 0.5
                            RoboRebels.elevation_lock = false;         // No elevation target lock
                        } else if (RoboRebels.target_elevation == RoboRebels.DOWN) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Tilt Down");
                            shooter.tiltSpeed = 0.2;            // 1.0 * TILT_SPEED * 0.5;
                            RoboRebels.elevation_lock = false;         // No elevation target lock
                        } else { // is HOLD
                            shooter.tiltSpeed = 0.0;
                            RoboRebels.elevation_lock = false;         // No elevation target lock
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Tilt Hold");
                        }

                        if (RoboRebels.target_muzzle_velocity == RoboRebels.FASTER) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Shooting Speed Up");
                            RoboRebels.muzzle_velocity = 8.0;
                            RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
                        } else if (RoboRebels.target_muzzle_velocity == RoboRebels.SLOWER) {
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Shooting Speed Down");
                            RoboRebels.muzzle_velocity = 7.5;
                            RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
                        } else if (RoboRebels.target_muzzle_velocity == RoboRebels.LOCK) {
                            // Muzzle velocity is fine - don't change
                            RoboRebels.muzzle_velocity_lock = true;     // muzzle velocity target lock
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Auto Shooting Lock");
                        } else {
                            RoboRebels.muzzle_velocity_lock = false;         // No muzzle velocity target lock
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Auto Shooting Hold");
                        }
//                } else {
                        //RRLogger.logDebug(this.getClass(),"trackTarget()","Auto no tracking due to troubleshooting mode.  Lock by DIP Switch: " + locked());
//                }

                    } else // Tracking timeout
                    {
                        shooter.tracking = false;
                        shooter.tracking_complete = false;
                        shooter.tracking_timeout = true;

                        shooter.lazySusanSpeed = 0.0;      // Stop azimuth tracking
                        shooter.tiltSpeed = 0.0;           // Stop elevation tracking

                        RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
                        RoboRebels.target_elevation = RoboRebels.HOLD;
                        RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;

                        RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Tracking Timeout!");
                        RoboRebels.printLCD(6, "Tracking Timeout!                ");

                        if (RoboRebels.autonomous_mode) {
                            RoboRebels.autonomous_tracking_failed = true;
                            RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: End autonomous tracking");
                        }
                    }
                }
            } else if (shooter.tracking) // Was tracking target, but button released
            {
                shooter.tracking = false;
                shooter.tracking_complete = false;
                shooter.tracking_timeout = false;

                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: End of tracking");

                shooter.lazySusanSpeed = 0.0;      // Stop azimuth tracking
                shooter.tiltSpeed = 0.0;           // Stop elevation tracking

                RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
                RoboRebels.target_elevation = RoboRebels.HOLD;
                RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;

                startThreads();

            } else {  // Currently not tracking
                shooter.tracking = false;
                shooter.tracking_complete = false;
                shooter.tracking_timeout = false;

                RoboRebels.target_azimuth = RoboRebels.HOLD;   //  Put hold on tracking until tracking is re-initiated
                RoboRebels.target_elevation = RoboRebels.HOLD;
                RoboRebels.target_muzzle_velocity = RoboRebels.HOLD;

//                 RRLogger.logDebug(this.getClass(),"","Tracker: Not tracking");

                //  startThreads();        // Don't need to do
            }

            if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock && RoboRebels.elevation_lock) {
                RoboRebels.printLCD(6, "All Locked!                ");
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: All Locked!");
                shooter.tracking = false;
                shooter.tracking_complete = true;
                shooter.tracking_timeout = true;

                // startThreads();              // Don't start threads when it locks or robot may move again suddenly
                
            } else if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock) {
                RoboRebels.printLCD(6, "LS & Speed Locked!          ");
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: LS & Speed Locked!");
            } else if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: LS & Angle Locked!");
                RoboRebels.printLCD(6, "LS & Angle Locked!          ");
            } else if (RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Angle & Speed Locked!");
                RoboRebels.printLCD(6, "Angle & Speed Locked!       ");
            } else if (RoboRebels.elevation_lock) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Angle Locked!");
                RoboRebels.printLCD(6, "Angle Locked!               ");
            } else if (RoboRebels.muzzle_velocity_lock) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Speed Locked!");
                RoboRebels.printLCD(6, "Speed Locked!               ");
            } else if (RoboRebels.azimuth_lock) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: LS Locked!");
                RoboRebels.printLCD(6, "LS Locked!                  ");
            } else if (shooter.tracking) {
                RRLogger.logDebug(this.getClass(),"trackTarget()","Tracker: Tracking");
                RoboRebels.printLCD(6, "Tracking Target...          ");
            } else {
                RoboRebels.printLCD(6, "                            ");   // Clear the display
            }
            //       }   // Old if (shooting)

            shooter.setTrackerSpeeds();

            //    RRLogger.logDebug(this.getClass(),"","accel angle: " + accelAngle());

        } catch (Exception ex) {
            RRLogger.logError(this.getClass(), "trackTarget()", ex);
        }
        RRLogger.logDebug(this.getClass(),"trackTarget()","Track Target Execution Time: " + round((Timer.getFPGATimestamp() - start) * 1000.0) + " milliseconds");
        //   }  // remove
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
        //       RRLogger.logDebug(this.getClass(),"accelAngle()","X Accel: " + axes.XAxis);
        //       RRLogger.logDebug(this.getClass(),"accelAngle()","Y Accel: " + axes.YAxis);
        //       RRLogger.logDebug(this.getClass(),accelAngle()","Z Accel: " + axes.ZAxis);

        // Probably should not get reading from accelerometer if if > 1 or < -1 as it is moving too fast.

        double yAxis = Math.min(1, axes.YAxis);
        yAxis = Math.max(-1, yAxis);

        //       double zAxis = Math.min(1, axes.ZAxis);
        //       zAxis = Math.max(-1, zAxis);

        //       double another_angle = (-180.0 * MathUtils.acos(zAxis) / Math.PI);  // Use this angle if angle is greater than 70 degrees

        //       RRLogger.logDebug(this.getClass(),"accelAngle()","Accel Angle from Z Axis:" + round(another_angle));

        // Need to subtract 90 degrees to return correct angle when
        // accelerometer is mounted on back of shooter

        double current_angle = 90.0 - (180.0 * MathUtils.asin(yAxis) / Math.PI);

        //       RRLogger.logDebug(this.getClass(),"accelAngle()","Accel Angle from Y Axis:" + round(current_angle));

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

    public int x(int raw_x, int target_image_width) {
        final double camera_offset = 9.0;      // 10.0 on practice bot;   // camera offset from center of robot in inches.  Positive is to the right side of robot
        final double target_width = 24.0;     // width of backboard target in inches   
        int correction = (int) (((camera_offset / target_width) * target_image_width) + 0.5);
 
        //    RRLogger.logDebug(this.getClass(),"","x: " + raw_x + "correction: " + correction);

        return (raw_x - 160 + correction);
    }

    public int y(int raw_y) {
        return (280 - raw_y);
    }

    static public double round(double x) // Rounds double X to one decimal place
    {
        double z = ((int) ((x + 0.05) * 10.0)) / 10.0;

        return z;

    }

    static public double round2(double x) // Rounds double X to two decimal places
    {
        double z = ((int) ((x + 0.005) * 100.0)) / 100.0;

        return z;

    }

    static public double correction(double x) {
        double temp;
        temp = 0.0;

        // coefficients from http://zunzun.com/Equation/2/Polynomial/Quadratic/
        double a = -1.4456094367262210E+01;
        double b = 1.7038026632980896E+00;
        double c = -4.6058642266421534E-02;

        temp += a + b * x + c * x * x;

        return temp;

    }

    public void stopThreads() {
        if (!RoboRebels.autonomous_mode) {
            drive.stopMotor();        // Need to stop drive motors when button is pressed since user input will not be polled.
            driveThread.disableDrive();          // Stop driving
        }

    }

    public void startThreads() {
        if (!RoboRebels.autonomous_mode) {
            driveThread.enableDrive();         // Enable driving
        }
    }
}
