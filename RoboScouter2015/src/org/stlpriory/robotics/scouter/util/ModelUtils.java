package org.stlpriory.robotics.scouter.util;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;

public class ModelUtils {

    /**
     * Cannot construct - only static helper methods
     */
    private ModelUtils() {
        // cannot construct
    }
    
    public static List<MatchInfo> filterMatchInfos(final TeamInfo theTeam,
                                                   final Collection<MatchInfo> theMatches) {
        return theMatches.stream()
                         .filter(m -> m.getTeams().contains(theTeam))
                         .collect(Collectors.toList());
    }
    
    public static List<TeamResult> filterTeamResults(final TeamInfo theTeam, 
                                                     final Collection<TeamResult> theResults) {
        return theResults.stream()
                         .filter(tr -> tr.getTeam().equals(theTeam))
                         .collect(Collectors.toList());        
    }
    
    public static Double averageScore(final Collection<TeamResult> theTeamResults,
                                      final Category theCategory) {
        return theTeamResults.stream()
                             .filter(tr -> tr.hasScores())
                             .map(tr -> tr.getScore(theCategory))
                             .mapToInt(x -> x)
                             .average()
                             .orElse(Double.valueOf(TeamResult.NON_EXISTENT_SCORE));
    }
    
    public static Integer maxScore(final Collection<TeamResult> theTeamResults,
                                   final Category theCategory) {
        return theTeamResults.stream()
                             .map(tr -> tr.getScore(theCategory))
                             .mapToInt(x -> x)
                             .max()
                             .orElse(theCategory.getMaxValue());
    }
    
    public static Integer minScore(final Collection<TeamResult> theTeamResults,
                                   final Category theCategory) {
        return theTeamResults.stream()
                             .map(tr -> tr.getScore(theCategory))
                             .mapToInt(x -> x)
                             .min()
                             .orElse(theCategory.getMinValue());
    }

}
