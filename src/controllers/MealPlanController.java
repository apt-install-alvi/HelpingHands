package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class MealPlanController extends TransitionUtils implements Initializable {
	  	@FXML private HBox roothb;
	    @FXML private StackPane return_btn;
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			 roothb.setOpacity(0);
		        fadeInToScene(roothb);
		        initializeSidebar(roothb);
				return_btn.setOnMouseClicked((e)->{
					fadeOutToScene(roothb, "Home");
				});
		}
	    
}
