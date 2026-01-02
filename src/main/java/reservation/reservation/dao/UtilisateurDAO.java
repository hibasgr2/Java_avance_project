package reservation.reservation.dao;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.SupUser;
import reservation.reservation.model.Utilisateur;

public class UtilisateurDAO {

    public void save(Utilisateur utilisateur) {
        Transaction transaction = null;
        Session session = null;

        try {
            // Ouvre une session
            session = HibernateUtil.getSession();

            // Démarre la transaction
            transaction = session.beginTransaction();

            // Sauvegarde l'utilisateur
            session.save(utilisateur);

            // Commit la transaction
            transaction.commit();

            System.out.println("✅ Utilisateur sauvegardé avec ID: " + utilisateur.getId());
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }


    public void update(Utilisateur utilisateur) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();
            session.update(utilisateur);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        Session session = null;
        Transaction transaction = null;
        try  {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Utilisateur utilisateur = session.get(Utilisateur.class, id);
            System.out.println("hada howa l user li l9ina "+utilisateur.getId());
            if (utilisateur != null) {
                session.delete(utilisateur);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    public Utilisateur getById(int id) {
        try (Session session = HibernateUtil.getSession()) {
            return session.get(Utilisateur.class, id);
        }
    }

    public List<Utilisateur> getAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("from Utilisateur", Utilisateur.class).list();
        }
    }

    public Utilisateur findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "FROM Utilisateur u WHERE u.email = :email",
                            Utilisateur.class
                    ).setParameter("email", email)
                    .uniqueResult();
        }

    }
}
