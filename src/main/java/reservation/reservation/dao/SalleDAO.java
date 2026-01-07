package reservation.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.Salle;

import java.util.List;

public class SalleDAO {

    public SalleDAO(){};

    public List<Salle> getSallesByRespo(Long respoId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Salle> salles = session.createQuery(
                        "FROM Salle s WHERE s.respo.id = :id", Salle.class)
                .setParameter("id", respoId)
                .getResultList();
        session.close();
        return salles;
    }

    public List<Salle> getAllSalles() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return session.createQuery(
                "SELECT s FROM Salle s WHERE s.dispo = true",
                Salle.class
        ).getResultList();
    }

    public void save(Salle salle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(salle);
        tx.commit();
        session.close();
    }

    public void update(Salle salle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(salle);
        tx.commit();
        session.close();
    }

    public void delete(Salle salle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(salle);
        tx.commit();
        session.close();
    }
}