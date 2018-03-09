package Book;

import java.util.ArrayList;
import java.util.List;

import Model.Author;
import Model.Book;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.Launcher;

public class AuditTrailController {
	
	private Book book;
	@FXML private Label auditLabel;
	@FXML private ListView<String> auditList;
	private List<String> auditFetch = new ArrayList<String>();
	
	public AuditTrailController(Book book) {
		this.book = book;
	}
	
	public void initialize() {
		auditLabel.setText("Audit Trail for " + this.book.getTitle());
		auditFetch = Launcher.bookGateway.fetchAuditTrail(book);
		ObservableList<String> items = auditList.getItems();
		for(String a : auditFetch){
			items.add(a);
		}
	}
}
