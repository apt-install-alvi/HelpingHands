package controllers;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
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

public class LoginController extends TransitionUtils implements Initializable {

    @FXML private VBox rootvb;
    @FXML private TextField usernameOrEmail;
    @FXML private PasswordField password;
    @FXML private CheckBox showPassword;
    @FXML private TextField passwordVisible;
    @FXML private Button login = new Button();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rootvb.setOpacity(0);
        fadeInToScene(rootvb);
        login.setOnAction(this::handleLogin);
    }

    /** Toggle password visibility */
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

    /** Handle Login */
    @FXML
    public void handleLogin(ActionEvent event) {
        String userInput = usernameOrEmail.getText().trim();
        String passInput = password.getText().trim();

        if (userInput.isEmpty() || passInput.isEmpty()) {
            
            showAlert(Alert.AlertType.ERROR,"Incomplete fields" ,"Username/Email and password are required!");
            return;
        }


        User user = authenticateUser(userInput, passInput);

        if (user != null) {
           
            UserProfileController.setUserData(user.getUsername(), user.getEmail());
            showAlert(Alert.AlertType.INFORMATION ,null,"Login Successful!!");
            
            fadeOutToScene(rootvb, "Home");
        } else {
        	showAlert(Alert.AlertType.ERROR,"Invalid Input" ,"Invalid username/email or password!");
           
        }
    }

    private User authenticateUser(String userInput, String passInput) {
        String sql = "SELECT id, username, email, password FROM user WHERE username = ? OR email = ?";
        try (Connection con = DatabaseConnection.connect();
             PreparedStatement statement = con.prepareStatement(sql)) {

            statement.setString(1, userInput);
            statement.setString(2, userInput);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    if (BCrypt.checkpw(passInput, dbPassword)) {
                        // Fetch user details
                        int userId = rs.getInt("id");
                        String username = rs.getString("username");
                        String email = rs.getString("email");

                        // Store user details in the session
                        UserSession.getInstance().setUserId(userId);
                        UserSession.getInstance().setUsername(username);

                        // Create User object to store fetched details
                        return new User(username, email);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  
    }

    /** Button Effects */
    @FXML
    public void loginButtonClick(MouseEvent e) {
        login.setStyle("-fx-text-fill:#044dbb; -fx-background-color: #fff; -fx-border-color: #044dbb;");
    }

    @FXML
    public void loginButtonRelease(MouseEvent e) {
        login.setStyle("-fx-text-fill:#fff; -fx-background-color: #044dbb;");
    }

    @FXML
    public void loginButtonHover(MouseEvent e) {
        login.setStyle("-fx-background-color: #bcdcfa; -fx-text-fill:#044dbb;");
    }

    @FXML
    public void loginButtonExit(MouseEvent e) {
        login.setStyle("-fx-background-color: #044dbb; -fx-text-fill:#fff;");
    }

    // Inner User class to store the user's details
    public static class User {
        private String username;
        private String email;

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
}
}
   
