package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.Objects;
//import javafx.scene.layout.VBox;


public class Main extends Application {
	
	Parent root;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			root= FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/controllers/OnboardingScreen1.fxml")));

			Scene scene = new Scene(root, Color.WHITE);
//			scene.getStylesheets().add(getClass().getResource("/resources/css/onboarding.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/resources/icons/helpinghands_logo.png"))));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
