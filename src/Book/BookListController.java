package Book;

import java.util.List;

import Model.Book;
import Model.Publisher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class BookListController {
	@FXML TextField tfSearchBook;
	@FXML Button bSearch;
	@FXML ListView<Book> bookList;
	private List<Book> books;
	
	public BookListController(List<Book> books) {
		this.books = books;
	}
	
	public void initialize(){
		ObservableList<Book> items = bookList.getItems();
		for(Book a : books){
			items.add(a);
		}
	}
}
