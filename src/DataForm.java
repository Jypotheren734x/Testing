import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

public class DataForm {
	private CategoryAxis xAxis = new CategoryAxis();
	private NumberAxis yAxis = new NumberAxis();

	public LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

	public HBox hbox, table;
	public ScrollPane sp;


	@SuppressWarnings("unchecked")
	public DataForm(String title, String xAxisLabel, String yAxisLabel, String lineChartTitle, HashMap<String, ObservableList<Data>> data) {
		for(String s : data.keySet()){
			Series temp = new Series();
			temp.setName(s);
			for(Data d : data.get(s)) {
				temp.getData().add(d);
			}
			lineChart.getData().add(temp);
		}

		xAxis.setLabel(xAxisLabel);
		yAxis.setLabel(yAxisLabel);

		lineChart.setTitle(lineChartTitle);
		lineChart.setCreateSymbols(false);

		hbox = new HBox();
		sp = new ScrollPane();
		table = new HBox();

		HBox.setHgrow(lineChart, Priority.ALWAYS);
		hbox.getChildren().add(lineChart);
		for (ObservableList<XYChart.Data> l : data.values()) {
			VBox c1 = new VBox();
			VBox c2 = new VBox();
			c1.getChildren().add(new Label(xAxisLabel));
			c2.getChildren().add(new Label(yAxisLabel));
			for (int i = 0; i < l.size(); i++) {
				c1.getChildren().add(new Label(l.get(i).getXValue().toString()));
				c2.getChildren().add(new Label(l.get(i).getYValue().toString()));
			}
			table.getChildren().add(c1);
			table.getChildren().add(c2);
		}
		table.setSpacing(10);
		sp.setContent(table);
		hbox.getChildren().add(sp);
		sp.setHbarPolicy(ScrollBarPolicy.NEVER);
		sp.setFitToWidth(true);
	}
}