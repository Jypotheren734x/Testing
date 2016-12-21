import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;
import java.util.stream.Stream;

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
        Button test1 = new Button("Test 1");
        root.add(test1,0,0);
        test1.setOnAction(e ->{
            try (Stream<String> stream = Files.lines(Paths.get("Output.txt"))){
                Object[] lines = stream.toArray();
                ObservableList<Data> data = FXCollections.observableArrayList();
                HashMap<String, ObservableList<Data>> map = new HashMap();
                int numberOfSeries = Integer.parseInt(lines[0].toString());
                int index = 1;
                DataForm dForm = null;
                for(int k = 0; k < numberOfSeries; k++){
                    String[] titles = lines[index].toString().split(" ");
                    index++;
                    for(int i = index; i < lines.length; i++){
                        if(lines[i].toString().matches("-----------------------")){
                            index = i;
                            break;
                        }
                        String[] line = lines[i].toString().split(" ");
                        String x = line[0];
                        String y = line[1];
                        Data d = new Data(x,Integer.parseInt(y));
                        data.add(d);
                    }
                    map.put(titles[4], data);
                    dForm = new DataForm(titles[0],titles[1],titles[2],titles[3],map);
                    dForm.show();
                }
            }catch(Exception exception){
                System.out.println(exception);
            }
        });
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.sizeToScene();
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
