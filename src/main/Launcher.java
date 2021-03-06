package main;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import Controllers.*;
import Model.Author;
import Model.AuthorTableGateway;
import Model.Book;
import Model.BookTableGateway;
import Model.PublisherTableGateway;
import Model.Publisher;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


// CS 4743 Assignment 2 by Casey Bowden and Hugo Chavez

public class Launcher extends Application {
	
	public static BorderPane rootNode;
	public static Stage primaryStage;
	public static SingletonController controller;
	//public static AuthorListController aController
	public static AuthorTableGateway authorGateway;
	public static PublisherTableGateway publisherGateway;
	public static BookTableGateway bookGateway;
	private static Logger logger = LogManager.getLogger();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void init() throws Exception{
		super.init();
		
		//create gateway and exit if problem
		logger.info("Launcher init called");
		authorGateway = new AuthorTableGateway();
		publisherGateway = new PublisherTableGateway();
		bookGateway = new BookTableGateway();
	}
	
	@Override
	public void stop() throws Exception{
		super.stop();
		
		//close gateway
		logger.info("Launcher stop called");
		authorGateway.close();
		publisherGateway.close();
		bookGateway.close();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		MasterController c = new MasterController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		loader.setController(c);
		rootNode = loader.load();
		//generate a scenegraph from the fxml file
		//note that the entire view will be laid out in the fxml file
		
		Scene scene = new Scene(rootNode, 600, 400);
		primaryStage.setTitle("Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		Launcher.primaryStage = primaryStage;
		
		/*controller = SingletonController.getInstance();
		controller.setRootNode((BorderPane) rootNode);
		
		fxmlFile = this.getClass().getResource("Menu.fxml");
		loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		Parent view = loader.load();
		rootNode.setCenter(view);*/
		//build a scene with the scenegraph as its root node
	}
}
