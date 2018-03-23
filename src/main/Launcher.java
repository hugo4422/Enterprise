package main;

import java.net.URL; 
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;

import Controllers.*;
import Model.Author;
import Model.AuthorTableGateway;
import Model.BookTableGateway;
import Model.PublisherTableGateway;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


// CS 4743 Assignment 2 by Casey Bowden and Hugo Chavez

public class Launcher extends Application {
	
	public static BorderPane rootNode;
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
		
		URL fxmlFile = this.getClass().getResource("MainView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		rootNode = loader.load();
		//generate a scenegraph from the fxml file
		//note that the entire view will be laid out in the fxml file
		
		Scene scene = new Scene(rootNode, 600, 400);
		primaryStage.setTitle("Menu");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		controller = SingletonController.getInstance();
		controller.setRootNode((BorderPane) rootNode);
		
		fxmlFile = this.getClass().getResource("Menu.fxml");
		loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		Parent view = loader.load();
		rootNode.setCenter(view);
		//build a scene with the scenegraph as its root node
	}
}
