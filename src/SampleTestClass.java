import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.HashMap;


/**
 * Created by komar on 12/18/2016.
 */
public class SampleTestClass {
    ProgressForm pForm = new ProgressForm();
    Label title = new Label("Sample Test Class");
    Button button1 = new Button("Sample Test");
    ObservableList<Button> elements = FXCollections.observableArrayList();
    HashMap<String, ObservableList<Data>> series = new HashMap<>();
    public SampleTestClass(){
        elements.addAll(button1);
        button1.setOnAction(e ->{
            pForm.activateProgressBar(task1);
            task1.setOnSucceeded(event -> {
                pForm.getDialogStage().close();
                DataForm dForm = new DataForm("Sample Test Class", "X-Axis", "Y-Axis", "Sample Test Class", series);
                dForm.show();
                button1.setDisable(false);
            });
            button1.setDisable(true);
            pForm.getDialogStage().show();

            Thread thread = new Thread(task1);
            thread.start();
        });
    }
    private Task<Void> task1 = new Task<Void>() {
        ObservableList<Data> data = FXCollections.observableArrayList();
        @Override
        public Void call() throws Exception {
            int progress = 0;
            int totalCount = 10;
            while(progress != totalCount){
                updateProgress(progress, totalCount);
                progress++;
                data.add(new Data(""+progress, progress));
            }
            series.put("Series1", data);
            updateProgress(totalCount, totalCount);
            return null;
        }
    };
}
