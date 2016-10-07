package org.stlpriory.robotics.scouter.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;

public class CachingDataStore implements IDataStore {
    private final IDataStore store;
    private final Map<Integer, MatchInfo> matchCache;
    private final Map<Integer, TeamInfo> teamCache;
    private final List<TeamResult> resultCache;
    private final List<Category> categories;

    // ==================================================================================
    //                        C O N S T R U C T O R S
    // ==================================================================================

    public CachingDataStore(final IDataStore theBackingStore) {
        this.matchCache  = new HashMap<>();
        this.teamCache   = new HashMap<>();
        this.resultCache = new ArrayList<>();
        this.categories  = new ArrayList<>();
        this.store = theBackingStore;
    }

    // ==================================================================================
    //                      P U B L I C   M E T H O D S
    // ==================================================================================

    // ----------------------------------------------------------------------------------
    //       Scoring Category information methods
    // ----------------------------------------------------------------------------------

    @Override
    public List<Category> fetchAllScoringCategories() {
        if (this.categories.isEmpty()) {
            this.categories.addAll(this.store.fetchAllScoringCategories());
        }
        return Collections.unmodifiableList(this.categories);
    }

    @Override
    public void saveAllScoringCategories(final Collection<Category> theCategories) {
        this.categories.clear();
        this.store.saveAllScoringCategories(theCategories);
    }
    
    // ----------------------------------------------------------------------------------
    //       Team information methods
    // ----------------------------------------------------------------------------------

    @Override
    public TeamInfo fetchTeam(final int theTeamNumber) {
        if (this.teamCache.isEmpty()) {
            refreshTeamCache();
        }
        return this.teamCache.get(theTeamNumber);
    }

    @Override
    public List<TeamInfo> fetchAllTeams() {
        if (this.teamCache.isEmpty()) {
            refreshTeamCache();
        }
        
        Comparator<TeamInfo> byTeamNumber = 
                (e1, e2) -> Integer.compare(e1.getTeamNumber(), e2.getTeamNumber());
        return this.teamCache.values().stream().sorted(byTeamNumber).collect(Collectors.toList());
    }

    @Override
    public void saveTeams(final Collection<TeamInfo> theTeams) {
        this.teamCache.clear();
        this.store.saveTeams(theTeams);
    }
    
    private void refreshTeamCache() {
        this.teamCache.clear();
        this.store.fetchAllTeams().forEach(t -> this.teamCache.put(t.getTeamNumber(), t));
    }

    // ----------------------------------------------------------------------------------
    //       Match information methods
    // ----------------------------------------------------------------------------------

    @Override
    public MatchInfo fetchMatch(int theMatchNumber) {
        if (this.matchCache.isEmpty()) {
            refreshMatchCache();
        }
       return this.matchCache.get(theMatchNumber);
    }
    
    @Override
    public List<MatchInfo> fetchAllMatches() {
        if (this.matchCache.isEmpty()) {
            refreshMatchCache();
        }
        
        Comparator<MatchInfo> byMatchNumber = 
                (e1, e2) -> Integer.compare(e1.getMatchNumber(), e2.getMatchNumber());
        return this.matchCache.values().stream().sorted(byMatchNumber).collect(Collectors.toList());
    }
    
    @Override
    public void saveMatches(Collection<MatchInfo> theMatches) {
        this.matchCache.clear();
        this.store.saveMatches(theMatches);
    }
    
    private void refreshMatchCache() {
        this.matchCache.clear();
        this.store.fetchAllMatches().forEach(m -> this.matchCache.put(m.getMatchNumber(), m));
    }
    
    // ----------------------------------------------------------------------------------
    //       Team result information methods
    // ----------------------------------------------------------------------------------
    
    @Override
    public TeamResult fetchTeamResult(final int theMatchNumber, final int theTeamNumber) {
        if (this.resultCache.isEmpty()) {
            refreshTeamResultCache();
        }
        return this.resultCache.stream()
                               .filter(tr -> tr.getMatch().getMatchNumber() == theMatchNumber &&
                                             tr.getTeam().getTeamNumber() == theTeamNumber)
                               .findFirst()
                               .orElse(null);
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final MatchInfo theMatch) {
        if (this.resultCache.isEmpty()) {
            refreshTeamResultCache();
        }
        int theMatchNumber = theMatch.getMatchNumber();
        return this.resultCache.stream()
                               .filter(tr -> tr.getMatch().getMatchNumber() == theMatchNumber)
                               .collect(Collectors.toList());
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final TeamInfo theTeam) {
        if (this.resultCache.isEmpty()) {
            refreshTeamResultCache();
        }
        int theTeamNumber = theTeam.getTeamNumber();
        return this.resultCache.stream()
                               .filter(tr -> tr.getTeam().getTeamNumber() == theTeamNumber)
                               .collect(Collectors.toList());
    }
    
    @Override
    public List<TeamResult> fetchAllTeamResults() {
        if (this.resultCache.isEmpty()) {
            refreshTeamResultCache();
        }
        return this.resultCache.stream().collect(Collectors.toList());
    }
    
    @Override
    public void saveTeamResults(Collection<TeamResult> theResults) {
        this.resultCache.clear();
        this.store.saveTeamResults(theResults);
    }
    
    private void refreshTeamResultCache() {
        this.resultCache.clear();
        this.store.fetchAllTeamResults().forEach(tr -> this.resultCache.add(tr));
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
    
    @Override
    public String toString() {
        return this.store.toString();
    }

}
