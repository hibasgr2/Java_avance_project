package reservation.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.Reservation;

import java.util.List;

public class ReservationDAO {

    public List<Reservation> getReservationsByRespo(Long respoId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Reservation> reservations = session.createQuery(
                        "FROM Reservation r JOIN FETCH r.client WHERE r.respo.id = :id", Reservation.class)
                .setParameter("id", respoId)
                .getResultList();
        session.close();
        return reservations;
    }

    public void update(Reservation reservation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(reservation);
        tx.commit();
        session.close();
    }

    public List<Reservation> findByClientId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Reservation> reservations = session.createQuery(
                        "FROM Reservation r JOIN FETCH r.salle WHERE r.client.id = :id", Reservation.class)
                .setParameter("id", id)
                .getResultList();
        session.close();
        return reservations;
    }


    public void save(Reservation reservation) {
        Transaction transaction = null;
        Session session = null;

        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();
            session.save(reservation);
            transaction.commit();

            System.out.println("✅ Reservation reussi avec ID: " + reservation.getId());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Reservation findById(int reservationId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Reservation> res = session.createQuery(
                        "FROM Reservation r WHERE r.id = :id", Reservation.class)
                .setParameter("id", reservationId)
                .getResultList();
        session.close();
        return (Reservation) res;
    }
}