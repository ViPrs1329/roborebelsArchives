/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.*;

/**
 *
 * The shooter class has the following components:
 * 
 * - Shooting wheel
 * - Getting/setting shooting wheel speed
 * - Tilting mechanism
 * - Lazy Susan
 * 
 * Currently, all manual control has been developed for
 * controlling the various parts of the shooter module.  
 * 
 * TODO:
 * 
 * - Test!
 * - Implement accelerometer code
 * - Extend public methods for autonomous control
 * 
 * @author dmw
 */
public class RRShooter {

    private final double MAX_SHOOTING_SPEED = 1.0;
    private final double STARTING_SHOOTING_SPEED = 0.60;
    private final double LS_SPEED = 0.20;
    private final double TILT_SPEED = 0.4;
    private final double EXP_CONTR_TILT_MULT = 0.75;
    private final double EXP_CONT_MAX_ANGLE = 85,
            EXP_CONT_MIN_ANGLE = 60;        // 55 got in the way of tracking
    private int swj_channel;        // Shooter Wheel Jaguar channel
    private int lsv_channel;        // Lazy Susan Victor channel
    private int tltv_channel;       // Tilter Victor channel
    private int tltls_channel;      // Tilter Limit Swith channel
    private int os_top_channel;     // Top Optical Sensor channel
    private int os_bottom_channel;  // Bottom Optical Sensor channel
    private double shootingWheelSpeed = 0.0;
    public double lazySusanSpeed = 0.0;
    private double current_lazySusanSpeed = 0.0;
    public double tiltSpeed = 0.0;
    private boolean shootingWheelState;
    private boolean shootingButtonPressed;
    private boolean shootingAltButtonPressed;
    private boolean shootingAltEnabled;
    private boolean retractionContractionInProgress = false;
    private Jaguar shootingWheelJaguar;
    private Victor tiltVictor;
    private Victor lsVictor;
    private DigitalInput tiltLimitSwitch;
    private DigitalInput topOpticalSensor;
    private DigitalInput bottomOpticalSensor;
    private Joystick shootingJoystick;
    private RRTracker tracker;
    private RRBallSensor sensor;
    private RRGatherer gatherer;
    private RRDIPSwitch dipSwitch;
    public boolean tracking = false; // Indicates if robot is tracking target  // was private, changed to public
    public boolean tracking_complete = false; // Indicates if robot has finished tracking
    public boolean tracking_timeout = false;
    //private     boolean         elevation_tracking = false;  // I don't think we need separate tracking
    // for azimuth and elevation.
    private boolean isRetracting = false;
    private boolean isExpanding = false;
    private boolean ball_present = false;

    //int target_direction = 1;
    /**
     * Sets up the speed controllers for the shooter
     * @param swjc Shooting Wheel Jaguar Channel
     * @param lsvc Lazy Susan Victor Channel
     * @param tltvc Tilter Victor Channel 
     * @param tltlsc Tilter Limit Switch Channel
     * @param js Joystick to monitor for button/axis events
     */
    public RRShooter(int swjc, int lsvc, int tltvc, int tltlsc, RRTracker t, RRBallSensor ballSensor, RRGatherer gr) {
        swj_channel = swjc;
        lsv_channel = lsvc;
        tltv_channel = tltvc;
        tltls_channel = tltlsc;

        sensor = ballSensor;
        //       dipSwitch = ds;
        gatherer = gr;

        shootingWheelState = false;         // start with the shooting wheel off!

        shootingButtonPressed = false;      // indicates if the
        shootingAltButtonPressed = false;
        shootingAltEnabled = false;



        if (t != null) {
            tracker = t;
        } else {
            throw new NullPointerException("RRShooter was passed a null RRTracker object!");
        }

        shootingWheelJaguar = new Jaguar(swj_channel);
        tiltVictor = new Victor(tltv_channel);
        lsVictor = new Victor(lsv_channel);
        //tiltLimitSwitch = new DigitalInput(tltls_channel);
    }

    /**
     * 
     * @param distance distance is distance to basket in feet
     * @param targetID targetID indicates if lower (0), middle (1), or upper (2) is the target
     * @return Returns the angle for which to move the shooter at
     */
    static double determineAngle(double distance, double muzzleVelocity, int targetID) {
//      double muzzleVelocity = 8.1; //meters per second
//      double muzzleVelocity = 7.1; //meters per second

        double gravity = 9.81;  //meters per (second)^2
        double yLower = 0.7112;         // .466953;
        double yMiddle = 1.5494;       // 1.30515;
        double yHigher = 2.4892;        //2.24485;
        double xDistance = 3.6576; // distance to base of basket (as if shooting from key)
        double shooterHeight = .914; // meters off the ground
        double y = yHigher; // determined by targetID (see below)
        double theta = 0;

        xDistance = distance / 3.28; // converts feet into meters

        if (targetID == RoboRebels.LOWEST_TARGET) {
            y = yLower;
        } else if ((targetID == RoboRebels.LEFT_TARGET) && (targetID == RoboRebels.RIGHT_TARGET)) {
            y = yMiddle;
        } else if (targetID == RoboRebels.HIGHEST_TARGET) {
            y = yHigher;
        } else if (targetID == RoboRebels.AUTO_TARGET) {
            if (RoboRebels.going_for_highest) {
                y = yHigher;
            } else {
                y = yLower;
            }
        }

        y -= Math.max(shooterHeight, 0);

        //     RRLogger.logDebug(this.getClass(),"determineAngle()","d: " + RRTracker.round(distance) + "v: " + RRTracker.round(muzzleVelocity) +
        //            "y: " + y + "x: " + xDistance);

        double tempSqrtEquation = (muzzleVelocity * muzzleVelocity * muzzleVelocity * muzzleVelocity)
                - (2 * gravity * muzzleVelocity * muzzleVelocity * y) - (gravity * gravity * xDistance * xDistance);

        if (tempSqrtEquation > 0) {
            //     RRLogger.logDebug(this.getClass(),"determineAngle()","tempSqrtEq: " + RRTracker.round2(tempSqrtEquation));
            theta = MathUtils.atan(((muzzleVelocity * muzzleVelocity) + (Math.sqrt(tempSqrtEquation))) / (gravity * xDistance));
        } else {
            theta = 0;    // There is no angle for this muzzle velocity
        }
        theta = theta * (180.0 / 3.14159265); // converts radians to degreese

        //theta -= 10;    // Fudge factor to make theta correct.

        return theta;
    }

    /**
     * Gathers input states and sets up the necessary motor speeds
     */
    private void gatherInputStates() {
        double retExpAngle = 0.0;
        double LSXValue, TXValue;
        double LSX_DEAD_ZONE = 0.15,
                TX_DEAD_ZONE = 0.15;
        boolean LSLState, LSRState, TUState, TDState, TTState, CSState, ESState, ECSState, ReverseShootingState1, ReverseShootingState2;
        boolean shooterButtonState = RRButtonMap.getActionObject(RRButtonMap.SHOOTER_ENABLED).getButtonState();
        boolean shooterAltButtonState = RRButtonMap.getActionObject(RRButtonMap.SHOOTER_ALT_ENABLED).getButtonState();


        RoboRebels.printLCD(3, "SS: " + RRTracker.round2(shootingWheelJaguar.get()) + " Z: " + RRTracker.round2(this.getTransformedZValue()));
        //RoboRebels.printLCD(4, "Z:" + RRTracker.round2(this.getTransformedZValue()));
        //RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooting Speed: " + RRTracker.round2(shootingWheelJaguar.get()));
        //RRLogger.logDebug(this.getClass(),"gatherInputStates()","Z: " + RRTracker.round2(this.getTransformedZValue()));
        //RRLogger.logDebug(this.getClass(),"gatherInputStates()","Limit Switch: " + tiltLimitSwitch.get());

        /*     This is the old trigger code that used to turn the shooter motor on and off.  New trigger code puts shooter motor under shooting control
         *
        // Spin up if trigger is pressed 
        //if ( shootingJoystick.getRawButton(RRButtonMap.SHOOT) && !shootingButtonPressed )
        if ( shooterButtonState && !shootingButtonPressed )
        {
        RRLogger.logDebug(this.getClass(),"gatherInputStates()","Trigger");
        shootingWheelState = !shootingWheelState;
        }
        //else if ( !shootingJoystick.getRawButton(RRButtonMap.SHOOT) )
        else if ( !shooterButtonState )
        {
        shootingButtonPressed = false;
        }
        
        if ( shootingWheelState ) // Shooting button just pressed
        {
        //    shootingWheelSpeed = this.getTransformedZValue();

        // Set shooter wheel to speed set by tracking calculations

        if (RoboRebels.muzzle_velocity == 7.5)
        shootingWheelSpeed = 0.85;
        else if (RoboRebels.muzzle_velocity == 8.0)
        shootingWheelSpeed = 1.0;
        else
        shootingWheelSpeed = 0.85;

        shootBall();

        } else 
        {
        shootingWheelSpeed = 0.0;
        }
        
         */

        if (shooterButtonState) {
            if (!shootingButtonPressed) // Button is pushed for first time
            //           if (first_time)
            {
                shootingButtonPressed = true;
                //               first_time = false

                // Set shooter wheel to speed set by tracking calculations

                if (RoboRebels.muzzle_velocity == 7.5) {
                    shootingWheelSpeed = 0.85;
                } else if (RoboRebels.muzzle_velocity == 8.0) {
                    shootingWheelSpeed = 1.0;
                } else {
                    shootingWheelSpeed = 0.85;
                }
                
                shootingWheelSpeed = this.getTransformedZValue();       // Set shooter wheel to speed set by joystick Z control
                
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Trigger joystick button pressed, setting speed to "
                            + tracker.round2(shootingWheelSpeed));

                shootBall();
            }

        } else {
            shootingButtonPressed = false;
        }


        // Check for tilting button up, down 
        //if ( shootingJoystick.getRawButton(RRButtonMap.TILT_UP) )

        if (!tracking) {
            TUState = RRButtonMap.getActionObject(RRButtonMap.TILT_UP).getButtonState();
            TDState = RRButtonMap.getActionObject(RRButtonMap.TILT_DOWN).getButtonState();
            TXValue = RRButtonMap.getActionObject(RRButtonMap.TILT_X).getAxisState();
            //      RRLogger.logDebug(this.getClass(),"gatherInputStates()","Tilt Value: " + TXValue);

            if (Math.abs(TXValue) > TX_DEAD_ZONE) {
                if (TXValue > 0.0) {
                    tiltSpeed = TILT_SPEED;
                } else {
                    tiltSpeed = -1.0 * TILT_SPEED;
                }
            } else {
                tiltSpeed = 0.0;
            }

            if (TUState) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Tilt up");
                }
                RoboRebels.elevation_lock = false;         // No elevation target lock

                tiltSpeed = -1.0 * TILT_SPEED;

            } else if (TDState) {
                if (RoboRebels.tilt_angle > RoboRebels.MIN_TILT_ANGLE) // Check to make sure accel tilt angle isn't too low
                {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Tilt down");
                    }

                    /*if (!tiltLimitSwitch.get())
                    {
                    tiltSpeed = TILT_SPEED;
                    } else {
                    //                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Tilter limit switch pressed!");
                    tiltSpeed = 0.0;
                    }*/
                    RoboRebels.elevation_lock = false;         // No elevation target lock

                    tiltSpeed = TILT_SPEED;
                } else {
                    //              RRLogger.logDebug(this.getClass(),"gatherInputStates()","Min tilt angle exceeded!");
                    tiltSpeed = 0.0;              // Need to renenable this later@!!!
                }
            } else if (!TUState && !TDState) {
                tiltSpeed = 0.0;
            }

        }

        if (!tracking) {
            LSLState = RRButtonMap.getActionObject(RRButtonMap.LAZY_SUSAN_LEFT).getButtonState();
            LSRState = RRButtonMap.getActionObject(RRButtonMap.LAZY_SUSAN_RIGHT).getButtonState();
            LSXValue = RRButtonMap.getActionObject(RRButtonMap.LAZY_SUSAN_X).getAxisState();
            //    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Elevate value: " + LSXValue);

            //        if ( Math.abs(LSXValue) > LSX_DEAD_ZONE )
            //        {
            //            if ( LSXValue > 0.0 )
            //            {
            //                lazySusanSpeed = -1.0 * LS_SPEED;
            //            }
            //            else
            //            {
            //                if (LS_SPEED == 0.2)
            //                    lazySusanSpeed = 1.0 * LS_SPEED * 1.2;  // Motor runs more slowly to left at this speed;;
            //                else
            //                    lazySusanSpeed = 1.0 * LS_SPEED;
            //            }
            //            RoboRebels.azimuth_lock = false;         // No azimuth target lock
            //        }
            //        else
            //        {
            //            lazySusanSpeed = 0.0;
            //        }

            // Check for lazy susan button left, right
            //if ( shootingJoystick.getRawButton(RRButtonMap.LAZY_SUSAN_LEFT) )

            if (LSLState) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Lazy susan Right");
                }
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
                if (LS_SPEED == 0.2) {
                    lazySusanSpeed = 1.0 * LS_SPEED * 1.2;  // Motor runs more slowly to left at this speed;;
                } else {
                    lazySusanSpeed = 1.0 * LS_SPEED;
                }
            } else if (LSRState) {
                RoboRebels.azimuth_lock = false;         // No azimuth target lock
                lazySusanSpeed = -1.0 * LS_SPEED;
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Lazy susan Left");
                }
            } else if (!LSLState && !LSRState) {
                lazySusanSpeed = 0.0;
            }
        }


        //     RRLogger.logDebug(this.getClass(),"gatherInputStates()","Target_Azimuth:" + RRTracker.round(RoboRebels.target_azimuth) + " Targeting: " +
        //             tracking + " lazySusanSpeed: " + RRTracker.round2(lazySusanSpeed) +
        //             " tiltSpeed: " + RRTracker.round2(tiltSpeed));

        ECSState = RRButtonMap.getActionObject(RRButtonMap.EXPAND_CONTRACT_SHOOTER).getButtonState();
        //CSState = RRButtonMap.getActionObject(RRButtonMap.CONTRACT_SHOOTER).getButtonState();

        if (ECSState) {
            retExpAngle = tracker.accelAngle();
            if (retExpAngle >= EXP_CONT_MAX_ANGLE) {
                if (!isRetracting) {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter will expand now! " + retExpAngle);
                    }
                    isExpanding = true;
                }
            } else if (retExpAngle < EXP_CONT_MAX_ANGLE) {
                if (!isExpanding) {
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter will contract now" + retExpAngle);
                    }
                    isRetracting = true;
                }
            }
        }


        if (isRetracting) {
            retExpAngle = tracker.accelAngle();
            if (retExpAngle < EXP_CONT_MAX_ANGLE) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter is retracting! " + tracker.round(retExpAngle));
                }
                tiltSpeed = -1 * TILT_SPEED * EXP_CONTR_TILT_MULT;
                RoboRebels.elevation_lock = false;                  // No tilt lock
            } else if (retExpAngle >= EXP_CONT_MAX_ANGLE) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter stop retracting! " + tracker.round(retExpAngle));
                }
                tiltSpeed = 0;
                isRetracting = false;
            }
        }
        if (isExpanding) {
            retExpAngle = tracker.accelAngle();
            if (retExpAngle >= EXP_CONT_MIN_ANGLE) {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter is expanding! " + tracker.round(retExpAngle));
                }
                tiltSpeed = 1.0;    // TILT_SPEED * EXP_CONTR_TILT_MULT * 4.0;     //3.5; //   3.0 Try making this 1.25 Expand a bit faster to give more time during autonomous
                RoboRebels.elevation_lock = false;                  // No tilt lock
            } else if (retExpAngle < (EXP_CONT_MIN_ANGLE + 5)) // Try adding a +10 to this to account for angle lag
            {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Shooter stopping expanding! " + tracker.round(retExpAngle));
                }

                tiltSpeed = 0;
                isExpanding = false;
            }
        }

//        ReverseShootingState1 = RRButtonMap.getActionObject(RRButtonMap.REVERSE_SHOOTING_1).getButtonState();
//        ReverseShootingState2 = RRButtonMap.getActionObject(RRButtonMap.REVERSE_SHOOTING_2).getButtonState();
//        if ((ReverseShootingState1) && (ReverseShootingState2)) {
//            shootingWheelState = false;
//            shootingWheelSpeed = -this.getTransformedZValue();
//        }

        if (shooterAltButtonState) {
//            if (RoboRebels.DEBUG_ON) {
//                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Alt button pressed");
//            }

            if (!shootingAltButtonPressed) {

                shootingAltButtonPressed = true;

                if (RoboRebels.muzzle_velocity == 7.5) {
                    shootingWheelSpeed = 0.85;
                } else if (RoboRebels.muzzle_velocity == 8.0) {
                    shootingWheelSpeed = 1.0;
                } else {
                    shootingWheelSpeed = 0.85;
                }
                
                shootingWheelSpeed = this.getTransformedZValue();       // Set shooter wheel to speed set by joystick Z control
                
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Alt button pressed, setting speed to "
                            + tracker.round2(shootingWheelSpeed));
                }
                
            }
        } else {
//            if (RoboRebels.DEBUG_ON)
//                    RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Alt Button Not Pressed isShooting: " + RoboRebels.isShooting );

            shootingAltButtonPressed = false;

            if (!RoboRebels.shooter_motor_running) {
                shootingWheelSpeed = 0.0;               // Think this is causing the problem
            }
        }

        if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock && RoboRebels.elevation_lock) // locked())
        {
            RoboRebels.printLCD(6, "All Locked!                ");
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: All Locked!");
            }
        } else if (RoboRebels.azimuth_lock && RoboRebels.muzzle_velocity_lock) {
            RoboRebels.printLCD(6, "LS & Speed Locked!          ");
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: LS & Speed Locked!");
            }
        } else if (RoboRebels.azimuth_lock && RoboRebels.elevation_lock) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: LS & Angle Locked!");
            }
            RoboRebels.printLCD(6, "LS & Angle Locked!          ");
        } else if (RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Angle & Speed Locked!");
            }
            RoboRebels.printLCD(6, "Angle & Speed Locked!       ");
        } else if (RoboRebels.elevation_lock) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Angle Locked!");
            }
            RoboRebels.printLCD(6, "Angle Locked!               ");
        } else if (RoboRebels.muzzle_velocity_lock) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Speed Locked!");
            }
            RoboRebels.printLCD(6, "Speed Locked!               ");
        } else if (RoboRebels.azimuth_lock) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"","Shooter: LS Locked!");
            }
            RoboRebels.printLCD(6, "LS Locked!                  ");
        } else if (tracking) {
            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"gatherInputStates()","Shooter: Tracking");
            }
            RoboRebels.printLCD(6, "Tracking Target...          ");
        } else {
            RoboRebels.printLCD(6, "                            ");   // Clear the display
        }
    }

    /**
     * This is the general shoot periodic method for shooting functions.
     */
    public void shoot() {

        //       if (!tracking)        // Only check for user input and shoot if not tracking target
        //       {

        // Process input from joystick and other inputs
        //RRLogger.logDebug(this.getClass(),"","Starting Shoot");

        gatherInputStates();

        //RRLogger.logDebug(this.getClass(),"","Middle of Shoot");

        if ((RoboRebels.isShooting)) //
        {
            double time_left = Timer.getFPGATimestamp();

            if ((time_left - RoboRebels.time_started_shooting) <= RoboRebels.SHOOTER_SPINUP_TIME) {
                // Waiting for motor to spinup
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Waiting for Shooting Motor to Spinup " + tracker.round(time_left - RoboRebels.time_started_shooting));
                }
            } else if (((time_left - RoboRebels.time_started_shooting) >= RoboRebels.SHOOTER_SPINUP_TIME)
                    && ((time_left - RoboRebels.time_started_shooting) < RoboRebels.MAX_SHOOTING_TIME)) {
                // Motor is up to speed.  Sense ball and run gatherer.
                boolean ball = sensor.getShootSensor();

//                if (RoboRebels.DEBUG_ON)
//                    RRLogger.logDebug(this.getClass(),"","Shooter: Ball Sensor: " + ball);  // True if ball is there, false if no ball

                if (ball) {
                    ball_present = true;
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Ball is Present");
                    }
                    gatherer.descend();                       // Turn on gatherer motor
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Gatherer Motor is On!");
                    }
                } else if (!ball && ball_present) {
                    // load motor stop
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Ball has been shot!");
                    }

                    gatherer.stop();
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Gatherer Motor is Off!");
                    }

                    ball_present = false;

                    stopShootingBall();
                    
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shot Ball Info: Tilt: " + tracker.round(RoboRebels.tilt_angle) + 
                                " Calc Tilt: " + tracker.round(RoboRebels.calc_angle) +
                                " c: " + tracker.round(RoboRebels.calc_angle - RoboRebels.tilt_angle) + " d: " + tracker.round(RoboRebels.distance));
                    }
                } else {

                    gatherer.descend();                          // Turn on gatherer motor
                    if (RoboRebels.DEBUG_ON) {
                        RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Gatherer Motor is On!");
                    }
                }

                //              if ((time_left - RoboRebels.time_after_shooting) <=  RoboRebels.DELAY_BETWEEN_SHOTS)
                //              {
                //                  RRLogger.logDebug(this.getClass(),"","Waiting for ball to be shot!");
                //              }

            } else // too much time has elapsed
            {
                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Ball Shooting Timeout!");
                }

                gatherer.stop();

                if (RoboRebels.DEBUG_ON) {
                    RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Gatherer Motor is Off!");
                }

                ball_present = false;

                stopShootingBall();
            }

        } // Finished shooting and now wait before turning off shooting motor.
        else if (RoboRebels.shooter_motor_running) {
            double time_left = RoboRebels.SHOOTER_SPINDOWN_TIME - (Timer.getFPGATimestamp() - RoboRebels.time_after_shooting);

            if (RoboRebels.DEBUG_ON) {
                RRLogger.logDebug(this.getClass(),"shoot()","Shooter: Waiting for Shooting Motor to Spin Down " + tracker.round(time_left));
            }

            if (time_left < 0.0) {
                RoboRebels.shooter_motor_running = false;
                shootingWheelSpeed = 0.0;
                //              RoboRebels.isShooting = false;      // Don't do this in stopShootingBall or else motor shuts off too soon.
            }
        }

        // Process shooter states
        setShooterSpeeds();
    }

//    }   // Part of if (!tracking)
    /**
     * This private method sets the determined speeds for the various
     * mechanisms used by the shooter module.  
     */
    private void setShooterSpeeds() {
//         if (RoboRebels.DEBUG_ON)
//             RRLogger.logDebug(this.getClass(),"setShooterSpeeds()","Shooter: setShooterSpeeds()");

//        shootingWheelJaguar.set(-1.0 * shootingWheelSpeed * 0.56);      //0.58);        // 0.58 is a speed to shoot lowest basket for indoors

        shootingWheelJaguar.set(-1.0 * shootingWheelSpeed);

        //   RRLogger.logDebug(this.getClass(),"setShooterSpeeds()","s: " + RRTracker.round2(tiltSpeed));
        tiltVictor.set(tiltSpeed);

        //       int result = check_ls_position();    // Check for LS position before changing LS Speed.

        //       if ((lazySusanSpeed < 0.0) && result == RoboRebels.AT_LEFT_LIMIT)
        //       {
        //           RRLogger.logDebug(this.getClass(),"setShooterSpeeds()","LS Stopped at Left Limit");
        //lazySusanSpeed = 0.0;
        //      }
        //      else if ((lazySusanSpeed > 0.0) && result == RoboRebels.AT_RIGHT_LIMIT)
        //      {
        //         RRLogger.logDebug(this.getClass(),"setShooterSpeeds()","LS Stopped at Right Limit");
        //lazySusanSpeed = 0.0;
        //     }
        lsVictor.set(lazySusanSpeed);
        //       current_lazySusanSpeed = lazySusanSpeed;    // Stores current speed for next position calculation
    }

    public void setTrackerSpeeds() {
//         if (RoboRebels.DEBUG_ON)
//             RRLogger.logDebug(this.getClass(),"setTrackerSpeeds()","Shooter: setTrackerSpeeds()");

        tiltVictor.set(tiltSpeed);
        lsVictor.set(lazySusanSpeed);
    }

    /**
     * This method gets the value of the Z dial on a joystick and
     * transforms it to fit within the speed range of the shooter.
     * 
     * @return transformed speed from the corresponding Z value
     */
    private double getTransformedZValue() {
        double speed;
        RRAction aoS = RRButtonMap.getActionObject(RRButtonMap.SHOOTER_SPEED);
        //       return MAX_SHOOTING_SPEED * (aoS.js.getZ() + 1.0) / 2.0;

        //  speed is adjusted between (2*STARTING_SHOOTING_SPEED - MAX_SHOOTING_SPEED) and MAX_SHOOTING_SPEED

        speed = STARTING_SHOOTING_SPEED + (STARTING_SHOOTING_SPEED - MAX_SHOOTING_SPEED) * aoS.js.getZ(); // getZ() is in range +/- 1?
        //      RRLogger.logDebug(this.getClass(),"getTransformedZValue()","New Shooting Speed: " + RRTracker.round2(speed));
        return speed;
    }

    /**
     * This public immediately stops the lazySusan to prevent overshoot
     * mechanisms used by the tracker module  
     */
    public void stopLazySusan() {
        lazySusanSpeed = 0.0;
        //check_ls_position();
        lsVictor.set(0.0);
//        current_lazySusanSpeed = lazySusanSpeed;

        if (RoboRebels.DEBUG_ON) {
            RRLogger.logDebug(this.getClass(),"stopLazySusan()","Shooter: Halting LazySusan!");
        }

    }

    public void shootBall() {
        if (RoboRebels.DEBUG_ON) {
            RRLogger.logDebug(this.getClass(),"shootBall()","Shooter: Shooting Ball!");
        }

        RoboRebels.isShooting = true;
        RoboRebels.shooter_motor_running = true;
        RoboRebels.time_started_shooting = Timer.getFPGATimestamp();
        if (RoboRebels.autonomous_mode) {
            RoboRebels.autonomous_mode_tracking = false; // Don't target while shooting since motor vibration will interfere with targeting
        }
        return;
    }

    public void stopShootingBall() {
        if (RoboRebels.DEBUG_ON) {
            RRLogger.logDebug(this.getClass(),"stopShootingBall()","Shooter: Stop Shooting Ball!");
        }

        RoboRebels.isShooting = false;
        RoboRebels.isFinishedShooting = true;
        RoboRebels.time_after_shooting = Timer.getFPGATimestamp();

        return;
    }

    /**
     * This method retracts the shooter by making use of the accelerometer in 
     * RRTracker
     */
    public void retractShooter() {
        isRetracting = true;
        //      RRLogger.logDebug(this.getClass(),"retractShooter()","About to retract...");
    }

    public void expandShooter() {
        isExpanding = true;
        //       RRLogger.logDebug(this.getClass(),"expandShooter()","About to expand...");
    }

//    public int check_ls_position()      // Didn't get this working - not used.
//    {
//        
//        double calibration_factor = 46.0;  // converts lazySusanSpeed to angular velocity in degrees per tick
//                                          // 23.0 is approximate value for 0.2 LS SPEED
//        
////        RRLogger.logDebug(this.getClass(),"check_ls_position()","Checking Position...");
//        
//        double time_current = Timer.getFPGATimestamp();
//        
//        RoboRebels.angle_position += current_lazySusanSpeed * calibration_factor * (time_current - RoboRebels.time_last_update);
//        
//        RoboRebels.time_last_update = time_current;
//        
//        RoboRebels.printLCD(5, "LS Position: " + RRTracker.round(RoboRebels.angle_position)); 
//        
//  //      RRLogger.logDebug(this.getClass(),"check_ls_position()","LS Position: " + RRTracker.round(RoboRebels.angle_position));
//        
//        int result = RoboRebels.OK;                 // Return indication if at either limit
//        if (RoboRebels.angle_position > 45.0)       // Make 80 degrees when done testing
//            result = RoboRebels.AT_RIGHT_LIMIT;
//        else if (RoboRebels.angle_position < -45.0) // Make -80 degrees when done testing
//            result = RoboRebels.AT_LEFT_LIMIT;
//       
//        return (result);
//    }
    public void reset() {
        tracking = false;
        isRetracting = false;
        isExpanding = false;
        ball_present = false;

        shootingWheelSpeed = 0.0;
        lazySusanSpeed = 0.0;
        current_lazySusanSpeed = 0.0;
        tiltSpeed = 0.0;

        tracking_complete = false; // Indicates if robot has finished tracking
        tracking_timeout = false;

    }
    
    public void reset_after_auton() {
        
        tracking = false;
        isRetracting = false;
        isExpanding = false;
 //       ball_present = false;

 //       shootingWheelSpeed = 0.0;
 //       lazySusanSpeed = 0.0;
 //       current_lazySusanSpeed = 0.0;
 //       tiltSpeed = 0.0;

 //       tracking_complete = false; // Indicates if robot has finished tracking
 //       tracking_timeout = false;

    }

    public boolean locked() {
        boolean lock_value;

//        if (!RoboRebels.troubleshooting)
        lock_value = RoboRebels.azimuth_lock && RoboRebels.elevation_lock && RoboRebels.muzzle_velocity_lock;
//        else
//            lock_value = true;      //      dipSwitch.getState(3);
//        if (RoboRebels.DEBUG_ON)
//            RRLogger.logDebug(this.getClass(),"locked()","Shooter: locked() " + lock_value);

        return lock_value;
    }

    public void auton_shoot() {
        if (RoboRebels.DEBUG_ON) {
            RRLogger.logDebug(this.getClass(),"auton_shoot()","Shooter: auton_shoot() speed: " + RoboRebels.muzzle_velocity);
        }

        if (RoboRebels.muzzle_velocity == 7.5) {
            shootingWheelSpeed = 0.85;
        } else if (RoboRebels.muzzle_velocity == 8.0) {
            shootingWheelSpeed = 1.0;
        } else {
            shootingWheelSpeed = 0.85;
        }
    }
}
