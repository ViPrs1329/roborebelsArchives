/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author dward
 *
 * Represents a Dip Switch connected via DigitalInputs
 */
public class RRDipSwitch {

    private     int                 m_numberOfDipSwitches;
    private     DigitalInput        m_dipSwitches[];

    /**
     * Constructor for a RRDipSwitch object.  It's arguments are a range
     * of digital input ports
     * 
     * @param diChannelStart Port start of digital inputs used
     * @param diChannelEnd Port end of digital inputs used
     */
    
    public RRDipSwitch( int diChannelStart, int diChannelEnd )
    {
        m_numberOfDipSwitches = diChannelEnd - diChannelStart + 1;
        m_dipSwitches = new DigitalInput[m_numberOfDipSwitches];

        for ( int i = 0, chan = diChannelStart; i < m_numberOfDipSwitches; i++, chan++ )
        {
            m_dipSwitches[i] = new DigitalInput(chan);
        }
        
    }

    /**
     * Returns the state of a switch channel (ie. a switch)
     * 
     * @param switchChannel Get state of this passed switch channel
     * @return True or false for the state of the switch
     */
    
    public boolean getState( int switchChannel )
    {
        if ( switchChannel < m_numberOfDipSwitches && switchChannel >= 0 ){
            return m_dipSwitches[switchChannel].get();
         
        }
        else
        {
            System.out.println("RRDipSwitch::getState() - Passed switchChannel out of range!");
            return false;
        }
    }
}
