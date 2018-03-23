package Model;

import java.sql.SQLException; 
import java.sql.Timestamp;
import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import main.Launcher;

public class Book {
	
	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	private Timestamp time;
	
	public Book(int id, String title, String summary, int year_published, int publisher_id, String isbn, Timestamp timestamp) {
		this.id = id;
		this.title = new SimpleStringProperty(title);
		this.summary = new SimpleStringProperty(summary);
		this.yearPublished = new SimpleIntegerProperty(year_published);
		this.publisher = new SimpleObjectProperty<Publisher>(Launcher.publisherGateway.getPublisherById(publisher_id));
		this.isbn = new SimpleStringProperty(isbn);
		if(timestamp != null) {
			this.dateAdded = new SimpleObjectProperty<LocalDate>(timestamp.toLocalDateTime().toLocalDate());
			this.time = timestamp;
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	public int getId() {
		return this.id;
	}

	public String getTitle() {
		return title.getValue();
	}
	
	public void setTitle(SimpleStringProperty title) {
		this.title = title;
	}
	
	public boolean validateTitle() {
		return (title.getValue().length() < 255 && title.getValue().length() > 1);
	}
	
	public String getSummary() {
		return summary.getValue();
	}
	
	public void setSummary(SimpleStringProperty summary) {
		this.summary = summary;
	}
	
	public boolean validateSummary() {
		if(summary.getValue() == null) {
			return true;
		}
		return (summary.getValue().length() < 65536);
	}
	
	public int getYearPublished() {
		return yearPublished.getValue();
	}
	
	public void setYearPublished(SimpleIntegerProperty yearPublished) {
		this.yearPublished = yearPublished;
	}
	
	public boolean validateYearPublished() {
		return (yearPublished.get() <= 2018);
	}
	
	public int getPublisherId() {
		return publisher.getValue().getId();
	}
	
	public void setPublisher(SimpleObjectProperty<Publisher> publisher) {
		this.publisher = publisher;
	}
	
	public String getDateAdded() {
		return dateAdded.getValue().toString();
	}
	
	public void setDateAdded(SimpleObjectProperty<LocalDate> dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public String getIsbn() {
		return isbn.getValue();
	}
	
	public void setIsbn(SimpleStringProperty isbn) {
		this.isbn = isbn;
	}
	
	public boolean validateIsbn() {
		if(isbn == null) {
			return false;
		}
		return (isbn.getValue().length() <= 13);
	}
	
	public Timestamp getTime() {
		return this.time;
	}
	
	public void Save(Book oldBook, Book book) throws Exception {
		
		if(this.validateTitle() == false)
			throw new Exception("Validation failed");
		if(this.validateSummary() == false)
			throw new Exception("Validation failed");
		if(this.validateIsbn() == false)
			throw new Exception("Validation failed");
		if(this.validateYearPublished() == false)
			throw new Exception("Validation failed");
		if(id != 0){
			compareBook(oldBook, book);
			Launcher.bookGateway.updateBook(this);
		}
		else {
			Launcher.bookGateway.insertAuditTrail(this.id, "Book Added");
			Launcher.bookGateway.insertBook(this);
		}
	}
	
	public void compareBook(Book oldBook, Book newBook) throws SQLException {
		if(!oldBook.getTitle().equals(newBook.getTitle())) {
			Launcher.bookGateway.insertAuditTrail(this.id, "Title changed from " + oldBook.getTitle() + " to " + newBook.getTitle());
		}
		if(!oldBook.getIsbn().equals(newBook.getIsbn())) {
			Launcher.bookGateway.insertAuditTrail(this.id, "ISBN changed from " + oldBook.getIsbn() + " to " + newBook.getIsbn());
		} 
		if(!oldBook.getSummary().equals(newBook.getSummary())) {
			Launcher.bookGateway.insertAuditTrail(this.id, "Summary changed from " + oldBook.getSummary() + " to " + newBook.getSummary());
		}
		if(oldBook.getPublisherId() != newBook.getPublisherId()) {
			Launcher.bookGateway.insertAuditTrail(this.id, "Publisher id changed from " + oldBook.getPublisherId() + " to " + newBook.getPublisherId());
		}
		if(oldBook.getYearPublished() != newBook.getYearPublished()) {
			Launcher.bookGateway.insertAuditTrail(this.id, "Year Published changed from " + oldBook.getYearPublished() + " to " + newBook.getYearPublished());
		}
	}
	
	public String toString() {
		return title + " " + summary + " " + yearPublished + " " + publisher + " " + isbn + " " + dateAdded;
	}
}
