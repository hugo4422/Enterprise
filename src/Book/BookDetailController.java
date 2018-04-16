package Book;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controllers.SingletonController;
import Model.Author;
import Model.AuthorBook;
import Model.Book;
import Model.GatewayException;
import Model.Publisher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Launcher;

public class BookDetailController {
	@FXML private ComboBox<Publisher> cbPubList;
	@FXML private ListView<AuthorBook> authorRoyalty;
	@FXML private Button bSave;
	@FXML private TextField tfTitle;
	@FXML private TextArea taSummary;
	@FXML private TextField tfYearPub;
	@FXML private TextField tfIsbn;
	@FXML private TextField tfDateAdded;
	@FXML private Button bAuditTrail;
	@FXML private Button addAuthor;
	@FXML private Button delAuthor;

	private List<Publisher> pubList;
	private List<AuthorBook> authorBooks;
	private List<Book> books;
	private Book book;
	
	final static Logger logger = LogManager.getLogger();
	
	public BookDetailController(Book book) {
		this.book = book;
		authorBooks = new ArrayList<AuthorBook>();
	}

	@FXML
	private void handleButtonClick(ActionEvent event) throws Exception {
		if (event.getSource() == bSave) {
			Book oldBook = new Book(book.getId(), book.getTitle(), book.getSummary(), book.getYearPublished(), book.getPublisherId(), book.getIsbn(), book.getTime());
			book.setTitle(new SimpleStringProperty(tfTitle.getText()));
			book.setYearPublished(new SimpleIntegerProperty(Integer.parseInt(tfYearPub.getText())));
			book.setSummary(new SimpleStringProperty(taSummary.getText()));
			book.setIsbn(new SimpleStringProperty(tfIsbn.getText()));
			book.setPublisher(new SimpleObjectProperty<Publisher>(cbPubList.getValue()));
			logger.info("Save button pressed");

			try {
				book.Save(oldBook, book);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 logger.info("Book list clicked");
			 FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			 books = Launcher.bookGateway.getBooks(0);
			 loader.setController(new BookListController(books, 0));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == bAuditTrail) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/AuditTrailView.fxml"));
			 loader.setController(new AuditTrailController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == addAuthor) {
			 logger.info("Add Author was Pressed");
			 URL fxmlFile = this.getClass().getResource("/Book/AddAuthorRoyalty.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new AddAuthorRoyaltyController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} 

		authorRoyalty.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent click){
				if(click.getClickCount() == 1){
					//Use ListView's getSelected Item
					
					
					try{
						if(event.getSource() == delAuthor){
							AuthorBook selected = authorRoyalty.getSelectionModel().getSelectedItem();
							Launcher.bookGateway.deleteAuthorFromBook(selected.getAuthor());
							Launcher.bookGateway.insertAuditTrail(book.getId(), book.getTitle() + " was deleted");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void initialize() {
		pubList = Launcher.publisherGateway.getPublishers();
		ObservableList<Publisher> items = cbPubList.getItems();
		for(Publisher a : pubList){
			items.add(a);
		}
		
		authorBooks = book.getAuthors();
		ObservableList<AuthorBook> abList = authorRoyalty.getItems();
		for(AuthorBook x : authorBooks){
			abList.add(x);
		}
		
		authorRoyalty.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				if(click.getClickCount() == 2){
					//Use ListView's getSelected Item
					AuthorBook selected = authorRoyalty.getSelectionModel().getSelectedItem();
					try {
						Launcher.bookGateway.deleteAuthorFromBook(selected.getAuthor());
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.info("double-clicked " + selected);
					
					try{
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookDetailView.fxml"));
						loader.setController(new BookDetailController(book));
						Parent view = loader.load();
						//attach view to application center of border pane
						Launcher.rootNode.setCenter(view);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		if(this.book.getId() == 0) {
			cbPubList.getSelectionModel().select(0);
		} else {
			Publisher publisher = Launcher.publisherGateway.getPublisherById(this.book.getPublisherId());
			if(publisher == null) {
				cbPubList.getSelectionModel().select(0);
			} else {
				cbPubList.getSelectionModel().select(publisher);
			}
			tfTitle.setText(book.getTitle());
			taSummary.setText(book.getSummary());
			tfYearPub.setText(String.valueOf(book.getYearPublished()));
			tfIsbn.setText(book.getIsbn());
			tfDateAdded.setText(book.getDateAdded());
		}
	}
}
