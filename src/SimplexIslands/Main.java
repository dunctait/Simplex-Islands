package SimplexIslands;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "SimplexIslands.fxml"));
            Parent root = loader.load();
            Controller ctrl = loader.getController();

            primaryStage.setTitle("Simplex Islands");
            Scene scene = new Scene(root, 800, 615);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            //primaryStage.setMinWidth(816);
            //primaryStage.setMinHeight(635);

            primaryStage.show();

            ctrl.setStage(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
