/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.ADXL345_I2C;

/**
 *
 * @author Derek Ward
 */
public class RRAccel 
{
    private     ADXL345_I2C         m_accel;
    
    
    /**
     * RRAccel default constructor
     */
    public RRAccel()
    {
        m_accel = new ADXL345_I2C(4, ADXL345_I2C.DataFormat_Range.k2G);
    }
    
    
    public double getX()
    {
        return m_accel.getAcceleration(ADXL345_I2C.Axes.kX);
    }
    
    
    public double getY()
    {
        return m_accel.getAcceleration(ADXL345_I2C.Axes.kY);
    }
    
    
    public double getZ()
    {
        return m_accel.getAcceleration(ADXL345_I2C.Axes.kZ);
    }
    
    public ADXL345_I2C.AllAxes getAll()
    {
        return m_accel.getAccelerations();
    }
    
}
