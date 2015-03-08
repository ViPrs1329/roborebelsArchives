package org.stlpriory.robotics.scouter.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Class to store the match results for a particular team 
 */
public class TeamResult {
    public static final String NON_EXISTENT_SCORE = "0";
    
    private final MatchInfo match;
    private final TeamInfo team;
    private final Map<Category,Integer> scores;
    private String notes;

    public TeamResult(final MatchInfo theMatch, 
                      final TeamInfo theTeam) {
        this(theMatch, theTeam, Collections.emptyMap());
    }

    public TeamResult(final MatchInfo theMatch, 
                      final TeamInfo theTeam, 
                      final Map<Category,Integer> theScores) {
        
        this.match = Objects.requireNonNull(theMatch);
        this.team = Objects.requireNonNull(theTeam);
        this.scores = new HashMap<>( Objects.requireNonNull(theScores) );
        this.notes = "";
    }

    public MatchInfo getMatch() {
        return this.match;
    }

    public TeamInfo getTeam() {
        return this.team;
    }
    
    public boolean hasScores() {
        return !this.scores.values().isEmpty();
    }
    
    public Set<Category> getScoringCategories() {
        return this.scores.keySet();
    }

    public Integer getScore(final Category theCategory) {
        return (this.scores.containsKey(theCategory) ? this.scores.get(theCategory) : Integer.valueOf(NON_EXISTENT_SCORE));
    }

    public void setScore(final Category theCategory, final Integer theScore) {
        if (theCategory != null && theScore != null) {
            Integer score = Math.min(theCategory.getMaxValue(), Math.max(theCategory.getMinValue(), theScore));
            this.scores.put(theCategory, score);
        }
    }

    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(final String theNotes) {
        if (theNotes != null) {
            this.notes = theNotes;
        }
    }

    @Override
    public String toString() {
        return "Team " + this.team.getTeamNumber() + ", match " + this.getMatch().getMatchNumber() + ", scores: " + this.scores;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.match.getMatchNumber(), this.team.getTeamNumber(), this.scores);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TeamResult)) {
            return false;
        }
        TeamResult that = (TeamResult) obj;
        return this.match.getMatchNumber() == that.match.getMatchNumber() 
                && this.team.getTeamNumber() == that.team.getTeamNumber()
                && this.scores.equals(that.scores);
    }

}
