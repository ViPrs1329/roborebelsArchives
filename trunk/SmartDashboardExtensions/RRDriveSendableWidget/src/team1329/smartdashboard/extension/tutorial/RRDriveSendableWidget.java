/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package team1329.smartdashboard.extension.tutorial;

import edu.wpi.first.smartdashboard.gui.Widget;
import edu.wpi.first.smartdashboard.properties.Property;
import edu.wpi.first.smartdashboard.types.DataType;
import edu.wpi.first.wpilibj.networking.NetworkTable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

/**
 *
 * @author dmw
 */
public class RRDriveSendableWidget extends Widget
{
    public static final DataType[] TYPES = { DataType.TABLE };

    NetworkTable        table;
    
    @Override
    public void init() 
    {
        
    }

    @Override
    public void propertyChanged(Property prprt)
    {
        
    }

    @Override
    public void setValue(Object o)
    {
        table = (NetworkTable) o;
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) 
    {
        Dimension size = getSize();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, size.width, size.height);
    }
}
