
package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 *
 * @author dward
 *
 * Represents a Dip Switch connected via DigitalInputs
 */
public class RRDIPSwitch {

    private     int                 m_numberOfDipSwitches;
    private     DigitalInput        m_dipSwitches[];

    public RRDIPSwitch( int diChannelStart, int diChannelEnd )
    {
        m_numberOfDipSwitches = diChannelEnd - diChannelStart + 1;
        m_dipSwitches = new DigitalInput[m_numberOfDipSwitches];

        for ( int i = 0, chan = diChannelStart; i < m_numberOfDipSwitches; i++, chan++ )
        {
            m_dipSwitches[i] = new DigitalInput(chan);
        }
        
    }

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