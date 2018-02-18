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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import main.Launcher;

public class SingletonController /*implements Initializable*/ {
	
	private static SingletonController controller;
	private BorderPane rootNode;

	@FXML private Button bSave;
	@FXML private MenuItem aQuit;
	@FXML private MenuItem authorList;
	@FXML private MenuItem addAuthor;
	@FXML private TextField tFirstName, tLastName, tDoB, tGender, tWebsite;
	List<Author> authors;
	private Author author;
	final static Logger logger = LogManager.getLogger(SingletonController.class);
	
	protected SingletonController() {
		// Exists only to defeat instantiation.
		author = null;
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
			logger.error("Quiting");
			Platform.exit();
		}
		 else if(event.getSource() == authorList){
			logger.warn("Author List Pressed");
			List<Author> authors = Launcher.authorGateway.getAuthors();
			URL fxmlFile = this.getClass().getResource("AuthorList.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(new AuthorListController(authors));
			Parent view = loader.load();
			rootNode.setCenter(view);
		}
		 
		 else if(event.getSource() == addAuthor){
			logger.error("Add author was called");
			SingletonController.getInstance().setAuthor(new Author(0, "", "", "", "", ""));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
	}
	
	@FXML private void handleButtonAction(ActionEvent event) throws Exception {
		
		if(event.getSource() == bSave) {
			author.setFirstName(tFirstName.getText());
			author.setLastName(tLastName.getText());
			author.setDob(tDoB.getText());
			author.setGender(tGender.getText());
			author.setWebsite(tWebsite.getText());
			logger.warn("Save button pressed");
			
			 try {
				author.Save(author);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<Author> authors = Launcher.authorGateway.getAuthors();
			URL fxmlFile = this.getClass().getResource("AuthorList.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(new AuthorListController(authors));
			Parent view = loader.load();
			rootNode.setCenter(view);
		} 
		
	}
	
	public void initialize() {
		// TODO Auto-generated method stub
		//load field data
		if(this.author != null) {
			tFirstName.setText(author.getFirstName());
			tLastName.setText(author.getLastName());
			tDoB.setText(author.getDob());
			tGender.setText(author.getGender());
			tWebsite.setText(author.getWebsite());
		}
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	/*@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				/*logger.error("Detailed View init has been called");
				//load field data
				tFirstName.setText(author.getFirstName());
				tLastName.setText(author.getLastName());
				tDoB.setText(author.getDob());
				tGender.setText(author.getGender());
				tWebsite.setText(author.getWebsite());*/
	//}
}
