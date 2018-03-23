package Model;

import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

import javafx.beans.property.SimpleStringProperty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PublisherTableGateway {
	private Connection conn;
	private static Logger logger = LogManager.getLogger();
	
	public PublisherTableGateway() throws GatewayException{
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
	
	public List<Publisher> getPublishers() {
		List<Publisher> publishers = new ArrayList<Publisher>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try{
			st = conn.prepareStatement("Select * from Publisher");
			rs = st.executeQuery();
			
			while(rs.next()){
				//create an author object from the record
				SimpleStringProperty publisherName = new SimpleStringProperty(rs.getString("publisher_name"));
				Publisher publisher = new Publisher(rs.getInt("id"), publisherName);
				publishers.add(publisher);
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
		
		return publishers;
	}
	
	public Publisher getPublisherById(int pubId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Publisher publisher = null;
		
		try{
			st = conn.prepareStatement("Select * from Publisher WHERE id = '" + pubId + "'");
			rs = st.executeQuery();
			
			while(rs.next()){
				//create an author object from the record
				SimpleStringProperty publisherName = new SimpleStringProperty(rs.getString("publisher_name"));
				publisher = new Publisher(rs.getInt("id"), publisherName);
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
		
		return publisher;
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
