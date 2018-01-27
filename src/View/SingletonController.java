package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SingletonController implements Initializable {
	
	private static SingletonController view;
	private BorderPane rootNode;
	@FXML private Button bList;
	@FXML private Button bQuit;
	@FXML private Button bBack;

	protected SingletonController() {
		// Exists only to defeat instantiation.
	}

	public static SingletonController getInstance() {
		if (view == null) {
			view = new SingletonController();
		}
		return view;
	}
	
	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}

	@FXML void handleMenuEvent(ActionEvent event) throws IOException {
		if(event.getSource() == bList) {
			System.out.println("Author List Clicked");
		}
		else if(event.getSource() == bQuit) {
			Platform.exit();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}