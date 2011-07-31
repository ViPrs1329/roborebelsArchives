/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 * An interface which all other RRAuton classes
 * must implement
 * 
 * @author Derek Ward
 */
public interface RRAuton 
{
    public void init();
    
    public void run();
    
    public void reset();
    
    public double getTime();
    
    public int getCount();
    
    public double getAngle(); 
}
