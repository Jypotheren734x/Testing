package Tests;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



/**
 * @author Nickolas Komarnitsky
 * Testing.Tests
 * 4/24/2017
 * 2:07 PM
 */
public class TestPane extends BorderPane{
    private ExecutorService exec;
    private TestTableView testTableView = new TestTableView();
    private DataPane dataPane = new DataPane();

    public void addTests(Test... tests) {
        testTableView.addTests(tests);
        dataPane.addTests(tests);
    }

    public TestPane() {
        super();
        MenuBar menuBar = new MenuBar();
        Menu control = new Menu("Control");
        MenuItem launch = new MenuItem("Launch Tasks");
        MenuItem stopAll = new MenuItem("Stop");
        control.getItems().addAll(launch, stopAll);
        menuBar.getMenus().addAll(control);
        getStylesheets().add("Tests/stylesheet.css");

        this.setTop(menuBar);
        this.setLeft(testTableView);
        this.setCenter(dataPane);

        launch.setOnAction(e -> testTableView.getTests().forEach(exec::execute));
        stopAll.setOnAction(e -> stop());
    }

    public void setExec() {
        exec = Executors.newFixedThreadPool(testTableView.getTests().size(), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void stop() {
        exec.shutdownNow();
    }

}
