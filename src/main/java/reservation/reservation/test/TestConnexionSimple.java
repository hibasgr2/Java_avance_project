package reservation.reservation.test;


import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnexionSimple {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/projet_java_rs?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "root"; // Remplacez par votre mot de passe

        System.out.println("Test de connexion à : " + url);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("✓ Connexion MySQL réussie !");
            System.out.println("✓ Base de données : " + conn.getCatalog());
            System.out.println("✓ Version MySQL : " + conn.getMetaData().getDatabaseProductVersion());
        } catch (Exception e) {
            System.err.println("✗ Échec de connexion : " + e.getMessage());
            System.out.println("\nVérifiez :");
            System.out.println("1. MySQL est-il démarré ?");
            System.out.println("2. Le mot de passe est-il correct ?");
            System.out.println("3. La base 'projet_java_js' existe-t-elle ?");

            // Test de connexion sans base spécifique
            try {
                String urlSansBase = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
                Connection conn2 = DriverManager.getConnection(urlSansBase, user, password);
                System.out.println("\n✓ Connexion à MySQL réussie (sans base spécifique)");
                conn2.close();
            } catch (Exception e2) {
                System.err.println("\n✗ Impossible de se connecter à MySQL du tout : " + e2.getMessage());
            }
        }
    }
}
