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
	
	public Author(int id, String first_name, String last_name, String dob, String gender, String web_site){
		this.id = id;
		
		this.firstName = first_name;
		this.validateFirstName(first_name);
		
		this.lastName = last_name;
		this.validateLastName(last_name);
		
		this.dob = dob;
		
		this.gender = gender;
		this.validateGender(gender);
		
		this.website = web_site;
		this.validateWebsite(web_site);
		
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
	
	public boolean validateFirstName(String first_name){
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
	
	public boolean validateLastName(String last_name){
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
	
	public boolean validateGender(String gender){
		if(this.gender.equals("M") || this.gender.equals("Male") || this.gender.equals("F") || this.gender.equals("Female") || this.gender.equals("Unknown"))
			return true;
		else
			return false;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String web_site) {
		this.website = web_site;
	}
	
	public boolean validateWebsite(String web_site){
		if(this.website.length() > 100)
			return false;
		else
			return true;
	}
	
	public void Save(Author author) throws Exception {
			
			if(this.validateFirstName(firstName) == false)
				throw new Exception("Validation failed");
			if(this.validateLastName(lastName) == false)
				throw new Exception("Validation failed");
			if(this.validateGender(gender) == false)
				throw new Exception("Validation failed");
			if(this.validateWebsite(website) == false)
				throw new Exception("Validation failed");
			/*if(id != 0){
				Launcher.authorGateway.updateAuthor(this);
			}
			//else
				Launcher.authorGateway.insertAuthor(this);
		    */
	}
}