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

import edu.wpi.first.wpilibj.Joystick;

public class RRButtonMap 
{
    private class RRAction
    {
        int         button_map;
        Joystick    joystick;
    }
    
    static int      SHOOT,
                    LOADER_UP,
                    LOADER_DOWN,
                    LAZY_SUSAN_LEFT,
                    LAZY_SUSAN_RIGHT,
                    TILT_UP,
                    TILT_DOWN,
                    BRIDGE_ARM_DOWN,
                    BRIDGE_ARM_UP,
                    SPINNER,
                    TRACK_TARGET;


    RRButtonMap(Joystick j1, Joystick j2, Joystick j3)
    {
        
    }

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
}
