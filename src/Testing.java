import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * JAVA FX chart generator method Nickolas Komarnitsky u0717854
 *
 */
public class Testing extends Application {

	public static void main(String[] args) {launch(args);}

	@Override
	public void start(Stage primaryStage) throws Exception {
		SampleTestClass stc = new SampleTestClass();
		ListView<Button> root = new ListView<>(stc.elements);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
