package reservation.reservation.dao;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import reservation.reservation.model.*;

public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Configuration Hibernate
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // IMPORTANT : Ajoutez votre classe d'entité
            configuration.addAnnotatedClass(Utilisateur.class);
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(Respo.class);
            configuration.addAnnotatedClass(SupUser.class);
            configuration.addAnnotatedClass(Salle.class);
            configuration.addAnnotatedClass(Reservation.class);
            configuration.addAnnotatedClass(Etage.class);
            configuration.addAnnotatedClass(Immeuble.class);

            // Build SessionFactory
            sessionFactory = configuration.buildSessionFactory();

            System.out.println("✓ SessionFactory créée avec succès");

        } catch (Throwable ex) {
            System.err.println("✗ Échec création SessionFactory: " + ex.getMessage());
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static org.hibernate.Session getSession() {
        // Utilisez try-with-resources ou gérez manuellement la fermeture
        return sessionFactory.openSession();
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}