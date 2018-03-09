package Controllers;

import java.io.IOException; 
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Model.Author;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import main.Launcher;

public class AuthorListController {
	
	@FXML private ListView<Author> itemList;
	private BorderPane rootNode;
	@FXML private MenuItem aQuit;
	@FXML private MenuItem addAuthor;
	private static Logger logger = LogManager.getLogger(AuthorListController.class);
	@FXML private Button authorDelete;
	private Author author;
	private List<Author> authors;
	
	public AuthorListController(List<Author> authors) {
		// TODO Auto-generated constructor stub
		this.authors = authors;
	}
	
	Author temp = null;
	
	@FXML void handleListAction(ActionEvent event) throws IOException {
		Object source = event.getSource();
		//Press delete then select author to be deleted
		if(source == addAuthor) {
			logger.info("Add author was called");
			SingletonController.getInstance().setAuthor(new Author(0, "", "", "", "", ""));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
		
		itemList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click){
				if(click.getClickCount() == 1){
					//Use ListView's getSelected Item
					
					
					try{
						if(source == authorDelete){
							Author selected = itemList.getSelectionModel().getSelectedItem();
							Launcher.authorGateway.deleteAuthor(selected);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		
	}

	public void initialize(){
		ObservableList<Author> items = itemList.getItems();
		for(Author a : authors){
			items.add(a);
		}
		
		itemList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				if(click.getClickCount() == 2){
					//Use ListView's getSelected Item
					Author selected = itemList.getSelectionModel().getSelectedItem();
					
					logger.info("double-clicked " + selected);
					
					try{
						FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
						SingletonController.getInstance().setAuthor(selected);
						loader.setController(SingletonController.getInstance());
						Parent view = loader.load();
						//attach view to application center of border pane
						Launcher.rootNode.setCenter(view);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
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
