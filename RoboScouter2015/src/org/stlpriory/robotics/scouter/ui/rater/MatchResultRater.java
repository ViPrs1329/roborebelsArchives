package org.stlpriory.robotics.scouter.ui.rater;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamAlliance.AllianceColor;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;


public class MatchResultRater extends JPanel {
    private static final long serialVersionUID = 1L;
    private final static int HORIZONTAL_GAP = 0;
    private final static int VERTICAL_GAP   = 10;

    /** Raters for each team */
    private final MatchResult matchResult;
    private final MatchInfo match;
    private final Map<TeamInfo,TeamResultRater> teamRaters;
    private final Map<TeamInfo,TeamResult> teamResults;
    /** Save team rating and notes for the match */
    private final JButton saveButton;
    
    /**
     * The constructor.
     */
    public MatchResultRater(final MatchResult theResult, final List<Category> theScoringCateogries) {
        this.matchResult = theResult;
        this.match = theResult.getMatch();
        
        setBorder(new TitledBorder("Match "+this.match.getMatchNumber()));
        setLayout(new GridLayout(7,0,HORIZONTAL_GAP,VERTICAL_GAP));
        setVisible(true);
        
        this.teamRaters  = new HashMap<>();
        this.teamResults = new HashMap<>();
        
        
        for (TeamResult teamResult : this.matchResult.getBlueTeamResults()) {
            TeamResultRater rater = new TeamResultRater(teamResult,theScoringCateogries);
            //rater.setBackground(LIGHT_BLUE);
            TeamInfo team = teamResult.getTeam();
            this.teamRaters.put(team,rater);
            this.teamResults.put(team, teamResult);
            add(rater);
        }

        for (TeamResult teamResult : this.matchResult.getRedTeamResults()) {
            TeamResultRater rater = new TeamResultRater(teamResult,theScoringCateogries);
            //rater.setBackground(LIGHT_RED);
            TeamInfo team = teamResult.getTeam();
            this.teamRaters.put(team,rater);
            this.teamResults.put(team, teamResult);
            add(rater);
        }
        
        this.saveButton = new JButton("Save");
        this.saveButton.setPreferredSize(new Dimension(40, 20));
        this.saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                for (TeamResult teamResult : MatchResultRater.this.matchResult.getBlueTeamResults()) {
                    TeamInfo team = teamResult.getTeam();
                    TeamResultRater rater = MatchResultRater.this.teamRaters.get(team);
                    rater.updateResults();
                }

                for (TeamResult teamResult : MatchResultRater.this.matchResult.getRedTeamResults()) {
                    TeamInfo team = teamResult.getTeam();
                    TeamResultRater rater = MatchResultRater.this.teamRaters.get(team);
                    rater.updateResults();
                }
                
                System.out.println("Save button selected and all TeamResult objects updated");
            }
        });
        add(this.saveButton);
    }
    
    public int getSelection(final TeamInfo team, final Category category) {
        TeamResultRater rater = this.teamRaters.get(team);
        return (rater != null ? rater.getSelection(category) : 0);
    }
    
    public void setSelection(final TeamInfo team, final Category category, final int selection) {
        TeamResultRater rater = this.teamRaters.get(team);
        if (rater != null) {
            rater.setSelection(category,selection);            
        }
    }
    
    public float getRating(final TeamInfo team, final Category category) {
        TeamResultRater rater = this.teamRaters.get(team);
        return (rater != null ? rater.getRating(category) : 0);
    }
    
    public void setRating(final TeamInfo team, final Category category, final float rating) {
        TeamResultRater rater = this.teamRaters.get(team);
        if (rater != null) {
            rater.setRating(category,rating);
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
    
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Nested Layout Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        File f1 = new File("D:/FRC/TestRegional/images/team00045.jpg");
        File f2 = new File("D:/FRC/TestRegional/images/team00135.jpg");
        File f3 = new File("D:/FRC/TestRegional/images/team00292.jpg");
        File f4 = new File("D:/FRC/TestRegional/images/team00337.jpg");
        File f5 = new File("D:/FRC/TestRegional/images/team00461.jpg");
        File f6 = new File("D:/FRC/TestRegional/images/team00829.jpg");
        
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
                                            new Category("TeleopRating",null),
                                            new Category("Category1",null),
                                            new Category("Category2",null));
        Map<Category,Integer> scores = new HashMap<>();
        cats.forEach(c -> scores.put(c, Integer.valueOf(0)));
        
        TeamResult r1 = new TeamResult(match,t1,scores);
        TeamResult r2 = new TeamResult(match,t2,scores);
        TeamResult r3 = new TeamResult(match,t3,scores);
        TeamResult r4 = new TeamResult(match,t4,scores);
        TeamResult r5 = new TeamResult(match,t5,scores);
        TeamResult r6 = new TeamResult(match,t6,scores);
        
        MatchResult matchResult = new MatchResult(match,Arrays.asList(r1,r2,r3,r4,r5,r6));
        final MatchResultRater gui = new MatchResultRater(matchResult,cats);
        frame.add(gui);

        frame.setVisible(true);
        frame.setContentPane(gui);
        frame.setTitle("Test for TeamResultRater");
        frame.pack();
    }

}
