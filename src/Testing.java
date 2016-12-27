import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Label;

import java.awt.*;

import javax.swing.plaf.FileChooserUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by komar on 12/20/2016.
 */
public class Testing  extends Application {

    private int Rows = 0;

    DataForm dForm = null;
    public VBox root = new VBox();
    TabPane tabPane = new TabPane();
    FileChooser chooser = new FileChooser();

    public static void main(String[] args) {
        RecieveFromServer rfs = new RecieveFromServer();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem open = new MenuItem("Open");
        Menu series = new Menu("Series");
        open.setOnAction(e ->{
            File file = chooser.showOpenDialog(null);
            Tab tab = new Tab(file.getName());
            BuildGraph(file, tab);
            for(XYChart.Series s : dForm.series){
                MenuItem ser = new MenuItem(s.getName());
                ser.setOnAction(e_->{
                    EditSeries es = new EditSeries(s, tabPane.getSelectionModel().getSelectedItem());
                    es.show();
                });
                series.getItems().add(ser);
            }
        });
        Menu menuView = new Menu("View");
        Menu subTable = new Menu("Table");
        MenuItem hidetable = new MenuItem("Hide");
        MenuItem showtable = new MenuItem("Show");
        hidetable.setOnAction(e ->{
            dForm.hbox.getChildren().remove(dForm.sp);
            subTable.getItems().remove(hidetable);
            subTable.getItems().add(showtable);
        });
        showtable.setOnAction(e ->{
            dForm.hbox.getChildren().add(dForm.sp);
            subTable.getItems().remove(showtable);
            subTable.getItems().add(hidetable);
        });
        Menu graph = new Menu("Graph");
        graph.getItems().addAll(series);
        subTable.getItems().add(hidetable);
        menuFile.getItems().addAll(open);
        menuView.getItems().add(subTable);
        menuBar.getMenus().addAll(menuFile, menuView, graph);
        root.getChildren().add(menuBar);
        root.getChildren().add(tabPane);
        root.autosize();
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("stylesheet.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private void BuildGraph(File file, Tab tab){
        try (Stream<String> stream = Files.lines(file.toPath())) {
            Object[] lines = stream.toArray();
            ObservableList<Data> data = FXCollections.observableArrayList();
            HashMap<String, ObservableList<Data>> map = new HashMap();
            int numberOfSeries = Integer.parseInt(lines[0].toString());
            int index = 1;
            for (int k = 0; k < numberOfSeries; k++) {
                String[] titles = lines[index].toString().split(" ");
                index++;
                for (int i = index; i < lines.length; i++) {
                    if (lines[i].toString().matches("-----------------------")) {
                        index = i;
                        break;
                    }
                    String[] line = lines[i].toString().split(" ");
                    String x = line[0];
                    String y = line[1];
                    Data d = new Data(x, Integer.parseInt(y));
                    data.add(d);
                }
                map.put(titles[4], data);
                dForm = new DataForm(titles[0], titles[1], titles[2], titles[3], map);
                tab.setContent(dForm.hbox);
                tabPane.getTabs().add(tab);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }
}
