package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.msgServer.RBACPolicyAuth;

import Book.BookDetailController;
import Book.BookListController;
import Model.Author;
import Model.AuthorBook;
import Model.Book;
import Model.GatewayException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import main.Client;
import main.Launcher;

public class AuthorListController {
	
	@FXML private ListView<Author> itemList;
	private BorderPane rootNode;
	@FXML private MenuItem authorList;
	@FXML private MenuItem addAuthor;
	@FXML private MenuItem bookList;
	@FXML private MenuItem addBook;
	@FXML private MenuItem quit;
	private static Logger logger = LogManager.getLogger(AuthorListController.class);
	@FXML private Button authorDelete;
	private Author author;
	private List<Author> authors;
	private List<Book> books;
	private Client client;
	
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
		} else if(event.getSource() == quit) {
			logger.error("Quiting");
			Platform.exit();
		} else if(event.getSource() == addBook) {
			 logger.info("Add Book was Pressed");
			 URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(new Book(0, null, null, 0, 0, null, null)));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		 } else if(event.getSource() == bookList) {
			 logger.info("Book list clicked");
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			 books = Launcher.bookGateway.getBooks(0);
			 for(Book x : books) {
				 System.out.println(x);
			 }
			 loader.setController(new BookListController(books, 0));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		 } else if(event.getSource() == authorDelete) {
			 List<AuthorBook> authorBooks = null;
			 authorBooks = Launcher.bookGateway.getAuthorBooks();
			 for(AuthorBook x : authorBooks) {
				 System.out.println(x.getAuthor());
				 System.out.println(author);
				 if(author.toString().equals(x.getAuthor().toString())) {
					 System.out.println("Matched: " + x);
					 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					 alert.setTitle("Error");
					 alert.setHeaderText("Author has royalty to books. Cannot delete.");
					 alert.setResizable(false);
					 alert.showAndWait();
					 return;
				 }
			 }
			 if(author != null) {
				try {
					Launcher.authorGateway.deleteAuthor(author);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				authors = Launcher.authorGateway.getAuthors();
				FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorList.fxml"));
				loader.setController(new AuthorListController(authors));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			 }
		 }
		
	}

	public void initialize() {
		if(SingletonController.getInstance().getAuth().hasAccess(SingletonController.getInstance().getSessionId(), RBACPolicyAuth.CAN_ACCESS_CHOICE_2))
			authorDelete.setDisable(false);
		else
			authorDelete.setDisable(true);
		
		if(SingletonController.getInstance().getAuth().hasAccess(SingletonController.getInstance().getSessionId(), RBACPolicyAuth.CAN_ACCESS_CHOICE_1)) {
			addAuthor.setDisable(false);
			addBook.setDisable(false);
		} else {
			addAuthor.setDisable(true);
			addBook.setDisable(true);
		}
			
		ObservableList<Author> items = itemList.getItems();
		for(Author a : authors){
			items.add(a);
		}
		
		itemList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				author = itemList.getSelectionModel().getSelectedItem();
				if(click.getClickCount() == 2){
					//Use ListView's getSelected Item
					author = itemList.getSelectionModel().getSelectedItem();
					
					logger.info("double-clicked " + author);
					
					try{
						FXMLLoader loader = new FXMLLoader(getClass().getResource("AuthorDetailView.fxml"));
						try {
							author.setOrigModified(Launcher.authorGateway.getAuthorLastModifiedById(author.getId()));
						} catch (GatewayException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SingletonController.getInstance().setAuthor(author);
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
	
}
