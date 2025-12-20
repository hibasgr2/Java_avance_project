package reservation.reservation.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath, String title) {
        try {
            var fxmlUrl = SceneManager.class.getResource(fxmlPath);
            if (fxmlUrl == null) {
                throw new RuntimeException("FXML introuvable : " + fxmlPath);
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


