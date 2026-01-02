package reservation.reservation.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import reservation.reservation.model.Utilisateur;
import reservation.reservation.util.SessionCon;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static reservation.reservation.util.SceneManager.switchScene;

public class HomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label userLabel;

    @FXML
    private Label dateLabel;

    private  Utilisateur current = SessionCon.getUser();

    @FXML
    public void initialize() {
        // Mettre à jour la date actuelle
        welcomeLabel.setText("Bienvenue, " + current.getNomComplet() + " !");
        if(current != null) {
            userLabel.setText("Bienvenue " + current.getNomComplet());
        }
        updateCurrentDate();
    }



    private void updateCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);
        dateLabel.setText(formattedDate);
    }

    @FXML
    private void showProfile() {
        // Logique pour afficher le profil
        System.out.println("Affichage du profil de " + current.getNomComplet());
        // Vous pouvez ouvrir une nouvelle fenêtre ou changer de vue ici
    }

    @FXML
    private void handleLogout() {
            switchScene("/reservation/Views/connexion.fxml", "Connexion");
            System.out.println("Déconnexion réussie");

    }

    @FXML
    private void showReservations() {
        // Logique pour afficher les réservations
        System.out.println("Affichage des réservations");
    }

    @FXML
    private void newReservation() {
        // Logique pour nouvelle réservation
        System.out.println("Création d'une nouvelle réservation");
    }

    @FXML
    private void showSettings() {
        // Logique pour afficher les paramètres
        System.out.println("Affichage des paramètres");
    }


}