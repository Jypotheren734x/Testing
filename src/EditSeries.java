import javafx.scene.Scene;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.swing.*;

/**
 * Created by komar on 12/26/2016.
 */
public class EditSeries {

    Stage stage;

    public EditSeries(Series series, Tab current){
        stage = new Stage();
        GridPane root = new GridPane();
        Label title = new Label(series.getName());
        ColorPicker color = new ColorPicker();
        Button apply = new Button("Apply");
        title.setOnMouseClicked(e ->{
            root.getChildren().remove(title);
            TextField temp = new TextField();
            temp.setPromptText(title.getText());
            root.add(temp,0,0);
            temp.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    title.setText(temp.getText());
                    root.getChildren().remove(temp);
                    root.add(title,0,0);
                }
            });
        });
        apply.setOnAction(e ->{
            if(promptYesNo("Apply Changes?")) {
                String hex = Integer.toHexString(color.getValue().hashCode()).substring(0, 6).toUpperCase();
                series.setName(title.getText());
                series.getNode().setStyle("-fx-stroke: #" + hex + ";");
                stage.close();
            }
        });
        root.add(title,0,0);
        root.add(color,0,1);
        root.add(apply,0,2);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
    }
    public static boolean promptYesNo(String message){
        Object[] options={ "yes", "no" };
        int foo=JOptionPane.showOptionDialog(null,
                message,
                "Warning",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        return foo==0;
    }

    public void show(){
        stage.show();
    }

}
