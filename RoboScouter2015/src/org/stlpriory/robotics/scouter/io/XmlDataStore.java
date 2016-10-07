package org.stlpriory.robotics.scouter.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamAlliance;
import org.stlpriory.robotics.scouter.model.TeamAlliance.AllianceColor;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.util.FileUtils;
import org.stlpriory.robotics.scouter.util.StreamUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 *  DataStore implementation where all data is stored in XML files
 */
public class XmlDataStore implements IDataStore {
    private final File rootDir;
    private final File teamDir;
    private final File matchDir;
    private final File resultsDir;
    private final File imageDir;

    public XmlDataStore(final File theRootDirectory) {
        this.rootDir    = ensureDirectoryExists(theRootDirectory);
        this.teamDir    = ensureDirectoryExists(new File(this.rootDir,"teams"));
        this.matchDir   = ensureDirectoryExists(new File(this.rootDir,"matches"));
        this.resultsDir = ensureDirectoryExists(new File(this.rootDir,"results"));
        this.imageDir   = ensureDirectoryExists(new File(this.rootDir,"images"));
    }

    // ==================================================================================
    //                        C O N S T R U C T O R S
    // ==================================================================================

    public XmlDataStore(final String theRootDirectoryPath) {
        this(new File(theRootDirectoryPath));
    }

    // ==================================================================================
    //                      P U B L I C   M E T H O D S
    // ==================================================================================

    // ----------------------------------------------------------------------------------
    //       Scoring Category information methods
    // ----------------------------------------------------------------------------------

    @Override
    public List<Category> fetchAllScoringCategories() {
        File f = FileUtils.findFile(Arrays.asList(this.rootDir), "categories.xml");
        return readCategoriesXmlFile(f);
    }

    @Override
    public void saveAllScoringCategories(final Collection<Category> theCategories) {
        writeCategoriesXmlFile(theCategories);
    }
    
    // ----------------------------------------------------------------------------------
    //       Team information methods
    // ----------------------------------------------------------------------------------

    @Override
    public TeamInfo fetchTeam(final int theTeamNumber) {
        File inputFile = createTeamXmlFile(theTeamNumber);
        if (!inputFile.exists()) {
            throw new RuntimeException("The team file '" + inputFile.getAbsolutePath() + "' does not exist.");
        }
        return readTeamXmlFile(inputFile);
    }
    
    private List<TeamInfo> fetchTeams(final List<Integer> theTeamNumbers) {
        return theTeamNumbers.stream().map(t -> fetchTeam(t)).collect(Collectors.toList());
    }

    @Override
    public List<TeamInfo> fetchAllTeams() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(final File f) {
                String fileName = f.getName().toLowerCase();
                return f.isFile() && fileName.startsWith("team") && fileName.endsWith(".xml");
            }
        };

        List<TeamInfo> teams = new ArrayList<>();
        for (File inputFile : FileUtils.listFiles(this.teamDir, filter, null)) {
            teams.add(readTeamXmlFile(inputFile));
        }
        
        Comparator<TeamInfo> byTeamNumber = 
                (e1, e2) -> Integer.compare(e1.getTeamNumber(), e2.getTeamNumber());
        Collections.sort(teams, byTeamNumber);
        return teams;
    }

    @Override
    public void saveTeams(final Collection<TeamInfo> theTeams) {
        for (TeamInfo team : theTeams) {
            writeTeamXmlFile(team);
        }
    }

    // ----------------------------------------------------------------------------------
    //       Match information methods
    // ----------------------------------------------------------------------------------
    
    @Override
    public MatchInfo fetchMatch(final int theMatchNumber) {
        File inputFile = createMatchXmlFile(theMatchNumber);
        if (!inputFile.exists()) {
            throw new RuntimeException("The match file '" + inputFile.getAbsolutePath() + "' does not exist.");
        }
        return readMatchXmlFile(Collections.emptyList(),inputFile);
    }
    
    @Override
    public List<MatchInfo> fetchAllMatches() {
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(final File f) {
                String fileName = f.getName().toLowerCase();
                return f.isFile() && fileName.startsWith("match") && fileName.endsWith(".xml");
            }
        };

        List<MatchInfo> matches  = new ArrayList<>();
        List<TeamInfo> teamCache = fetchAllTeams();
        for (File inputFile : FileUtils.listFiles(this.matchDir, filter, null)) {
            matches.add(readMatchXmlFile(teamCache,inputFile));
        }
        
        Comparator<MatchInfo> byMatchNumber = 
                (e1, e2) -> Integer.compare(e1.getMatchNumber(), e2.getMatchNumber());
        Collections.sort(matches, byMatchNumber);
        return matches;
    }
    
    @Override
    public void saveMatches(final Collection<MatchInfo> theMatches) {
        for (MatchInfo match : theMatches) {
            writeMatchXmlFile(match);
        }
    }
    
    // ----------------------------------------------------------------------------------
    //       Team result information methods
    // ----------------------------------------------------------------------------------

    @Override
    public TeamResult fetchTeamResult(final int theMatchNumber, final int theTeamNumber) {
        MatchInfo theMatch = fetchMatch(theMatchNumber);
        TeamInfo theTeam   = fetchTeam(theTeamNumber);
        
        List<MatchInfo> matchCache = Arrays.asList(theMatch);
        List<TeamInfo> teamCache   = Arrays.asList(theTeam);
        List<Category> categories  = fetchAllScoringCategories();

        File inputFile = createTeamResultXmlFile(theMatchNumber,theTeamNumber);
        if (inputFile.exists()) {
            return readTeamResultXmlFile(matchCache, teamCache, categories, inputFile);
        } 
        return new TeamResult(theMatch,theTeam);
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final MatchInfo theMatch) {
        List<MatchInfo> matchCache = Arrays.asList(theMatch);
        List<TeamInfo> teamCache   = theMatch.getTeams();
        List<Category> categories  = fetchAllScoringCategories();

        List<TeamResult> results = new ArrayList<>();
        for (TeamInfo theTeam : theMatch.getTeams()) {
            File inputFile = createTeamResultXmlFile(theMatch.getMatchNumber(),theTeam.getTeamNumber());
            if (inputFile.exists()) {
                results.add( readTeamResultXmlFile(matchCache, teamCache, categories, inputFile) );
            } else {
                results.add( new TeamResult(theMatch,theTeam) );
            }
        }
        
        return results;
    }
    
    @Override
    public List<TeamResult> fetchTeamResults(final TeamInfo theTeam) {
        List<MatchInfo> matchCache = fetchAllMatches();
        List<TeamInfo> teamCache   = Arrays.asList(theTeam);
        List<Category> categories  = fetchAllScoringCategories();

        List<TeamResult> results = new ArrayList<>();
        for (MatchInfo theMatch : matchCache) {
            for (TeamInfo team : theMatch.getTeams()) {
                if (team.equals(theTeam)) {
                    File inputFile = createTeamResultXmlFile(theMatch.getMatchNumber(),theTeam.getTeamNumber());
                    if (inputFile.exists()) {
                        results.add( readTeamResultXmlFile(matchCache, teamCache, categories, inputFile) );
                    } else {
                        results.add( new TeamResult(theMatch,theTeam) );
                    }
                }
            }
        }
        
        return results;
    }
    
    @Override
    public List<TeamResult> fetchAllTeamResults() {
        List<MatchInfo> matchCache = fetchAllMatches();
        List<TeamInfo> teamCache   = fetchAllTeams();
        List<Category> categories  = fetchAllScoringCategories();
        
        List<TeamResult> results = new ArrayList<>();
        for (MatchInfo theMatch : matchCache) {
            for (TeamInfo theTeam : theMatch.getTeams()) {
                File inputFile = createTeamResultXmlFile(theMatch.getMatchNumber(),theTeam.getTeamNumber());
                if (inputFile.exists()) {
                    results.add( readTeamResultXmlFile(matchCache, teamCache, categories, inputFile) );
                } else {
                    results.add( new TeamResult(theMatch,theTeam) );
                }
            }
        }
        
        Comparator<TeamResult> byMatchNumber = 
                (e1, e2) -> Integer.compare(e1.getMatch().getMatchNumber(), e2.getMatch().getMatchNumber());
        Collections.sort(results, byMatchNumber);
        return results;
    }
    
    @Override
    public void saveTeamResults(Collection<TeamResult> theResults) {
        for (TeamResult result : theResults) {
            writeTeamResultXmlFile(result);
        }
    }
    
    // ----------------------------------------------------------------------------------
    //       Match result information methods
    // ----------------------------------------------------------------------------------

    @Override
    public MatchResult fetchMatchResult(final int theMatchNumber) {
        MatchInfo match = fetchMatch(theMatchNumber);
        if (match != null) {
            List<TeamResult> results = fetchTeamResults(match);
            return new MatchResult(match,results);
        }
        return null;
    }

    @Override
    public List<MatchResult> fetchAllMatchResults() {
        Multimap<MatchInfo, TeamResult> resultsMap = ArrayListMultimap.create();
        for (TeamResult tr : fetchAllTeamResults()) {
            resultsMap.put(tr.getMatch(), tr);
        }

        List<MatchResult> matchResults = new ArrayList<>();
        for (MatchInfo match : fetchAllMatches()) {
            matchResults.add(new MatchResult(match,resultsMap.get(match)));
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
        return this.rootDir.getName();
    }

    // =================================================================================================================
    //                   PACKAGE METHODS
    // =================================================================================================================

    List<Category> readCategoriesXmlFile(final File categoriesFile) {
        List<Category> categories = Collections.emptyList();
        try {
            Document doc = readFile(categoriesFile);
            categories = readCategoriesXml(doc.getRootElement());
        } catch (Exception ex) {
            Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categories;
    }

    File writeCategoriesXmlFile(final Collection<Category> categories) {
        Element root = writeCategoriesXml(categories);
        File outputFile = createCategoriesXmlFile();
        deleteExistingFile(outputFile);
        writeFile(outputFile, new Document(root));
        return outputFile;
    }

    TeamInfo readTeamXmlFile(final File teamFile) {
        try {
            Document doc = readFile(teamFile);
            return readTeamXml(doc.getRootElement());
        } catch (Exception ex) {
            Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    File writeTeamXmlFile(final TeamInfo team) {
        Element root = writeTeamXml(team);

        File outputFile = createTeamXmlFile(team.getTeamNumber());
        deleteExistingFile(outputFile);
        writeFile(outputFile, new Document(root));
        return outputFile;
    }
    
    MatchInfo readMatchXmlFile(final List<TeamInfo> teamCache, final File matchFile) {
        try {
            Document doc = readFile(matchFile);
            return readMatchXml(teamCache, doc.getRootElement());
        } catch (Exception ex) {
            Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    File writeMatchXmlFile(final MatchInfo match) {
        Element root = writeMatchXml(match);

        File outputFile = createMatchXmlFile(match.getMatchNumber());
        deleteExistingFile(outputFile);
        writeFile(outputFile, new Document(root));
        return outputFile;
    }
    
    TeamResult readTeamResultXmlFile(final List<MatchInfo> matchCache, 
                                     final List<TeamInfo> teamCache, 
                                     final List<Category> categories,
                                     final File teamFile) {
        try {
            Document doc = readFile(teamFile);
            return readTeamResultXml(matchCache, teamCache, categories, doc.getRootElement());
        } catch (Exception ex) {
            Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    File writeTeamResultXmlFile(final TeamResult result) {
        Element root = writeTeamResultXml(result);

        File outputFile = createTeamResultXmlFile(result.getMatch().getMatchNumber(),result.getTeam().getTeamNumber());
        deleteExistingFile(outputFile);
        writeFile(outputFile, new Document(root));
        return outputFile;
    }
    
    // =================================================================================================================
    //                   PRIVATE METHODS
    // =================================================================================================================
    
    private File ensureDirectoryExists(final File f) {
        if (f == null) {
            throw new RuntimeException("The java.io.File argument cannot be null.");
        }
        if (!f.exists()) {
            try {
                FileUtils.mkdirs(f);
            } catch (IOException ex) {
                Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (f.isFile()) {
            throw new RuntimeException("The specified file '" + f.getAbsolutePath() + "' must be a folder not a file.");
        }
        return f;
    }

    private void deleteExistingFile(final File f) {
        if ((f != null) && f.exists()) {
            try {
                FileUtils.delete(f);
            } catch (IOException ex) {
                Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private Document readFile(final File f) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        return builder.build(f);
    }

    private void writeFile(final File f, final Document doc) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            XMLOutputter serializer = new XMLOutputter();
            serializer.output(doc, fos);
            fos.flush();
            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(XmlDataStore.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            StreamUtils.closeQuietly(fos);
        }
    }

    // ----------------------------------------------------------------------------------
    // Reading and writing the category XML file content
    // ----------------------------------------------------------------------------------

    private File createCategoriesXmlFile() {
        String fileName = "categories.xml";
        return new File(this.rootDir, fileName);
    }

    private List<Category> readCategoriesXml(final Element rootElm) throws Exception {
        /*  example category XML file content
        <categories>
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
        </categories>
        */
        List<Category> categories = new ArrayList<>();

        for (Element categoryElm : rootElm.getChildren("category")) {
            String theName = categoryElm.getAttribute("name").getValue();
            String theDisplayName = categoryElm.getAttribute("displayName").getValue();

            Integer theMinValue = null;
            String minValueStr = categoryElm.getAttribute("minValue").getValue();
            if (minValueStr != null && minValueStr.length() > 0) {
                theMinValue = Integer.valueOf(minValueStr);
            }

            Integer theMaxValue = null;
            String maxValueStr = categoryElm.getAttribute("maxValue").getValue();
            if (maxValueStr != null && maxValueStr.length() > 0) {
                theMaxValue = Integer.valueOf( maxValueStr);
            }

            categories.add( new Category(theName,theDisplayName,theMinValue,theMaxValue) );
        }

        return categories;
    }

    private Element writeCategoriesXml(final Collection<Category> categories) {
        /*  example category XML file content
        <categories>
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
            <category name="cat1" displayName="Category 1" valueType="java.lang.Integer" minValue="0" maxValue="10" />
        </categories>
        */
        Element rootElm = new Element("categories");

        for (Category c : categories) {
            Element categoryElm = new Element("category");
            categoryElm.setAttribute("name", c.getName());
            categoryElm.setAttribute("displayName", c.getDisplayName());
            if (c.getMinValue() != null) {
                categoryElm.setAttribute("minValue", c.getMinValue().toString());
            }
            if (c.getMaxValue() != null) {
                categoryElm.setAttribute("maxValue", c.getMaxValue().toString());
            }

            rootElm.addContent(categoryElm);
        }

        return rootElm;
    }

    // ----------------------------------------------------------------------------------
    // Reading and writing the team XML file content
    // ----------------------------------------------------------------------------------

    private File createTeamXmlFile(final int teamNumber) {
        String fileName = "team" + String.format("%05d", teamNumber) + ".xml";
        return new File(this.teamDir, fileName);
    }

    private TeamInfo readTeamXml(final Element team) throws Exception {
        /*  example team XML file content
        <team teamNumber="1329" name="RoboRebels" city="St Louis" state="MO" country="US">
            General notes about this team
        </team>
        */
        int theTeamNumber = team.getAttribute("teamNumber").getIntValue();
        String theTeamName = team.getAttribute("name").getValue();
        String theTeamCity = team.getAttribute("city").getValue();
        String theTeamState = team.getAttribute("state").getValue();
        String theTeamCountry = team.getAttribute("country").getValue();
        String theNotes = team.getTextNormalize();
        
        TeamInfo theTeam = new TeamInfo(theTeamNumber, theTeamName, theTeamCity, theTeamState, theTeamCountry, null);
        theTeam.setNotes(theNotes);
        theTeam.setImageFile( locateImageFile(theTeamNumber) );
        
        return theTeam;
    }

    private Element writeTeamXml(final TeamInfo team) {
        /*  example team XML file content
        <team teamNumber="1329" name="RoboRebels" city="St Louis" state="MO" country="US">
            General notes about this team
        </team>
        */
        Element e = new Element("team");
        e.setAttribute("teamNumber", Integer.toString(team.getTeamNumber()));
        e.setAttribute("name", team.getTeamName());
        if (team.getCity().length() > 0) {
            e.setAttribute("city", team.getCity());
        }
        if (team.getState().length() > 0) {
            e.setAttribute("state", team.getState());
        }
        if (team.getCountry().length() > 0) {
            e.setAttribute("country", team.getCountry());
        }
        e.setText(team.getNotes());
        return e;
    }
    
    private File locateImageFile(final int theTeamNumber) {
        String imageFilePrefix1 = "team" + String.format("%02d", theTeamNumber) + ".";
        String imageFilePrefix2 = "team" + String.format("%03d", theTeamNumber) + ".";
        String imageFilePrefix3 = "team" + String.format("%04d", theTeamNumber) + ".";
        String imageFilePrefix4 = "team" + String.format("%05d", theTeamNumber) + ".";
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(final File f) {
                String fileName = f.getName().toLowerCase();
                return f.isFile() && 
                        (fileName.startsWith(imageFilePrefix1) || 
                        fileName.startsWith(imageFilePrefix2) || 
                        fileName.startsWith(imageFilePrefix3) || 
                        fileName.startsWith(imageFilePrefix4));
            }
        };
        
        File[] files = this.imageDir.listFiles(filter);
        return (files.length > 0 ? files[0] : null);
    }

    // ----------------------------------------------------------------------------------
    // Reading and writing the match XML file content
    // ----------------------------------------------------------------------------------

    private File createMatchXmlFile(final int matchNumber) {
        String fileName = "match" + String.format("%02d", matchNumber) + ".xml";
        return new File(this.matchDir, fileName);
    }

    private MatchInfo readMatchXml(final List<TeamInfo> teamCache, final Element matchElm) throws Exception {
        /*  example match XML file content
        <match matchNumber="1">
            <alliance color="BLUE" team1="1" team2="2" team3="3" />
            <alliance color="RED"  team1="4" team2="5" team3="6" />
        </match>
        */
        
        // Temporary collections for build result
        Map<Integer, TeamInfo> teamsByNumber = new HashMap<>();
        teamCache.forEach(t -> teamsByNumber.put(t.getTeamNumber(),t));
        
        // Resulting model objects
        List<TeamAlliance> theAlliances = new ArrayList<>(2);
        
        // Process <match> XML element
        int theMatchNumber = matchElm.getAttribute("matchNumber").getIntValue();
        for (Element allianceElm : matchElm.getChildren("alliance")) {
            List<Integer> theTeamNumbers = new ArrayList<>(3);
            
            // Process <alliance> XML element
            AllianceColor theColor = AllianceColor.valueOf(allianceElm.getAttributeValue("color"));
            
            Attribute attrib = null;
            attrib = allianceElm.getAttribute("team1");
            if (attrib != null) {
                theTeamNumbers.add(attrib.getIntValue());
            }
            attrib = allianceElm.getAttribute("team2");
            if (attrib != null) {
                theTeamNumbers.add(attrib.getIntValue());
            }
            attrib = allianceElm.getAttribute("team3");
            if (attrib != null) {
                theTeamNumbers.add(attrib.getIntValue());
            }
            
            List<TeamInfo> theTeams = new ArrayList<>(3);
            if (teamCache.isEmpty()) {
                theTeams = fetchTeams(theTeamNumbers);
            } else {
                for (Integer teamNumber : theTeamNumbers) {
                    theTeams.add(teamsByNumber.get(teamNumber));
                }
            }
            theAlliances.add( new TeamAlliance(theColor,theTeams) );
        }
        
        return new MatchInfo(theMatchNumber,theAlliances);
    }    
    
    private Element writeMatchXml(final MatchInfo match) {
        /*  example match XML file content
        <match matchNumber="1">
            <alliance color="BLUE" team1="1" team2="2" team3="3" />
            <alliance color="RED"  team1="4" team2="5" team3="6" />
        </match>
        */
        
        Element rootElm = new Element("match");
        rootElm.setAttribute("matchNumber", Integer.toString(match.getMatchNumber()));
        
        // Output match results for the blue alliance
        TeamAlliance alliance = match.getBlueAlliance();
        Element allianceElm = new Element("alliance");
        
        allianceElm.setAttribute("color", alliance.getAllianceColor().name());
        int counter = 1;
        for (Integer teamNum : alliance.getTeamNumbers()) {
            allianceElm.setAttribute("team"+counter, Integer.toString(teamNum));
            counter++;
        }
        
        rootElm.addContent(allianceElm);

        // Output match results for the red alliance
        alliance = match.getRedAlliance();
        allianceElm = new Element("alliance");
        
        allianceElm.setAttribute("color", alliance.getAllianceColor().name());
        counter = 1;
        for (Integer teamNum : alliance.getTeamNumbers()) {
            allianceElm.setAttribute("team"+counter, Integer.toString(teamNum));
            counter++;
        }
        
        rootElm.addContent(allianceElm);
        return rootElm;
    }

    // ----------------------------------------------------------------------------------
    // Reading and writing the team result XML file content
    // ----------------------------------------------------------------------------------

    private File createTeamResultXmlFile(final int matchNumber, final int teamNumber) {
        String fileName = "match" + String.format("%02d", matchNumber) + "_team" + String.format("%05d", teamNumber) + ".xml";
        return new File(this.resultsDir, fileName);
    }

    private TeamResult readTeamResultXml(final List<MatchInfo> matchCache, 
                                         final List<TeamInfo> teamCache, 
                                         final List<Category> categories,
                                         final Element resultElm) throws Exception {
        /*  example team result XML file content
        <result matchNumber="1" teamNumber="1" cat1="-1" cat2="-1" cat3="-1" ... >
            Specific notes about this team and this match
        </result>
        */
        
        // Process <result> XML element
        int theMatchNumber = resultElm.getAttribute("matchNumber").getIntValue();
        MatchInfo match = matchCache.stream().filter(m -> m.getMatchNumber() == theMatchNumber).findFirst().get();
        
        int theTeamNumber = resultElm.getAttribute("teamNumber").getIntValue();
        TeamInfo team = teamCache.stream().filter(t -> t.getTeamNumber() == theTeamNumber).findFirst().get();
        
        Map<Category,Integer> scores = new HashMap<>();
        for (Category c : categories) {
            String score = resultElm.getAttributeValue(c.getName());
            if ((score != null) && (score.length() > 0)) {
                scores.put(c, Integer.valueOf(score));
            }
        }

        TeamResult tr = new TeamResult(match, team, scores);
        tr.setNotes(resultElm.getTextNormalize());
        
        return tr;
    }    
    
    private Element writeTeamResultXml(final TeamResult result) {
        /*  example team result XML file content
        <result matchNumber="1" teamNumber="1" cat1="-1" cat2="-1" cat3="-1" ... >
            Specific notes about this team and this match
        </result>
        */
        
        Element rootElm = new Element("result");
        rootElm.setAttribute("matchNumber", Integer.toString(result.getMatch().getMatchNumber()));
        rootElm.setAttribute("teamNumber", Integer.toString(result.getTeam().getTeamNumber()));
        
        for (Category c : result.getScoringCategories()) {
            rootElm.setAttribute(c.getName(), result.getScore(c).toString());
        }
        
        rootElm.setText(result.getNotes());
        return rootElm;
    }
    
}
