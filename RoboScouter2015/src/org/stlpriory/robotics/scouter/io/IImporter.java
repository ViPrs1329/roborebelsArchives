/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.io;

import java.util.Collection;
import java.util.List;

import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamInfo;

/**
 *  Interface for all IImporter implementations
 */
public interface IImporter {

    List<MatchInfo> importMatchSchedule(Collection<TeamInfo> theTeams);
    
    List<TeamInfo> importTeams();
    
}
