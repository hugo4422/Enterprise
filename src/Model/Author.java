package Model;

import main.Launcher;

public class Author {
	
	private int id;
	private String lastName;
	private String dob;
	private String gender;
	private String website;
	private String firstName;
	
	public Author(){
		this.id = 0;
		this.firstName = "";
		this.lastName = "";
		this.dob = "";
		this.gender = "";
		this.website = "";
	}
	
	public Author(int id, String first_name, String last_name, String dob, String gender, String website) {
		this.id = id;
		
		this.firstName = first_name;
		this.validateFirstName();
		
		this.lastName = last_name;
		this.validateLastName();
		
		this.dob = dob;
		
		this.gender = gender;
		this.validateGender();
		
		this.website = website;
		this.validateWebsite();
		
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean validateId(int id){
		if(this.id < 0) {
			return false;
		}
		else
			return true;
	}
	
	public int getId() {
		return id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String first_name) {
		this.firstName = first_name;
	}
	
	public boolean validateFirstName(){
		if(this.firstName == "" || this.firstName.length() > 100)
			return false;
		else 
			return true;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String last_name) {
		this.lastName = last_name;
	}
	
	public boolean validateLastName(){
		if(this.lastName == "" || this.lastName.length() > 100)
			return false;
		else
			return true;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public boolean validateGender(){
		if(this.gender.equals("M") || this.gender.equals("Male") || this.gender.equals("F") || this.gender.equals("Female") || this.gender.equals("Unknown"))
			return true;
		else
			return false;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	
	public boolean validateWebsite(){
		if(this.website == null) {
			return true;
		}
		else if(this.website.length() > 100)
			return false;
		else
			return true;
	}
	
	@Override
	public String toString() {
		return id + " " + firstName + " " + lastName + " " + dob + " " + gender + " " + website;
	}
	
	public void Save(Author author) throws Exception {
			
			if(this.validateFirstName() == false)
				throw new Exception("Validation failed");
			if(this.validateLastName() == false)
				throw new Exception("Validation failed");
			if(this.validateGender() == false)
				throw new Exception("Validation failed");
			if(this.validateWebsite() == false)
				throw new Exception("Validation failed");
			if(id != 0){
				Launcher.authorGateway.updateAuthor(this);
			}
			else
				Launcher.authorGateway.insertAuthor(this);
		    
	}
}