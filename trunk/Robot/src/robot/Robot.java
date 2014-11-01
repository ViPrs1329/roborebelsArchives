/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import robot.commands.CommandBase;
/**
 *
 * @author William
 */
public class Robot implements Runnable {

    /**
     * @param args the command line arguments
     */
    private static boolean running = true;
    public static void main(String[] args) {
        // TODO code application logic here
        Thread t = new Thread(new Robot(), "new Thread");
        t.start();
        RoboRebels.AutonomousInit();
        terminate();
    }
    
    public static void terminate() {
        running = false;
        System.exit(0);
    }
    public void run() {
        while (running) {
            Handler.handle(CommandBase.getDriveTrain());
        }
    }
            
            
}
