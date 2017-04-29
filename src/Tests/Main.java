package Tests;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author Nickolas Komarnitsky
 * Testing.Tests
 * 4/26/2017
 * 2:45 AM
 */
public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        TestPane root = new TestPane();
        root.addTests(
                new Test("Test 1",() ->{
                    Thread.sleep(500);
                    return null;
                },100,1),
                new Test("Test 2"),
                new Test("Test 3",400,10)
        );
        root.setExec();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
