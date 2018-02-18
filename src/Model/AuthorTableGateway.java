package Model;

import java.io.FileInputStream; 
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class AuthorTableGateway {
	private Connection conn;
	private static Logger logger = LogManager.getLogger(AuthorTableGateway.class);
	
	public List<Author> getAuthors() {
		List<Author> authors = new ArrayList<Author>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try{
			st = conn.prepareStatement("Select * from Author");
			rs = st.executeQuery();
			
			while(rs.next()){
				//create an author object from the record
				Author author = new Author(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("dob"), rs.getString("gender"), rs.getString("website"));
				authors.add(author);
			}
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try{
				if(rs != null)
					rs.close();
				if(st != null)
					st.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		
		return authors;
	}
	
	public AuthorTableGateway() throws GatewayException{
		conn = null;
		
		//create a connection to the cars database
		//create datasource
		//read db credentials from properties file
		Properties props = new Properties();
		FileInputStream fis = null;
		try{
			fis = new FileInputStream("db.properties");
			props.load(fis);
			fis.close();
			
			//create the datasource
			MysqlDataSource ds = new MysqlDataSource();
			ds.setURL(props.getProperty("MYSQL_DB_URL"));
			ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
			ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
			
			//create the connection
			conn = (Connection) ds.getConnection();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new GatewayException(e);
		}
	}
	
	public void insertAuthor(Author newAuthor) throws SQLException{
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try{
			st = conn.prepareStatement("insert into Author (first_name, last_name, dob, gender, website) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			logger.error(newAuthor.getDob());
			st.setString(1, newAuthor.getFirstName());
			st.setString(2, newAuthor.getLastName());
			st.setString(3, newAuthor.getDob());
			st.setString(4, newAuthor.getGender());
			st.setString(5, newAuthor.getWebsite());
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if(rs != null && rs.next()){
				newAuthor.setId(rs.getInt(1));
			}
		}catch(SQLException e){
			logger.error("The insert has failed");
			e.printStackTrace();
			System.out.println(newAuthor.getFirstName() + " <---");
			System.out.println(newAuthor.getDob() + " <---");
			System.out.println(newAuthor.getLastName() + " <---");
			System.out.println(newAuthor.getGender() + " <---");
			System.out.println(newAuthor.getWebsite() + " <---");
		}finally{
			if(rs != null){
				rs.close();
			}
			if(st != null){
				st.close();
			}
		}
	}
	
	public void updateAuthor(Author updated){
		PreparedStatement st = null;
		
		try{
			st = conn.prepareStatement("update Author set first_name = '" + updated.getFirstName() + "',"
					+ "last_name = '" + updated.getLastName() + "',"
					+ "dob = '" + updated.getDob() + "',"
					+ "gender = '" + updated.getGender() + "',"
					+ "website = '" + updated.getWebsite() + "'"
					+ "where id = '" + updated.getId() + "'");
			
			st.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			try{
				if(st != null)
					st.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	public void deleteAuthor(Author authorDeleting){
		PreparedStatement st = null;
		try{
		st = conn.prepareStatement("delete from Author where id = '" + authorDeleting.getId() + "'");
		st.executeUpdate();
		logger.error("The delete button was clicked");
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void close(){
		if(conn != null){
			try{
				conn.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}
}