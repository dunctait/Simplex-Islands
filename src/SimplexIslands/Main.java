package SimplexIslands;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "SimplexIslands.fxml"));
            Parent root = (Parent) loader.load();
            Controller ctrl = loader.getController();

            //Parent root = FXMLLoader.load(getClass().getResource("SimplexIslands.fxml"));
            primaryStage.setTitle("Simplex Islands");
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setScene(scene);
            //primaryStage.setResizable(false);
            primaryStage.setMinWidth(816);
            primaryStage.setMinHeight(639);

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
