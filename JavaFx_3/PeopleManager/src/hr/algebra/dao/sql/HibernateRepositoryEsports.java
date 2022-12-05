/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.RepositoryEsportsTeam;
import hr.algebra.model.EsportsTeam;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author 38591
 */
public class HibernateRepositoryEsports implements RepositoryEsportsTeam{

    @Override
    public int addEsportsTeam(EsportsTeam data) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            
            em.getTransaction().begin();
            EsportsTeam esports = new EsportsTeam(data);
            em.persist(esports);
            em.getTransaction().commit();
            
            return esports.getIDETeam();
        }
    }

    @Override
    public void deleteEsportsTeam(EsportsTeam team) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            
            em.getTransaction().begin();
            em.remove(em.contains(team) ? team : em.merge(team));
            em.getTransaction().commit();
        }
    }

    @Override
    public List<EsportsTeam> getEsportsTeams() throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            
            return entityManager.get().createNamedQuery(HibernateFactory.SELECT_TEAM).getResultList();
        }
    }

    @Override
    public EsportsTeam getEsportTeam(int idETeam) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            
            return em.find(EsportsTeam.class, idETeam);
        }
    }

    @Override
    public void updateEsportsTeam(EsportsTeam team) throws Exception {
        try (EntityManagerWrapper entityManager = HibernateFactory.getEntityManager()) {
            EntityManager em = entityManager.get();
            
            em.getTransaction().begin();
            em.find(EsportsTeam.class, team.getIDETeam()).updateDetailsEsports(team);
            em.getTransaction().commit();            
        }
    }

    @Override
    public void release() throws Exception {
        HibernateFactory.release();
    }
    
}
