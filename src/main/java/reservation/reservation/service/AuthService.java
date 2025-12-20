package reservation.reservation.service;

import reservation.reservation.dao.UtilisateurDAO;
import reservation.reservation.model.Client;
import reservation.reservation.model.Role;
import reservation.reservation.model.Utilisateur;
import reservation.reservation.util.PasswordUtil;

public class AuthService {

    private final UtilisateurDAO utilisateurDao = new UtilisateurDAO();

    public void registerClient(String nom, String email, String tel, String password) {
        if (utilisateurDao.findByEmail(email) != null) {
            throw new RuntimeException("Email déjà utilisé");
        }


      Utilisateur u = new Client(nom,tel,email, PasswordUtil.hashPassword(password),0.0);
        utilisateurDao.save(u);
    }

    public Utilisateur login(String email, String password) {
        Utilisateur u = utilisateurDao.findByEmail(email);

        if (u == null || !PasswordUtil.checkPassword(password, u.getPassword())) {
            throw new RuntimeException("Email ou mot de passe incorrect");
        }
        return u;
    }
}
