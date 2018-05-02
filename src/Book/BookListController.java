package Book;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controllers.AuthorListController;
import Controllers.SingletonController;
import Model.Author;
import Model.Book;
import Model.Publisher;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Launcher;

public class BookListController {
	@FXML TextField tfBookSearch;
	@FXML Button bSearch;
	@FXML private MenuItem authorList;
	@FXML private MenuItem addAuthor;
	@FXML private MenuItem addBook;
	@FXML private MenuItem quit;
	@FXML ListView<Book> bookList;
	@FXML private Button bNext;
	@FXML private Button bPrev;
	@FXML private Button bFirst;
	@FXML private Button bLast;
	@FXML private Button bDelete;
	@FXML private Label bookLabel;
	private List<Author> authors;
	private List<Book> books;
	private int page, numPages, numBooks;
	
	private Book selected;
	
	final static Logger logger = LogManager.getLogger(SingletonController.class);
	
	public BookListController(List<Book> books, int page) {
		this.books = books;
		this.page = page;
		this.numBooks = Launcher.bookGateway.getNumBooks();
		this.numPages = numBooks/50;
		selected = null;
	}
	
	@FXML
	private void handleSearchClick(ActionEvent event) throws Exception {
		if(event.getSource() == bSearch) {
			if(bSearch.getText().isEmpty()) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
				books = Launcher.bookGateway.getBooks(0);
				loader.setController(new BookListController(books, 0));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			} else {
				books = Launcher.bookGateway.searchBook(tfBookSearch.getText());
				logger.info("Book list clicked");
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
				loader.setController(new BookListController(books, 0));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			}
		}
	}
	
	@FXML void handleListAction(ActionEvent event) throws IOException {
		Object source = event.getSource();
		//Press delete then select author to be deleted
		if(source == addAuthor) {
			logger.info("Add author was called");
			SingletonController.getInstance().setAuthor(new Author(0, "", "", "", "", ""));
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Controllers/AuthorDetailView.fxml"));
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == authorList){
			logger.info("Author List Pressed");
			authors = Launcher.authorGateway.getAuthors();
			URL fxmlFile = this.getClass().getResource("/Controllers/AuthorList.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(new AuthorListController(authors));
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
		 } else if(event.getSource() == bDelete) {
			 if(selected != null) {
				try {
					Launcher.bookGateway.deleteBook(selected);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				books = Launcher.bookGateway.getBooks(page);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
				loader.setController(new BookListController(books, page));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			 }
		 }
	}
	
	@FXML void handleListPaging(ActionEvent event) throws IOException {
		Object source = event.getSource();
		if(source == bNext) {
			if(page == numPages) {
				return;
			} else {
				page++;
				books = Launcher.bookGateway.getBooks(page);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
				loader.setController(new BookListController(books, page));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			}
		} else if(source == bPrev) {
			if(page == 0) {
				return;
			} else {
				page--;
				books = Launcher.bookGateway.getBooks(page);
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
				loader.setController(new BookListController(books, page));
				Parent view = loader.load();
				Launcher.rootNode.setCenter(view);
			}
		} else if(source == bFirst) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			 books = Launcher.bookGateway.getBooks(0);
			 loader.setController(new BookListController(books, 0));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(source == bLast) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			 books = Launcher.bookGateway.getBooks(numPages);
			 loader.setController(new BookListController(books, numPages));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
	}
	
	public void initialize(){
		ObservableList<Book> items = bookList.getItems();
		List<String> sortedBooks = new ArrayList<String>();
		for(Book a : books){
			sortedBooks.add(a.getTitle());
		}
		
		Collections.sort(sortedBooks);
		for(String x : sortedBooks) {
			for(Book b : books) {
				if(b.getTitle() == x) {
					items.add(b);
				}
			}
		}

		bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				selected = bookList.getSelectionModel().getSelectedItem();
				if(click.getClickCount() == 2){
					//Use ListView's getSelected Item
					selected = bookList.getSelectionModel().getSelectedItem();
					logger.info("double-clicked " + selected);
					
					try{
						FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetailView.fxml"));
						loader.setController(new BookDetailController(selected));
						Parent view = loader.load();
						//attach view to application center of border pane
						Launcher.rootNode.setCenter(view);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		int upperBooks = ((page*50)+50);
		if(upperBooks > numBooks) {
			upperBooks = numBooks;
		}
		bookLabel.setText("Fetching Records " + (page*50 + 1) + " to " + upperBooks + " out of " + numBooks);
		
	}
}
