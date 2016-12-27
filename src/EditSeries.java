import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by komar on 12/26/2016.
 */
public class EditSeries {

    Stage stage;

    public EditSeries(Series series, Tab current){
        stage = new Stage();
        GridPane root = new GridPane();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
    }

    public void show(){
        stage.show();
    }

}
