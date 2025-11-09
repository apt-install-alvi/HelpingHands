package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;
import utils.DatabaseConnection;
import utils.UserSession;

import application.Main;

public class RegistrationController extends TransitionUtils implements Initializable {

    @FXML private VBox rootvb;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private PasswordField password;
    @FXML private CheckBox showPassword;
    @FXML private TextField passwordVisible;
    @FXML private Button signup = new Button();

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rootvb.setOpacity(0);
        fadeInToScene(rootvb);
        signup.setOnAction(this::signUp);
    }

   
    @FXML
    public void togglePasswordVisibility() {
        if (showPassword.isSelected()) {
            passwordVisible.setText(password.getText());
            passwordVisible.setVisible(true);
            password.setVisible(false);
        } else {
            password.setText(passwordVisible.getText());
            password.setVisible(true);
            passwordVisible.setVisible(false);
        }
    }

  
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }


  
    private boolean isEMAILTaken(String email) {
        String sql = "SELECT COUNT(*) FROM 	user WHERE email = ?";


        try (Connection con = DatabaseConnection.connect();
             PreparedStatement statement = con.prepareStatement(sql)) {
            
            statement.setString(1, email);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

  
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @FXML
    public void signUp(ActionEvent e) {
        String userInput = username.getText().trim();
        String emailInput = email.getText().trim();
        String passInput = password.getText().trim();

        if (userInput.isEmpty() || emailInput.isEmpty() || passInput.isEmpty()) {
        	 showAlert(Alert.AlertType.ERROR,"Incomplete fields" ,"Username/Email and password are required!");
           
            return;
        }

        if (!isValidEmail(emailInput)) {
        	 showAlert(Alert.AlertType.ERROR,"Invalid email format!" ,"  Please enter a valid email.");
         
            return;
        }

        if (isEMAILTaken(userInput)) {
        	showAlert(Alert.AlertType.ERROR,"" ,"Email already exists! Please choose another.");
            
            return;
        }

        String hashedPassword = hashPassword(passInput);

        String sql = "INSERT INTO user (username, email, password) VALUES (?, ?, ?)";

        try (Connection con = DatabaseConnection.connect();
           
             PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, userInput);
            statement.setString(2, emailInput);
            statement.setString(3, hashedPassword);
            statement.executeUpdate();

          
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int userId = generatedKeys.getInt(1);

                  
                    UserSession.getInstance().setUserId(userId);
                    UserSession.getInstance().setUsername(userInput);
                    showAlert(Alert.AlertType.INFORMATION,"" ,"Successfully Created New Account");
                    
                    UserProfileController.setUserData(userInput, emailInput);

                    fadeOutToScene(rootvb, "HealthProfile");
                } else {
                	showAlert(Alert.AlertType.ERROR,"" ,"Failed to create account. Please try again");
                    
                    
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    /** Button Effects */
    @FXML
    public void signupButtonClick(MouseEvent e) {
        signup.setStyle("-fx-text-fill:#044dbb; -fx-background-color: #fff; -fx-border-color: #044dbb;");
    }

    @FXML
    public void signupButtonRelease(MouseEvent e) {
        signup.setStyle("-fx-text-fill:#fff; -fx-background-color: #044dbb;");
    }

    @FXML
    public void signupButtonHover(MouseEvent e) {
        signup.setStyle("-fx-background-color: #bcdcfa; -fx-text-fill:#044dbb;");
    }

    @FXML
    public void signupButtonExit(MouseEvent e) {
        signup.setStyle("-fx-background-color: #044dbb; -fx-text-fill:#fff;");
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
}
}
