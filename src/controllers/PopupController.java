package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DatabaseUtil;
import utils.UserSession;

public class PopupController {

    @FXML private ChoiceBox<String> appoint_choice;
    @FXML private ChoiceBox<String> amorpm;
    @FXML private TextField timehour;
    @FXML private TextField timemin;
    @FXML private Button appoint_set;

    private int day;
    private int month;
    private int year;
    private CalendarController calendarController; 

  
    public void initPopup(int day, int month, int year, CalendarController calendarController) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.calendarController = calendarController;

        appoint_choice.getItems().addAll("Doctor", "Private Consultation", "Operation", "Dentist", "Family Event");
        amorpm.getItems().addAll("AM", "PM");

    
        appoint_choice.setValue("---");
        amorpm.setValue("AM");

       
        appoint_set.setOnAction(event -> handleSetAppointment());
    }
    
    private void handleSetAppointment() {
        String appointmentType = appoint_choice.getValue();
        String time = timehour.getText() + ":" + timemin.getText() + " " + amorpm.getValue();

        
        if (timehour.getText().isEmpty() || timemin.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid time.");
            return;
        }

      
        int userId = UserSession.getInstance().getUserId();

      
        boolean success = DatabaseUtil.saveAppointment(userId, day, month, year, appointmentType, time);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment set successfully!");

            
            calendarController.updateAppointmentList();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save the appointment. Please try again.");
        }

       
        Stage stage = (Stage) appoint_set.getScene().getWindow();
        stage.close();
    }

       

  
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}