import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by komar on 12/20/2016.
 */
public class Testing  extends Application {

    public static void main(String[] args) {
        RecieveFromServer rfs = new RecieveFromServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.sizeToScene();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
