package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Book.AuditTrailController;
import Book.BookDetailController;
import Book.BookListController;
import Model.Author;
import Model.AuthorAuditTrail;
import Model.Book;
import Model.Publisher;
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
	@FXML private MenuItem authorList;
	@FXML private MenuItem addAuthor;
	@FXML private MenuItem bookList;
	@FXML private MenuItem addBook;
	@FXML private MenuItem quit;
	@FXML private Button bAuditTrail;
	@FXML private TextField tFirstName, tLastName, tDoB, tGender, tWebsite;
	List<Author> authors;
	List<Book> books;
	List<Publisher> publishers;
	private Author author;
	
	private LocalDateTime originalTimestamp;
	
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
		
		 if(event.getSource() == quit) {
			logger.error("Quiting");
			Platform.exit();
		}
		 else if(event.getSource() == authorList){
			logger.info("Author List Pressed");
			authors = Launcher.authorGateway.getAuthors();
			URL fxmlFile = this.getClass().getResource("AuthorList.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(new AuthorListController(authors));
			Parent view = loader.load();
			rootNode.setCenter(view);
		}
		 
		 else if(event.getSource() == addAuthor){
			logger.info("Add author was called");
			SingletonController.getInstance().setAuthor(new Author(0, "", "", "", "", ""));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
		 else if(event.getSource() == addBook) {
			 logger.info("Add Book was Pressed");
			 URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(new Book(0, null, null, 0, 0, null, null)));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		 }
		 else if(event.getSource() == bookList) {
			 logger.info("Book list clicked");
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			 books = Launcher.bookGateway.getBooks(0);
			 for(Book x : books) {
				 System.out.println(x);
			 }
			 loader.setController(new BookListController(books, 0));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		 }
	}
	
	@FXML private void handleButtonAction(ActionEvent event) throws Exception {
		
		if(event.getSource() == bSave) {
			Author oldAuthor = new Author(author.getId(), author.getFirstName(), author.getLastName(), author.getDob(), author.getGender(), author.getWebsite());
			LocalDateTime currentTimestamp = null;
			try {
	    		currentTimestamp = Launcher.authorGateway.getAuthorLastModifiedById(author.getId());
	    		if(!currentTimestamp.equals(author.getOrigModified())) {
	    			logger.error("Cannot Save! \nRecord has changed since this view loaded \nPlease refresh your view and try again. :(");
	    			System.out.println("Original Timestamp = " + author.getLastModified() + " \nCurrent Timestamp = " + currentTimestamp);
	    			return;
	    		}
	    	} catch(Exception e) {
	    		logger.error(e.getMessage());
	    	}
			
			//Launcher.authorGateway.updateAuthor(author);
			
			logger.info("Changes Saved");
			author.setFirstName(tFirstName.getText());
			author.setLastName(tLastName.getText());
			author.setDob(tDoB.getText());
			author.setGender(tGender.getText());
			author.setWebsite(tWebsite.getText());
			author.setOrigModified(currentTimestamp);
			logger.info("Save button pressed");
			
			 try {
				author.Save(oldAuthor, author);
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
		} else if(event.getSource() == bAuditTrail) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controllers/AuthorAuditTrail.fxml"));
			 loader.setController(new AuthorAuditTrail(author));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
		
	}
	
	public void initialize() throws SQLException {
		// TODO Auto-generated method stub
		//load field dataList<Publisher> publishers = new ArrayList<Publisher>();
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
}
