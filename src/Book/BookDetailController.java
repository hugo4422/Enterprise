package Book;

import java.util.List;

import Model.Author;
import Model.Book;
import Model.Publisher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class BookDetailController {
	@FXML ComboBox<Publisher> cbPubList;
	private List<Publisher> pubList;
	
	public BookDetailController(List<Publisher> publishers) {
		this.pubList = publishers;
	}

	public void initialize() {
		ObservableList<Publisher> items = cbPubList.getItems();
		for(Publisher a : pubList){
			items.add(a);
		}
		cbPubList.getSelectionModel().select(0);
	}
}
