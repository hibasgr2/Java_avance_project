package reservation.reservation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import reservation.reservation.dao.ReservationDAO;
import reservation.reservation.model.Client;
import reservation.reservation.model.EtatReservation;
import reservation.reservation.model.Reservation;
import reservation.reservation.model.Salle;
import reservation.reservation.service.ReservationService;
import reservation.reservation.util.SessionCon;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MesReservationsController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Void> actionColumn;
    @FXML private TableColumn<Reservation, EtatReservation> etatColumn;
    @FXML private TableColumn<Reservation, Salle> salleColumn;
    @FXML private TableColumn<Reservation, LocalDateTime> dateColumn;

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final ReservationService reservationService = new ReservationService();
    private Client currentClient;

    @FXML
    public void initialize() {
        if (SessionCon.getUser() instanceof Client) {
            this.currentClient = (Client) SessionCon.getUser();

            setupActionColumn();
            setupEtatColumn(EtatReservation.ANNULEE);

            // Lier les colonnes aux propriétés
            salleColumn.setCellValueFactory(new PropertyValueFactory<>("salle"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));
            loadReservations();
//            // Charger toutes les réservations au début
//            reservationsTable.setItems(FXCollections.observableArrayList(getAllReservations()));

        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun client connecté.");
        }
    }

    private void filterReservationsByEtat(EtatReservation etat) {
        List<Reservation> allReservations = getAllReservations(); // ta méthode DAO/service
        List<Reservation> filtered = allReservations.stream()
                .filter(r -> r.getEtat() == etat)
                .collect(Collectors.toList());

        reservationsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    public void showValidees(MouseEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.VALIDEE);
    }

    public void showAttente(MouseEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.EN_ATTENTE);
    }

    public void showRejetees(MouseEvent mouseEvent) {
        filterReservationsByEtat(EtatReservation.REFUSEE);
    }

    private List<Reservation> getAllReservations() {
        List<Reservation> reservations = reservationDAO.findByClientId(currentClient.getId());
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
        return  reservations;
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationDAO.findByClientId(currentClient.getId());
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
    }

    private void setupActionColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button cancelButton = new Button("Annuler");

            {
                cancelButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
                cancelButton.setOnAction(event -> {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    handleCancel(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation reservation = getTableView().getItems().get(getIndex());
                    if (reservation.getEtat() == EtatReservation.EN_ATTENTE) {
                        setGraphic(cancelButton);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    public void setupEtatColumn(EtatReservation etatReservation) {
        etatColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(EtatReservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    switch (item) {
                        case VALIDEE:
                            setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                            break;
                        case EN_ATTENTE:
                            setStyle("-fx-text-fill: orange; -fx-font-weight: bold;");
                            break;
                        case ANNULEE:
                        case REFUSEE:
                            setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                            break;
                    }
                }
            }
        });
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



}
