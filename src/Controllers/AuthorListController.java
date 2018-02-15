package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import Views.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import main.Launcher;

public class AuthorListController {
	
	@FXML private ListView<String> itemList;
	private BorderPane rootNode;
	@FXML private MenuItem aQuit;
	@FXML private MenuItem authorList;
	private static Logger logger = LogManager.getLogger(AuthorListController.class);
	
	public AuthorListController() {
		// TODO Auto-generated constructor stub
	}
	
	String temp = "";
	
	@FXML void handleListAction() throws IOException {
		String item = itemList.getSelectionModel().getSelectedItem();
		if(item == null) {
			return;
		} else if(item != temp) {
			temp = item;
		} else if(item == temp) {
			logger.trace("Author Double Clicked!");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
		
	}

	public void initialize() {
		// TODO Auto-generated method stub
		ObservableList<String> items = FXCollections.observableArrayList("Casey Bowden", "Hugo Chavez", "Bowden Casey");
		itemList.setItems(items);
	}
	
	public void setRootNode(BorderPane rootNode) {
		this.rootNode = rootNode;
	}
	
	@FXML void handleMenuEvent(ActionEvent event) throws IOException {
		
		 if(event.getSource() == aQuit) { 
			Platform.exit();
			}
	}
	
	
}
