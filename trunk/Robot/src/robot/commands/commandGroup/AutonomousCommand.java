/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.commands.commandGroup;
import robot.commands.*;

/**
 *
 * @author William
 */
public class AutonomousCommand extends CommandGroup {
    public AutonomousCommand() {
        //Use this section to add commands to the commandgroup
        //to methods to do so, addSequential, and addParallel
        //addParallel will not be used in this project
        addSequential(new ExampleCommand());
        addSequential(new WaitCommand(5000));
    }
            
}
