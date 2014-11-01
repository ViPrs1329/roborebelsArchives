/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.commands;

/**
 *
 * @author William
 */
public class ExampleCommand extends CommandBase {
    public ExampleCommand() {
        //In actual robot code, this would be used to require subsystems.
        //eg require(driveTrain);
        //This is not necessary for the purposes of this project
    }
    public void initialize() {
        //Called once at the beginning of the run method
    }
    public void execute() {
        //Called continuously until isFinished evaluates to true
    }
    public boolean isFinished() {
        //Returns whether or not the command is finished executing
        return true;
    }
    public void end() {
        //called after isFinished evaluates to true
        
    }
            
            
    
}
