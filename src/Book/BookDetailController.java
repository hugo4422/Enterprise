package Book;

import java.util.List;

import Model.Publisher; 
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

public class BookDetailController {
	@FXML ComboBox cbPubList;
	private List<Publisher> pubList;
	
	public BookDetailController(List<Publisher> pubList) {
		this.pubList = pubList;
	}

}
