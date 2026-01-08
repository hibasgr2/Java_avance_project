package reservation.reservation.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import reservation.reservation.dao.SalleDAO;
import reservation.reservation.model.Salle;
import reservation.reservation.model.Utilisateur;
import reservation.reservation.service.AuthService;
import reservation.reservation.service.ReservationService;
import reservation.reservation.util.SessionCon;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static reservation.reservation.util.SceneManager.switchScene;

public class ReservationController {
    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker datePicker;

    private Utilisateur current = SessionCon.getUser();

    private final ReservationService resService = new ReservationService();

    private final SalleDAO salleDAO = new SalleDAO();

    @FXML
    private ComboBox<Salle> salleComboBox;


    @FXML
    private void initialize() {

        SalleDAO salleDAO = new SalleDAO();

        salleComboBox.setItems(
                FXCollections.observableArrayList(salleDAO.getAllSalles())
        );

        salleComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(Salle salle, boolean empty) {
                super.updateItem(salle, empty);
                setText(empty || salle == null
                        ? null
                        : salle.getTypeSalle() + " - " + salle.getCapacite() + " places");
            }
        });

        System.out.println("Salles chargées : " + salleComboBox.getItems().size());


        salleComboBox.setButtonCell(salleComboBox.getCellFactory().call(null));
    }



    @FXML
    private void cancelReservation() {
        descriptionField.clear();
        datePicker.setValue(null);
        switchScene("/reservation/Views/home.fxml", "Home");

    }

    @FXML
    private void addReservation() throws Exception {

        Salle salleSelectionnee = salleComboBox.getValue();

        if (salleSelectionnee != null) {
            System.out.println("Type : " + salleSelectionnee.getTypeSalle());
            System.out.println("Capacité : " + salleSelectionnee.getCapacite());
        }

        String desc = descriptionField.getText();
        LocalDateTime date = datePicker.getValue().atStartOfDay();

        if(desc.isEmpty() || date == null){
            System.out.println("Veuillez remplir tous les champs !");
            return;
        }

        System.out.println("Nouvelle réservation : " + desc + " le " + date);

        resService.createReservation(current,salleSelectionnee,date,desc);
    }

    public List<Salle> getSalles() {
        return salleDAO.getAllSalles();
    }
}
