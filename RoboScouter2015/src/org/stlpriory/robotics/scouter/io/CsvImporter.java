/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamAlliance.AllianceColor;
import org.stlpriory.robotics.scouter.model.TeamInfo;

/**
 *
 */
public class CsvImporter implements IImporter {

    private final File csvFile;

    public CsvImporter(final String theCsvFilePath) {
        this(new File(theCsvFilePath));
    }

    public CsvImporter(final File theCsvFile) {
        assertValidFileArgument(theCsvFile);
        this.csvFile = theCsvFile;
    }

    @Override
    public List<TeamInfo> importTeams() {
        List<TeamInfo> theTeams = new ArrayList<>();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.csvFile));

            int lineCount = 0;
            String cvsSplitBy = ",";

            String line;
            String[] columnHeaders = null;
            Map<String, String> nameToValue = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount == 1) {
                    // Assume first line contains column headers
                    columnHeaders = line.split(cvsSplitBy);
                } else {
                    String[] columnValues = line.split(cvsSplitBy);
                    for (int i = 0; i < columnValues.length; i++) {
                        String columnName = columnHeaders[i].trim();
                        String columnValue = columnValues[i].trim();
                        nameToValue.put(columnName.toLowerCase(), columnValue);
                    }
                    theTeams.add(createTeam(nameToValue));
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvImporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuietly(reader);
        }

        return theTeams;
    }

    @Override
    public List<MatchInfo> importMatchSchedule(final Collection<TeamInfo> theTeams) {
        List<MatchInfo> theMatches = new ArrayList<>();
        
        Map<Integer, TeamInfo> teamsByNumber = new HashMap<>();
        theTeams.forEach(t -> teamsByNumber.put(t.getTeamNumber(),t));

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(this.csvFile));

            int lineCount = 0;
            String cvsSplitBy = ",";

            String line;
            String[] columnHeaders = null;
            Map<String, String> nameToValue = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                lineCount++;
                if (lineCount == 1) {
                    // Assume first line contains column headers
                    columnHeaders = line.split(cvsSplitBy);
                } else {
                    String[] columnValues = line.split(cvsSplitBy);
                    for (int i = 0; i < columnValues.length; i++) {
                        String columnName = columnHeaders[i].trim();
                        String columnValue = columnValues[i].trim();
                        nameToValue.put(columnName.toLowerCase(), columnValue);
                    }
                    theMatches.add( createScheduledMatch(teamsByNumber,nameToValue) );
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CsvImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CsvImporter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeQuietly(reader);
        }

        return theMatches;
    }

    // =================================================================================================================
    //                   PRIVATE METHODS
    // =================================================================================================================
    
    private void assertValidFileArgument(final File theCsvFile) {
        if (theCsvFile == null) {
            throw new RuntimeException("The java.io.File argument cannot be null.");
        }
        if (!theCsvFile.exists()) {
            throw new RuntimeException("The specified file '" + theCsvFile.getAbsolutePath() + "' does not exist.");
        }
        if (theCsvFile.isDirectory()) {
            throw new RuntimeException("The specified file '" + theCsvFile.getAbsolutePath() + "' cannot be a directory.");
        }
    }

    private void closeQuietly(final Reader reader) {
        try {
            reader.close();
        } catch (IOException ex) {
            // do nothing
        }
    }
    
    private TeamInfo createTeam(final Map<String, String> csvMap) {
        String theTeamCountry = null;
        if (csvMap.containsKey("country")) {
            theTeamCountry = csvMap.get("country");
        }

        String theTeamState = null;
        if (csvMap.containsKey("state")) {
            theTeamState = csvMap.get("state");
        }
        
        String theTeamCity = null;
        if (csvMap.containsKey("city")) {
            theTeamCity = csvMap.get("city");
        }
        
        String theTeamName = null;
        if (csvMap.containsKey("name")) {
            theTeamName = csvMap.get("name");
        } else if (csvMap.containsKey("team name")) {
            theTeamName = csvMap.get("team name");
        } else if (csvMap.containsKey("teamname")) {
            theTeamName = csvMap.get("teamname");
        } else {
            throw new RuntimeException("Unable to find a team name in the import file.");
        }
        
        int theTeamNumber = 0;
        if (csvMap.containsKey("teamnumber")) {
            theTeamNumber = Integer.valueOf(csvMap.get("teamnumber"));
        } else if (csvMap.containsKey("team number")) {
            theTeamNumber = Integer.valueOf(csvMap.get("team number"));
        } else {
            throw new RuntimeException("Unable to find a team number in the import file.");
        }

        return new TeamInfo(theTeamNumber,theTeamName,theTeamCity,theTeamState,theTeamCountry,null);
    }

    private MatchInfo createScheduledMatch(final Map<Integer,TeamInfo> teamsByNumber, 
                                           final Map<String, String> csvMap) {
        //Time  Match, Red 1, Red 2, Red 3, Blue 1, Blue 2, Blue 3, Red Score, Blue Score
        int theMatchNumber = 0;
        if (csvMap.containsKey("match")) {
            theMatchNumber = Integer.valueOf(csvMap.get("match"));
        }
        
        List<TeamInfo> redTeams  = new ArrayList<>(3);
        if (csvMap.containsKey("red 1")) {
            int teamNumber = Integer.valueOf(csvMap.get("red 1"));
            redTeams.add( teamsByNumber.get(teamNumber) );
        }
        if (csvMap.containsKey("red 2")) {
            int teamNumber = Integer.valueOf(csvMap.get("red 2"));
            redTeams.add( teamsByNumber.get(teamNumber) );
        }
        if (csvMap.containsKey("red 3")) {
            int teamNumber = Integer.valueOf(csvMap.get("red 3"));
            redTeams.add( teamsByNumber.get(teamNumber) );
        }
        TeamAlliance redAlliance = new TeamAlliance(AllianceColor.RED, redTeams);

        List<TeamInfo> blueTeams = new ArrayList<>(3);
        if (csvMap.containsKey("blue 1")) {
            int teamNumber = Integer.valueOf(csvMap.get("blue 1"));
            blueTeams.add( teamsByNumber.get(teamNumber) );
        }
        if (csvMap.containsKey("blue 2")) {
            int teamNumber = Integer.valueOf(csvMap.get("blue 2"));
            blueTeams.add( teamsByNumber.get(teamNumber) );
        }
        if (csvMap.containsKey("blue 3")) {
            int teamNumber = Integer.valueOf(csvMap.get("blue 3"));
            blueTeams.add( teamsByNumber.get(teamNumber) );
        }
        TeamAlliance blueAlliance = new TeamAlliance(AllianceColor.BLUE, blueTeams);
        
        return new MatchInfo(theMatchNumber, Arrays.asList(blueAlliance,redAlliance));
    }

}
