import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by komar on 12/27/2016.
 */
public class EditAxis {
    private Stage stage;
    public EditAxis(Axis axis){
        stage = new Stage();
        GridPane root = new GridPane();
        Label name = new Label(axis.getLabel());
        name.setOnMouseClicked(e ->{
            root.getChildren().remove(name);
            TextField temp = new TextField();
            temp.setPromptText(name.getText());
            root.add(temp,0,0);
            temp.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    name.setText(temp.getText());
                    root.getChildren().remove(temp);
                    root.add(name,0,0);
                }
            });
        });
        root.add(name,0,0);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public void show(){
        stage.show();
    }
}
