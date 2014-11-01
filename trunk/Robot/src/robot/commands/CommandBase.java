/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.commands;
import robot.subsystems.*;

/**
 *
 * @author William
 */
public class CommandBase {
    public static DriveTrain driveTrain = new DriveTrain();
    
    public CommandBase() {
        
    }
    public static DriveTrain getDriveTrain() {
        return driveTrain;
    }
    
    public void run() {
        initialize();
        do {
            execute();
        } while (!isFinished()); 
            
        
        end();
    }
    public void initialize() {
        
    }
    public void execute() {
        
    }
    public boolean isFinished() {
        return false;
    }
    public void end() {
        
    }
            
            
            
    
    
}
