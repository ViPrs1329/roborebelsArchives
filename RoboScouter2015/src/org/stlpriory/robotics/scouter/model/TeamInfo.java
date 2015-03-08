package org.stlpriory.robotics.scouter.model;

import static org.stlpriory.robotics.scouter.util.StreamUtils.createTempFileFromResource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

/**
 * Class to store information about a team
 */
public class TeamInfo {
    /** Default robot image */
    private static File DEFAULT_IMAGE_FILE = null;
    static {
        try {
            DEFAULT_IMAGE_FILE = createTempFileFromResource(TeamInfo.class,"defaultImage.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private final int teamNumber;
    private String teamName;
    private String city;
    private String state;
    private String country;
    private File imageFile;
    private String notes;
    
    public TeamInfo(final int theTeamNumber, final String theTeamName) {
        this(theTeamNumber, theTeamName, null, null, null, null);
    }
    
    public TeamInfo(final int theTeamNumber, final String theTeamName, final File theImageFile) {
        this(theTeamNumber, theTeamName, null, null, null, theImageFile);
    }

    public TeamInfo(final int theTeamNumber, 
                    final String theTeamName, 
                    final String theTeamCity, 
                    final String theTeamState, 
                    final String theTeamCountry,
                    final File theImageFile) {
        if (theTeamNumber <= 0) {
            throw new IllegalArgumentException("The team number must be > 0");
        }
        if (theTeamName == null || theTeamName.length() == 0) {
            throw new IllegalArgumentException("The team name cannot be null or empty");
        }
        
        this.teamNumber = theTeamNumber;
        this.teamName = theTeamName;
        this.city = (theTeamCity != null ? theTeamCity : "");
        this.state = (theTeamState != null ? theTeamState : "");
        this.country = (theTeamCountry != null ? theTeamCountry : "");
        this.imageFile = (theImageFile != null ? theImageFile : DEFAULT_IMAGE_FILE);
        this.notes = "";
    }

    public int getTeamNumber() {
        return this.teamNumber;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public String getCountry() {
        return this.country;
    }

    public String getState() {
        return this.state;
    }

    public String getCity() {
        return this.city;
    }

    public void setTeamName(final String teamName) {
        this.teamName = teamName;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public File getImageFile() {
        return this.imageFile;
    }

    public void setImageFile(final File theImageFile) {
        this.imageFile = (theImageFile != null ? theImageFile : DEFAULT_IMAGE_FILE);
    }

    public void setImageFile(final URL theImageFile) {
        try {
            this.imageFile = (theImageFile != null ? new File(theImageFile.toURI()) : DEFAULT_IMAGE_FILE);
        } catch (URISyntaxException e) {
            e.printStackTrace();
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
        return "TeamInfo " + this.teamNumber + " - " + this.teamName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.teamNumber, this.teamName);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TeamInfo)) {
            return false;
        }
        TeamInfo that = (TeamInfo) obj;
        return this.teamNumber == that.teamNumber 
                && this.teamName.toLowerCase().equals(that.teamName.toLowerCase());
    }

}
