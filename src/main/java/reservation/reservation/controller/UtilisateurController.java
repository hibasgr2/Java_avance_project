package reservation.reservation.controller;

import java.util.List;

import javafx.event.ActionEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;
import reservation.reservation.dao.UtilisateurDAO;
import reservation.reservation.model.Utilisateur;

public class UtilisateurController {

    private final UtilisateurDAO utilisateurDAO;

    public UtilisateurController() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    // Ajouter un nouvel utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.save(utilisateur);
        System.out.println("CONTROLLER: Utilisateur créé - " + utilisateur.getNomComplet());
    }

    // Mettre à jour un utilisateur existant
    public void mettreAJourUtilisateur(Utilisateur utilisateur) {
        utilisateurDAO.update(utilisateur);
    }

    // Supprimer un utilisateur par id
    public void supprimerUtilisateur(Long id) {
        utilisateurDAO.delete(id);
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> listerUtilisateurs() {
        return utilisateurDAO.getAll();
    }

    // Récupérer un utilisateur par id
    public Utilisateur getUtilisateurParId(int id) {
        return utilisateurDAO.getById(id);
    }

    public Utilisateur getUtilisateurParEmail(String email) {
        return utilisateurDAO.findByEmail(email);
    }
}
