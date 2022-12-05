/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 38591
 */
@Entity
@Table(name = "EsportsTeam")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EsportsTeam.findAll", query = "SELECT e FROM EsportsTeam e")
    , @NamedQuery(name = "EsportsTeam.findByIDETeam", query = "SELECT e FROM EsportsTeam e WHERE e.iDETeam = :iDETeam")
    , @NamedQuery(name = "EsportsTeam.findByTeamName", query = "SELECT e FROM EsportsTeam e WHERE e.teamName = :teamName")
    , @NamedQuery(name = "EsportsTeam.findByCountry", query = "SELECT e FROM EsportsTeam e WHERE e.country = :country")})
public class EsportsTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDETeam")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDETeam;
    @Basic(optional = false)
    @Column(name = "TeamName")
    private String teamName;
    @Basic(optional = false)
    @Column(name = "Country")
    private String country;
    @JoinColumn(name = "PersonID", referencedColumnName = "IDPerson")
    @ManyToOne
    private Person personID;

    public EsportsTeam() {
    }
    
    public EsportsTeam(EsportsTeam data) {
        updateDetailsEsports(data);
    }

    public EsportsTeam(Integer iDETeam) {
        this.iDETeam = iDETeam;
    }

    public EsportsTeam(Integer iDETeam, String teamName, String country, Person personID) {
        this.iDETeam = iDETeam;
        this.teamName = teamName;
        this.country = country;
        this.personID = personID;
    }

    public Integer getIDETeam() {
        return iDETeam;
    }

    public void setIDETeam(Integer iDETeam) {
        this.iDETeam = iDETeam;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Person getPersonID() {
        return personID;
    }

    public void setPersonID(Person personID) {
        this.personID = personID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDETeam != null ? iDETeam.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EsportsTeam)) {
            return false;
        }
        EsportsTeam other = (EsportsTeam) object;
        if ((this.iDETeam == null && other.iDETeam != null) || (this.iDETeam != null && !this.iDETeam.equals(other.iDETeam))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.EsportsTeam[ iDETeam=" + iDETeam + " ]";
    }

    public void updateDetailsEsports(EsportsTeam data) {
        this.teamName = data.getTeamName();
        this.country = data.getCountry();
        this.personID = data.getPersonID();
    }
    
}
