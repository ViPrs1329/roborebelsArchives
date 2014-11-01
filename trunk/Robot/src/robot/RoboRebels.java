/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import robot.commands.commandGroup.AutonomousCommand;

/**
 *
 * @author William
 */
public class RoboRebels {
    
    public static void AutonomousInit() {
        AutonomousCommand autonCommand = new AutonomousCommand();
        autonCommand.run();
        
    }
    public void AutonomousPeriodic() {
        
    }
    
}
