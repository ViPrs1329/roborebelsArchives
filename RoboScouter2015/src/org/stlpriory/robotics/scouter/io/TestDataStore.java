package org.stlpriory.robotics.scouter.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamAlliance.AllianceColor;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;

public class TestDataStore implements IDataStore {
    private final List<MatchInfo> matches;
    private final List<TeamInfo> teams;
    private final List<TeamResult> teamResults;
    private final List<Category> categories;

    public TestDataStore() {
        File f1 = new File("D:/FRC/TestRegional/images/robot1.jpg");
        File f2 = new File("D:/FRC/TestRegional/images/robot2.jpg");
        File f3 = new File("D:/FRC/TestRegional/images/robot3.jpg");
        File f4 = new File("D:/FRC/TestRegional/images/robot4.jpg");
        File f5 = new File("D:/FRC/TestRegional/images/robot5.jpg");
        File f6 = new File("D:/FRC/TestRegional/images/robot6.jpg");

        TeamInfo t1 = new TeamInfo(135, "The Black Knights", f1);
        TeamInfo t2 = new TeamInfo(292, "PantherTech", f2);
        TeamInfo t3 = new TeamInfo(337, "Hard Working Hard Hats", f3);
        TeamInfo t4 = new TeamInfo(1329, "Robo Rebels", f4);
        TeamInfo t5 = new TeamInfo(2655, "Flying Platypi", f5);
        TeamInfo t6 = new TeamInfo(2909, "Zebra Bots", f6);
        this.teams = Arrays.asList(t1, t2, t3, t4, t5, t6);

        TeamAlliance blue = new TeamAlliance(AllianceColor.BLUE, Arrays.asList(t1, t2, t3));
        TeamAlliance red = new TeamAlliance(AllianceColor.RED, Arrays.asList(t4, t5, t6));
        MatchInfo match1 = new MatchInfo(1, Arrays.asList(blue, red));

        Map<Category, Integer> emptyStores = new HashMap<>();
        TeamResult m1t1 = new TeamResult(match1, t1, emptyStores);
        TeamResult m1t2 = new TeamResult(match1, t2, emptyStores);
        TeamResult m1t3 = new TeamResult(match1, t3, emptyStores);
        TeamResult m1t4 = new TeamResult(match1, t4, emptyStores);
        TeamResult m1t5 = new TeamResult(match1, t5, emptyStores);
        TeamResult m1t6 = new TeamResult(match1, t6, emptyStores);

        blue = new TeamAlliance(AllianceColor.BLUE, Arrays.asList(t6, t4, t2));
        red = new TeamAlliance(AllianceColor.RED, Arrays.asList(t5, t3, t1));
        MatchInfo match2 = new MatchInfo(2, Arrays.asList(blue, red));
        TeamResult m2t1 = new TeamResult(match2, t1, emptyStores);
        TeamResult m2t2 = new TeamResult(match2, t2, emptyStores);
        TeamResult m2t3 = new TeamResult(match2, t3, emptyStores);
        TeamResult m2t4 = new TeamResult(match2, t4, emptyStores);
        TeamResult m2t5 = new TeamResult(match2, t5, emptyStores);
        TeamResult m2t6 = new TeamResult(match2, t6, emptyStores);

        this.matches = Arrays.asList(match1, match2);
        this.teamResults = Arrays.asList(m1t1,m1t2,m1t3,m1t4,m1t5,m1t6,m2t1,m2t2,m2t3,m2t4,m2t5,m2t6);

        this.categories = Arrays.asList(new Category("AutonRating", null), 
                                        new Category("TeleopRating", null));
    }
    
    // ----------------------------------------------------------------------------------
    //       Scoring Category information methods
    // ----------------------------------------------------------------------------------

    @Override
    public List<Category> fetchAllScoringCategories() {
        return this.categories;
    }

    @Override
    public void saveAllScoringCategories(final Collection<Category> theCategories) {
        this.categories.clear();
        this.categories.addAll(theCategories);
    }
    
    // ----------------------------------------------------------------------------------
    //       Team information methods
    // ----------------------------------------------------------------------------------
    
    @Override
    public TeamInfo fetchTeam(final int theTeamNumber) {
        return this.teams.stream()
                         .filter(t -> t.getTeamNumber() == theTeamNumber)
                         .findFirst().orElse(null);
    }

    @Override
    public List<TeamInfo> fetchAllTeams() {
        return this.teams;
    }


    @Override
    public void saveTeams(final Collection<TeamInfo> theTeams) {
        this.teams.clear();
        this.teams.addAll(theTeams);
    }

    // ----------------------------------------------------------------------------------
    //       Match information methods
    // ----------------------------------------------------------------------------------

    @Override
    public MatchInfo fetchMatch(final int theMatchNumber) {
        return this.matches.stream().filter(m -> m.getMatchNumber() == theMatchNumber).findAny().orElse(null);
    }
    
    @Override
    public List<MatchInfo> fetchAllMatches() {
        return this.matches;
    }
    
    @Override
    public void saveMatches(final Collection<MatchInfo> theMatches) {
        this.matches.clear();
        this.matches.addAll(theMatches);
    }
    
    // ----------------------------------------------------------------------------------
    //       Team result information methods
    // ----------------------------------------------------------------------------------
    
    @Override
    public TeamResult fetchTeamResult(final int theMatchNumber, final int theTeamNumber) {
        return this.teamResults.stream()
                               .filter(tr -> tr.getMatch().getMatchNumber() == theMatchNumber)
                               .filter(tr -> tr.getTeam().getTeamNumber() == theTeamNumber)
                               .findAny().orElse(null);
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final MatchInfo theMatch) {
        return this.teamResults.stream()
                               .filter(tr -> tr.getMatch().equals(theMatch))
                               .collect(Collectors.toList());
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final TeamInfo theTeam) {
        return this.teamResults.stream()
                               .filter(tr -> tr.getTeam().equals(theTeam))
                               .collect(Collectors.toList());
    }
    
    @Override
    public List<TeamResult> fetchAllTeamResults() {
        return this.teamResults;
    }
    
    @Override
    public void saveTeamResults(final Collection<TeamResult> theResults) {
        this.teamResults.clear();
        this.teamResults.addAll(theResults);
    }
    
    // ----------------------------------------------------------------------------------
    //       Match result information methods
    // ----------------------------------------------------------------------------------
    
    @Override
    public MatchResult fetchMatchResult(final int theMatchNumber) {
        MatchInfo match = fetchMatch(theMatchNumber);
        if (match != null) {
            List<TeamResult> results = fetchTeamResults(match);
            return new MatchResult(match, results);
        }
        return null;
    }

    @Override
    public List<MatchResult> fetchAllMatchResults() {
        List<MatchResult> matchResults = new ArrayList<>();
        for (MatchInfo match : fetchAllMatches()) {
            List<TeamResult> results = fetchTeamResults(match);
            matchResults.add( new MatchResult(match,results) );
        }
        return matchResults;
        
    }

    @Override
    public void saveMatchResults(final Collection<MatchResult> theResults) {
        for (MatchResult matchResult : theResults) {
            saveTeamResults(matchResult.getTeamResults());
        }
    }
    

}
