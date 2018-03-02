package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import Book.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class BookTableGateway {
	private Connection conn;
	private static Logger logger = LogManager.getLogger(BookTableGateway.class);
	
	public BookTableGateway() throws GatewayException{
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
	
	public List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try{
			st = conn.prepareStatement("Select * from Book");
			rs = st.executeQuery();
			
			while(rs.next()){
				//create an author object from the record
				Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"), rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"), rs.getTimestamp("date_added"));
				books.add(book);
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
		
		return books;
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