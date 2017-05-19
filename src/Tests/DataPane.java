package Tests;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

/**
 * Created by komar on 5/5/2017.
 */
public class DataPane extends TabPane {

    public DataPane(){
        super();
    }

    public void addTests(Test...tests){
        for(Test test : tests) {
            TableView<Series> table = test.getTableView();
            LineChart chart = test.getDataPane();
            for (Object s : chart.getData()) {
                Series current = (Series) s;
                TableColumn tableColumn = new TableColumn(current.getName());
                tableColumn.setPrefWidth(100);
                tableColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
                tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            }
            table.getItems().addAll(chart.getData());
            Tab tab = new Tab();
            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(chart);
            borderPane.setRight(table);
            tab.setContent(borderPane);
            tab.setText(test.getTitle());
            getTabs().add(tab);
        }
    }
}
