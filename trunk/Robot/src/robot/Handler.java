/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot;
import robot.subsystems.DriveTrain;

/**
 *
 * @author William
 */
public class Handler {
    public static void handle(DriveTrain d) {
        if((d.getLeftSideValue() <d.THRESHHOLD) && (d.getLeftSideValue()>-d.THRESHHOLD) && (d.getRightSideValue() < d.THRESHHOLD) && (d.getRightSideValue() > -d.THRESHHOLD)) {
            System.out.println("The Robot is not moving");
        }
        else if ((d.getLeftSideValue() > d.THRESHHOLD) && (d.getLeftSideValue() > d.getRightSideValue()+d.THRESHHOLD) && (d.getRightSideValue() > d.THRESHHOLD)) {
            System.out.println("The Robot is moving forward and turning to the right");
        }
         else if ((d.getRightSideValue() > d.THRESHHOLD) && (d.getRightSideValue() > d.getLeftSideValue()+d.THRESHHOLD) && (d.getLeftSideValue() > d.THRESHHOLD)) {
             System.out.println("The Robot is moving forward and turning to the left");
         }
         else if ((d.getLeftSideValue()>d.THRESHHOLD) && (d.getRightSideValue() > d.THRESHHOLD) && (d.getLeftSideValue() <= d.getRightSideValue()+d.THRESHHOLD) && (d.getLeftSideValue() >= d.getRightSideValue()-d.THRESHHOLD)) {
             System.out.println("The Robot is moving forward");
         }
         else if ((d.getLeftSideValue()<-d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD) && (d.getLeftSideValue() <= d.getRightSideValue()+d.THRESHHOLD) && (d.getLeftSideValue() >= d.getRightSideValue()-d.THRESHHOLD)) {
             System.out.println("The Robot is moving backward");
         }
        else if ((d.getLeftSideValue() < -d.THRESHHOLD) && (d.getLeftSideValue() < d.getRightSideValue()-d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD)) {
            System.out.println("The Robot is moving backward and turning to the left");
        }
        else if ((d.getLeftSideValue() < -d.THRESHHOLD) && (d.getRightSideValue() < d.getLeftSideValue()-d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD)) {
            System.out.println("The Robot is moving backward and turning to the right");
        }
        else if ((d.getLeftSideValue() > d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD) && (d.getLeftSideValue()<-d.getRightSideValue()+d.THRESHHOLD) && (d.getLeftSideValue() > -d.getRightSideValue()-d.THRESHHOLD)) {
            System.out.println("The Robot is rotating to the right");
        }
        else if ((d.getRightSideValue() > d.THRESHHOLD) && (d.getLeftSideValue() < -d.THRESHHOLD) && (-d.getLeftSideValue()<d.getRightSideValue()+d.THRESHHOLD) && (-d.getLeftSideValue() > d.getRightSideValue()-d.THRESHHOLD)) {
            System.out.println("The Robot is rotating to the left");
        }
        else if ((d.getLeftSideValue() > d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD) &&  (d.getLeftSideValue() > -d.getRightSideValue()+d.THRESHHOLD)) {
            System.out.println("The Robot is Driving forward in a circle to the right");
        }
        else if ((d.getLeftSideValue() > d.THRESHHOLD) && (d.getRightSideValue() < -d.THRESHHOLD) &&  (d.getLeftSideValue() < -d.getRightSideValue()-d.THRESHHOLD)) {
            System.out.println("The Robot is Driving backward in a circle to the left");
        }
        else if ((d.getLeftSideValue() < -d.THRESHHOLD) && (d.getRightSideValue() > d.THRESHHOLD) &&  (-d.getLeftSideValue() < d.getRightSideValue()-d.THRESHHOLD)) {
            System.out.println("The Robot is Driving forward in a circle to the left");
        }
        else if ((d.getLeftSideValue() < -d.THRESHHOLD) && (d.getRightSideValue() > d.THRESHHOLD) &&  (-d.getLeftSideValue() > d.getRightSideValue()+d.THRESHHOLD)) {
            System.out.println("The Robot is Driving backward in a circle to the right");
        }
        
    }
}
