package Tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;



/**
 * @author Nickolas Komarnitsky
 * Testing.Tests
 * 4/24/2017
 * 2:07 PM
 */
public class Main extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        Timing timing = new Timing();
        timing.addTests(new Test<Void>(() -> {
            Thread.sleep(500);
            return null;
        },100,1));
        timing.setExec();
        Scene scene = new Scene(timing);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
