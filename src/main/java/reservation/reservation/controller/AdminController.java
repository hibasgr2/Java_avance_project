package reservation.reservation.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import reservation.reservation.dao.UtilisateurDAO;
import reservation.reservation.model.Respo;
import reservation.reservation.model.Role;
import reservation.reservation.model.Utilisateur;
import reservation.reservation.service.AuthService;
import reservation.reservation.util.PasswordUtil;
import reservation.reservation.util.SceneManager;
import reservation.reservation.controller.UtilisateurController;
import reservation.reservation.util.SessionCon;

import java.io.IOException;
import java.util.List;

public class AdminController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField telField;

    @FXML
    private TextField infosRespo;
    @FXML private Label activeLabel;
    @FXML private Label totalLabel;
    @FXML private Label nomRespo;
    @FXML private Label inactiveLabel;
    @FXML private TableView<Respo> respoTable;


    @FXML
    public void initialize(){
        countActifRespo();
        listRespos();
        ajouterbtnscols();
        if (nomRespo != null) {
            nomRespo.setText("Bienvenue " + SessionCon.getUser().getNomComplet());
        }
    }

    @FXML
    public void redirectToAddRespo() {
        SceneManager.switchScene("/reservation/Views/InterfacesSuperUser/gestion_respos.fxml", "Ajouter Salle");
    }

    @FXML
    public void redirectToDashboard() {
        SceneManager.switchScene("/reservation/Views/InterfacesSuperUser/homeadm.fxml", "Dashboard Super User");
    }

    @FXML
    public void showAddRespoModal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/reservation/Views/InterfacesSuperUser/Add_respo.fxml")
            );
            Parent root = loader.load();
            Stage modalStage = new Stage();
            Scene scene = new Scene(root);
            modalStage.setTitle("Ajouter un Responsable");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(scene);
            modalStage.setWidth(600);
            modalStage.setHeight(650);
            modalStage.centerOnScreen();
            modalStage.showAndWait();
        } catch (IOException e) {
            System.err.println("❌ Erreur de chargement du FXML: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir le formulaire");
            alert.setContentText("Le fichier FXML n'a pas pu être chargé.\n" + e.getMessage());
            alert.showAndWait();
        }
    }
    public AuthService serviceAuth;
    public UtilisateurDAO userDAO = new UtilisateurDAO();
    public UtilisateurController userC = new UtilisateurController();




    public void AddRespo() {
        if (userDAO.findByEmail(emailField.getText()) != null)
            throw new RuntimeException("Email déjà utilisé");

        Respo respo = new Respo(nomField.getText(), telField.getText(), emailField.getText(), PasswordUtil.hashPassword(passwordField.getText()), true);
        userDAO.save(respo);

        countActifRespo();
    }
    @FXML
    public void countActifRespo() {
        try {
            long numeroRespoActif = userDAO.getAll().stream().filter(e -> e.getRole() == Role.RESPO).map(e -> (Respo) e).filter(Respo::isActif).count();
            if (activeLabel != null) {
                activeLabel.setText(String.valueOf(numeroRespoActif));
            }
            if (totalLabel != null || inactiveLabel != null) {
                long total = userDAO.getAll().stream().filter(e -> e.getRole() == Role.RESPO).count();
                long inactifs = total - numeroRespoActif;
                if (totalLabel != null) totalLabel.setText(String.valueOf(total));
                if (inactiveLabel != null) inactiveLabel.setText(String.valueOf(inactifs));
            }
        } catch (Exception e) {
            System.err.println("Erreur dans countActifRespo: " + e.getMessage());
        }
    }

    @FXML public void listRespos(){
        try {
            respoTable.getItems().setAll(userDAO.getAll().stream()
                    .filter(e -> e.getRole() == Role.RESPO)
                    .map(e -> (Respo) e).toList());
        }catch (Exception e){
            System.out.println("erreur howa hada" + e.getMessage());
        }
    }

    // pour ajouter des buttons des actions dans la
    private void ajouterbtnscols() {
        try {
            // L'index 5 si vous avez 6 colonnes (0 à 5)
            TableColumn<Respo, Void> actionsCol = (TableColumn<Respo, Void>) respoTable.getColumns().get(5);

            actionsCol.setCellFactory(param -> new TableCell<Respo, Void>() {
                private final Button editBtn = new Button("Modifier");
                private final Button deleteBtn = new Button("Supprimer");
                private final HBox pane = new HBox(5, editBtn, deleteBtn);

                {

                    editBtn.setStyle("-fx-background-color: #003161; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                    deleteBtn.setStyle("-fx-background-color: #006A67; -fx-text-fill: white; -fx-font-size: 12px; -fx-padding: 5 10;");
                    editBtn.setOnAction(event -> {
                        try {
                            // Récupérer l'utilisateur
                            Respo user = getTableView().getItems().get(getIndex());

                            FXMLLoader loader = new FXMLLoader(
                                    getClass().getResource("/reservation/Views/InterfacesSuperUser/modify_respo.fxml")
                            );
                            Parent root = loader.load();
                            AdminController modalController = loader.getController();
                            modalController.nomField.setText(user.getNomComplet());
                            modalController.emailField.setText(user.getEmail());
                            modalController.telField.setText(user.getTel());
                            modalController.passwordField.setText("");
                            Stage modalStage = new Stage();
                            Scene scene = new Scene(root);
                            modalStage.setTitle("Modifier un Responsable");
                            modalStage.initModality(Modality.APPLICATION_MODAL);
                            modalStage.setScene(scene);
                            modalStage.setWidth(600);
                            modalStage.setHeight(650);
                            modalStage.centerOnScreen();
                            modalStage.showAndWait();
                            listRespos();
                        } catch (Exception e){
                            System.out.println("erreur : " + e.getMessage());
                            e.printStackTrace();
                        }
                    });
                    deleteBtn.setOnAction(event -> {

                       Respo user = getTableView().getItems().get(getIndex());
                        try {
                            System.out.println(user.getId().intValue());
                            userC.supprimerUtilisateur(user.getId());
                            listRespos();
                        } catch (Exception e) {
                            System.out.println("erreur howa hada " + e.getMessage());
                        }
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(pane);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void ReturnResposFiltree(){
        if (infosRespo == null)
            throw new RuntimeException("Le champs est vide");

        List<Respo> resultats = respoTable.getItems().stream()
                .filter(e ->(e.getNomComplet().contains(infosRespo.getText())) ||
                                (e.getTel().contains(infosRespo.getText())) ||
                                (e.getEmail().contains(infosRespo.getText()))
                ).collect(java.util.stream.Collectors.toList());
        respoTable.getItems().setAll(resultats);
    }

    public void modifierRespo(){
        Utilisateur user = userC.getUtilisateurParEmail(emailField.getText());
        user.setNomComplet(nomField.getText());
        user.setEmail(nomField.getText());
        user.setTel(telField.getText());
        if (passwordField.getText() != null)
            user.setPassword(PasswordUtil.hashPassword(passwordField.getText()));
        userC.mettreAJourUtilisateur(user);
    }
}