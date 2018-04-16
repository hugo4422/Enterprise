package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Book.BookDetailController;
import Book.BookListController;
import Model.Author;
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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
						try {
							selected.setOrigModified(Launcher.authorGateway.getAuthorLastModifiedById(selected.getId()));
						} catch (GatewayException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
	
}
