/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UiUtils {

    // ==================================================================================
    //              C O N S T R U C T O R S
    // ==================================================================================
    
    private UiUtils() {
        // do not allow an instance to be created
    }

    // ==================================================================================
    //               P U B L I C   M E T H O D S
    // ==================================================================================
    
    /**
     * Rescale a buffered image to the specified size
     * 
     * @param originalImage The original image
     * @param imageWidth The new image width
     * @param imageHeight The new image width
     * @return the rescaled image
     */
    public static BufferedImage rescale(final BufferedImage originalImage, 
                                        final int imageWidth, 
                                        final int imageHeight)  {
        BufferedImage resizedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, imageWidth, imageHeight, null);
        g.dispose();
        return resizedImage;
    }
    
}
