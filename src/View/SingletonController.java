package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SingletonController implements Initializable {
	
	private static SingletonController controller;
	private BorderPane rootNode;
	@FXML private Button bList;
	@FXML private Button bQuit;
	@FXML private Button bBack;

	protected SingletonController() {
		// Exists only to defeat instantiation.
	}

	public static SingletonController getInstance() {
		if (controller == null) {
			controller = new SingletonController();
		}
		return controller;
	}
	
	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}

	@FXML void handleMenuEvent(ActionEvent event) throws IOException {
		if(event.getSource() == bList) {
			authorListView();
		} else if(event.getSource() == bBack){
			backToMainMenu();
		} else if(event.getSource() == bQuit) {
			System.exit(0);
		}
	}
	
	@FXML public void backToMainMenu() throws IOException {
		URL fxmlFile = this.getClass().getResource("Menu.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		loader.setController(SingletonController.getInstance());
		Parent view = loader.load();
		rootNode.setCenter(view);
	}
	
	@FXML public void authorListView() throws IOException {
		URL fxmlFile = this.getClass().getResource("AuthorList.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		loader.setController(SingletonController.getInstance());
		Parent view = loader.load();
		rootNode.setCenter(view);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
}