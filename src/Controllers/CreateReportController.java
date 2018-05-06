package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Model.Book;
import Model.GatewayException;
import Model.Publisher;
import Model.ReportCreator;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import main.Launcher;

public class CreateReportController {

	final static Logger logger = LogManager.getLogger(CreateReportController.class);
	@FXML
	private ComboBox<Publisher> cbPublishers;
	@FXML
	private Button bReport;
	private List<Publisher> publishers;

	public CreateReportController() {

	}

	public void handleButton(ActionEvent e) throws IOException {
		if (e.getSource() == bReport) {
			logger.info("Creating report... (This may take a while)");
			ReportCreator report = new ReportCreator(cbPublishers.getSelectionModel().getSelectedItem());
		}
	}

	public void initialize() {
		publishers = Launcher.publisherGateway.getPublishers();
		ObservableList<Publisher> items = cbPublishers.getItems();
		for (Publisher x : publishers) {
			items.add(x);
		}
		cbPublishers.getSelectionModel().select(0);
	}
}
