package reservation.reservation.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.Immeuble;
import reservation.reservation.dao.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class ImmeubleDAO {

    // 1. CRÉER un nouvel immeuble
    public void save(Immeuble immeuble) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(immeuble);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 2. RÉCUPÉRER tous les immeubles (vous l'avez déjà)
    public List<Immeuble> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Immeuble", Immeuble.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // 3. RÉCUPÉRER un immeuble par ID (vous l'avez déjà)
    public Immeuble getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Immeuble.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 4. METTRE À JOUR un immeuble existant
    public void update(Immeuble immeuble) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(immeuble);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    // 5. SUPPRIMER un immeuble
    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Immeuble immeuble = session.get(Immeuble.class, id);
            if (immeuble != null) {
                session.delete(immeuble);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }
}