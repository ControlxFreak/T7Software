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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Logger;

import app.view.TelemetryDataOverviewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import networking.server.UAVServer;

public class MainApp extends Application {

	private static Logger logger			= Logger.getLogger(MainApp.class.getName());
	private Stage primaryStage;
	private BorderPane rootLayout;
	private static Socket servSocket = null;
	private static TelemetryDataListener tel_listener;
	private static TelemetryDataOverviewController tel_controller;
	private static FXMLLoader tel_loader;
	private volatile static boolean timeToExit = false;
	private static ObjectInputStream telemetryStream = null;
	private volatile static boolean airTempUpdateAvailable = false;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Home Station");

		initRootLayout();

		showTelemetryOverview();

		tel_listener = new TelemetryDataListener(this);
		(new Thread(tel_listener)).start();

		//listenToTelemetryData();

		/*
		Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				do {
					try {
						System.out.println("Establishing connection with server.");
						servSocket = new Socket(InetAddress.getLocalHost(), UAVServer.APP_PORT_NUM);
					} catch(Exception e) {
						continue;
					}
					break;
				} while(!timeToExit);

				ObjectInputStream telemetryStream = new ObjectInputStream(servSocket.getInputStream());
				while(!timeToExit) {
					double airTemp;
					try {
						airTemp = telemetryStream.readDouble();
						System.out.println("Tried to read.");
						updateAirTemp(airTemp);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
				}
				return null;
			}
		};
		*/
	}

	private void listenToTelemetryData() {
		/*
		int pullAttempts = 0;

		while(true) {
			if(pullAttempts++ % 100000 == 0) {
				System.out.println("Pull attmept #" + pullAttempts);
				Platform.runLater(() -> airTempTest());
			}
		}
		*/

		/*
		while(telemetryStream != null) {
			if(pullAttempts++ % 1000 == 0) {
				System.out.println("Pull attempt #" + pullAttempts);
			}
			if(isAirTempUpdateAvailable() == true) {
				Platform.runLater(() -> pullData());
			}
		}
		*/

		/*
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Waking up.");
		Platform.runLater(() -> pullData());
		*/

		/*
		while(!timeToExit) {
			try {
				Thread.sleep(1500);
				if(pullAttempts++ % 100 == 0) {
					System.out.println("Attempting pull #" + pullAttempts);
				}
				Platform.runLater(() -> pullData());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
	}

	/*
	private void airTempTest() {
		Random rnd = new Random();
		tel_controller.updateAirTemp(rnd.nextDouble());
	}
	*/

	/*
	private void pullData() {
		double airTemp;
		try {
			if(telemetryStream.available() > 0) {
				airTemp = telemetryStream.readDouble();
				System.out.println("Tried to read.");
				//tel_data.setAirTemp(airTemp);
				tel_controller.updateAirTemp(airTemp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	*/

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

	protected void updateAirTemp(double air_temp) {
		//tel_data.setAirTemp(air_temp);
		tel_controller.updateAirTemp(air_temp);
	}

	/*
	public TelemetryData getTelemetryData() {
		return tel_data;
	}
	*/

	public static void main(String[] args) {
		System.out.println("java version: " + System.getProperty("java.version"));
		System.out.println("javafx version: " + System.getProperty("javafx.version"));

		Platform.setImplicitExit(false);
		/*
		tel_data.airTempProperty().addListener(new ChangeListener<Number>(){

			public void changed(ObservableValue<? extends Number> observable, Number oldValue,
					Number newValue) {
				tel_controller.updateAirTemp(newValue.doubleValue());
			}
		});
		*/
		launch(args);
		try {
			if(servSocket != null) {
				servSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tel_listener.shutDown();
		timeToExit = true;
	}

	public static boolean isAirTempUpdateAvailable() {
		return airTempUpdateAvailable;
	}

	public static void makeAirTempUpdateAvailable() {
		airTempUpdateAvailable = true;
	}
}
