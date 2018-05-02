package Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.Author;
import Model.Book;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.Launcher;

public class AuditTrailController {
	
	private Book book;
	@FXML private Label auditLabel;
	@FXML private ListView<String> auditList;
	@FXML private Button bBack;
	private List<String> auditFetch = new ArrayList<String>();
	
	public AuditTrailController(Book book) {
		this.book = book;
	}
	
	public void initialize() {
		auditLabel.setText("Audit Trail for " + this.book.getTitle());
		auditFetch = Launcher.bookGateway.fetchAuditTrail(book);
		ObservableList<String> items = auditList.getItems();
		for(String a : auditFetch) {
			items.add(a);
		}
	}
	@FXML void handleButtonClick(ActionEvent event) throws IOException {
		if(event.getSource() == bBack) {
			 URL fxmlFile = this.getClass().getResource("/Book/BookDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 loader.setController(new BookDetailController(book));
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
	}
}
