package reservation.reservation.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import reservation.reservation.dao.*;
import reservation.reservation.model.*;
import reservation.reservation.util.SessionCon;

import java.util.List;

import static reservation.reservation.model.EtatReservation.*;
import static reservation.reservation.util.SceneManager.switchScene;

public class RespoController {

    private Respo respo;

    private final SalleDAO salleDAO = new SalleDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final EtageDAO etageDAO = new EtageDAO();

    @FXML
    private TableView<Salle> tableSalles;

    @FXML
    private TableView<Reservation> tableReservations;

    @FXML
    private Label salleFormTitle;

    @FXML
    private TextField salleTypeField;

    @FXML
    private TextField salleCapaciteField;

    @FXML
    private TextField salleNumEtageField;

    @FXML
    private TextField sallePrixField;

    @FXML
    private ComboBox<Etage> salleEtageCombo;

    @FXML
    private CheckBox salleDispoCheck;

    private Salle salleEnEdition;

    @FXML
    public void initialize() {
        respo = (Respo) SessionCon.getUser();
        if (respo == null) {
            showAlert("Erreur", "Aucun utilisateur connecté");
            return;
        }

        TableColumn<Reservation, String> nomClientCol = new TableColumn<>("Nom Client");
        nomClientCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getNomComplet()));

        TableColumn<Reservation, String> telClientCol = new TableColumn<>("Téléphone");
        telClientCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getClient().getTel()));

// Ajouter les colonnes au TableView
        tableReservations.getColumns().addAll(nomClientCol, telClientCol);


        initSalleForm();
        refreshSalles();
        refreshReservations();
    }

    // ================= SALLES =================

    @FXML
    public void ajouterSalle() {
        salleEnEdition = null;
        salleFormTitle.setText("Ajouter une salle");
        clearSalleForm();
        salleDispoCheck.setSelected(true);
    }

    @FXML
    public void modifierSalle() {
        Salle salle = getSelectedSalle("Veuillez sélectionner une salle à modifier");
        if (salle == null) return;

        salleEnEdition = salle;
        salleFormTitle.setText("Modifier la salle #" + salle.getId());
        fillSalleFormFromSalle(salle);
    }

    @FXML
    public void enregistrerSalle() {
        String type = salleTypeField.getText();
        String capaciteText = salleCapaciteField.getText();
        String numEtageText = salleNumEtageField.getText();
        String prixText = sallePrixField.getText();
        Etage etage = salleEtageCombo.getSelectionModel().getSelectedItem();

        if (type == null || type.trim().isEmpty() || capaciteText == null || numEtageText == null || prixText == null || etage == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs correctement");
            return;
        }

        int capacite;
        int numEtage;
        double prix;
        try {
            capacite = Integer.parseInt(capaciteText.trim());
            numEtage = Integer.parseInt(numEtageText.trim());
            prix = Double.parseDouble(prixText.trim());
        } catch (Exception e) {
            showAlert("Erreur", "Capacité, numéro d'étage et prix doivent être des nombres valides");
            return;
        }

        if (salleEnEdition == null) {
            Salle nouvelleSalle = new Salle();
            nouvelleSalle.setTypeSalle(type.trim());
            nouvelleSalle.setCapacite(capacite);
            nouvelleSalle.setNumEtage(numEtage);
            nouvelleSalle.setPrix(prix);
            nouvelleSalle.setDispo(salleDispoCheck.isSelected());
            nouvelleSalle.setEtage(etage);
            nouvelleSalle.setRespo(respo);

            salleDAO.save(nouvelleSalle);
            refreshSalles();
            showAlert("Succès", "Salle ajoutée avec succès");
        } else {
            salleEnEdition.setTypeSalle(type.trim());
            salleEnEdition.setCapacite(capacite);
            salleEnEdition.setNumEtage(numEtage);
            salleEnEdition.setPrix(prix);
            salleEnEdition.setDispo(salleDispoCheck.isSelected());
            salleEnEdition.setEtage(etage);

            salleDAO.update(salleEnEdition);
            refreshSalles();
            showAlert("Succès", "Salle modifiée avec succès");
        }

        annulerEditionSalle();
    }

    @FXML
    private void showProfile() {
        // Logique pour afficher le profil
        switchScene("/reservation/Views/client/Profil.fxml", "Profil");

    }

    @FXML
    private void handleLogout() {
        switchScene("/reservation/Views/connexion.fxml", "Connexion");
        System.out.println("Déconnexion réussie");

    }

    @FXML
    public void annulerEditionSalle() {
        salleEnEdition = null;
        salleFormTitle.setText("Formulaire Salle");
        clearSalleForm();
        salleDispoCheck.setSelected(true);
    }

    @FXML
    public void supprimerSalle() {
        Salle salle = getSelectedSalle("Veuillez sélectionner une salle à supprimer");
        if (salle == null) return;

        salleDAO.delete(salle);
        refreshSalles();
        showAlert("Succès", "Salle supprimée avec succès");
    }

    @FXML
    public void changerDisponibilite() {
        Salle salle = getSelectedSalle("Veuillez sélectionner une salle");
        if (salle == null) return;

        salle.setDispo(!salle.isDispo());
        salleDAO.update(salle);

        tableSalles.refresh();
    }

    // ================= RESERVATIONS =================

    @FXML
    public void validerReservation() {
        Reservation reservation = getSelectedReservation("Sélectionnez une réservation");
        System.out.println(reservation);
        if (reservation == null) return;

        if (!respo.isActif()) {
            showAlert("Accès refusé", "Responsable désactivé");
            return;
        }

        reservation.setEtat(EtatReservation.VALIDEE);
        reservationDAO.update(reservation);
        tableReservations.refresh();
    }

    @FXML
    public void refuserReservation() {
        Reservation reservation = getSelectedReservation("Sélectionnez une réservation");
        if (reservation == null) return;

        reservation.setEtat(EtatReservation.REFUSEE);
        reservationDAO.update(reservation);
        tableReservations.refresh();
    }

    // ================= UTIL =================

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }

    private void refreshSalles() {
        tableSalles.getItems().setAll(salleDAO.getSallesByRespo(respo.getId()));
    }

    private void refreshReservations() {

        tableReservations.getItems().setAll(reservationDAO.getReservationsByRespo(respo.getId()));
    }

    private void initSalleForm() {
        if (salleEtageCombo == null) return;
        salleEtageCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Etage item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ("Etage #" + item.getId()));
            }
        });
        salleEtageCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Etage item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ("Etage #" + item.getId()));
            }
        });

        refreshEtagesCombo();
        annulerEditionSalle();
    }

    private void refreshEtagesCombo() {
        if (salleEtageCombo == null) return;
        salleEtageCombo.getItems().setAll(etageDAO.getEtagesByRespo(respo.getId()));
    }

    private void clearSalleForm() {
        if (salleTypeField != null) salleTypeField.clear();
        if (salleCapaciteField != null) salleCapaciteField.clear();
        if (salleNumEtageField != null) salleNumEtageField.clear();
        if (sallePrixField != null) sallePrixField.clear();
        if (salleEtageCombo != null) salleEtageCombo.getSelectionModel().clearSelection();
    }

    private void fillSalleFormFromSalle(Salle salle) {
        salleTypeField.setText(salle.getTypeSalle());
        salleCapaciteField.setText(String.valueOf(salle.getCapacite()));
        salleNumEtageField.setText(String.valueOf(salle.getNumEtage()));
        sallePrixField.setText(String.valueOf(salle.getPrix()));
        salleDispoCheck.setSelected(salle.isDispo());
        refreshEtagesCombo();
        selectEtage(salleEtageCombo, salle.getEtage());
    }

    private Salle getSelectedSalle(String errorMessage) {
        Salle salle = tableSalles.getSelectionModel().getSelectedItem();
        if (salle == null) {
            showAlert("Erreur", errorMessage);
        }
        return salle;
    }

    private Reservation getSelectedReservation(String errorMessage) {
        Reservation reservation = tableReservations.getSelectionModel().getSelectedItem();
        if (reservation == null) {
            showAlert("Erreur", errorMessage);
        }
        return reservation;
    }

    private void selectEtage(ComboBox<Etage> combo, Etage selected) {
        if (selected == null) return;
        for (Etage e : combo.getItems()) {
            if (e != null && e.getId() == selected.getId()) {
                combo.getSelectionModel().select(e);
                return;
            }
        }
    }
        public void ValiderReservationRespos(){
//        if (tableReservations == null)
//            throw new RuntimeException("Le champs est vide");
//
//        List<Reservation> resultats = tableReservations.getItems().stream()
//                .peek(e -> {
//                    (e.setEtat(EtatReservation.valueOf("")););
//                }).collect(java.util.stream.Collectors.toList());
//        tableReservations.getItems().setAll(resultats);
    }

    private ComboBox<Etage> buildEtageCombo() {
        List<Etage> etages = etageDAO.getEtagesByRespo(respo.getId());
        ComboBox<Etage> etageCombo = new ComboBox<>();
        etageCombo.getItems().setAll(etages);

        etageCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Etage item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ("Etage #" + item.getId()));
            }
        });
        etageCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Etage item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : ("Etage #" + item.getId()));
            }
        });

        return etageCombo;
    }
}