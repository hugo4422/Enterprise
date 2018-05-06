package Model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import Book.BookDetailController;
import Controllers.SingletonController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.Launcher;

public class AuthorAuditTrail {
	private Author author;
	@FXML private Label auditLabel;
	@FXML private ListView<String> auditList;
	@FXML private Button bBack;
	private List<String> auditFetch = new ArrayList<String>();
	
	public AuthorAuditTrail(Author a) {
		this.author = a;
	}
	
	public void initialize() {
		auditLabel.setText("Audit Trail for " + this.author.getFirstName() + " " + author.getLastName());
		auditFetch = Launcher.authorGateway.fetchAuditTrail(author);
		ObservableList<String> items = auditList.getItems();
		for(String a : auditFetch){
			items.add(a);
		}
	}
	@FXML void handleButtonClick(ActionEvent event) throws IOException {
		if(event.getSource() == bBack) {
			 URL fxmlFile = this.getClass().getResource("/Controllers/AuthorDetailView.fxml");
			 FXMLLoader loader = new FXMLLoader(fxmlFile);
			 //publishers = Launcher.publisherGateway.getPublishers();
			 SingletonController.getInstance().setAuthor(author);
			 loader.setController(SingletonController.getInstance());
			 Parent view = loader.load();
			 Launcher.rootNode.setCenter(view);
		}
	}
}