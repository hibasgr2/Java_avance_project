package reservation.reservation.test;

import reservation.reservation.dao.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.model.SupUser;
import reservation.reservation.util.PasswordUtil;


public class CreationTabs  {

    public static void main(String[] args) {
        System.out.println("🚀 Démarrage de l'application...");

        String hash = PasswordUtil.hashPassword("1234");
        System.out.println(hash);

        System.out.println(
                PasswordUtil.checkPassword("1234", hash)
        ); // true


        try {

            // 1. Initialiser Hibernate et créer les tables
            System.out.println("📊 Création des tables en base de données...");
            createTables();

//            // 2. Insérer des données de test
//            System.out.println("📝 Insertion des données de test...");
//            insertTestData();

            System.out.println("✅ Application démarrée avec succès !");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du démarrage : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void createTables() {
        // Cette méthode va créer les tables automatiquement grâce à hbm2ddl.auto=create/update
        // On ouvre simplement une session pour déclencher la création

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            // Exécuter une requête simple pour déclencher la création des tables
            session.createNativeQuery("SELECT 1").getResultList();

            PasswordUtil pass = new PasswordUtil();

            System.out.println("👤 Création d'un SuperUser...");
            SupUser superUser = new SupUser(
                    "Admin Principal",
                    "0612345678",
                    "admin@reservation.com",
                    PasswordUtil.hashPassword("admin123")

            );
            session.persist(superUser);
            transaction.commit();
            System.out.println("✅ Tables créées avec succès !");

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("❌ Erreur lors de la création des tables : " + e.getMessage());
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
//    private static void insertTestData() {
//        Session session = null;
//        Transaction transaction = null;
//
//        try {
//            session = HibernateUtil.getSessionFactory().openSession();
//            transaction = session.beginTransaction();
//
////            // Créer un SuperUser
////            System.out.println("👤 Création d'un SuperUser...");
////            SupUser superUser = new SupUser(
////                    "Admin Principal",
////                    "0612345678",
////                    "admin@reservation.com",
////                    "admin123"
////            );
////            session.persist(superUser);
////
//
//            transaction.commit();
//
//        } catch (Exception e) {
//            if (transaction != null) transaction.rollback();
//            System.err.println("❌ Erreur lors de l'insertion des données : " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            if (session != null && session.isOpen()) {
//                session.close();
//            }
//        }
//    }

}

