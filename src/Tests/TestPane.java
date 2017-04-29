package Tests;

import javafx.beans.binding.Bindings;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Test> table = new TableView<>();


    public void addTests(Test... tests) {
        table.getItems().addAll(tests);
    }

    public TestPane() {
        super();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        MenuBar menuBar = new MenuBar();
        Menu control = new Menu("Control");
        MenuItem launch = new MenuItem("Launch Tasks");
        MenuItem stopAll = new MenuItem("Stop");
        control.getItems().addAll(launch, stopAll);
        menuBar.getMenus().addAll(control);

        TableColumn<Test, String> titleCol = new TableColumn("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(75);

        TableColumn<Test, String> statusCol = new TableColumn("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        statusCol.setPrefWidth(75);

        TableColumn<Test, Double> progressCol = new TableColumn("Progress");
        progressCol.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progressCol.setCellFactory(ProgressBarTableCell.forTableColumn());

        TableColumn<Test, String> messageCol = new TableColumn("Message");
        messageCol.setCellValueFactory(new PropertyValueFactory<>("message"));
        messageCol.setPrefWidth(75);
        messageCol.setSortable(false);

        table.getColumns().addAll(titleCol, statusCol, messageCol, progressCol);

        table.setRowFactory(tableView -> {
            final TableRow<Test> row = new TableRow<>();
            final ContextMenu contextMenu = new ContextMenu();
            final MenuItem removeMenuItem = new MenuItem("Remove");

            final MenuItem settings = new MenuItem("Settings");
            final MenuItem run = new MenuItem("Launch");
            final MenuItem stop = new MenuItem("Stop");
            removeMenuItem.setOnAction(event -> table.getItems().remove(row.getItem()));
            settings.setOnAction(e -> row.getItem().settings());
            run.setOnAction(e -> exec.execute(row.getItem()));
            stop.setOnAction(e -> row.getItem().cancel());
            contextMenu.getItems().add(removeMenuItem);
            contextMenu.getItems().add(settings);
            contextMenu.getItems().add(run);
            contextMenu.getItems().add(stop);
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            row.setOnMouseClicked(e ->{
                if(!row.isEmpty()) {
                    this.setCenter(row.getItem().getDataPane());
                }
            });
            return row;
        });

        this.setTop(menuBar);
        this.setLeft(table);
        this.setCenter(new LineChart(new NumberAxis(), new NumberAxis()));

        launch.setOnAction(e -> table.getItems().forEach(exec::execute));
        stopAll.setOnAction(e -> stop());
    }


    public void setExec() {
        exec = Executors.newFixedThreadPool(table.getItems().size(), r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public void stop() {
        exec.shutdownNow();
    }

}
