/*
 * ---------------------------------------------------------------------------------
 * Title: MainDisplayController.java
 * Description:
 * The controller class for the telemetry data overview GUI.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 15 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	15 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import T7.T7Messages.GenericMessage;
import T7.T7Messages.MoveCamera;
import T7.T7Messages.GenericMessage.MsgType;
import app.KeySpinner;
import app.MainApp;
import app.model.Animal;
import app.model.Snapshot;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SnapshotExplorerController extends Tapper {

	private static Logger logger					= Logger.getLogger(SnapshotExplorerController.class.getName());
	private MainDisplayController main_controller;

	@FXML
	private RadioButton targetRadio;
	@FXML
	private RadioButton nonTargetRadio;
	@FXML
	private ImageView imageDisplay;
	@FXML
	private TextField descriptionField;
	@FXML
	private TextField timestampField;
	@FXML
	private TextField notesField;
	@FXML
	private Label priorityLabel;
	@FXML
	private TextField priorityField;
	@FXML
	private ListView<Snapshot> thumbnails;
	/*
	@FXML
	private VBox targetBox;
	*/
	@FXML
	private Label animalLabel;
	@FXML
	private ChoiceBox<Animal> animalBox;
	@FXML
	private Label qtyLabel;
	@FXML
	private Spinner<Integer> qtySpinner;
	@FXML
	private HBox controlsBox;
	@FXML
	private Button resetButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button updateButton;
	@FXML
	private Button displayButton;
	@FXML
	private Button exitButton;
	
	private KeySpinner keySpinner;
	private static volatile boolean thumb_spinning = false;
	private volatile long tapStart = 0;
	private volatile boolean foot_down = false;
	private volatile int tapNum = 1;
	private volatile int timerNum = 1;
	//private volatile boolean singleTap = false;
	private ArrayList<Node> focusList = new ArrayList<>();

	@FXML
	private void initialize() {
		
		focusList.add(targetRadio);
		focusList.add(nonTargetRadio);
		focusList.add(animalBox);
		focusList.add(qtySpinner);
		focusList.add(resetButton);
		focusList.add(deleteButton);
		focusList.add(updateButton);
		focusList.add(displayButton);
		focusList.add(exitButton);
		focusList.add(thumbnails);

		ObservableList<Snapshot> snapList = MainApp.getSnapshotData();
		
		Tapper tapper = this;
		
		targetRadio.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					System.out.println("ENTER received, handling target selection.");
					handleTargetSelection();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, targetRadio)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});

		nonTargetRadio.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					System.out.println("ENTER received, handling non-target selection.");
					handleNonTargetSelection();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, nonTargetRadio)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});

		resetButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleReset();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, resetButton)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		deleteButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleDelete();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, deleteButton)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		updateButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleUpdate();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, updateButton)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		displayButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleDisplay();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, displayButton)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		animalBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					animalBox.show();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, animalBox)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		qtySpinner.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					if(qtySpinner.getValueFactory().getValue() == 10) {
						qtySpinner.getValueFactory().setValue(1);
					} else {
						qtySpinner.increment();
					}
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, qtySpinner)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		exitButton.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleExit();
				}
				else if(ke.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new TapTimer(tapper, exitButton)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		thumbnails.setOnKeyTyped(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCharacter());
				if(ke.getCharacter().equals("b"))
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");

						new Thread(new Runnable() {

							@Override
							public void run() {
								System.out.println("Kicking off timer.");
								try {
									Thread.sleep(400);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

								if(singleTap) {
									Platform.runLater(() -> MainApp.getSnapshotExplorerController().singleTap());
								} else {
									Platform.runLater(() -> MainApp.getSnapshotExplorerController().doubleTap());
								}
							}
						}).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
		});
		
		qtySpinner.setValueFactory(new IntegerSpinnerValueFactory(1, 10));
		animalBox.getItems().addAll(Animal.values());
		
		if(snapList.size() == 0) {
			
		controlsBox.setDisable(true);
		}
		
		thumbnails.setItems(MainApp.getSnapshotData());
		thumbnails.setCellFactory(new Callback<ListView<Snapshot>, ListCell<Snapshot>>()
		{
			@Override
			public ListCell<Snapshot> call(ListView<Snapshot> listView)
			{
				ListCell<Snapshot> cell = new ListCell<Snapshot>() {

					@Override
					protected void updateItem(Snapshot item, boolean empty) {
						super.updateItem(item, empty);
						ImageView thumbnail = new ImageView();
						thumbnail.setPreserveRatio(true);
						thumbnail.setFitHeight(150);
						thumbnail.setFitWidth(225);
						if(item != null) {
							thumbnail.setImage(item.getImage());
							setGraphic(thumbnail);
						} else {
							setGraphic(null);
						}
					}
				};
				return cell;
			}
		});
		if(thumbnails.getItems().size() > 0) {
			showSnapshotDetails(thumbnails.getItems().get(0));
			thumbnails.getSelectionModel().select(0);
		} else {
			showSnapshotDetails(null);
		}
		thumbnails.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showSnapshotDetails(newValue));
	}

	protected void doubleTap() {
		System.out.println("Running doubleTap!");
		thumb_spinning = !thumb_spinning;
	}

	protected void singleTap() {
		System.out.println("Running singleTap!");
		System.out.println("thumb_spinning = " + thumb_spinning);
		singleTap = false;
		if(thumb_spinning) {
			int index = thumbnails.getSelectionModel().getSelectedIndex();
			if(index == thumbnails.getItems().size() - 1) {
				System.out.println("Last snap!");
				System.out.println("Snap index: " + index);
				thumbnails.getSelectionModel().clearAndSelect(0);
			} else {
				System.out.println("Snap index: " + index);
				thumbnails.getSelectionModel().clearAndSelect(index + 1);
			}
		} else {
			singleTap(thumbnails);
		}
	}
	
	protected void doubleTap(Node n) {
		KeyEvent artificialEvent = new KeyEvent(KeyEvent.KEY_PRESSED,
				KeyCode.ENTER.getName(), "Forwarded from foot switch.",
				KeyCode.ENTER, false, false, false, false);
		System.out.println("About to handle artificial ENTER.");
		n.getOnKeyPressed().handle(artificialEvent);
	}
	
	protected void singleTap(Node n) {
		singleTap = false;
		
		int index = focusList.indexOf(n);
		int new_index = index+1;
		
		/*
		if(index == focusList.size() - 1) {
			focusList.get(0).requestFocus();
		} else {
			focusList.get(index + 1).requestFocus();
		}
		*/
		
		while(index != new_index) {
			if(new_index == focusList.size()) {
				new_index = 0;
			}
			
			Node target = focusList.get(new_index);
			
			if(!target.isVisible()) {
				new_index++;
				continue;
			}
			
			target.requestFocus();
			return;
		}
	}

	@FXML
	private void handleDelete() {
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		ObservableList<Snapshot> snap_list = MainApp.getSnapshotData();
		main_controller.checkDeleteDisplayedSnapshot(snap_list.get(index));
		snap_list.remove(index);
		MainApp.updatePriorities();
		
		if(snap_list.size() == 0) {
			
		controlsBox.setDisable(true);
		}
	}

	@FXML
	private void handleReset() {
		showSnapshotDetails(thumbnails.getItems().get(thumbnails.getSelectionModel().getSelectedIndex()));
	}

	@FXML
	private void handleUpdate() {
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		Snapshot snap = MainApp.getSnapshotData().get(index);

		snap.setDescription(descriptionField.getText());
		snap.setNotes(notesField.getText());
		
		//main_controller.displaySnapshot(snap);

		snap.setTarget(targetRadio.isSelected());
		snap.setAnimal(animalBox.getValue());
		snap.setAnimalQty(qtySpinner.getValue());

		MainApp.updatePriorities();
		if(snap.isTarget()) {
			handleTargetSelection();
		} else {
			handleNonTargetSelection();
		}
		
		priorityField.setText(Integer.toString(snap.getRelativePriority()));
		System.out.println("main_controller = " + main_controller);
	}

	@FXML
	private void handleDisplay() {
		handleUpdate();
		
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		Snapshot snap = MainApp.getSnapshotData().get(index);
		
		main_controller.displaySnapshot(snap);
	}

	@FXML
	private void handleExit() {
		Stage stage = MainApp.getExplorerStage();
		stage.close();
	}
	
	@FXML
	private void handleTargetSelection() {
		
		targetRadio.setSelected(true);
		nonTargetRadio.setSelected(false);
		animalLabel.setVisible(true);
		animalBox.setVisible(true);
		qtySpinner.setVisible(true);
		qtyLabel.setVisible(true);
		priorityLabel.setVisible(true);
		priorityField.setVisible(true);
	}
	
	@FXML
	private void handleNonTargetSelection() {
		targetRadio.setSelected(false);
		nonTargetRadio.setSelected(true);
		animalLabel.setVisible(false);
		animalBox.setVisible(false);
		qtyLabel.setVisible(false);
		qtySpinner.setVisible(false);
		priorityLabel.setVisible(false);
		priorityField.setVisible(false);
	}

	private void showSnapshotDetails(Snapshot snap) {
		boolean imagePresent = false;
		if(snap == null) {
			logger.warning("Tried to show null snapshot.");
			imageDisplay.setImage(null);
			descriptionField.setText("");
			priorityField.setText("");
			timestampField.setText("");
			notesField.setText("");
			animalBox.setValue(Animal.UNKNOWN);
			qtySpinner.getValueFactory().setValue(1);
			handleNonTargetSelection();
		} else {
			imagePresent = true;
			imageDisplay.setImage(snap.getImage());
			if(snap.isTarget()) {
				handleTargetSelection();
			} else {
				handleNonTargetSelection();
			}
			descriptionField.setText(snap.getDescription());
			timestampField.setText(snap.getTimestamp().toString());
			notesField.setText(snap.getNotes());
			if(snap.getRelativePriority() == -1) {
				priorityField.setText(null);
			} else {
				priorityField.setText(Integer.toString(snap.getRelativePriority()));
			}
			animalBox.setValue(snap.getAnimal());
			if(qtySpinner.getValueFactory() != null) {
				qtySpinner.getValueFactory().setValue(snap.getAnimalQty());
			}
		}
		descriptionField.setEditable(imagePresent);
		notesField.setEditable(imagePresent);
		centerImage();
	}

	private void centerImage() {
		Image img = imageDisplay.getImage();
		if(img != null) {
			double w = 0;
			double h = 0;

			double ratioX = imageDisplay.getFitWidth() / img.getWidth();
			double ratioY = imageDisplay.getFitHeight() / img.getHeight();

			double reducCoeff = 0;
			if(ratioX >= ratioY) {
				reducCoeff = ratioY;
			} else {
				reducCoeff = ratioX;
			}

			w = img.getWidth() * reducCoeff;
			h = img.getHeight() * reducCoeff;

			imageDisplay.setX((imageDisplay.getFitWidth() - w) / 2 + 3);
			imageDisplay.setY((imageDisplay.getFitHeight() - h) / 2 + 3);
		}
	}
	
	public void setMainController(MainDisplayController main_controller) {
		this.main_controller = main_controller;
	}
}

