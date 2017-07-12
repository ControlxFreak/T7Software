
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



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.MoveCamera;
import T7.T7Messages.Terminate;
import T7.T7Messages.ThermalRequest;
import T7.T7Messages.ConfigData;
import T7.T7Messages.ConfigData.ToggleKeys;
import T7.T7Messages.GenericMessage.MsgType;
import app.model.Snapshot;
import app.view.DataConfigurationDialogController;
import app.view.MainDisplayController;
import app.view.SnapshotExplorerController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import networking.client.UAVClient;
import networking.server.UAVServer;

public class MainApp extends Application {

	private static Logger logger			= Logger.getLogger(MainApp.class.getName());
	private static Stage primaryStage;
	private static Stage explorerStage;
	private static AnchorPane rootLayout;
	private static MainDisplayController main_controller;
	private static DataConfigurationDialogController configController;
	private static SnapshotExplorerController snapshotExplorerController;
	private static UAVServer server = new UAVServer();
	private static UAVClient camera_client = null;
	private static UAVClient config_client = null;
	private static UAVClient termination_client = null;
	private static UAVClient array_client = null;
	private static ObservableList<Snapshot> snapshotData = FXCollections.observableArrayList();
	private static boolean[] config_arr = new boolean[9];
	private static volatile long tapStart = 0;
	private static volatile int tapNum = 1;
	private static volatile int timerNum = 1;
	private static EventHandler<KeyEvent> keyHandler;

	@Override
	public void start(Stage primaryStage) {
		MainApp.primaryStage = primaryStage;
		MainApp.primaryStage.setTitle("Home Station");

		initRootLayout();

		initDataConfiguration();

		initServer();

		initClients();
	}

	private void initDataConfiguration() {		
		config_arr[ToggleKeys.toggleAccel_VALUE] = true;
		config_arr[ToggleKeys.toggleGyro_VALUE] = true;
		config_arr[ToggleKeys.toggleAltitude_VALUE] = true;
		config_arr[ToggleKeys.toggleAttitude_VALUE] = true;
		config_arr[ToggleKeys.toggleTemp_VALUE] = true;
		config_arr[ToggleKeys.toggleBat_VALUE] = true;
		config_arr[ToggleKeys.toggleArray_VALUE] = true;
		config_arr[ToggleKeys.toggleHead_VALUE] = true;
		config_arr[ToggleKeys.toggleWifi_VALUE] = true;
	}

	private void initClients() {
		camera_client = new UAVClient();
		new Thread(camera_client).start();

		config_client = new UAVClient();
		new Thread(config_client).start();

		termination_client = new UAVClient();
		new Thread(termination_client).start();
		
		array_client = new UAVClient();
		new Thread(array_client).start();
	}

	private void initServer() {
		new Thread(server).start();
	}

	private void takeSnapshot() {
		snapshotData.add(0, new Snapshot(main_controller.takeSnapshot()));
		GenericMessage.Builder gmBuilder = GenericMessage.newBuilder();
		gmBuilder.setMsgtype(MsgType.THERMAL_REQUEST.getNumber()).setTime(System.currentTimeMillis())
		.setThermalrequest(ThermalRequest.newBuilder().setRequest(true));
		try {
			array_client.sendMessage(gmBuilder.build());
		} catch(SocketException e) {
			e.printStackTrace();
			array_client.shutDown();
			array_client = new UAVClient();
			new Thread(array_client).start();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(snapshotData.get(0).getMaxThermal() == Double.MIN_VALUE) {
					System.out.println("Waiting for thermal response.");
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Platform.runLater(() -> MainApp.updatePriorities());

				Platform.runLater(() -> MainApp.showSnapshotExplorer());
			}
		}).start();
	}

	private static void showSnapshotExplorer() {
		if(getSnapshotData().isEmpty()) {
			System.out.println("No snapshots!");
			return;
		}
		
		try {
		// Load the fxml file and create a new dialog.
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(MainApp.class.getResource("view/SnapshotExplorer.fxml"));
		AnchorPane explorer = (AnchorPane) loader.load();

		explorerStage = new Stage();
		explorerStage.setTitle("Snapshot Explorer");
		explorerStage.initModality(Modality.WINDOW_MODAL);
		explorerStage.initOwner(primaryStage);
		Scene scene = new Scene(explorer);
		explorerStage.setScene(scene);

		snapshotExplorerController = loader.getController();
		System.out.println("main_controller = " + main_controller);
		snapshotExplorerController.setMainController(main_controller);

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

			configController = loader.getController();
			configController.setDialogStage(dialogStage);

			boolean[] copy_arr = config_arr.clone();

			System.out.println("array before dialog: " + config_arr.toString());
			dialogStage.showAndWait();
			System.out.println("array after dialog: " + config_arr.toString());
			for(int i = 0; i < config_arr.length; i++) {
				if(!config_arr[i]) {
					clearDisplay(ToggleKeys.forNumber(i));
				}
			}

			// Send configData messages
			for(int i = 0; i < config_arr.length; i++) {
				if(copy_arr[i] != config_arr[i]) {
					GenericMessage.Builder gmBuilder = GenericMessage.newBuilder();
					gmBuilder.setMsgtype(MsgType.CONFIG_DATA.getNumber()).setTime(System.currentTimeMillis())
					.setConfigdata(ConfigData.newBuilder().setConfigKey(i));
					config_client.sendMessage(gmBuilder.build());
				}
			}

		} catch(IOException e) {
			logger.warning("Exception when trying to show data config manager dialog: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void showUavTerminationDialog() {
		List<String> choices = new ArrayList<>();
		choices.add("Reboot Software");
		choices.add("Soft Shutdown");
		choices.add("Emergency Stop");

		ChoiceDialog<String> dialog = new ChoiceDialog<String>("Reboot Software", choices);
		dialog.setTitle("UAV Termination");
		dialog.setHeaderText("Select a termination command to send to the UAV."
				 + '\n' + '\t' + "Reboot Software:	Reboot software, but do not affect hardware."
				 + '\n' + '\t' + "Soft Shutdown:	Come to a safe landing, then shut down software."
				 + '\n' + '\t' + "Emergency Stop:	Shut down software and hardware now. WARNING - Use with caution!");
		dialog.setContentText("Command:");
		
		Node ok = dialog.getDialogPane().lookupButton(ButtonType.OK);
		ok.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.B)
				{
					dialog.close();
				}
			}
			
		});
		
		dialog.getDialogPane().setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if(event.getCode() == KeyCode.B)
				{
					ok.requestFocus();
				}
			}
			
		});
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			logger.finer("Your choice: " + result.get());
			try {
				termination_client.sendMessage(GenericMessage.newBuilder().setMsgtype(MsgType.TERMINATE_VALUE)
						.setTime(System.currentTimeMillis()).setTerminate(Terminate.newBuilder()
								.setTerminateKey(choices.indexOf(result.get()))).build());
			} catch (SocketException e) {
				e.printStackTrace();
				termination_client.shutDown();
				termination_client = new UAVClient();
				new Thread(termination_client).start();
			}
		}
	}
	
	public static void clearHeartbeat() {
		updateTelemetryDisplay(Double.MIN_VALUE, MsgType.HEARTBEAT);
	}

	public static void clearDisplay(ToggleKeys type) {
		switch(type) {
		case toggleAccel:
			updateTelemetryDisplay(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, MsgType.ACCEL);
			break;
		case toggleGyro:
			updateTelemetryDisplay(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, MsgType.GYRO);
			break;
		case toggleAltitude:
			updateTelemetryDisplay(Double.MIN_VALUE, MsgType.ALTITUDE);
			break;
		case toggleAttitude:
			updateTelemetryDisplay(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE, MsgType.ATTITUDE);
			break;
		case toggleTemp:
			updateTelemetryDisplay(Double.MIN_VALUE, MsgType.TEMP);
			break;
		case toggleBat:
			updateTelemetryDisplay(Double.MIN_VALUE, MsgType.BAT);
			break;
		case toggleHead:
			updateTelemetryDisplay(Double.MIN_VALUE, MsgType.HEAD);
			break;
		case toggleWifi:
			updateTelemetryDisplay(Double.MIN_VALUE, MsgType.WIFI);
			break;
		default:
			break;
		}
	}

	private void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			logger.finest("RootLayout resource = " + getClass().getResource("view/MwRootLayout.fxml"));
			loader.setLocation(getClass().getResource("view/MwRootLayout.fxml"));
			logger.finest("RootLayout loader location=" + loader.getLocation());
			rootLayout = (AnchorPane) loader.load();
			main_controller = loader.getController();
			logger.finest("Got main_controller.");

			Scene scene = new Scene(rootLayout);

			// Create event handler for keys.
			keyHandler = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					logger.finest("Key event: " + e.getText());
					System.out.println("Key event: " + e.getText() + " - " + e.getEventType());
					GenericMessage.Builder gmBuilder = GenericMessage.newBuilder();
					if(e.getEventType() == KeyEvent.KEY_PRESSED) {
						switch(e.getCode()) {
						case UP:
							logger.info("Sending camera command: " + e.getCode());
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(0));
							try {
								camera_client.sendMessage(gmBuilder.build());
							} catch (SocketException e2) {
								e2.printStackTrace();
								camera_client.shutDown();
								camera_client = new UAVClient();
								new Thread(camera_client).start();
							}
							break;
						case RIGHT:
							logger.info("Sending camera command: " + e.getCode());
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(1));
							try {
								camera_client.sendMessage(gmBuilder.build());
							} catch (SocketException e2) {
								e2.printStackTrace();
								camera_client.shutDown();
								camera_client = new UAVClient();
								new Thread(camera_client).start();
							}
							break;
						case DOWN:
							logger.info("Sending camera command: " + e.getCode());
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(2));
							try {
								camera_client.sendMessage(gmBuilder.build());
							} catch (SocketException e2) {
								e2.printStackTrace();
								camera_client.shutDown();
								camera_client = new UAVClient();
								new Thread(camera_client).start();
							}
							break;
						case LEFT:
							logger.info("Sending camera command: " + e.getCode());
							gmBuilder.setMsgtype(MsgType.MOVE_CAMERA.getNumber()).setTime(System.currentTimeMillis())
							.setMovecamera(MoveCamera.newBuilder().setArrowKey(3));
							try {
								camera_client.sendMessage(gmBuilder.build());
							} catch (SocketException e2) {
								e2.printStackTrace();
								camera_client.shutDown();
								camera_client = new UAVClient();
								new Thread(camera_client).start();
							}
							break;
						case B:
							tapStart = System.currentTimeMillis();
							new Thread(new Runnable() {

								@Override
								public void run() {
									int currentTap = timerNum++;
									try {
										Thread.sleep(500);
									} catch (InterruptedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									if(currentTap == tapNum) {
										Platform.runLater(() -> MainApp.footswitchLaunch());
									}
								}
							}).start();
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
						case S:
							takeSnapshot();
							break;
						case T:
							showUavTerminationDialog();
							break;
						case B:
							++tapNum;
							timerNum = tapNum;
							long tapEnd = System.currentTimeMillis();
							if(tapEnd - tapStart < 700) {
								main_controller.spinKey();
							}
							break;
						default:
							break;
						}
					}
				}
			};
			scene.addEventFilter(KeyEvent.ANY, keyHandler);
			main_controller.setup();
			primaryStage.setScene(scene);
			logger.finest("Set scene for Main Display.");
			primaryStage.show();
			logger.finest("Showed Main Display.");
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

	public static void updateTelemetryDisplay(double datumX, double datumY, double datumZ, MsgType type) {
		main_controller.updateVectorData(datumX, datumY, datumZ, type);
	}

	public static void updateSnapshotThermalReading(double response) {
		snapshotData.get(0).setMaxThermal(response);
	}

	public void updateSnapshotDisplay(Snapshot snap) {

	}

	public static ObservableList<Snapshot> getSnapshotData() {
		System.out.println("getting Snapshot Data");
		return snapshotData;
	}

	public static void main(String[] args) {
		FileHandler handler = null;
		SimpleFormatter sf = null;
		try {
			handler = new FileHandler("mission_logs/telemetry%u.log", 100000000, 10, false);
			sf = new SimpleFormatter();
			handler.setFormatter(sf);
			Logger.getLogger("").addHandler(handler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MainApp Logger: " + logger.getName());
		launch(args);

		server.shutDown();
		camera_client.shutDown();
		config_client.shutDown();
		termination_client.shutDown();
		array_client.shutDown();
		main_controller.stopTimer();
		try {
			serializeSnapshots();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		handler.close();
	}

	private static void serializeSnapshots() throws IOException {
		FileOutputStream fileOut =
				new FileOutputStream("snapshot_data/employee.ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		
		for(Snapshot snap : snapshotData) {
			out.writeObject(snap);
		}
		
		out.close();
		fileOut.close();
		logger.info("Finished serializing snapshots.");
	}

	public static boolean[] getConfigArr() {
		return config_arr;
	}

	public static Boolean isDataTypeUnpaused(MsgType type) {
		switch(type) {
		case ACCEL:
			return config_arr[ToggleKeys.toggleAccel_VALUE];
		case GYRO:
			return config_arr[ToggleKeys.toggleGyro_VALUE];
		case ALTITUDE:
			return config_arr[ToggleKeys.toggleAltitude_VALUE];
		case ATTITUDE:
			return config_arr[ToggleKeys.toggleAttitude_VALUE];
		case TEMP:
			return config_arr[ToggleKeys.toggleTemp_VALUE];
		case BAT:
			return config_arr[ToggleKeys.toggleBat_VALUE];
		case HEAD:
			return config_arr[ToggleKeys.toggleHead_VALUE];
		case THERMAL_RESPONSE:
			return true;
		case HEARTBEAT:
			return true;
		case WIFI:
			return config_arr[ToggleKeys.toggleWifi_VALUE];
		default:
			throw new IllegalArgumentException("Illegal MsgType: " + type);
		}
	}
	
	public static void footswitchCycle() {
		
	}
	
	public static void footswitchLaunch() {
		KeyCode code = main_controller.getKeySpinner().getKey();
		KeyEvent artificialEvent =
				new KeyEvent(KeyEvent.KEY_RELEASED,
						code.getName(),
						"Forwarded from foot switch.",
						code,
						false, false, false, false);
		keyHandler.handle(artificialEvent);
	}
	
	public static void updatePriorities() {
		ArrayList<Snapshot> orderedSnaps = new ArrayList<>();
		orderedSnaps.addAll(snapshotData);
		for(int i = 0; i < orderedSnaps.size(); i++) {
			Snapshot s = orderedSnaps.get(i);
			if(!s.isTarget()) {
				orderedSnaps.remove(i);
			}
		}
		Collections.sort(orderedSnaps);
		
		for(int i = 0; i < orderedSnaps.size(); i++) {
			orderedSnaps.get(i).setRelativePriority(orderedSnaps.size() - i);
		}
		
		main_controller.updateEmbeddedSnap();
	}

	public static Stage getExplorerStage() {
		return explorerStage;
	}

	public static SnapshotExplorerController getSnapshotExplorerController() {
		return snapshotExplorerController;
	}

	public static DataConfigurationDialogController getConfigController() {
		return configController;
	}
}
