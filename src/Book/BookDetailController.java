package Book;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Controllers.SingletonController;
import Model.Author;
import Model.Book;
import Model.Publisher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Launcher;

public class BookDetailController {
	@FXML private ComboBox<Publisher> cbPubList;
	@FXML private Button bSave;
	@FXML private TextField tfTitle;
	@FXML private TextArea taSummary;
	@FXML private TextField tfYearPub;
	@FXML private TextField tfIsbn;
	@FXML private TextField tfDateAdded;
	@FXML private Button bAuditTrail;
	private List<Publisher> pubList;
	private List<Book> books;
	private Book book;
	
	final static Logger logger = LogManager.getLogger(SingletonController.class);
	
	public BookDetailController(Book book) {
		this.book = book;
	}

	@FXML
	private void handleButtonClick(ActionEvent event) throws Exception {
		if (event.getSource() == bSave) {
			Book oldBook = book;
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
			 books = Launcher.bookGateway.getBooks();
			 loader.setController(new BookListController(books));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == bAuditTrail) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/AuditTrailView.fxml"));
			 loader.setController(new AuditTrailController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
	}

	public void initialize() {
		pubList = Launcher.publisherGateway.getPublishers();
		ObservableList<Publisher> items = cbPubList.getItems();
		for(Publisher a : pubList){
			items.add(a);
		}
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
