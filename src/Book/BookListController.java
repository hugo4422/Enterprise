package Book;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import Controllers.SingletonController;
import Model.Author;
import Model.Book;
import Model.Publisher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.Launcher;

public class BookListController {
	@FXML TextField tfBookSearch;
	@FXML Button bSearch;
	@FXML ListView<Book> bookList;
	private List<Book> books;
	
	final static Logger logger = LogManager.getLogger(SingletonController.class);
	
	public BookListController(List<Book> books) {
		this.books = books;
	}
	
	@FXML
	private void handleSearchClick(ActionEvent event) throws Exception {
		if(event.getSource() == bSearch) {
			books = Launcher.bookGateway.searchBook(tfBookSearch.getText());
			logger.info("Book list clicked");
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/Book/BookListView.fxml"));
			loader.setController(new BookListController(books));
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
		}
	}
	
	public void initialize(){
		ObservableList<Book> items = bookList.getItems();
		List<String> sortedBooks = new ArrayList<String>();
		for(Book a : books){
			sortedBooks.add(a.getTitle());
		}
		
		Collections.sort(sortedBooks);
		for(String x : sortedBooks) {
			for(Book b : books) {
				if(b.getTitle() == x) {
					items.add(b);
				}
			}
		}

		bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent click){
				if(click.getClickCount() == 2){
					//Use ListView's getSelected Item
					Book selected = bookList.getSelectionModel().getSelectedItem();
					
					logger.info("double-clicked " + selected);
					
					try{
						FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetailView.fxml"));
						loader.setController(new BookDetailController(selected));
						Parent view = loader.load();
						//attach view to application center of border pane
						Launcher.rootNode.setCenter(view);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
