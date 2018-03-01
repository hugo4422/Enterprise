package Model;

import javafx.beans.property.SimpleStringProperty;

public class Publisher {
	private int id;
	private SimpleStringProperty publisherName;
	
	public Publisher(int id, SimpleStringProperty string) {
		this.id = id;
		this.publisherName = string;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public SimpleStringProperty getPublisherName() {
		return publisherName;
	}
	
	public void setPublisherName(SimpleStringProperty publisherName) {
		this.publisherName = publisherName;
	}
}