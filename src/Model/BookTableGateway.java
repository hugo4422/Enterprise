package Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import Book.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class BookTableGateway {
	private Connection conn;
	private static Logger logger = LogManager.getLogger();

	public BookTableGateway() throws GatewayException {
		conn = null;

		// create a connection to the cars database
		// create datasource
		// read db credentials from properties file
		Properties props = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("db.properties");
			props.load(fis);
			fis.close();

			// create the datasource
			MysqlDataSource ds = new MysqlDataSource();
			ds.setURL(props.getProperty("MYSQL_DB_URL"));
			ds.setUser(props.getProperty("MYSQL_DB_USERNAME"));
			ds.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));

			// create the connection
			conn = (Connection) ds.getConnection();
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new GatewayException(e);
		}
	}
	
	public void insertAuditTrail(int id, String msg) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"insert into Book_Audit_Trail (book_id, entry_msg) values (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, id);
			st.setString(2, msg);
			st.executeUpdate();
			rs = st.getGeneratedKeys();
		} catch (SQLException e) {
			logger.error("The insert has failed");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
		}
	}
	
	public List<String> fetchAuditTrail(Book book) {
		List<String> auditTrail = new ArrayList<String>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("Select * from Book_Audit_Trail WHERE book_id = '" + book.getId() + "'");
			rs = st.executeQuery();

			while (rs.next()) {
				// create an author object from the record
				String audit = (rs.getInt("id") + " " + rs.getInt("book_id") + " " + rs.getTimestamp("date_added") +
						" " + rs.getString("entry_msg"));
				auditTrail.add(audit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return auditTrail;
	}

	public List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("Select * from Book");
			rs = st.executeQuery();

			while (rs.next()) {
				// create an author object from the record
				Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"),
						rs.getTimestamp("date_added"));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return books;
	}

	public void insertBook(Book newBook) throws SQLException {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement(
					"insert into Book (title, summary, year_published, publisher_id, isbn) values (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			st.setString(1, newBook.getTitle());
			st.setString(2, newBook.getSummary());
			st.setInt(3, newBook.getYearPublished());
			st.setInt(4, newBook.getPublisherId());
			st.setString(5, newBook.getIsbn());
			st.executeUpdate();
			rs = st.getGeneratedKeys();
			if (rs != null && rs.next()) {
				newBook.setId(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error("The insert has failed");
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
		}
	}

	public void updateBook(Book updated) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update Book set title = '" + updated.getTitle() + "'," 
				+ "summary = '"+ updated.getSummary() + "'," 
				+ "year_published = '" + updated.getYearPublished() + "',"
				+ "publisher_id = '" + updated.getPublisherId() 
				+ "'," + "isbn = '" + updated.getIsbn() + "'"
				+ "where id = '" + updated.getId() + "'");

			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<Book> searchBook(String search) {
		List<Book> books = new ArrayList<Book>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("SELECT * from Book WHERE title LIKE '%" + search + "%'");
			rs = st.executeQuery();

			while (rs.next()) {
				// create an author object from the record
				Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"),
						rs.getTimestamp("date_added"));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return books;
	}

	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}