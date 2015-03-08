package org.stlpriory.robotics.scouter.ui.rater;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.model.TeamAlliance.AllianceColor;

public class TeamResultRater extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /** 5 star rating widget */
    private final TeamCategoryRater rater;
    /** Image of the team's robot */
    private BufferedImage robotImage;
    private final JLabel robotImageLabel;
    /** Team notes for the match */
    private final JButton notesButton;
    //private String notes;
    
    private final TeamResult teamResult;
    private final List<Category> categories;
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = 80;
    private static final int IMAGE_HEIGHT = 80;
    
    
    /**
     * The constructor.
     */
    public TeamResultRater(final TeamResult theResult, final List<Category> theScoringCateogries) {
        this.teamResult = theResult;
        this.categories = theScoringCateogries;
        
        TeamInfo theTeam = this.teamResult.getTeam();
        TitledBorder border = new TitledBorder(theTeam.toString());
        border.setTitleColor(getAllianceColor());
        setBorder(border);
        setLayout(new FlowLayout());
        setVisible(true);

        File imageFile = theTeam.getImageFile();
        try {
            this.robotImage = rescale(ImageIO.read(imageFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        this.robotImageLabel = new JLabel(new ImageIcon(this.robotImage));
        add(this.robotImageLabel);

        this.rater = new TeamCategoryRater(this.categories);
        //this.rater.setPreferredSize(new Dimension(700, 100));
        add(this.rater); 
        
        //this.notes = "";
        this.notesButton = new JButton("Notes");
        //this.notesButton.setPreferredSize(new Dimension(40, 20));
        
        this.notesButton.addActionListener(e -> {
            //JTextArea textArea = new JTextArea(this.notes.toString());
            JTextArea textArea = new JTextArea(theTeam.getNotes());
            textArea.setColumns(1);
            textArea.setRows(30);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setCaretPosition(textArea.getDocument().getLength());
            JScrollPane scrollPane = new JScrollPane(textArea);

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(500, 100));
            UIManager.put("OptionPane.minimumSize", new Dimension(510, 200));

            int result = JOptionPane.showConfirmDialog(null, scrollPane, 
                                                       "Enter notes for " + theTeam.toString(),
                                                       JOptionPane.OK_CANCEL_OPTION, 
                                                       JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                //this.notes = textArea.getText();
                theTeam.setNotes(textArea.getText());
            }
        });
        add(this.notesButton);
    }
    
    public Color getAllianceColor() {
        TeamInfo theTeam = this.teamResult.getTeam();
        if (this.teamResult.getMatch().getBlueTeams().contains(theTeam)) {
            return Color.BLUE;
        }
        return Color.RED;
    }
    
    public int getSelection(final Category category) {
        return this.rater.getSelection(category);
    }
    
    public void setSelection(final Category category, final int selection) {
        this.rater.setSelection(category,selection);
    }
    
    public float getRating(final Category category) {
        return this.rater.getRating(category);
    }
    
    public void setRating(final Category category, final float rating) {
        this.rater.setRating(category,rating);
    }
    
    public void updateResults() {
        for (Category category : this.categories) {
            Integer score = this.rater.getSelection(category);
            this.teamResult.setScore(category, score);
        }
    }

    /**
     * Called to enable/disable.
     *
     * @param enabled True for enabled.
     */
    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            repaint();
        }
    }
    
    private BufferedImage rescale(final BufferedImage originalImage)  {
        BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Nested Layout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        File f1 = new File("D:/FRC/TestRegional/images/robot1.jpg");
        File f2 = new File("D:/FRC/TestRegional/images/robot2.jpg");
        File f3 = new File("D:/FRC/TestRegional/images/robot3.jpg");
        File f4 = new File("D:/FRC/TestRegional/images/robot4.jpg");
        File f5 = new File("D:/FRC/TestRegional/images/robot5.jpg");
        File f6 = new File("D:/FRC/TestRegional/images/robot6.jpg");
        
        TeamInfo t1 = new TeamInfo(135,"The Black Knights",f1);
        TeamInfo t2 = new TeamInfo(292,"PantherTech",f2);
        TeamInfo t3 = new TeamInfo(337,"Hard Working Hard Hats",f3);
        TeamInfo t4 = new TeamInfo(1329,"Robo Rebels",f4);
        TeamInfo t5 = new TeamInfo(2655,"Flying Platypi",f5);
        TeamInfo t6 = new TeamInfo(2909,"Zebra Bots",f6);
        
        TeamAlliance blue = new TeamAlliance(AllianceColor.BLUE, Arrays.asList(t1,t2,t3));
        TeamAlliance red = new TeamAlliance(AllianceColor.RED, Arrays.asList(t4,t5,t6));

        MatchInfo match = new MatchInfo(12,Arrays.asList(blue,red));
        List<Category> cats = Arrays.asList(new Category("AutonRating",null), 
                                            new Category("TeleopRating",null));
        Map<Category,Integer> scores = new HashMap<>();
        cats.forEach(c -> scores.put(c, Integer.valueOf(0)));
        TeamResult r = new TeamResult(match,t1,scores);
        final TeamResultRater gui = new TeamResultRater(r,cats);
        frame.add(gui);

        frame.setVisible(true);
        frame.setContentPane(gui);
        frame.setTitle("Test for TeamResultRater");
        frame.pack();
    }

}
