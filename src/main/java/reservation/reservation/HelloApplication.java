package reservation.reservation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import reservation.reservation.util.SceneManager;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(
//                getClass().getResource("/reservation/Views/connexion.fxml")
//        );
//        Scene scene = new Scene(loader.load());
//        stage.setTitle("Connexion");
//        stage.setScene(scene);
//        stage.show();

        SceneManager.setStage(stage);
        SceneManager.switchScene(
                "/reservation/Views/connexion.fxml",
                "Connexion"
        );

    }

    public static void main(String[] args) {
        launch(args); // 👈 démarre JavaFX
    }
}


