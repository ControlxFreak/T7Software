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



import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import app.org.multiwii.swingui.gui.MwConfiguration;
import app.org.multiwii.swingui.gui.MwGuiFrame;

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
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import app.org.multiwii.swingui.gui.MwConfiguration;
import networking.client.UAVClient;
import networking.server.UAVServer;

public class MainApp extends Application {

	private static Logger logger			= Logger.getLogger(MainApp.class.getName());
	private Stage primaryStage;
	private Stage secondaryStage;
	private static AnchorPane rootLayout;
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

		//testInitSnapshot();
		testInitSnapshot2();

		initServer();

		initClients();
	}

	private void testInitSnapshot() {
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/kelly.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/jessie.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/topanga.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/dani.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/lenna.png")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/alexandra.gif")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/audrey.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/shelly.gif")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/dolores.jpg")).toURI().toString())));
			snapshotData.add(new Snapshot(new Image((new File("/home/jarrett/Downloads/gina.jpg")).toURI().toString())));
	}

	private void testInitSnapshot2() {
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/fire1.jpg").toURI().toString())));
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/fire2.jpeg").toURI().toString())));
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/fire3.jpg").toURI().toString())));
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/landscape1.jpg").toURI().toString())));
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/landscape2.jpg").toURI().toString())));
		snapshotData.add(new Snapshot(new Image(new File("/home/jarrett/Downloads/landscape3.jpg").toURI().toString())));
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

	private void takeSnapshot() {
		snapshotData.add(0, new Snapshot(new Image((new File("/home/jarrett/Downloads/topanga.jpg")).toURI().toString())));

		showSnapshotExplorer();
	}

	private void showSnapshotExplorer() {
		try {
		// Load the fxml file and create a new dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/SnapshotExplorer.fxml"));
		AnchorPane explorer = (AnchorPane) loader.load();

		Stage explorerStage = new Stage();
		explorerStage.setTitle("Snapshot Explorer");
		explorerStage.initModality(Modality.WINDOW_MODAL);
		explorerStage.initOwner(primaryStage);
		Scene scene = new Scene(explorer);
		explorerStage.setScene(scene);

		/*
		SnapshotExplorerController controller = loader.getController();
		controller.setDialogStage(explorerStage);
		*/

		System.out.println("list before dialog: " + snapshotData.toString());
		explorerStage.showAndWait();
		} catch(IOException e) {
			logger.warning("Exception when trying to show snapshot explorer: " + e.getMessage());
			e.printStackTrace();
		}
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

		System.out.println("hashmap before dialog: " + configMap.toString());
		dialogStage.showAndWait();
		System.out.println("hashmap after dialog: " + configMap.toString());
		for(MsgType type : configMap.keySet()) {
			if(!configMap.get(type)) {
				clearDisplay(type);
			}
		}
		} catch(IOException e) {
			logger.warning("Exception when trying to show data config manager dialog: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void clearDisplay(MsgType type) {
		switch(type) {
		case TEMP:
		case ATTITUDE:
		case BAT:
		case ALTITUDE:
			updateTelemetryDisplay(Double.MIN_VALUE, type);
			break;
		case GYRO:
		case ACCEL:
			updateTelemetryDisplay(new double[]{Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE}, type);
			break;
		default:
			break;
		}
	}

	private static void setConfig(Map<MsgType, Boolean> configMap) {
		MainApp.configMap = configMap;
	}

	private void showMainDisplay() {
		SwingNode swingNode = new SwingNode();

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				MwConfiguration.setLookAndFeel();

				MwGuiFrame frame = new MwGuiFrame(new MwConfiguration());
				swingNode.setContent(frame.getRealTimePanel());
				frame.setVisible(true);
				frame.repaint();
			}

		});

		AnchorPane.setTopAnchor(swingNode, 0.0);
		AnchorPane.setLeftAnchor(swingNode, 0.0);
		AnchorPane.setRightAnchor(swingNode, 0.0);
		AnchorPane.setBottomAnchor(swingNode, 0.0);
		rootLayout.getChildren().add(swingNode);

		/*
		main_controller = (MainDisplayController)main_loader.getController();
		System.out.println("TEL_CONTROLLER = " + main_controller);
		*/
	}

	private void initSnapLayout() {

	}

	private void initConfigLayout() {

	}

	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			System.out.println("RootLayout resource = " + getClass().getResource("view/MwRootLayout.fxml"));
			loader.setLocation(getClass().getResource("view/MwRootLayout.fxml"));
			System.out.println("RootLayout loader location=" + loader.getLocation());
			rootLayout = (AnchorPane) loader.load();

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
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(0));
							camera_client.sendMessage(gmBuilder.build());
							break;
						case RIGHT:
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(1));
							camera_client.sendMessage(gmBuilder.build());
							break;
						case DOWN:
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(2));
							camera_client.sendMessage(gmBuilder.build());
							break;
						case LEFT:
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(3));
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
						case E:
							showSnapshotExplorer();
							break;
						case T:
							takeSnapshot();
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
		//main_controller.updateDatum(datum, type);
	}

	public static void updateTelemetryDisplay(double[] data, MsgType type) {
		//main_controller.updateVectorDatum(data, type);
	}

	public void updateSnapshotDisplay(Snapshot snap) {

	}

	public static ObservableList<Snapshot> getSnapshotData() {
		System.out.println("getting Snapshot Data");
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

	public static Boolean isDataTypeUnpaused(MsgType type) {
		return configMap.get(type);
	}
}
