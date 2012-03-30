/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkListener;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboardData;

/**
 *
 * @author dmw
 */
public class RRDriveSendable extends RRDrive implements SmartDashboardData
{
    private                 NetworkTable            table;
    
    /** The time (in seconds) between updates to the table */
    private static final    double                  DEFAULT_TIME_BETWEEN_UPDATES = .2;
    
    /** The period (in seconds) between value updates */
    private                 double                  period = DEFAULT_TIME_BETWEEN_UPDATES;
    
    protected               int                     leftEncoderOffset, rightEncoderOffset;
    
    
    
    public RRDriveSendable(int leftChannel, int rightChannel, int leftAEncoderChannel, int leftBEncoderChannel, 
                           int rightAEncoderChannel, int rightBEncoderChannel)
    {
        super(leftChannel, rightChannel, leftAEncoderChannel, leftBEncoderChannel, rightAEncoderChannel, rightBEncoderChannel);
        
        System.out.println("RRDriveSendable::RRDriveSendable() Constructing RRDriveSendable object...");
        
        leftEncoderOffset = rightEncoderOffset = 0;
    }
    
    public int getLeftEncoderValue()
    {
        return leftEncoderOffset + super.getLeftEncoderValue();
    }
    
    public int getRightEncoderValue()
    {
        return rightEncoderOffset + super.getRightEncoderValue();
    }
    
    public void resetLeftEncoderToValue(int c)
    {
        super.resetLeftEncoder();
        
        leftEncoderOffset = c;
    }
    
    public void resetRightEncoderToValue(int c)
    {
        super.resetRightEncoder();
        
        rightEncoderOffset = c;
    }
    
    public void drive(boolean tankDrive)
    {
        super.drive(tankDrive);
    }
    
    
    public String getType() 
    {
        return "RRDriveSendable";
    }

    public NetworkTable getTable() 
    {
        if ( table == null )
        {
            table = new NetworkTable();
            table.beginTransaction();
            table.putDouble("leftMotorSpeed", leftMotorSpeed);
            table.putDouble("rightMotorSpeed", rightMotorSpeed);
            table.putInt("leftDriveEncoder", super.getLeftEncoderValue());
            table.putInt("rightDriveEncoder", super.getRightEncoderValue());
            
            table.addListener("angle", new NetworkListener() {

                public void valueChanged(String key, Object value) {
                    if (key.equals("leftDriveEncoder"))
                    {
                        resetLeftEncoderToValue(((Integer) value).intValue());
                    }
                    else if (key.equals("rightDriveEncoder"))
                    {
                        resetRightEncoderToValue(((Integer) value).intValue());
                    }
                }

                public void valueConfirmed(String key, Object value) {
                }
            });
            
            table.endTransaction();
            
            new Thread() {

                public void run() {
                    while (true) {
                        Timer.delay(period);
                        System.out.println("RRDriveSendable::getTable()::Thread " + getLeftEncoderValue() + " | " + getRightEncoderValue());
                        table.beginTransaction();
                        table.putInt("leftDriveEncoder", (int) getLeftEncoderValue());
                        table.putInt("rightDriveEncoder", (int) getRightEncoderValue());
                        table.endTransaction();
                    }
                }
            }.start();
        }
        
        return table;
    }
    
}
