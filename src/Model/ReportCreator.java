package Model;

import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;

/*import Controllers.AuthorBook;
import Controllers.BigDecimal;
import Controllers.Cell;
import Controllers.DateTimeFormatter;
import Controllers.File;
import Controllers.FileOutputStream;
import Controllers.HSSFCellStyle;
import Controllers.HSSFFont;
import Controllers.HSSFSheet;
import Controllers.HSSFWorkbook;
import Controllers.LocalDateTime;
import Controllers.Row;*/
import main.Launcher;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

import Controllers.SingletonController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ReportCreator {
	
	final static Logger logger = LogManager.getLogger(ReportCreator.class);
	
	public ReportCreator(Publisher publisher) throws IOException {
		List<Book> books = Launcher.bookGateway.getBooksByPublisher(publisher.getId());
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Royalty Report");
		HSSFFont titleFont = workbook.createFont();
		titleFont.setFontName("Arial");
		titleFont.setFontHeightInPoints((short) 18);
		titleFont.setBold(true);
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titleFont);
		HSSFFont mediumFont = workbook.createFont();
		mediumFont.setFontName("Arial");
		mediumFont.setFontHeightInPoints((short) 14);
		mediumFont.setBold(true);
		HSSFCellStyle mediumStyle = workbook.createCellStyle();
		mediumStyle.setFont(mediumFont);
		

		Row row = sheet.createRow(0);
		Cell cell = row.createCell(0);
		cell.setCellValue("Royalty Report");
		cell.setCellStyle(titleStyle);
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("Publisher: " + publisher);
		cell.setCellStyle(titleStyle);
		

		DateTimeFormatter format = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm");
		LocalDateTime date = LocalDateTime.now();
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue("Report generated on " + date.format(format));
		cell.setCellStyle(mediumStyle);

		int rowNum = 4;

		HSSFCellStyle centerStyle = workbook.createCellStyle();
		centerStyle.setAlignment(HorizontalAlignment.CENTER);
		sheet.setDefaultColumnStyle(2, centerStyle);
		HSSFFont font = workbook.createFont();
		font.setBold(true);
		HSSFCellStyle boldStyle = workbook.createCellStyle();
		boldStyle.setFont(font);
		HSSFCellStyle boldCenterStyle = workbook.createCellStyle();
		boldCenterStyle.cloneStyleFrom(boldStyle);
		boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);

		row = sheet.createRow(rowNum++);
		int cellNum = 0;
		cell = row.createCell(cellNum++);
		cell.setCellValue("Book Title");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("ISBN");
		cell.setCellStyle(boldStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Author");
		cell.setCellStyle(boldCenterStyle);
		cell = row.createCell(cellNum++);
		cell.setCellValue("Royalty");
		cell.setCellStyle(boldStyle);

		for(Book book : books) {
			row = sheet.createRow(rowNum);
			cell = row.createCell(0);
			cell.setCellValue(book.getTitle());
			cell = row.createCell(1);
			cell.setCellValue(book.getIsbn());
			

			BigDecimal totalRoyalty = BigDecimal.ZERO;
			int count = 0;
			List<AuthorBook> authorBooks = Launcher.bookGateway.getAuthorsForBook(book);
			for(AuthorBook authorBook : authorBooks) {
				cell = row.createCell(2);
				cell.setCellValue(authorBook.getAuthor().toString());
				totalRoyalty = totalRoyalty.add(authorBook.getRoyalty());
				cell = row.createCell(3);
				cell.setCellValue("%" + authorBook.getRoyalty().doubleValue() * 100);
				

				if(++count < book.getAuthors().size()) {
					rowNum += 1;
					row = sheet.createRow(rowNum);
				}
			}
			rowNum += 1;
			row = sheet.createRow(rowNum);
			

			cell = row.createCell(2);
			cell.setCellValue("Total Royalty");
			cell.setCellStyle(boldStyle);
			cell = row.createCell(3);
			BigDecimal rounded = totalRoyalty.multiply(new BigDecimal(100));
			cell.setCellValue(String.format("%.2f%s", rounded, "%"));
			cell.setCellStyle(boldStyle);
			

			rowNum = rowNum + 2;
		}
		
		String chosenPath = "C://";
		sheet.autoSizeColumn(0);
		JFileChooser path = new JFileChooser();
		int returnVal = path.showSaveDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            path.getSelectedFile().toString());
	       chosenPath = path.getSelectedFile().toString();
	       try (FileOutputStream f = new FileOutputStream(new File(chosenPath))) {
				workbook.write(f);
				logger.info("Report successfully saved");
	        } catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			URL fxmlFile = this.getClass().getResource("/main/MainMenu.fxml");
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(SingletonController.getInstance());
			Parent view = loader.load();
			Launcher.rootNode.setCenter(view);
	    } else {
	    	workbook.close();
	    	return;
	    }
	}
}
