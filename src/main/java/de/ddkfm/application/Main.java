package de.ddkfm.application;
	
import de.ddkfm.models.Processor;
import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.ThemeUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MicroIIUtils.loadingLogger();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MicroII.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Micro 2 ");
			primaryStage.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
			primaryStage.show();
			primaryStage.setOnCloseRequest(e->{
				MicroIIController controller = loader.getController();
				controller.closeOtherStages();
			});
			primaryStage.widthProperty().addListener( (observable, oldValue, newValue) -> {
				double scaleX = newValue.doubleValue() / oldValue.doubleValue();
				Scale scale = new Scale();
				scale.setPivotX(0);
				scale.setPivotY(0);
				scale.setX(scaleX);
				root.getCenter().getTransforms().add(scale);
			});
			primaryStage.heightProperty().addListener( (observable, oldValue, newValue) -> {
				double scaleY = newValue.doubleValue() / oldValue.doubleValue();
				Scale scale = new Scale();
				scale.setPivotX(0);
				scale.setPivotY(0);
				scale.setY(scaleY);
				root.getCenter().getTransforms().add(scale);
			});

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
