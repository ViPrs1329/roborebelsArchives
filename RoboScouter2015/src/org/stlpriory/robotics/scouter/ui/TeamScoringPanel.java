package org.stlpriory.robotics.scouter.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamInfo;

public class TeamScoringPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = 180;
    private static final int IMAGE_HEIGHT = 180;

    private static final Dimension VGAP = new Dimension(IMAGE_WIDTH,10);
    
    private final JPanel autoPanel;
    private final JPanel toteStackHeightPanel;
    private final JPanel toteStackNumberPanel;
    private final JPanel toteSourcePanel;
    private final JPanel toteFeaturePanel;
    private final JPanel binStackHeightPanel;
    private final JPanel binStackNumberPanel;
    private final JPanel binFeaturesPanel;
    private final JPanel performancePanel;
    private final JPanel flawsPanel;
    private final JPanel foulsPanel;
    private final JPanel matchScorePanel;

    public TeamScoringPanel(final MatchInfo theMatch,
                            final TeamInfo theTeam) {
        
        setBorder(BorderFactory.createTitledBorder(theTeam.toString()));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setVisible(true);
        
        addImage(theTeam);
        add(Box.createRigidArea(VGAP));
        
        this.autoPanel = addAutoPanel();
        add(Box.createRigidArea(VGAP));
        
        this.toteStackHeightPanel = addToteStackHeightPanel();
        add(Box.createRigidArea(VGAP));
        
        this.toteStackNumberPanel = addToteStackNumberPanel();
        add(Box.createRigidArea(VGAP));
        
        this.toteSourcePanel = addToteSourcePanel();
        add(Box.createRigidArea(VGAP));
        
        this.toteFeaturePanel = addToteFeaturePanel();
        add(Box.createRigidArea(VGAP));
        
        this.binStackHeightPanel = addBinStackHeightPanel();
        add(Box.createRigidArea(VGAP));
        
        this.binStackNumberPanel = addBinStackNumberPanel();
        add(Box.createRigidArea(VGAP));
        
        this.binFeaturesPanel = addBinFeaturesPanel();
        add(Box.createRigidArea(VGAP));
        
        this.performancePanel = addPerformancePanel();
        add(Box.createRigidArea(VGAP));
        
        this.flawsPanel = addFlawsPanel();
        add(Box.createRigidArea(VGAP));
        
        this.foulsPanel = addFoulsPanel();
        add(Box.createRigidArea(VGAP));
        
        this.matchScorePanel = addMatchScorePanel(getAllianceColor(theMatch,theTeam));
        add(Box.createRigidArea(VGAP));
        add(Box.createRigidArea(VGAP));
        
    }
    
    // ==================================================================================
    //                      P U B L I C   M E T H O D S
    // ==================================================================================
    
    public Integer getAutoScore() {
        return getValuedRadioButtonValue(this.autoPanel);
    }
    
    public Integer getTotesScore() {
        int stackHeight = getValuedRadioButtonValue(this.toteStackHeightPanel);
        int stackNumber = getValuedRadioButtonValue(this.toteStackNumberPanel);
        return stackHeight * stackNumber;
    }
    
    public Integer getBinsScore() {
        int stackHeight = getValuedRadioButtonValue(this.binStackHeightPanel);
        int stackNumber = getValuedRadioButtonValue(this.binStackNumberPanel);
        return stackHeight + stackNumber;
    }
    
    public Integer getFlawsScore() {
        int performance = getValuedRadioButtonValue(this.performancePanel);
        int flaws = getValuedCheckBoxValues(this.flawsPanel).stream().reduce(0, (a, b) -> a + b);
        int fouls = getValuedRadioButtonValue(this.foulsPanel);
        return performance * flaws - fouls;
    }
    
    public Integer getMatchScore() {
        for (Component c : matchScorePanel.getComponents()) {
            if (c instanceof JFormattedTextField) {
                JFormattedTextField b = (JFormattedTextField) c;
                String value = b.getText();
                return (value != null && value.length() > 0 ? Integer.valueOf(value) : Integer.valueOf(0));
            }
        }
        return 0;
    }
    
    public String getMatchNotes() {
        StringBuilder sb = new StringBuilder();
        sb.append( getCheckedBoxesAsNotes(this.toteSourcePanel) );
        sb.append( getCheckedBoxesAsNotes(this.toteFeaturePanel) );
        sb.append( getCheckedBoxesAsNotes(this.binFeaturesPanel) );
        return sb.toString();
    }
    
    // ==================================================================================
    //                      P R I V A T E   M E T H O D S
    // ==================================================================================
    
    private Integer getValuedRadioButtonValue(final JPanel panel) {
        Integer value = Integer.valueOf(0);
        for (Component c : panel.getComponents()) {
            ValuedRadioButton b = (ValuedRadioButton) c;
            if (b.isSelected()) {
                value = b.getValue();
                break;
            }
        }
        return value;
    }
    
    private List<Integer> getValuedCheckBoxValues(final JPanel panel) {
        List<Integer> values = new ArrayList<>();
        
        for (Component c : panel.getComponents()) {
            ValuedCheckBox b = (ValuedCheckBox) c;
            if (b.isSelected()) {
                values.add( b.getValue() );
            }
        }
        return values;
    }
    
    private String getCheckedBoxesAsNotes(final JPanel panel) {
        List<String> strs = new ArrayList<>();
        for (Component c : panel.getComponents()) {
            if (c instanceof JCheckBox) {
                JCheckBox b = (JCheckBox) c;
                if (b.isSelected()) {
                    strs.add(b.getText());
                }
            }
        }
        
        return strs.isEmpty() ? "" : strs.stream().collect(Collectors.joining(", ", " [", "] "));
    }
    
    private String getAllianceColor(final MatchInfo theMatch,
                                    final TeamInfo theTeam) {
        if (theMatch.getBlueAlliance().getTeams().contains(theTeam)) {
            return TeamAlliance.AllianceColor.BLUE.name();
        }
        return TeamAlliance.AllianceColor.RED.name();
    }
    
    private JPanel addImage(final TeamInfo theTeam) {
        JPanel panel = new JPanel();

        File imageFile = theTeam.getImageFile();
        BufferedImage robotImage = null;
        try {
            robotImage = rescale(ImageIO.read(imageFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        JLabel robotImageLabel = new JLabel(new ImageIcon(robotImage));
        panel.add(robotImageLabel);
        
        add(panel, BorderLayout.LINE_START);
        return panel;
    }
    
    private JPanel addAutoPanel() {
        String[] names = {"None", "1 Tote", "1 Bin", "Tote Stack", "Both Tote and Bin"};
        int[] values = {0, 2, 5, 15, 7};

        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 3));
        
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Autonomous capability"));
        return panel;
    }
    
    private JPanel addToteStackHeightPanel() {
        String[] names = {"0 Totes", "1 Tote",  "2 Totes", "3 Totes", "4 Totes", 
                          "5 Totes", "6 Totes", "7 Totes", "8 Totes"};
        int[] values = {0, 1, 4, 7, 10, 15, 20, 25, 35};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Average stack height"));
        return panel;
    }
    
    private JPanel addToteStackNumberPanel() {
        String[] names = {"0 Stacks", "1 Stacks", "2 Stacks", 
                          "3 Stacks", "4 Stacks", "5 Stacks", 
                          "6 Stacks", "7 Stacks", "8 Stacks", 
                          "9 Stacks", "10 Stacks"};
        int[] values = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Number of stacks made"));
        return panel;
    }
    
    private JPanel addToteSourcePanel() {
        String[] names = {"Landfill", "Ground (not landfill)", "Human Player"};
        
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            JCheckBox b = new JCheckBox(names[i]);
            b.setActionCommand(names[i]);
            b.setSelected(false);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Tote source (check all that apply)"));
        return panel;
    }
    
    private JPanel addToteFeaturePanel() {
        String[] names = {"With Bin", "Coopertition"};
        
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            JCheckBox b = new JCheckBox(names[i]);
            b.setActionCommand(names[i]);
            b.setSelected(false);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Tote features (check all that apply)"));
        return panel;
    }
    
    private JPanel addBinStackHeightPanel() {
        String[] names = {"0 Totes", "1 Tote",  "2 Totes", "3 Totes", "4 Totes", 
                          "5 Totes", "6 Totes"};
        int[] values = {0, 1, 2, 4, 8, 15, 20};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("How many totes high can they place a bin"));
        return panel;
    }
    
    private JPanel addBinStackNumberPanel() {
        String[] names = {"0 Stacks", "1 Stacks", "2 Stacks", 
                          "3 Stacks", "4 Stacks", "5 Stacks", 
                          "6 Stacks", "7 Stacks", "8 Stacks", 
                          "9 Stacks", "10 Stacks"};
        int[] values = {0, 1, 2, 3, 4, 8, 15, 20, 25, 30, 35};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 3));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Number of stacks topped with a bin"));
        return panel;
    }
    
    private JPanel addBinFeaturesPanel() {
        String[] names = {"Noodle", "Stable Stacks"};
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < names.length; i++) {
            JCheckBox b = new JCheckBox(names[i]);
            b.setActionCommand(names[i]);
            b.setSelected(false);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Bin features (check all that apply)"));
        return panel;
    }
    
    private JPanel addPerformancePanel() {
        String[] names = {"Potato", "Competent", "Alliance Material"};
        int[] values = {-5, -2, -1};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 1 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("General Performance"));
        return panel;
    }
    
    private JPanel addFlawsPanel() {
        String[] names = {"Bad Driver", "Broke Down", "Bad Human Player", "Unbalanced", "Bad Team Player"};
        int[] values = {4, 5, 3, 5, 4};
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < names.length; i++) {
            ValuedCheckBox b = new ValuedCheckBox(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected(false);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Flaws (check all that apply)"));
        return panel;
    }
    
    private JPanel addFoulsPanel() {
        String[] names = {"0 Fouls", "1 Foul", "2 Fouls", "3 Fouls", "4 Fouls", "5 Fouls", "6 Fouls"};
        int[] values = {0, 6, 12, 18, 24, 30, 36};
        
        ButtonGroup group = new ButtonGroup();
        JPanel panel = new JPanel(new GridLayout(0, 2));
        for (int i = 0; i < names.length; i++) {
            ValuedRadioButton b = new ValuedRadioButton(names[i], values[i]);
            b.setActionCommand(names[i]);
            b.setSelected((i == 0 ? true : false));
            group.add(b);
            panel.add(b);
        }
 
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createTitledBorder("Fouls"));
        return panel;
    }
    
    private JPanel addMatchScorePanel(final String allianceColor) {
        JLabel label = new JLabel("Final match score for " + allianceColor + " alliance: ");
        NumberFormat format = NumberFormat.getIntegerInstance();
        JFormattedTextField valueField = new JFormattedTextField(format);
        valueField.setHorizontalAlignment(JTextField.CENTER);
        
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(label);
        panel.add(valueField);
        add(panel, BorderLayout.LINE_START);
        panel.setBorder(BorderFactory.createEmptyBorder());
        return panel;
    }

    private BufferedImage rescale(final BufferedImage originalImage) {
        BufferedImage resizedImage = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, null);
        g.dispose();
        return resizedImage;
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class ValuedRadioButton extends JRadioButton {
        private static final long serialVersionUID = 1L;
        private final Integer value;

        public ValuedRadioButton(final String name, final Integer theValue) {
            super(name);
            this.value  = theValue;
        }
        
        @Override
        public boolean isSelected() {
            return super.isSelected();
        }

        public Integer getValue() {
            return this.value;
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class ValuedCheckBox extends JCheckBox {
        private static final long serialVersionUID = 1L;
        private final Integer value;

        public ValuedCheckBox(final String name, final Integer theValue) {
            super(name);
            this.value  = theValue;
        }
        
        @Override
        public boolean isSelected() {
            return super.isSelected();
        }

        public Integer getValue() {
            return this.value;
        }
    }

}
