import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.XYChart.Data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;


/**
 * Created by komar on 12/18/2016.
 */
public class Test{
    HashMap<String, HashMap<Integer, Integer>> series;
    public Test(){
        series = new HashMap<>();
        HashMap<Integer, Integer> temp = new HashMap<>();
        for(int i =0; i<1000;i++){
            temp.put(i,i);
        }
        series.put("Series1", temp);
    }
}
