/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package roborebels.example.misc;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;

/**
 *
 */
public class DriverStationIO implements Constants {

    DriverStationEnhancedIO enhancedIO;

    public DriverStationIO() {
        enhancedIO = DriverStation.getInstance().getEnhancedIO();
    }

    private boolean getDigital(int channel) {
        try {
            return enhancedIO.getDigital(channel);
        } catch (Exception e) {
            Debug.println("[ERROR] DS IO error 1 - " + e);
            return false;
        }
    }
}
