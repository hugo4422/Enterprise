package Book;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Controllers.SingletonController;
import Model.Author;
import Model.AuthorBook;
import Model.Book;
import Model.Publisher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import main.Launcher;



public class AddAuthorRoyaltyController {
	@FXML private TextField tfRoyalty;
    @FXML private ComboBox<Author> authorList;
    @FXML private Button bAddRoyalty;
    @FXML private Button bBack;
    private List<AuthorBook> existingAuthors;
    private List<Author> authors;
    final static Logger logger = LogManager.getLogger(AddAuthorRoyaltyController.class);
	private Book book;

	public AddAuthorRoyaltyController(Book book) {
		this.book = book;
		existingAuthors = new ArrayList<AuthorBook>();
	}
	
	@FXML
    void handleSaveButton(ActionEvent event) throws SQLException, IOException {
		if(event.getSource() == bAddRoyalty && !tfRoyalty.getText().equals("")) {
			double x = Double.valueOf(tfRoyalty.getText());
			if(x <= 0.0 || x > 1.0){
				logger.error("Oops!", "Royalty is invalid", "Royalty must be "
	    				+ "between 0.0 and 1.0");
				return;
			}
			AuthorBook authorBook = new AuthorBook(authorList.getSelectionModel().getSelectedItem(),
					book, BigDecimal.valueOf(Double.valueOf(tfRoyalty.getText())));
			Launcher.bookGateway.addAuthorToBook(authorBook);
			Launcher.bookGateway.insertAuditTrail(book.getId(), authorBook.getAuthor() + " was added to " + authorBook.getBook() + " with royalty " + authorBook.getRoyalty());
			logger.info("Add royalty was Pressed");
			Launcher.bookGateway.insertAuditTrail(book.getId(), book.getTitle() + " added with author " + authorBook.getAuthor().getFirstName() + " " + authorBook.getAuthor().getLastName());
			 URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == bBack) {
			URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
    }

	public void initialize() {
		existingAuthors = book.getAuthors();
		authors = Launcher.authorGateway.getAuthors();
		
		for(Author author : authors) {
			Boolean flag = true;
			for(AuthorBook authorBook : existingAuthors) {
				if(author.getId() == authorBook.getAuthor().getId()) {
					flag = false;
				}
			}
			if(flag)
				authorList.getItems().add(author);
		}
	}
}
