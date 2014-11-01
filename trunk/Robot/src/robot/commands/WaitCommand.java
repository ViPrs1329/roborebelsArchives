/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.commands;

/**
 *
 * @author William
 */
public class WaitCommand extends CommandBase {
    int time;
    public WaitCommand(int t) {
        if (t > 0) time = t;
    }
    public void initialize() {
        
    }
    public void execute() {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) { }
    }
    public boolean isFinished() {
        return true;
    }
    public void end() {
        
    }
            
    
}
