package reservation.reservation.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import reservation.reservation.model.Role;
import reservation.reservation.model.Utilisateur;
import reservation.reservation.service.AuthService;
import reservation.reservation.util.SessionCon;

import static reservation.reservation.util.SceneManager.switchScene;

public class AuthController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField telField;
    @FXML private Label messageLabel;

    private final AuthService authService = new AuthService();

    // 🔐 Connexion
    @FXML
    public void handleLogin() {
        try {
            Utilisateur u = authService.login(
                    emailField.getText(),
                    passwordField.getText()
            );
            SessionCon.setUser(u);
            if(u.getRole() == Role.SUPERUSER) {

                switchScene("/reservation/Views/InterfacesSuperUser/homeadm.fxml", "homeadm");
            } else if(u.getRole() == Role.RESPO) {
                switchScene("/reservation/Views/respo.fxml", "Dashboard Responsable");
            }
            else
              switchScene("/reservation/Views/home.fxml", "Home");

        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }

    // 📝 Inscription Client
    @FXML
    public void handleRegister() {

        try {
            authService.registerClient(
                    nomField.getText(),
                    emailField.getText(),
                    telField.getText(),
                    passwordField.getText()

            );
            messageLabel.setText("Compte créé avec succès");
        } catch (Exception e) {
            messageLabel.setText(e.getMessage());
        }
    }


    @FXML
    public  void goToLogin() {
        switchScene("/reservation/Views/connexion.fxml", "Connexion");
    }



    @FXML
    public  void goToRegister() {
        switchScene("/reservation/Views/register.fxml", "Inscription");
    }

}