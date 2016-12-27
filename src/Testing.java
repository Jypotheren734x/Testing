import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.util.HashMap;
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
        MenuItem SendToServer = new MenuItem("Send to Server");
        Menu series = new Menu("Series");
        open.setOnAction(e ->{
            try {
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
            }catch (Exception exception){
                JOptionPane.showMessageDialog(null, exception);
            }
        });
        SendToServer.setOnAction(e ->{
            SendToServer sts = new SendToServer();
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
        Menu axis = new Menu("Axis");
        MenuItem xAxis = new MenuItem("X-Axis");
        MenuItem yAxis = new MenuItem("Y-Axis");
        xAxis.setOnAction(e ->{
            EditAxis en = new EditAxis(dForm.lineChart.getXAxis());
            en.show();
        });
        yAxis.setOnAction(e ->{
            EditAxis en = new EditAxis(dForm.lineChart.getYAxis());
            en.show();
        });
        axis.getItems().addAll(xAxis,yAxis);
        graph.getItems().addAll(series, axis);
        subTable.getItems().add(hidetable);
        menuFile.getItems().addAll(open, SendToServer);
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
