package Tests;

import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * @Author Nickolas Komarnitsky
 */
public class Test<Type> extends Task<Type>{
    private PrintWriter pw;
    private FileWriter fw;
    private Long start, end, total;
    private Pair<Integer, Integer> max_and_increment;
    private Callable function;

    /**
     * Creates a new new default test with a max of 100 and increment of 1
     */
    public Test(){
        max_and_increment = new Pair<>(100,1);
        total = new Long(0);
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
    }

    /**
     *
     * @param callable
     * @param max
     * @param increment
     */
    public Test(Callable callable, int max, int increment){
        this.function = callable;
        max_and_increment = new Pair<>(max,increment);
        total = new Long(0);
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
    }


    public Test(Callable callable){
        this.function = callable;
        max_and_increment = new Pair<>(100,10);
        total = new Long(0);
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
    }

    public Test(int max, int increment){
        max_and_increment = new Pair<>(max,increment);
        total = new Long(0);
        Button button = new Button("Settings");
        button.setOnAction(e ->{
            EditPrompt editPrompt = new EditPrompt(getMax(),getIncrement());
            Pair<Integer, Integer> temp = editPrompt.showPrompt();
            if(temp != max_and_increment) {
                max_and_increment = temp;
                updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
            }
        });
        updateMessage("Max: " + getMax() + "\nIncrement: " + getIncrement());
    }

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
            function.call();
        }
        return null;
    }

    @Override
    protected void succeeded(){
        super.succeeded();
        updateMessage("Done");
        updateProgress(getMax(),getMax());
        if(pw != null) {
            pw.close();
        }
    }

    public void updateProgress(int i){
        updateProgress(i,getMax());
        updateMessage("" + i+"/"+getMax());
    }

    public void setWriter(String filename) throws IOException {
        fw = new FileWriter(filename);
        pw = new PrintWriter(fw);
    }

    public void start(){
        start = System.nanoTime();
    }

    public void end(){
        end = System.nanoTime();
        total += (end-start);
    }

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

    private static class Control extends HBox{
        Button settings, launch, stop;
        public Control(){
            settings = new Button("Settings");
            launch = new Button("Run");
            stop = new Button("Stop");
        }
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
