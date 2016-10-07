package org.stlpriory.robotics.scouter.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Class to store information about a specific match
 */
public class MatchInfo {
    private final int matchNumber;
    private final TeamAlliance blueAlliance;
    private final TeamAlliance redAlliance;

    public MatchInfo(final int theMatchNumber, 
                     final List<TeamAlliance> theAlliances) {
        
        if (theMatchNumber <= 0) {
            throw new IllegalArgumentException("The match number must be > 0");
        }
        this.matchNumber = theMatchNumber;
        
        if (theAlliances == null || theAlliances.size() != 2) {
            throw new IllegalArgumentException("A match must be composed of 2 alliance teams");
        }
        if (theAlliances.get(0).getAllianceColor() == theAlliances.get(1).getAllianceColor()) {
            throw new IllegalArgumentException("There must be both a red and blue alliance team");
        }
        // Check that there are 6 unique teams for the match
        Set<TeamInfo> matchTeams = new HashSet<>();
        matchTeams.addAll(theAlliances.get(0).getTeams());
        matchTeams.addAll(theAlliances.get(1).getTeams());
        if (matchTeams.size() != 6) {
            throw new IllegalArgumentException("There must be 6 unique teams participating in a match");
        }

        // Sort alliances into BLUE then RED order
        theAlliances.sort((a1,a2) -> a1.getAllianceColor().compareTo(a2.getAllianceColor()));
        this.blueAlliance = theAlliances.get(0);
        this.redAlliance  = theAlliances.get(1);
    }

    public int getMatchNumber() {
        return this.matchNumber;
    }
    
    /**
     * Return all the teams in the blue alliance followed by the teams
     * in the red alliance 
     * @return
     */
    public List<TeamInfo> getTeams() {
        List<TeamInfo> result = new ArrayList<TeamInfo>(getBlueTeams());
        result.addAll(getRedTeams());
        return result;
    }

    public List<TeamInfo> getBlueTeams() {
        return getBlueAlliance().getTeams();
    }

    public List<TeamInfo> getRedTeams() {
        return getRedAlliance().getTeams();
    }

    public List<Integer> getBlueTeamNumbers() {
        return getBlueAlliance().getTeamNumbers();
    }

    public List<Integer> getRedTeamNumbers() {
        return getRedAlliance().getTeamNumbers();
    }
    
    public TeamAlliance getBlueAlliance() {
        return this.blueAlliance;
    }
    
    public TeamAlliance getRedAlliance() {
        return this.redAlliance;
    }

    @Override
    public String toString() {
        return "Match " + this.matchNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.matchNumber, this.blueAlliance, this.redAlliance);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MatchInfo)) {
            return false;
        }
        MatchInfo that = (MatchInfo) obj;
        return this.matchNumber == that.matchNumber 
                && this.blueAlliance.equals(that.blueAlliance)
                && this.redAlliance.equals(that.redAlliance);
    }

}
