package org.stlpriory.robotics.scouter.ui;

import java.awt.Dimension;
import java.awt.Label;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.util.ModelUtils;
import org.stlpriory.robotics.scouter.util.UiUtils;

public class TeamSummaryPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /** Display size of this panel */
    private static final int PREFERRED_WIDTH  = 500;
    private static final int PREFERRED_HEIGHT = 300;
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = 120;
    private static final int IMAGE_HEIGHT = 120;

    public TeamSummaryPanel(final TeamInfo theTeam, 
                            final List<Category> theScoringCateogries, 
                            final Collection<TeamResult> theResults) {
        setBorder(BorderFactory.createTitledBorder(theTeam.toString()));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setVisible(true);

        File imageFile = theTeam.getImageFile();
        BufferedImage robotImage = null;
        try {
            robotImage = UiUtils.rescale(ImageIO.read(imageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JLabel robotImageLabel = new JLabel(new ImageIcon(robotImage));
        add(robotImageLabel);
        
        List<TeamResult> theTeamResults = ModelUtils.filterTeamResults(theTeam, theResults);
        for (Category c : theScoringCateogries) {
            Double avgScore = ModelUtils.averageScore(theTeamResults, c);
            String avg = String.format("%.2f", avgScore);
            Label label = new Label(c.getDisplayName()+" = "+avg,Label.LEFT);
            add(label);
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Team Notes - ").append(theTeam.getNotes()).append("\n\n");
        for (TeamResult tr : theTeamResults) {
            sb.append("Match ").append(tr.getMatch().getMatchNumber()).append(" Notes - ").append(tr.getNotes()).append("\n");
        }
        
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setColumns(1);
        textArea.setRows(30);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setCaretPosition(textArea.getDocument().getLength());
        JScrollPane scrollPane = new JScrollPane(textArea);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
        add(scrollPane);
    }

}
