/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.subsystems;

/**
 *
 * @author William
 */
public class DriveTrain {
    public static final double THRESHHOLD = .005;
    
    private Jaguar leftSide;
    private Jaguar rightSide;
    public DriveTrain() {
        leftSide = new Jaguar();
        rightSide = new Jaguar();
    }
    
    public void arcadeDrive(double forward, double rotation) {
        leftSide.set(forward);
        rightSide.set(forward);
        if (rotation > THRESHHOLD) {
            leftSide.set(leftSide.get()+rotation);
            rightSide.set(rightSide.get()-rotation);
        }
        else if (rotation < -THRESHHOLD) {
            leftSide.set(leftSide.get()+rotation);
            rightSide.set(rightSide.get()-rotation);
        }
        
    }
    public double getLeftSideValue() {
        return leftSide.get();
    }
    public double getRightSideValue() {
        return rightSide.get();
    }
    
}
