package View;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;

public class AuthorListController {
	
	@FXML private ListView<String> itemList;
	
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
	}

	public void initialize() {
		// TODO Auto-generated method stub
		ObservableList<String> items = FXCollections.observableArrayList("Dummy One", "Dummy Two", "Dummy Three");
		itemList.setItems(items);
	}

}
