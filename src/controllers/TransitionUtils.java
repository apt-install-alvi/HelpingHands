package controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TransitionUtils {
	
	@FXML private Button profile_btn= new Button(); 
	@FXML private Button home_btn= new Button(); 
	@FXML private Button med_btn= new Button(); 
	@FXML private Button meal_btn= new Button(); 
	@FXML private Button calendar_btn= new Button(); 
	@FXML private Button map_btn= new Button(); 
	@FXML private Button store_btn= new Button(); 
	@FXML private Button guide_btn= new Button(); 
	
	
	/*VBox*/
	public void initializeSidebar(VBox rootNode)
	{
		profile_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "UserProfile");
		});
		home_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Home");
		});
		calendar_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Calendar");
		});
		med_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "MedTracker1");
		});
		meal_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "MealPlan");
		});
		map_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Map");
		});
		store_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "StoreMain");
		});
		guide_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "ExerciseGuide");
		});
	}
	
	public void switchToScene(VBox rootNode, String scene_name)
	{
		Parent root;
		try 
		{
			root = FXMLLoader.load(getClass().getResource("/controllers/"+scene_name+".fxml"));
			Scene scene = new Scene(root);
			Stage stage= (Stage)rootNode.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void fadeOutToScene(VBox rootNode, String scene_name)
	{
		FadeTransition fade= new FadeTransition();
		fade.setDuration(Duration.millis(300));
		fade.setNode(rootNode);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setOnFinished((ActionEvent e)->
		{
			switchToScene(rootNode, scene_name);
		});
		fade.play();
	}
	
	public void fadeInToScene(VBox rootNode)
	{
		FadeTransition fade= new FadeTransition();
		fade.setDuration(Duration.millis(300));
		fade.setNode(rootNode);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.play();
	}
	
	
	/*Overload functions with hbox*/
	public void switchToScene(HBox rootNode, String scene_name)
	{
		Parent root;
		try 
		{
			root = FXMLLoader.load(getClass().getResource("/controllers/"+scene_name+".fxml"));
			Scene scene = new Scene(root);
			Stage stage= (Stage)rootNode.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void fadeOutToScene(HBox rootNode, String scene_name)
	{
		FadeTransition fade= new FadeTransition();
		fade.setDuration(Duration.millis(300));
		fade.setNode(rootNode);
		fade.setFromValue(1);
		fade.setToValue(0);
		fade.setOnFinished((ActionEvent e)->
		{
			switchToScene(rootNode, scene_name);
		});
		fade.play();
	}
	
	public void fadeInToScene(HBox rootNode)
	{
		FadeTransition fade= new FadeTransition();
		fade.setDuration(Duration.millis(300));
		fade.setNode(rootNode);
		fade.setFromValue(0);
		fade.setToValue(1);
		fade.play();
	}
	
	public void initializeSidebar(HBox rootNode)
	{
		profile_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "UserProfile");
		});
		home_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Home");
		});
		calendar_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Calendar");
		});
		med_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "MedTracker1");
		});
		meal_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "MealPlan");
		});
		map_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "Map");
		});
		store_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "StoreMain");
		});
		guide_btn.setOnAction((e)->{
			fadeOutToScene(rootNode, "ExerciseGuide");
		});
	}
}
