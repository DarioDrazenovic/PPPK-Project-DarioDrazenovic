/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.EsportsTeam;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author 38591
 */
public class EsportsTeamViewModel {
    private final EsportsTeam team;

    public EsportsTeamViewModel(EsportsTeam team) {
        if (team == null) {
            team = new EsportsTeam(0,"","",null); //ako tima nema neka postane novi prazni tim
        }
        this.team = team;
    }

    public EsportsTeam getTeam() {
        return team;
    }
    
    public IntegerProperty getIDTeam(){
        return new SimpleIntegerProperty(team.getIDETeam());
    }
    
    public StringProperty getNameProperty() {
        return new SimpleStringProperty(team.getTeamName());
    }
    
     public StringProperty getCountryProperty() {
        return new SimpleStringProperty(team.getCountry());
    }
     
      public ObjectProperty getPersonIDProperty() {
        return new SimpleObjectProperty(team.getPersonID());
    }
}
