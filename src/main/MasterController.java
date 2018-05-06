package main;

import java.io.IOException;
import java.net.URL;

import javax.security.auth.login.LoginException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.msgServer.*;

import Controllers.SingletonController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class MasterController {
	private final Logger logger = LogManager.getLogger(MasterController.class);

	//MenuController is the app-level controller for this program
	//so it will have an authenticator to use for logging in
	private Authenticator auth;
	
	//user's local session id
	//session id should not normally be an int (too easy to guess other session ids) 
	int sessionId;
	
	@FXML private Text textLogin, textSessionId;
	@FXML private MenuBar menuBar;
	@FXML private MenuItem loginChoice, logoutChoice, exitChoice;
	@FXML private BorderPane rootPane;
	
	@FXML private void handleMenuAction(ActionEvent event) throws IOException, LoginException {
		//provideAboutFunctionality();
		if(event.getSource() == loginChoice) {
			doLogin();
		} else if(event.getSource() == logoutChoice) {
			doLogout();
		} else if(event.getSource() == exitChoice) {
			Platform.exit();
		}
	}
	
	public MasterController() {
		//create an authenticator
		auth = new AuthenticatorRemote();
		SingletonController.getInstance().setAuth(auth);
		//default to no session
		sessionId = Authenticator.INVALID_SESSION;
	}

	private void doLogout() throws IOException {
		sessionId = Authenticator.INVALID_SESSION;
		Launcher.rootNode.getChildren().clear();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		loader.setController(new MasterController());
		Launcher.rootNode = loader.load();
		
		Scene scene = new Scene(Launcher.rootNode, 600, 400);
		Launcher.primaryStage.setTitle("Menu");
		Launcher.primaryStage.setScene(scene);
		Launcher.primaryStage.show();
		auth.logout(sessionId);
		//restrict access to GUI controls based on current login session
		updateGUIAccess();
	}
	
	private void doLogin() throws javax.security.auth.login.LoginException, IOException {
		//display login modal dialog. get login (username) and password
		//key is login, value is pw
		Pair<String, String> creds = LoginDialog.showLoginDialog();
		if(creds == null) //canceled
			return;
		
		String userName = creds.getKey();
		String pw = creds.getValue();
		
		logger.info("userName is " + userName + ", password is " + pw);
		
		//hash password
		String pwHash = CryptoStuff.sha256(pw);
		
		logger.info("sha256 hash of password is " + pwHash);
		
		//if get session id back, then replace current session
		sessionId = auth.loginSha256(userName, pwHash);
		
		logger.info("session id is " + sessionId);
		
		//restrict access to GUI controls based on current login session
		updateGUIAccess();
		SingletonController controller = SingletonController.getInstance();
		controller.setSessionId(sessionId);
		controller.setRootNode((BorderPane) Launcher.rootNode);
		
		URL fxmlFile = this.getClass().getResource("Menu.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		Parent view = loader.load();
		Launcher.rootNode.setCenter(view);
	}
	
	private void updateGUIAccess() {
		//if logged in, login should be disabled
		if(sessionId == Authenticator.INVALID_SESSION)
			loginChoice.setDisable(false);
		else
			loginChoice.setDisable(true);
		
		//if not logged in, logout should be disabled
		if(sessionId == Authenticator.INVALID_SESSION)
			logoutChoice.setDisable(true);
		else
			logoutChoice.setDisable(false);
		
		//update fxml labels
		textLogin.setText(auth.getUserNameFromSessionId(sessionId));
		textSessionId.setText("Session id " + sessionId);
	}
	
	public void initialize() {
		menuBar.setFocusTraversable(true);
		
		//restrict access to the GUI based on current session id (should be invalid session)
		updateGUIAccess();

	}
}
