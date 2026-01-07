package reservation.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.Respo;

public class RespoDAO {

    public Respo findById(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Respo respo = session.get(Respo.class, id);
        session.close();
        return respo;
    }

    public void update(Respo respo) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.update(respo);
        tx.commit();
        session.close();
    }
}