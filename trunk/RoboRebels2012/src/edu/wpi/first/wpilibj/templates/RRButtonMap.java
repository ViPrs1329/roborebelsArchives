package edu.wpi.first.wpilibj.templates;




/**
 * This class will store data about the button map setup
 * 
 * TODO:
 * 
 *   - Add 
 * 
 * 
Joystick Buttons/axis	Action	                This button layout is if the auto ball sensing is not functional
1	                Shoot on, off           ie. Manual control
2                       Loader Up	
3	                Loader Down	        Also, this is in right hand config mode
4	                Lazy Susan left	
5	                Lazy Susan right	
6	                Tilt up	
7	                Tilt down	
8	                Bridge arm down	
9	                Bridge arm up	
10	                Spinner in, reverse, stop	
11	                Track target using Lazy Susan	
Z	                Shooter speed control	
Axis	                Drive (Arcade)	
		
		
Xbox buttons/axis	Action	
A                       Tilt down	
B	                Loader down	
X	                Loader up	
Y	                Tilt up	
L Bump	                Lazy Susan left	
R Bump	                Lazy Susan right	
Back	                Bridge arm up	
Start	                Bridge arm down	
L Click		
R Click	                Auto track on, off	
Left axis               Drive (arcade)	
Right axis              Shooter speed control (one axis)	
Left trigger            Spinner start, reverse, stop	
Right trigger           Shoot on, off	
 * 
 * 
 * 
 *  Xbox button map
 * 0
 * 1 A
 * 2 B
 * 3 X
 * 4 Y
 * 5 L Bumper
 * 6 R Bumper
 * 7 Back
 * 8 Start
 * 9 L Stick Click
 * 10 R Stick Click
 * 
 * Axis
 * 
 *  ¥1: Left Stick X Axis
        -Left:Negative ; Right: Positive
    ¥2: Left Stick Y Axis
        -Up: Negative ; Down: Positive
    ¥3: Triggers
        -Left: Positive ; Right: Negative
    ¥4: Right Stick X Axis
        -Left: Negative ; Right: Positive
    ¥5: Right Stick Y Axis
        -Up: Negative ; Down: Positive
    ¥6: Directional Pad (Not recommended, buggy)
 * 
 * 
 * 
 * 
 * @author dmw
 */

import com.sun.squawk.util.SquawkHashtable;
import edu.wpi.first.wpilibj.Joystick;

public class RRButtonMap 
{
    /************  NOTE:  THESE ARE NO LONGER BUTTON VARIABLES, BUT ACTION IDs!!! ********/
    static int      SHOOTER_ENABLED = 1,
                    LOADER_UP = 3,
                    LOADER_DOWN = 4,
                    LAZY_SUSAN_LEFT = 5,
                    LAZY_SUSAN_RIGHT = 6,
                    TILT_UP = 7,
                    TILT_DOWN = 8,
                    BRIDGE_ARM_DOWN = 9,
                    BRIDGE_ARM_UP = 10,
                    SPINNER_FORWARD = 11,
                    //SPINNER_DISABLED = 12,
                    SPINNER_REVERSED = 13,
                    TRACK_TARGET = 14,
                    CANNON_SPEED = 15,
                    ARCADE_STICK_X = 16,
                    ARCADE_STICK_Y = 17,
                    CONTRACT_SHOOTER = 18,
                    EXPAND_SHOOTER = 19;
    
    Joystick        lJoystick, rJoystick, xboxController;
    
    static SquawkHashtable    actionList;


    RRButtonMap(Joystick j1, Joystick j2, Joystick j3)
    {
        actionList = new SquawkHashtable();
        
        if ( j1 != null )
            lJoystick = j1;
        else
            throw new NullPointerException("RRButtonMap was passed a null Joystick object (j1)! ");
        
        if ( j2 != null )
            rJoystick = j2;
        else
            throw new NullPointerException("RRButtonMap was passed a null Joystick object (j2)! ");
        
        if ( j3 != null )
            xboxController = j3;
        else
            throw new NullPointerException("RRButtonMap was passed a null Joystick object (j3)! ");
        
        setControllers();
    }
    
     
    public void setControllers()
    {
        insertAction(ARCADE_STICK_X, -1, 1, xboxController );
        insertAction(ARCADE_STICK_Y, -1, 2, xboxController );
        insertAction(SHOOTER_ENABLED, 3, -1, xboxController);
        //insertAction(SHOOTER_ENABLED, 1,  -1, rJoystick);
        insertAction(LOADER_UP, 4,  -1, xboxController);
        insertAction(LOADER_DOWN, 1,  -1, xboxController);
        insertAction(SPINNER_FORWARD, -1, 3, xboxController); // RT
        insertAction(SPINNER_REVERSED, -1,  3, xboxController); // LT
        insertAction(LAZY_SUSAN_LEFT, 4,  -1, rJoystick);
        insertAction(LAZY_SUSAN_RIGHT, 5,  -1, rJoystick);
        insertAction(TILT_UP, 3,  -1, rJoystick);
        insertAction(TILT_DOWN, 2,  -1, rJoystick);
        insertAction(CANNON_SPEED, -1, -1, rJoystick); // Z axis on joystick
        insertAction(BRIDGE_ARM_DOWN, -1, 2, rJoystick); // Up on joystick
        insertAction(BRIDGE_ARM_UP, -1, 2, rJoystick); // Down on joystick
        insertAction(CONTRACT_SHOOTER, 6, -1, rJoystick);
        insertAction(EXPAND_SHOOTER, 7, -1, rJoystick);
        insertAction(TRACK_TARGET, 11, -1, rJoystick);
        
        // Didn't insert Tilt/Rotate Cannon because it used the DPad. The comment at the top
        // warns not to use the DPad.
        
        // Start and back at the same time causes an emergency backwards cannon pulse
    }
    
    public void insertAction(int actionID, int buttonID, int axisID, Joystick js)
    {
        actionList.put(Integer.valueOf(actionID), new RRAction(actionID, buttonID, axisID, js));
    }
    
    
    public static RRAction getActionObject(int k)
    {
        Object  a = RRButtonMap.actionList.get(Integer.valueOf(k));
        
        return (RRAction) a;
    }
    

    /*
    public static void setController(String type) {
        if (type.equals("joystick")) {
            SHOOT = 1;
            LOADER_UP = 2;
            LOADER_DOWN = 3;
            LAZY_SUSAN_LEFT = 4;
            LAZY_SUSAN_RIGHT = 5;
            TILT_UP = 6;
            TILT_DOWN = 7;
            BRIDGE_ARM_DOWN = 8;
            BRIDGE_ARM_UP = 9;
            SPINNER = 10;
            TRACK_TARGET = 11;
        } else if (type.equals("xbox")) {
            // TODO: Put Xbox Controller Values Here
            
        }
    }
    * 
    */
}
