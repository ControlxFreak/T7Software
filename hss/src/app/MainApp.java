/*
 * ---------------------------------------------------------------------------------
 * Title: MainApp.java
 * Description:
 * The main application class for the home station.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 16 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	16 April 2017 - Jarrett Mead - Application Birthday
 * ---------------------------------------------------------------------------------
 */
package app;



import java.io.IOException;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage.MsgType;
import app.view.TelemetryDataOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import networking.server.UAVServer;

public class MainApp extends Application {

	private static Logger logger			= Logger.getLogger(MainApp.class.getName());
	private Stage primaryStage;
	private static BorderPane rootLayout;
	private static TelemetryDataOverviewController tel_controller;
	private static FXMLLoader tel_loader;
	private static UAVServer server = new UAVServer();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Home Station");

		initRootLayout();

		showTelemetryOverview();

		initServer();
	}

	private void initServer() {
		new Thread(server).start();
	}

	private void showTelemetryOverview() {
		try {
			//Load telemetry overview.
			tel_loader = new FXMLLoader();
			System.out.println("TelData resource = " + getClass().getResource("view/TelemetryDataOverview.fxml"));
			tel_loader.setLocation(getClass().getResource("view/TelemetryDataOverview.fxml"));
			System.out.println("TelDataOverview loader location= " + tel_loader.getLocation());
			AnchorPane telemetryOverview = null;
			telemetryOverview = (AnchorPane) tel_loader.load();

			// Set telemetry overview into the center of root layout.
			rootLayout.setLeft(telemetryOverview);

			tel_controller = (TelemetryDataOverviewController)tel_loader.getController();
			System.out.println("TEL_CONTROLLER = " + tel_controller);
		} catch (IOException e) {
			logger.fine(e.toString());
		}
	}

	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			System.out.println("RootLayout resource = " + getClass().getResource("view/RootLayout.fxml"));
			loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
			System.out.println("RootLayout loader location=" + loader.getLocation());
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			logger.fine(e.toString());
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void updateDisplay(double datum, MsgType type) {
		tel_controller.updateTelemetryDatum(datum, type);
	}

	protected void updateDisplay(byte[] datum, MsgType type) {
		tel_controller.updateVectorDatum(datum, type);
	}

	public static void main(String[] args) {
		launch(args);

		server.shutDown();
	}
}
