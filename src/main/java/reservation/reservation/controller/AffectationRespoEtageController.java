package reservation.reservation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import reservation.reservation.dao.EtageDAO;
import reservation.reservation.dao.ImmeubleDAO;
import reservation.reservation.dao.UtilisateurDAO;
import reservation.reservation.model.*;
import reservation.reservation.util.SceneManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AffectationRespoEtageController implements Initializable {

    @FXML private ComboBox<Immeuble> immeubleComboBox;
    @FXML private ComboBox<Etage> etageComboBox;
    @FXML private ComboBox<Respo> respoComboBox;
    @FXML private Label respoActuelLabel;
    @FXML private TableView<AffectationDTO> affectationTable;

    private ImmeubleDAO immeubleDAO = new ImmeubleDAO();
    private EtageDAO etageDAO = new EtageDAO();
    private UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chargerDonnees();
        configurerCellFactories();  // <-- AJOUTÉE ICI
        configurerListeners();
        chargerAffectations();
    }

    // MÉTHODE AJOUTÉE POUR L'AFFICHAGE DES COMBOBOX
    private void configurerCellFactories() {
        // 1. Pour la ComboBox des Immeubles
        immeubleComboBox.setCellFactory(param -> new ListCell<Immeuble>() {
            @Override
            protected void updateItem(Immeuble immeuble, boolean empty) {
                super.updateItem(immeuble, empty);
                if (empty || immeuble == null) {
                    setText(null);
                } else {
                    setText("Immeuble " + immeuble.getId() + " - " + immeuble.getAdresse());
                }
            }
        });

        immeubleComboBox.setButtonCell(new ListCell<Immeuble>() {
            @Override
            protected void updateItem(Immeuble immeuble, boolean empty) {
                super.updateItem(immeuble, empty);
                if (empty || immeuble == null) {
                    setText("Sélectionner un immeuble");
                } else {
                    setText("Immeuble " + immeuble.getId() + " - " + immeuble.getAdresse());
                }
            }
        });

        // 2. Pour la ComboBox des Étages
        etageComboBox.setCellFactory(param -> new ListCell<Etage>() {
            @Override
            protected void updateItem(Etage etage, boolean empty) {
                super.updateItem(etage, empty);
                if (empty || etage == null) {
                    setText(null);
                    System.out.println(etage);
                } else {
                    System.out.println(etage);
                    setText("Étage " + etage.getId());
                }
            }
        });

        etageComboBox.setButtonCell(new ListCell<Etage>() {
            @Override
            protected void updateItem(Etage etage, boolean empty) {
                super.updateItem(etage, empty);
                if (empty || etage == null) {
                    setText("Sélectionner un étage");
                } else {
                    setText("Étage " + etage.getId());
                }
            }
        });

        // 3. Pour la ComboBox des Responsables
        respoComboBox.setCellFactory(param -> new ListCell<Respo>() {
            @Override
            protected void updateItem(Respo respo, boolean empty) {
                super.updateItem(respo, empty);
                if (empty || respo == null) {
                    setText(null);
                } else {
                    setText(respo.getNomComplet() + " (" + respo.getEmail() + ")");
                }
            }
        });

        respoComboBox.setButtonCell(new ListCell<Respo>() {
            @Override
            protected void updateItem(Respo respo, boolean empty) {
                super.updateItem(respo, empty);
                if (empty || respo == null) {
                    setText("Sélectionner un responsable");
                } else {
                    setText(respo.getNomComplet() + " (" + respo.getEmail() + ")");
                }
            }
        });
    }

    private void chargerDonnees() {
        // Charger les immeubles
        List<Immeuble> immeubles = immeubleDAO.getAll();
        immeubleComboBox.setItems(FXCollections.observableArrayList(immeubles));

        // Charger les responsables actifs
        List<Respo> responsables = utilisateurDAO.getAll().stream()
                .filter(u -> u.getRole() == Role.RESPO)
                .map(u -> (Respo) u)
                .filter(Respo::isActif)
                .collect(Collectors.toList());
        respoComboBox.setItems(FXCollections.observableArrayList(responsables));

        // Configurer la table
        configurerTable();
    }

    private void configurerTable() {
        TableColumn<AffectationDTO, String> immeubleCol = (TableColumn<AffectationDTO, String>) affectationTable.getColumns().get(0);
        TableColumn<AffectationDTO, String> etageCol = (TableColumn<AffectationDTO, String>) affectationTable.getColumns().get(0);
        TableColumn<AffectationDTO, String> respoCol = (TableColumn<AffectationDTO, String>) affectationTable.getColumns().get(2);

        immeubleCol.setCellValueFactory(new PropertyValueFactory<>("immeubleAdresse"));
        etageCol.setCellValueFactory(new PropertyValueFactory<>("etageId"));
        respoCol.setCellValueFactory(new PropertyValueFactory<>("respoNom"));
    }

    private void configurerListeners() {
        // Quand un immeuble est sélectionné, charger ses étages
        immeubleComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                chargerEtagesDeImmeuble(newVal);
                viderEtageSelection();
            }
        });

        // Quand un étage est sélectionné, afficher son responsable actuel
        etageComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                afficherRespoActuel(newVal);
            } else {
                respoActuelLabel.setText("Aucun");
            }
        });
    }

    private void chargerEtagesDeImmeuble(Immeuble immeuble) {
        System.out.println("=== DEBUG: Chargement étages pour immeuble ===");
        System.out.println("Immeuble ID: " + immeuble.getId());
        System.out.println("Immeuble Adresse: " + immeuble.getAdresse());

        List<Etage> etages = etageDAO.getByImmeubleId(immeuble.getId());

        System.out.println("Nombre d'étages récupérés: " + etages.size());
        for (Etage etage : etages) {
            System.out.println("  - Étage ID: " + etage.getId());
        }

        etageComboBox.setItems(FXCollections.observableArrayList(etages));
        etageComboBox.getSelectionModel().clearSelection();

        // DEBUG: Vérifiez ce qui est dans la ComboBox
        System.out.println("Items dans ComboBox Étages: " + etageComboBox.getItems().size());
    }

    private void viderEtageSelection() {
        etageComboBox.getSelectionModel().clearSelection();
        respoActuelLabel.setText("Aucun");
    }

    private void afficherRespoActuel(Etage etage) {
        if (etage.getRespo() != null) {
            respoActuelLabel.setText(etage.getRespo().getNomComplet() +
                    " (" + etage.getRespo().getEmail() + ")");
        } else {
            respoActuelLabel.setText("Aucun responsable affecté");
        }
    }

    private void chargerAffectations() {
        List<Etage> etagesAvecRespo = etageDAO.getAllWithRespo();
        ObservableList<AffectationDTO> affectations = FXCollections.observableArrayList();

        for (Etage etage : etagesAvecRespo) {
            if (etage.getRespo() != null) {
                String immeubleAdresse = etage.getImmeuble() != null ?
                        etage.getImmeuble().getAdresse() : "N/A";
                String respoNom = etage.getRespo().getNomComplet();

                affectations.add(new AffectationDTO(
                        immeubleAdresse,
                        etage.getId(),
                        respoNom
                ));
            }
        }

        affectationTable.setItems(affectations);
    }

    @FXML
    private void affecterRespoAetage() {
        try {
            Etage etage = etageComboBox.getValue();
            Respo respo = respoComboBox.getValue();

            if (etage == null || respo == null) {
                showAlert(Alert.AlertType.WARNING,
                        "Sélection incomplète",
                        "Veuillez sélectionner un étage et un responsable.");
                return;
            }

            // Mettre à jour l'étage avec le nouveau responsable
            etage.setRespo(respo);
            etageDAO.update(etage);

            // Mettre à jour l'affichage
            afficherRespoActuel(etage);
            chargerAffectations();

            showAlert(Alert.AlertType.INFORMATION,
                    "Succès",
                    "Le responsable " + respo.getNomComplet() +
                            " a été affecté à l'étage " + etage.getId());

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR,
                    "Erreur",
                    "Erreur lors de l'affectation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void annuler() {
        Stage stage = (Stage) immeubleComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}