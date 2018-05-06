package Model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AuthorBook {
	private Author author;
	private Book book;
	private BigDecimal royalty; 
	private double oldRoyalty;
	private boolean newRecord;
	private String royaltyS;
	
	public AuthorBook(Author author, Book book, BigDecimal royalty) {
		oldRoyalty = royalty.doubleValue();
		newRecord = true;
		royalty = royalty.setScale(3, RoundingMode.CEILING);
		this.author = author;
		this.book = book;
		this.royalty = royalty;
	}
	
	public Author getAuthor() {
		return this.author;
	}
	
	public void setOldRoyalty() {
		oldRoyalty = royalty.doubleValue();
	}
	
	public double getOldRoyalty() {
		return this.oldRoyalty;
	}
	
	public Book getBook() {
		return this.book;
	}
	
	public BigDecimal getRoyalty() {
		return this.royalty;
	}
	
	public void setRoyalty(BigDecimal royalty) {
		this.royalty = royalty;
		BigDecimal mult = royalty.multiply(new BigDecimal(100));
		this.royaltyS = String.format("%.2f%s", mult, "%");
	}
	
	public String getRoyalyPercent() {
		return this.royaltyS;
	}
	
	public Boolean isNewRecord() {
		return newRecord;
	}
	
	public void setSaved() {
		this.newRecord = false;
	}
	
	@Override
	public String toString() {
		return author.getFirstName() + " " + author.getLastName() + " 	Royalty: " + royalty;
	}
}
