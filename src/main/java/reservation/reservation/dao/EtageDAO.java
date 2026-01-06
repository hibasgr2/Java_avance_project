package reservation.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.Etage;

import java.util.ArrayList;
import java.util.List;

public class EtageDAO {

    public List<Etage> getByImmeubleId(int immeubleId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // REQUÊTE HQL CORRECTE : Utilise "immeuble.id" (la propriété, pas la colonne)
            String hql = "FROM Etage e WHERE e.immeuble.id = :immeubleId ORDER BY e.id";

            List<Etage> etages = session.createQuery(hql, Etage.class)
                    .setParameter("immeubleId", immeubleId)
                    .list();

            System.out.println("DEBUG: " + etages.size() + " étage(s) trouvé(s) pour immeuble ID=" + immeubleId);
            return etages;

        } catch (Exception e) {
            System.err.println("❌ Erreur dans getByImmeubleId: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }

    }
        public List<Etage> getAllWithRespo() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Etage e LEFT JOIN FETCH e.respo LEFT JOIN FETCH e.immeuble ORDER BY e.id";
            return session.createQuery(hql, Etage.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void update(Etage etage) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(etage);  // Méthode Hibernate pour mise à jour
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


}
