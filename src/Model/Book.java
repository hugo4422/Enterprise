package Model;

import java.time.LocalDate;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {
	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	
	public Book() {
		this.id = 0;
	}
	
	public SimpleStringProperty getTitle() {
		return title;
	}
	public void setTitle(SimpleStringProperty title) {
		this.title = title;
	}
	
	public boolean validateTitle() {
		return (title.getValue().length() < 255 && title.getValue().length() > 1);
	}
	public SimpleStringProperty getSummary() {
		return summary;
	}
	public void setSummary(SimpleStringProperty summary) {
		this.summary = summary;
	}
	
	public boolean validateSummary() {
		return (summary.getValue().length() < 65536);
	}
	public SimpleIntegerProperty getYearPublished() {
		return yearPublished;
	}
	public void setYearPublished(SimpleIntegerProperty yearPublished) {
		this.yearPublished = yearPublished;
	}
	
	public boolean validateYearPublished() {
		return (yearPublished.get() <= 2018);
	}
	
	public SimpleObjectProperty<Publisher> getPublisher() {
		return publisher;
	}
	
	public void setPublisher(SimpleObjectProperty<Publisher> publisher) {
		this.publisher = publisher;
	}
	
	public SimpleObjectProperty<LocalDate> getDateAdded() {
		return dateAdded;
	}
	
	public void setDateAdded(SimpleObjectProperty<LocalDate> dateAdded) {
		this.dateAdded = dateAdded;
	}
	
	public SimpleStringProperty getIsbn() {
		return isbn;
	}
	
	public void setIsbn(SimpleStringProperty isbn) {
		this.isbn = isbn;
	}
	
	public boolean validateIsbn() {
		return (isbn.getValue().length() <= 13);
	}
}
