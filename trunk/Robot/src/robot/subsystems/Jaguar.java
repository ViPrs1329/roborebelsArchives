/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package robot.subsystems;

/**
 *
 * @author William
 */
public class Jaguar {
    
    private double value;
    public Jaguar() {
        value = 0;
    }
    public void set(double v){
        if ((v>=-1) && (v <= 1)){
            value = v;
        }
    }
    public double get() {
        return value;
    }
    
}
