package Tests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.Callable;


/**
 * @author Nickolas Komarnitsky
 * Testing.Tests
 * 4/24/2017
 * 2:07 PM
 */
public class Test<Type> extends Task<Type>{
    private PrintWriter pw;
    private FileWriter fw;
    private Long start, end, total;
    private Pair<Integer, Integer> max_and_increment;
    private Callable function;
    private LineChart dataPane;
    private HashMap<String, Series> series = new HashMap<>();


    /**
     * Creates a new new default test with a max of 100 and increment of 1
     */
    public Test(){
        max_and_increment = new Pair<>(100,1);
        setup();
    }

    /**
     * Creates a new new default test with a max of 100 and increment of 1
     * @param title - Title of Test
     */
    public Test(String title){
        max_and_increment = new Pair<>(100,1);
        updateTitle(title);
        setup();
    }

    /**
     * Creates a new basic test with callable function in it and desired max and increment
     * @param callable - Callable method to test
     * @param max - Max Number of Tests
     * @param increment - Increment tests by ammount
     */
    public Test(Callable callable, int max, int increment){
        this.function = callable;
        max_and_increment = new Pair<>(max,increment);
        setup();
    }

    /**
     * Creates a new basic test with callable function in it and desired max and increment
     * @param title - Title of test
     * @param callable - Callable method to test
     * @param max - Max Number of Tests
     * @param increment - Increment tests by ammount
     */
    public Test(String title, Callable callable, int max, int increment){
        this.function = callable;
        max_and_increment = new Pair<>(max,increment);
        updateTitle(title);
        setup();
    }

    /**
     * Creates a new Basic test with callable function in it and defaul max and incremtn
     * @param callable - Callable method to test
     */
    public Test(Callable callable){
        this.function = callable;
        max_and_increment = new Pair<>(100,10);
    }

    /**
     * Creates a new Basic test with callable function in it and defaul max and increment
     * @param title - Title of test
     * @param callable - Callable method to test
     */
    public Test(String title, Callable callable){
        this.function = callable;
        max_and_increment = new Pair<>(100,10);
        updateTitle(title);
        setup();
    }

    /**
     * Creates new test with max and increment
     * @param title - Title of Test
     * @param max - Max Number of Tests
     * @param increment - Increment tests by ammount
     */
    public Test(String title, int max, int increment){
        max_and_increment = new Pair<>(max,increment);
        updateTitle(title);
        setup();
    }

    /**
     * Creates new test with max and increment
     * @param max - Max Number of Tests
     * @param increment - Increment tests by ammount
     */
    public Test(int max, int increment){
        max_and_increment = new Pair<>(max,increment);
        setup();
    }

    private void setup(){
        total = new Long(0);
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
        dataPane = new LineChart(new NumberAxis(), new NumberAxis());
        dataPane.titleProperty().bind(titleProperty());
        dataPane.setCreateSymbols(false);
    }

    /**
     * Opens setting window
     */
    public void settings(){
        EditPrompt editPrompt = new EditPrompt(getMax(),getIncrement());
        max_and_increment = editPrompt.showPrompt();
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
    }

    @Override
    protected void scheduled(){
        super.scheduled();
        updateProgress(ProgressIndicator.INDETERMINATE_PROGRESS, getMax());
    }

    @Override
    protected void running(){
        super.running();
        updateProgress(0,getMax());
        updateMessage("");
    }

    @Override
    protected void failed(){
        super.failed();
        updateMessage(getException().getMessage());
        getException().printStackTrace();
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        updateMessage("Cancled");
    }

    @Override
    protected Type call() throws Exception {
        for(int i = 0; i<getMax(); i+=getIncrement()){
            updateProgress(i);
            if(function != null) {
                start();
                function.call();
                end();
            }
            updateData(new Data(i,getTotal()),"Time");
        }
        return null;
    }

    @Override
    protected void succeeded(){
        super.succeeded();
        updateMessage("Done");
        updateProgress(getMax(),getMax());
        for(Series series : series.values()) {
                dataPane.getData().add(series);
        }
        if(pw != null) {
            pw.close();
        }
    }

    /**
     * Adds data to coresponding series
     * @param data - XYChart.Data containing the desired data
     * @param name - Title of the Series to add data to
     */
    public void updateData(Data data, String name){
        if(!series.containsKey(name)){
            series.put(name, new Series());
            series.get(name).setName(name);
        }else {
            series.get(name).getData().add(data);
        }
    }

    /**
     * updates progress for the current i based on the max
     * @param i - current i
     */
    public void updateProgress(int i){
        updateProgress(i,getMax());
        updateMessage("" + i+"/"+getMax());
    }

    /**
     * Sets a file writer for the test
     * @param filename - File to write test data to
     * @throws IOException
     */
    public void setWriter(String filename) throws IOException {
        fw = new FileWriter(filename);
        pw = new PrintWriter(fw);
    }

    /**
     * Sets start to current nanotime
     */
    public void start(){
        start = System.nanoTime();
    }

    /**
     * sets end to current nanotime and then add end -start to total
     */
    public void end(){
        end = System.nanoTime();
        total += (end-start);
    }

    /**
     * Prints inserted string to file set in setWriter()
     * @param string - String to write to file
     */
    public void println(String string){
        pw.println(string);
    }

    public Long getTotal() {
        return total;
    }

    public int getIncrement() {
        return max_and_increment.getValue();
    }

    public int getMax() {
        return max_and_increment.getKey();
    }

    public LineChart getDataPane() {
        return dataPane;
    }

    private static class EditPrompt extends Dialog<Pair<Integer, Integer>> {

        private Pair<Integer, Integer> values;
        public EditPrompt(int currentMax, int currentIncrement){
            setTitle("Settings");
            setHeaderText("Set Max and Increment");
            setResizable(true);

            values = new Pair<>(currentMax,currentIncrement);

            Label maxLabel = new Label("Max: ");
            Label incrementLabel = new Label("Increment: ");
            TextField maxInput = new TextField(""+currentMax);
            TextField incrementInput = new TextField(""+currentIncrement);

            GridPane root = new GridPane();
            root.add(maxLabel,0,0);
            root.add(maxInput,1,0);
            root.add(incrementLabel,0,1);
            root.add(incrementInput,1,1);

            getDialogPane().setContent(root);

            ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.APPLY);
            getDialogPane().getButtonTypes().add(submit);

            setResultConverter(param -> {
                if (param == submit) {
                    return new Pair(Integer.parseInt(maxInput.getText()), Integer.parseInt(incrementInput.getText()));
                }
                return null;
            });
        }

        public Pair<Integer, Integer> showPrompt(){

            Optional<Pair<Integer,Integer>> result = showAndWait();
            if(result.isPresent()){
                values = result.get();
            }
            return values;
        }
    }
}
