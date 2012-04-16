/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Victor;

/**
 *  Simple code to test the sliding mechanism as requested
 * 
 * @author deeek
 */
public class RRSlider 
{
    final double    SLIDER_SPEED = 0.5;
    
    Victor      v1,         // if 2V, front; if 4V, front left
                v2,         // if 2V, back; if 4V, back left
                v3,         // if 2V, null; if 4V, front right
                v4;         // if 2V, null; if 4V, back right
    
    double      frontSliderSpeed = 0.0,
                backSliderSpeed = 0.0;
    boolean     twoVictorSetup = true;
    
    public RRSlider(int vc1, int vc2, int vc3, int vc4)
    {
        if (vc1 > 0)
            v1 = new Victor(vc1);
        else
            throw new IllegalArgumentException("RRSlider was passed an invalid argument (vc1): " + vc1);
        
        if (vc2 > 0)
            v2 = new Victor(vc2);
        else
            throw new IllegalArgumentException("RRSlider was passed an invalid argument (vc2): " + vc2);
        
        if (vc3 > 0)
            v3 = new Victor(vc3);
        else
        {
            twoVictorSetup = true;
            v3 = null;
        }
        
        if (vc4 > 0)
            v4 = new Victor(vc4);
        else
        {
            twoVictorSetup = true;
            v3 = null;
            v4 = null;
        }
    }
    
    public void slide()
    {
        gatherInput();
        
        setMotorSpeeds();
    }
    
    private void gatherInput()
    {
        boolean frontSliderInPressed = RRButtonMap.getActionObject(RRButtonMap.FRONT_SLIDER_IN).getButtonState();
        boolean frontSliderOutPressed = RRButtonMap.getActionObject(RRButtonMap.FRONT_SLIDER_OUT).getButtonState();
        boolean backSliderInPressed = RRButtonMap.getActionObject(RRButtonMap.BACK_SLIDER_IN).getButtonState();
        boolean backSliderOutPressed = RRButtonMap.getActionObject(RRButtonMap.FRONT_SLIDER_OUT).getButtonState();
        
        if ( frontSliderInPressed )
            frontSlideIn();
        else if ( frontSliderOutPressed )
            frontSlideOut();
        else
            frontSlideStop();
        
        if ( backSliderInPressed )
            backSlideIn();
        else if ( backSliderOutPressed )
            backSlideOut();
        else
            backSlideStop();
    }
    
    private void setMotorSpeeds()
    {
        v1.set(frontSliderSpeed);
        v2.set(backSliderSpeed);
        
        if ( !twoVictorSetup )
        {
            v3.set(frontSliderSpeed);
            v4.set(backSliderSpeed);
        }
    }
    
    public void frontSlideIn()
    {
        frontSliderSpeed = SLIDER_SPEED;
    }
    
    public void frontSlideOut()
    {
        frontSliderSpeed = -1.0 * SLIDER_SPEED;
    }
    
    public void frontSlideStop()
    {
        frontSliderSpeed = 0.0;
    }
    
    public void backSlideIn()
    {
        backSliderSpeed = SLIDER_SPEED;
    }
    
    public void backSlideOut()
    {
        backSliderSpeed = -1.0 * SLIDER_SPEED;
    }
    
    public void backSlideStop()
    {
        backSliderSpeed = 0.0;
    }
}
