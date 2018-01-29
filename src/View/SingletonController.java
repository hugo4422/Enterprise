package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class SingletonController implements Initializable {
	
	private static SingletonController controller;
	private BorderPane rootNode;
	//@FXML private MenuButton dropList;
	@FXML private Button bSave;
	@FXML private MenuItem aQuit;
	@FXML private MenuItem authorList;

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
		
		 if(event.getSource() == aQuit) { 
			Platform.exit();
			}
		 else if(event.getSource() == authorList){
			authorListView();
		}
		 else if(event.getSource() == bSave){
			 authorListView();
		 }
	}
	
	@FXML public void authorListView() throws IOException {
		URL fxmlFile = this.getClass().getResource("AuthorList.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		loader.setController(new AuthorListController());
		Parent view = loader.load();
		rootNode.setCenter(view);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}
}