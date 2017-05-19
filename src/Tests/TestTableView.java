package Tests;

import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by komar on 5/3/2017.
 */
public class TestTableView extends VBox {
    private HashMap<String, Test> tests;
    private String[] styles = {"red-bar","orange-bar","yellow-bar","green-bar"};

    public TestTableView(){
        super();
        tests = new HashMap<>();
        getChildren().add(new Label("TESTS"));
    }

    public void addTests(Test...tests) {
        for (Test test : tests) {
            addTest(test);
        }
    }

    public void addTest(Test test){
        VBox vbox = new VBox();
        ProgressBar progressBar = new ProgressBar();
        Label title = new Label();
        Label message = new Label();
        Label status = new Label();
        progressBar .setPrefWidth(200);
        progressBar.progressProperty().addListener((observable, oldValue, newValue) -> {
            double progress = newValue == null ? 0 : newValue.doubleValue();
            getStyleClass().removeAll(styles);
            if (progress < 0.25) {
                setBarColor(progressBar, Color.RED);
            } else if (progress < 0.5) {
                setBarColor(progressBar, Color.ORANGE);
            } else if (progress < 0.75) {
                setBarColor(progressBar, Color.YELLOW);
            } else if (progress >= 1) {
                vbox.getChildren().remove(progressBar);
            }else {
                setBarColor(progressBar, Color.GREEN);
            }
        });
        progressBar.progressProperty().bind(test.progressProperty());
        title.textProperty().bind(test.titleProperty());
        message.textProperty().bind(test.messageProperty());
        status.textProperty().bind(test.stateProperty().asString());
        tests.put(test.getTitle(), test);
        vbox.getChildren().add(new HBox(title, new Label("-"), status));
        vbox.getChildren().add(progressBar);
        vbox.getChildren().add(message);
        getChildren().add(vbox);
    }

    public Collection<Test> getTests(){
        return tests.values();
    }

    private void setBarColor(ProgressBar bar, Color newColor) {
        bar.lookup(".bar").setStyle("-fx-background-color: -fx-box-border, " + createGradientAttributeValue(newColor));
    }

    private String createGradientAttributeValue(Color newColor) {
        String hsbAttribute = createHsbAttributeValue(newColor);
        return "linear-gradient(to bottom, derive(" + hsbAttribute+ ",30%) 5%, derive(" + hsbAttribute + ",-17%))";
    }
    private String createHsbAttributeValue(Color newColor) {
        return
                "hsb(" +
                        (int)  newColor.getHue()               + "," +
                        (int) (newColor.getSaturation() * 100) + "%," +
                        (int) (newColor.getBrightness() * 100) + "%)";
    }
}
