/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.commands.commandGroup;

import robot.commands.CommandBase;
import java.util.ArrayList;

/**
 *
 * @author William
 */
public class CommandGroup extends CommandBase {
    ArrayList<CommandBase> al = new ArrayList();
    public void addSequential( CommandBase b) {
        al.add(b);
        
    }
    public void run() {
        for (int i = 0; i<al.size();i++) {
             al.get(i).run();
        }
    }
    
}
