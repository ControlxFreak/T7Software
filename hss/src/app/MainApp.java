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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.MoveCamera;
import T7.T7Messages.GenericMessage.MsgType;
import app.model.Snapshot;
import app.view.DataConfigurationDialogController;
import app.view.MainDisplayController;
import app.view.SnapshotExplorerController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import networking.client.UAVClient;
import networking.server.UAVServer;

public class MainApp extends Application {

	private static Logger logger			= Logger.getLogger(MainApp.class.getName());
	private Stage primaryStage;
	private Stage secondaryStage;
	private static BorderPane rootLayout;
	private static AnchorPane snapLayout;
	private static AnchorPane configLayout;
	private static MainDisplayController main_controller;
	private static SnapshotExplorerController snap_controller;
	private static DataConfigurationDialogController config_controller;
	private static FXMLLoader main_loader;
	private static UAVServer server = new UAVServer();
	private static UAVClient camera_client = null;
	private static UAVClient params_client = null;
	private static UAVClient config_client = null;
	private static ObservableList<Snapshot> snapshotData = FXCollections.observableArrayList();
	private static Map<MsgType, Boolean> configMap = new HashMap<MsgType, Boolean>();

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Home Station");

		initRootLayout();

		showMainDisplay();

		initDataConfiguration();

		initServer();

		initClients();
	}

	private void initDataConfiguration() {
		configMap.put(MsgType.ACCEL, true);
		configMap.put(MsgType.ALTITUDE, true);
		configMap.put(MsgType.ATTITUDE, true);
		configMap.put(MsgType.BAT, true);
		configMap.put(MsgType.GYRO, true);
		configMap.put(MsgType.TEMP, true);
	}

	private void initClients() {
		camera_client = new UAVClient();
		new Thread(camera_client).start();
	}

	private void initServer() {
		new Thread(server).start();
	}

	private void showSnapshotExplorer() {

	}

	private void showDataConfigManager() {
		try {
		// Load the fxml file and create a new dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/DataConfiguration.fxml"));
		AnchorPane dialog = (AnchorPane) loader.load();

		Stage dialogStage = new Stage();
		dialogStage.setTitle("Data Configuration Manager");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.initOwner(primaryStage);
		Scene scene = new Scene(dialog);
		dialogStage.setScene(scene);

		DataConfigurationDialogController controller = loader.getController();
		controller.setDialogStage(dialogStage);

		/*
		// Create the dialog
		Dialog<Map<MsgType, Boolean>> dialog = new Dialog<Map<MsgType, Boolean>>();
		dialog.setDialogPane(dPane);
		dialog.initOwner(primaryStage);
		*/

		//DataConfigurationDialogController controller = loader.getController();

		System.out.println("hashmap before dialog: " + configMap.toString());
		dialogStage.showAndWait();
		System.out.println("hashmap after dialog: " + configMap.toString());
		/*
		Optional<Map<MsgType, Boolean>> result = dialog.showAndWait();
		if(result.isPresent()) {
			setConfig(result.get());
		}
		*/
		} catch(IOException e) {
			logger.warning("Exception when trying to show data config manager dialog: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void setConfig(Map<MsgType, Boolean> configMap) {
		MainApp.configMap = configMap;
	}

	private void showMainDisplay() {
		try {
			//Load main display.
			main_loader = new FXMLLoader();
			System.out.println("TelData resource = " + getClass().getResource("view/TelemetryDataOverview.fxml"));
			main_loader.setLocation(getClass().getResource("view/TelemetryDataOverview.fxml"));
			System.out.println("TelDataOverview loader location= " + main_loader.getLocation());
			AnchorPane telemetryOverview = null;
			telemetryOverview = (AnchorPane) main_loader.load();

			// Set telemetry overview into the center of root layout.
			rootLayout.setLeft(telemetryOverview);

			main_controller = (MainDisplayController)main_loader.getController();
			System.out.println("TEL_CONTROLLER = " + main_controller);
		} catch (IOException e) {
			logger.fine(e.toString());
		}
	}

	private void initSnapLayout() {

	}

	private void initConfigLayout() {

	}

	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			System.out.println("RootLayout resource = " + getClass().getResource("view/RootLayout.fxml"));
			loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
			System.out.println("RootLayout loader location=" + loader.getLocation());
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);

			// Create event handler for arrow keys.
			EventHandler<KeyEvent> keyHandler = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					logger.finest("Key event: " + e.getText());
					System.out.println("Key event: " + e.getText() + " - " + e.getEventType());
					GenericMessage.Builder gmBuilder = GenericMessage.newBuilder();
					if(e.getEventType() == KeyEvent.KEY_PRESSED) {
						switch(e.getCode()) {
						case UP:
						case DOWN:
						case LEFT:
						case RIGHT:
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setTbd(true));
							camera_client.sendMessage(gmBuilder.build());
							break;
						default:
							break;
						}
					} else if (e.getEventType() == KeyEvent.KEY_RELEASED) {
						switch(e.getCode()) {
						case C:
							showDataConfigManager();
							break;
						default:
							break;
						}
					}
				}
			};
			scene.addEventFilter(KeyEvent.ANY, keyHandler);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			logger.fine(e.toString());
		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void updateTelemetryDisplay(double datum, MsgType type) {
		main_controller.updateDatum(datum, type);
	}

	public void updateTelemetryDisplay(double[] data, MsgType type) {
		main_controller.updateVectorDatum(data, type);
	}

	public void updateSnapshotDisplay(Snapshot snap) {

	}

	public ObservableList<Snapshot> getSnapshotData() {
		return snapshotData;
	}

	public static void main(String[] args) {
		launch(args);

		server.shutDown();
		camera_client.shutDown();
	}

	public static Map<MsgType, Boolean> getConfigMap() {
		return configMap;
	}
}
