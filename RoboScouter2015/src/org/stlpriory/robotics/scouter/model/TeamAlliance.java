package org.stlpriory.robotics.scouter.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to store information about a team alliance
 */
public class TeamAlliance {
    /** alliance colors */
    public enum AllianceColor {
        BLUE, RED
    }

    private final AllianceColor allianceColor;
    private final List<TeamInfo> teams;

    public TeamAlliance(final AllianceColor theAllianceColor, 
                        final List<TeamInfo> theTeams) {
        
        this.allianceColor = Objects.requireNonNull(theAllianceColor);
        this.teams = new ArrayList<>(Objects.requireNonNull(theTeams));
        
        // Check that the alliance is composed of three unique teams
        Set<TeamInfo> uniqueTeams = new HashSet<>(this.teams);
        if (uniqueTeams.size() != 3) {
            throw new IllegalArgumentException("An alliance must be composed of 3 unique teams");
        }
    }

    public AllianceColor getAllianceColor() {
        return this.allianceColor;
    }

    public List<TeamInfo> getTeams() {
        return this.teams;
    }

    public List<Integer> getTeamNumbers() {
        return this.teams.stream()
                         .mapToInt(t -> t.getTeamNumber())
                         .boxed()
                         .collect(Collectors.toList());
    }
    
    @Override
    public String toString() {
        return this.allianceColor.name() + " Alliance: " + getTeamNumbers();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.allianceColor, getTeamNumbers());
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TeamAlliance)) {
            return false;
        }
        TeamAlliance that = (TeamAlliance) obj;
        return this.allianceColor.equals(that.allianceColor) 
                && this.getTeamNumbers().equals(that.getTeamNumbers());
    }

}
