package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import utils.Medicine;
import utils.MedicineDAO;
import utils.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MedTracker1Controller extends TransitionUtils implements Initializable {
    @FXML private HBox roothb;
    @FXML private StackPane return_btn;
	@FXML private VBox medscontainer; 
    @FXML private Label duetxt; 
    @FXML private Label TodayDate;
    @FXML private Button medadd_btn; 
    @FXML private Button viewhistory_btn ;
    private MedicineDAO medicineDAO = new MedicineDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        int userId = UserSession.getInstance().getUserId();
        if (userId != -1) {
            List<Medicine> medicines = medicineDAO.getMedicines(userId);
            displayMedicines(medicines);
        } else {
            duetxt.setText("No user is logged in.");
        }

        // Set today's date
        setTodaysDate();

        roothb.setOpacity(0);
        fadeInToScene(roothb);
        initializeSidebar(roothb);
		return_btn.setOnMouseClicked((e)->{
			fadeOutToScene(roothb, "Home");
		});
//		viewhistory_btn.setOnAction((e)->{
//			fadeOutToScene(roothb, "MedHistory");
//		});
		medadd_btn.setOnAction((e)->{
			fadeOutToScene(roothb, "MedicineAdd");
	
        });
    }

  
    private void displayMedicines(List<Medicine> medicines) {
        medscontainer.getChildren().clear(); // Clear existing content

        if (medicines.isEmpty()) {
            duetxt.setText("No medicines due today.");
            return;
        }

        GridPane medicineTable = new GridPane();
        medicineTable.setHgap(20); 
        medicineTable.setVgap(10);  
        medicineTable.setStyle("-fx-padding: 20;");

      
        Label serialHeader = new Label("No.");
        Label nameHeader = new Label("Medicine");
        Label typeHeader = new Label("Type");
        Label dosageHeader = new Label("Dosage");
        Label timeHeader = new Label("Time");
        Label statusHeader = new Label("Status");
        Label actionHeader = new Label("Action");

       
        String headerStyle = "-fx-font-size: 20px; -fx-font-family: 'Arial'; -fx-font-weight: bold;";
        serialHeader.setStyle(headerStyle);
        nameHeader.setStyle(headerStyle);
        typeHeader.setStyle(headerStyle);
        dosageHeader.setStyle(headerStyle);
        timeHeader.setStyle(headerStyle);
        statusHeader.setStyle(headerStyle);
        actionHeader.setStyle(headerStyle);

        
        medicineTable.addRow(0, serialHeader, nameHeader, typeHeader, dosageHeader, timeHeader, statusHeader, actionHeader);

        
        for (int i = 0; i < medicines.size(); i++) {
            Medicine medicine = medicines.get(i);
            int rowIndex = i + 1; 

          
            Label serialLabel = new Label(rowIndex + ".");
            serialLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

          
            Label nameLabel = new Label(medicine.getName());
            nameLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

            
            Label typeLabel = new Label(medicine.getType());
            typeLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

          
            Label dosageLabel = new Label(medicine.getDosageAmount() + " " + medicine.getDosageUnit());
            dosageLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

           
            Label timeLabel = new Label(medicine.getTime());
            timeLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");

            
            Label statusLabel = new Label(medicine.getStatus());
            statusLabel.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");
            if (medicine.getStatus().equalsIgnoreCase("taken")) {
                statusLabel.setTextFill(Color.GREEN); 
            } else {
                statusLabel.setTextFill(Color.BLACK); 
            }

          
            CheckBox takenCheckBox = new CheckBox("Taken");
            takenCheckBox.setSelected(medicine.getStatus().equalsIgnoreCase("taken"));
            takenCheckBox.setStyle("-fx-font-size: 20px; -fx-font-family: 'Arial';");
            takenCheckBox.setOnAction((e) -> {
                boolean isTaken = takenCheckBox.isSelected();
                updateMedicineStatus(medicine.getId(), isTaken ? "taken" : "pending");
                statusLabel.setText(isTaken ? "taken" : "pending");
                statusLabel.setTextFill(isTaken ? Color.GREEN : Color.BLACK); 
            });

            
            medicineTable.addRow(rowIndex, serialLabel, nameLabel, typeLabel, dosageLabel, timeLabel, statusLabel, takenCheckBox);
        }

       
        medscontainer.getChildren().add(medicineTable);
    }

   
    private void updateMedicineStatus(int medicineId, String status) {
        boolean isUpdated = medicineDAO.updateMedicineStatus(medicineId, status);
        if (!isUpdated) {
            System.err.println("Failed to update medicine status.");
        }
    }

   
    private void setTodaysDate() {
        java.time.LocalDate today = java.time.LocalDate.now();
        String dayOfWeek = today.getDayOfWeek().toString();
        String day = String.valueOf(today.getDayOfMonth());
        String month = today.getMonth().toString();
        TodayDate.setText(dayOfWeek + ", " + day + " " + month);
    }
}