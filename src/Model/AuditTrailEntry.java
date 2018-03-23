package Model;

import Controllers.*; 
import Book.*;
import main.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuditTrailEntry {
	
	private static Logger logger = LogManager.getLogger(AuditTrailEntry.class);
	
	private int id;
	private LocalDateTime dateAdded;
	private String message;
	
	public AuditTrailEntry(int id, String message){
		this.id = id;
		this.message = message;
		this.dateAdded = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setRecordDescriptor(int id) {
		this.id = id;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override 
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd HH:mm:ss");
		String text = this.dateAdded.format(formatter); 
		return text + "  " + this.message;
	}
	
	
}