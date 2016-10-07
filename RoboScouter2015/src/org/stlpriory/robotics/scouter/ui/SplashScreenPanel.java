package org.stlpriory.robotics.scouter.ui;

import static org.stlpriory.robotics.scouter.util.StreamUtils.createTempFileFromResource;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.stlpriory.robotics.scouter.util.UiUtils;

public class SplashScreenPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    public static final int CONTENT_HSIZE = RoboScouter.CONTENT_HSIZE-40;
    public static final int CONTENT_VSIZE = RoboScouter.CONTENT_VSIZE-20;
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = CONTENT_HSIZE;
    private static final int IMAGE_HEIGHT = CONTENT_VSIZE;

    public SplashScreenPanel() {
        setBorder(BorderFactory.createEtchedBorder());
        setPreferredSize(new Dimension(CONTENT_HSIZE, CONTENT_VSIZE));

        File splashImageFile = null;
        BufferedImage splashScreenImage = null;
        try {
            splashImageFile = createTempFileFromResource(SplashScreenPanel.class,"roborebels.jpg");
            splashScreenImage = UiUtils.rescale(ImageIO.read(splashImageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        add( new JLabel(new ImageIcon(splashScreenImage)) );
     }
    
}
