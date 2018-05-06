package Book;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.Author;
import Model.AuthorBook;
import Model.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.Launcher;

public class UpdateRoyaltyController {
	
	@FXML private Button saveRoyalty, bBack;
	@FXML private TextField tfRoyalty;
	@FXML private Label authorName;
	private AuthorBook authorBook;
	final static Logger logger = LogManager.getLogger(UpdateRoyaltyController.class);
	
	public UpdateRoyaltyController(AuthorBook authorBook) {
		this.authorBook = authorBook;
	}
	
	@FXML
    void handleSaveButton(ActionEvent event) throws SQLException, IOException {
		if(tfRoyalty.getText().equals("")) {
			return;
		} else if(event.getSource() == saveRoyalty) {
			double x = Double.valueOf(tfRoyalty.getText());
			if(x <= 0.0 || x > 1.0){
				logger.error("Oops!", "Royalty is invalid", "Royalty must be "
	    				+ "between 0.0 and 1.0");
				return;
			}
			
			BigDecimal c = new BigDecimal(x);
			authorBook.setRoyalty(c);
			
			Launcher.bookGateway.updateRoyalty(authorBook);
			Launcher.bookGateway.insertAuditTrail(authorBook.getBook().getId(), authorBook.getAuthor() + " royalty changed from " + authorBook.getOldRoyalty() + " to " + authorBook.getRoyalty());
			logger.info("royalty updated");
			authorBook.setOldRoyalty();
			 URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(authorBook.getBook()));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		} else if(event.getSource() == bBack) {
			URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(authorBook.getBook()));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
    }

	public void initialize() {
		authorName.setText(authorBook.getAuthor().getFirstName() + " " + authorBook.getAuthor().getLastName());
		tfRoyalty.setText(authorBook.getRoyalty().toString());
	}
}
