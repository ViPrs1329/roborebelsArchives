/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Gyro;

/**
 * This is a wrapper class to handle access to a gyro
 *
 * Note:
 *
 *   * When the gyro rotates left, the value is changed negatively
 *   * When the gyro rotates right, the value is changed positively
 *
 * @author Derek Ward
 */
public class RRGyro
{
    private Gyro            m_gyro;
    private final  int      m_gyroChannel;

    /**
     * Default constructor.  Assumes that the gyro channel
     * is 1.
     */
    public RRGyro()
    {
        m_gyroChannel = 1;

        SetupGyro();
    }

    /**
     * Allows a gyro channel to be passed in, however, if
     * the passed channel is garbage, it defaults to
     * channel 1.
     * @param channel
     */
    public RRGyro( int channel )
    {
        if ( channel > 0 )
            m_gyroChannel = channel;
        else
            m_gyroChannel = 1;

        SetupGyro();
    }

    /**
     * Setups up the gyro internally (ie. creatse a new object) and
     * resets the gyro.
     */
    private void SetupGyro()
    {
        // create a new gyro, with the appropriate gyro channel
        m_gyro = new Gyro( m_gyroChannel );

        // reset the gyro.
        m_gyro.reset();
    }


    public void reset()
    {
        m_gyro.reset();
    }

    /**
     * Returns the current angle.
     * @return Returns the angle in degrees.
     */
    public double getAngle()
    {
        return m_gyro.getAngle();
    }
}
