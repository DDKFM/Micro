package de.ddkfm.application;
	
import de.ddkfm.models.Processor;
import de.ddkfm.util.MicroIIUtils;
import de.ddkfm.util.ThemeUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			MicroIIUtils.loadingLogger();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MicroII.fxml"));
			BorderPane root = (BorderPane)loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Micro 2 ");
			primaryStage.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
			primaryStage.show();
			primaryStage.setOnCloseRequest(e->{
				MicroIIController controller = loader.getController();
				controller.closeOtherStages();
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
