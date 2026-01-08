package reservation.reservation.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import jdk.jfr.Description;
import reservation.reservation.dao.ReservationDAO;
import reservation.reservation.model.*;
import reservation.reservation.service.ReservationService;
import reservation.reservation.util.SessionCon;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static reservation.reservation.util.SceneManager.switchScene;

public class HomeController {

    @FXML
    private Label userLabel;

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, EtatReservation> etatColumn;
    @FXML private TableColumn<Reservation, String> descriptionColumn;
    @FXML private TableColumn<Reservation, String> TypeSalleColumn;
    @FXML private TableColumn<Reservation, LocalDateTime> dateColumn;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final ReservationService reservationService = new ReservationService();
    private Client currentClient;

    @FXML
    public void initialize() {
        if (SessionCon.getUser() instanceof Client) {
            this.currentClient = (Client) SessionCon.getUser();

            if(currentClient != null) {
                userLabel.setText("Bienvenue " + currentClient.getNomComplet());
            }


            try {

                //TypeSalleColumn.setCellValueFactory(new PropertyValueFactory<>("TypeSalle"));
                descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));

                 TypeSalleColumn.setCellValueFactory(cellData ->
                         new SimpleStringProperty(
                                 cellData.getValue().getSalle() != null
                                         ? cellData.getValue().getSalle().getTypeSalle()
                                         : ""
                         )
                 );

                dateColumn.setCellValueFactory(new PropertyValueFactory<>("DateReservation"));
                etatColumn.setCellValueFactory(new PropertyValueFactory<>("Etat"));

                loadReservations();


            } catch (RuntimeException e) {
                throw new RuntimeException("hada howa erreur "+e.getMessage());
            }


        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun client connecté.");
        }
    }

    private void filterReservationsByEtat(EtatReservation etat) {
        List<Reservation> allReservations = getAllReservations();
        System.out.println(etat);// ta méthode DAO/service
        List<Reservation> filtered = allReservations.stream()
                .filter(r -> r.getEtat() == etat)
                .collect(Collectors.toList());

        reservationsTable.setItems(FXCollections.observableArrayList(filtered));
        System.out.println("Reservations trouvées : " + filtered.size());
    }

    public void showValidees(ActionEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.VALIDEE);
    }

    public void showAttente(ActionEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.EN_ATTENTE);
    }

    public void showRejetees(ActionEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.REFUSEE);
    }

    private List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationDAO.findByClientId(currentClient.getId());

        System.out.println("Id"+currentClient.getId());
        return  reservations;
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationDAO.findByClientId(currentClient.getId());
        System.out.println("Id"+currentClient.getId());
        System.out.println("Reservations trouvées : " + reservations.size());
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
    }

    private void handleCancel(Reservation reservation) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation d'annulation");
        confirmation.setHeaderText("Voulez-vous vraiment annuler cette réservation ?");
        confirmation.setContentText("Salle : " + reservation.getSalle().getTypeSalle() + "\nDate : " + reservation.getDateReservation());

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reservationService.cancelClientReservation(reservation.getId(), currentClient);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Votre réservation a été annulée.");
                loadReservations(); // Refresh the table
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    public void AddResrvation(ActionEvent actionEvent) {

        switchScene("/reservation/Views/client/Add_res.fxml", "Reservation");

    }
}