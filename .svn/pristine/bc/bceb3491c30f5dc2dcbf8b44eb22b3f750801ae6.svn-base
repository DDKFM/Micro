package de.ddkfm.application;

import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Calendar;

import de.ddkfm.util.ThemeUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class fmAbout extends Stage{
	public fmAbout() {
		BorderPane aroot = new BorderPane();
	 	Scene ascene = new Scene(aroot);
	 	try {
			ascene.getStylesheets().add(ThemeUtils.getCSSFile().toExternalForm());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    this.setScene(ascene);
	    this.setTitle("\u00DCber Micro II");
	    this.getIcons().add(new Image(ThemeUtils.getResourcePart("symbol")));
	    ImageView imgView = new ImageView();
	    imgView.setImage(new Image(ThemeUtils.getResourcePart("Start")));
	    VBox mainbox = new VBox();
	    VBox textbox1 = new VBox(new Label("Copyright \u00A9 " + Calendar.getInstance().get(Calendar.YEAR)),
				   				 new Label("Maximilian Sch\u00E4dlich"),
				   				 new Label("Berufsakademie Sachsen"),
				   				 new Label("Staatliche Studienakademie Leipzig"),
				   				 new Label("CS16-1")
				   );
	    textbox1.setAlignment(Pos.CENTER);
	    
	    HBox upBox = new HBox(imgView,textbox1);
	    upBox.setSpacing(20);
	    VBox bottomBox = new VBox(new Label("Dieser Simulator basiert auf der Vorg\u00E4ngerversion MICRO.EXE der ETH Z\u00FCrich von 1995,"),
	    						  new Label("programmiert von Matthias M\u00FCller und Silvia Ackermann"),
	    						  new Label("An dieser Stelle bedanke ich mich bei Silvia Ackermann f\u00FCr die Information zum Programm."));
	    Button btLicense = new Button("MicroII-Lizenz");
	    	   btLicense.setOnAction(ea->{
	    		   try {
					Desktop.getDesktop().browse(new URI("http://www.gnu.org/licenses/gpl.html"));
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	   });
	    Button btRepository = new Button("Bitbucket-Repository");
	    btRepository.setOnAction(ea->{
    		   try {
				Desktop.getDesktop().browse(new URI("https://bitbucket.org/DDKFM/micro-ii"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    	   });
	    btRepository.setVisible(false);
	    HBox buttonBox = new HBox(btLicense,btRepository);
	    buttonBox.setSpacing(50);
	    mainbox.getChildren().addAll(upBox,bottomBox,buttonBox);
	    mainbox.setSpacing(30);
	    mainbox.setPadding(new Insets(10, 10, 10, 10));
	    mainbox.getStyleClass().add("helpform");
	    aroot.setCenter(mainbox);
	    this.show();
	}
}
