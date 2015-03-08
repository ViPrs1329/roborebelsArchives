package org.stlpriory.robotics.scouter.io;

import java.util.Collection;
import java.util.List;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;

/**
 *  Interface for all DateStore implementations
 */
public interface IDataStore {
    
    // ----------------------------------------------------------------------------------
    //       Scoring Category information methods
    // ----------------------------------------------------------------------------------
    
    List<Category> fetchAllScoringCategories();
    
    void saveAllScoringCategories(Collection<Category> theCategories);
    
    // ----------------------------------------------------------------------------------
    //       Team information methods
    // ----------------------------------------------------------------------------------

    TeamInfo fetchTeam(int theTeamNumber);
    
    List<TeamInfo> fetchAllTeams();
    
    void saveTeams(Collection<TeamInfo> theTeams);

    // ----------------------------------------------------------------------------------
    //       Match information methods
    // ----------------------------------------------------------------------------------

    MatchInfo fetchMatch(int theMatchNumber);
    
    List<MatchInfo> fetchAllMatches();
    
    void saveMatches(Collection<MatchInfo> theMatches);
    
    // ----------------------------------------------------------------------------------
    //       Team result information methods
    // ----------------------------------------------------------------------------------
    
    TeamResult fetchTeamResult(int theMatchNumber, int theTeamNumber);
    
    List<TeamResult> fetchTeamResults(MatchInfo theMatch);
    
    List<TeamResult> fetchTeamResults(TeamInfo theTeam);
    
    List<TeamResult> fetchAllTeamResults();
    
    void saveTeamResults(Collection<TeamResult> theResults);
   
    // ----------------------------------------------------------------------------------
    //       Match result information methods
    // ----------------------------------------------------------------------------------
    
    MatchResult fetchMatchResult(int theMatchNumber);

    List<MatchResult> fetchAllMatchResults();

    void saveMatchResults(Collection<MatchResult> theResults);
    
}
