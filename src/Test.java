import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by komar on 12/18/2016.
 */
public class Test{
    HashMap<String, HashMap<Integer, Long>> series;
    public Test(ProgressForm progressForm){
        series = new HashMap<>();
        progressForm.activateProgressBar(task1);
        progressForm.getDialogStage().show();
        Thread thread = new Thread(task1);
        thread.run();
    }
    private Task<HashMap<String, HashMap<Integer, Long>>> task1 = new Task<HashMap<String, HashMap<Integer, Long>>>() {
        @Override
        public HashMap<String, HashMap<Integer, Long>> call() throws InterruptedException, IOException {
            int total = 100;
            updateProgress(0,total);
            HashMap<Integer, Long> temp = new HashMap<>();
            ArrayList<Integer> arr = new ArrayList<>();
            for(int i =0; i<total;i++){
                Long startTime = System.nanoTime();
                arr.add(i);
                Long endTime = System.nanoTime();
                Long totalTime = endTime-startTime;
                temp.put(i,totalTime);
                updateProgress(i,total);
            }
            series.put("Series1", temp);
            updateProgress(total,total);
            return series;
        }
    };
}
