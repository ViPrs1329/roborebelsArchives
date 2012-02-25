/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Victor;

/**
 * The gatherer class has the following components:
 * 
 * - The spinner
 * - The ball storage system
 * - The ball sensor system
 * 
 * 
 * Joystick Buttons/axis	Action	        This button layout is if the auto ball sensing is not functional

2                       Loader Up	
3	                Loader Down	        Also, this is in right hand config mode
4	                
10	                Spinner in	
11	                Spinner out
* 
 *
 * TODO:
 * 
 * - TEST!
 * - Implement ball sensor system
 * - Implement autonomous extensions
 * 
 * @author dmw
 */
public class RRGatherer 
{
    private final double        SPINNER_SPEED = 0.5;
    private final double        CONVEYER_SPEED = 0.5;
    
    private     int             spinner_channel;
    private     int             ball_conveyer_channel;
    private     int             bottom_ball_sensor_channel,
                                middle_ball_sensor_channel,
                                top_ball_sensor_channel;
    
    private     double          spinnerSpeed = 0.0;
    private     double          conveyerSpeed = 0.0;
    
    private     int             spinnerState = 0;           // 0 = off, 1 = up, 2 = down
    private     boolean         spinnerButtonPressed = false;
    
    private     Joystick        js;
    private     Victor          spinnerVictor;
    private     Victor          ballConveyerVictor;
    private     DigitalInput    bbsDigitalInput,
                                mbsDigitalInput,
                                tbsDigitalInput;
    
    /**
     * 
     * @param sc Spinner channel
     * @param bcc Ball conveyer channel
     * @param bbsc Bottom ball sensor channel
     * @param mbsc Middle ball sensor channel
     * @param tbsc Top ball sensor channel
     * @param j Joystick
     */
    RRGatherer(int sc, int bcc, int bbsc, int mbsc, int tbsc, Joystick j)
    {
        spinner_channel = sc;
        ball_conveyer_channel = bcc;
        bottom_ball_sensor_channel = bbsc;
        middle_ball_sensor_channel = mbsc;
        top_ball_sensor_channel = tbsc;
        
        if ( j != null)
            js = j;
        else
            throw new NullPointerException("RRGatherer was passed a null Joystick object!!!");
        
        spinnerVictor = new Victor(spinner_channel);
        ballConveyerVictor = new Victor(ball_conveyer_channel);
        bbsDigitalInput = new DigitalInput(bottom_ball_sensor_channel);
        mbsDigitalInput = new DigitalInput(middle_ball_sensor_channel);
        tbsDigitalInput = new DigitalInput(top_ball_sensor_channel);
    }
    
    
    /**
     * 
     * 
     *  Joystick Buttons/axis	Action	        This button layout is if the auto ball sensing is not functional

        2                       Loader Up	
        3	                Loader Down	        Also, this is in right hand config mode
        	                
        10	                Spinner in, reverse, stop
     */
    private void gatherInputStates()
    {
        //System.out.println("RRGatherer::gatherInputStates()");
        
        // Get conveyer button state
        if ( js.getRawButton(RRButtonMap.LOADER_UP) && !js.getRawButton(RRButtonMap.LOADER_DOWN) )
        {
            conveyerSpeed = CONVEYER_SPEED;
        }
        else if ( js.getRawButton(RRButtonMap.LOADER_DOWN) && !js.getRawButton(RRButtonMap.LOADER_UP) )
        {
            conveyerSpeed = -1.0 * CONVEYER_SPEED;
        }
        else if ( !js.getRawButton(RRButtonMap.LOADER_UP) && !js.getRawButton(RRButtonMap.LOADER_DOWN) )
        {
            conveyerSpeed = 0.0;
        }
        
        
        // Check for spinner button state
        if ( js.getRawButton(RRButtonMap.SPINNER) && !spinnerButtonPressed )
        {
            if ( spinnerState == 0 )
            {
                spinnerSpeed = SPINNER_SPEED;
                spinnerState = 1;
            }
            else if ( spinnerState == 1 )
            {
                spinnerSpeed = -1.0 * SPINNER_SPEED;
                spinnerState = 2;
            }
            else if ( spinnerState == 2 )
            {
                spinnerSpeed = 0.0;
                spinnerState = 0;
            }
            
            spinnerButtonPressed = true;
        }
        else if ( !js.getRawButton(RRButtonMap.SPINNER) )
        {
            spinnerButtonPressed = false;
        }
        
        //System.out.println("RRGatherer::gatherInputStates - CS: " + conveyerSpeed + " | SS: " + spinnerSpeed);
        
        // Check ball sensors ...
    }
    
    
    /**
     * 
     */
    public void gather()
    {
        gatherInputStates();
        
        setGathererSpeeds();
    }
    
    /**
     * 
     */
    private void setGathererSpeeds()
    {
        spinnerVictor.set(spinnerSpeed);
        ballConveyerVictor.set(conveyerSpeed);
    }
}
