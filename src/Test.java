import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by komar on 12/30/2016.
 */
public class Test {
    Stage stage;
    public Test(){
        stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public void show(){
        this.stage.show();
    }
}
