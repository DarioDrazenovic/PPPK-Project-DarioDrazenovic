/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.EsportsTeam;
import java.util.List;

/**
 *
 * @author 38591
 */
public interface RepositoryEsportsTeam {
    int addEsportsTeam(EsportsTeam data) throws Exception;
    void deleteEsportsTeam(EsportsTeam team) throws Exception;
    List<EsportsTeam> getEsportsTeams() throws Exception;
    EsportsTeam getEsportTeam(int idETeam) throws Exception;
    void updateEsportsTeam(EsportsTeam team) throws Exception;
    
    default void release() throws Exception{};
}
