/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

/**
 * Abstract class which all other RRAuton classes
 * must implement
 * 
 * @author Derek Ward
 */
public interface RRAuton 
{
    public void init();
    
    public void run();
    
    public void reset();
}
