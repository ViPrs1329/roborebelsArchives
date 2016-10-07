package org.stlpriory.robotics.scouter.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class to store the results for a particular match 
 */
public class MatchResult {
    private final MatchInfo match;
    private final List<TeamResult> results;

    public MatchResult(final MatchInfo theMatch, 
                       final Collection<TeamResult> theResults) {
        
        this.match = Objects.requireNonNull(theMatch);
        Objects.requireNonNull(theResults);
        
        // Check that there is a result for each team in the match
        Set<TeamInfo> matchTeams  = new HashSet<>(this.match.getTeams());
        Set<TeamInfo> resultTeams = theResults.stream().map(t -> t.getTeam()).collect(Collectors.toSet());
        if (resultTeams.size() != 6 || !resultTeams.containsAll(matchTeams)) {
            throw new IllegalArgumentException("There is not a result for each team in this match");
        }
        
        // Sort the results by alliance team order
        this.results = sortTeamResults(theMatch, theResults);
    }
    
    private List<TeamResult> sortTeamResults(final MatchInfo theMatch, final Collection<TeamResult> theResults) {
        // Create map of TeamInfo -> TeamResult
        Map<TeamInfo, TeamResult> resultMap = new HashMap<>();
        theResults.forEach(r -> resultMap.put(r.getTeam(),r));
        
        // Create a output list that orders TeamResults by the order of the TeamInfos in the match
        List<TeamResult> sortedResults = new ArrayList<>(theResults.size());
        theMatch.getTeams().forEach(t -> sortedResults.add(resultMap.get(t)));
        return sortedResults;
    }

    public MatchInfo getMatch() {
        return this.match;
    }
    
    public List<TeamInfo> getBlueTeams() {
        return this.match.getBlueTeams();
    }
    
    public List<TeamInfo> getRedTeams() {
        return this.match.getRedTeams();
    }
    
    /**
     * Return the team results for the blue alliance followed by the results
     * for the red alliance
     * @return
     */
    public List<TeamResult> getTeamResults() {
        return this.results;
    }
    
    public List<TeamResult> getTeamResults(final TeamInfo theTeam) {
        return this.results.stream()
                           .filter(r -> r.getTeam().equals(theTeam))
                           .collect(Collectors.toList());
    }

    public List<TeamResult> getBlueTeamResults() {
        return this.results.stream()
                           .filter(r -> getBlueTeams().contains(r.getTeam()))
                           .collect(Collectors.toList());
    }

    public List<TeamResult> getRedTeamResults() {
        return this.results.stream()
                           .filter(r -> getRedTeams().contains(r.getTeam()))
                           .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Match " + this.match.getMatchNumber() + " Results";
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.match, this.results);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MatchResult)) {
            return false;
        }
        MatchResult that = (MatchResult) obj;
        return this.match.equals(that.match) 
                && this.results.equals(that.results);
    }

}
