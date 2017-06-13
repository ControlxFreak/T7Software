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

import app.KeySpinner;
import app.MainApp;
import app.model.Animal;
import app.model.Snapshot;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import javafx.util.Callback;

public class SnapshotExplorerController {

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
	private TextField priorityField;
	@FXML
	private ListView<Snapshot> thumbnails;
	@FXML
	private VBox targetBox;
	@FXML
	private ChoiceBox<Animal> animalBox;
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
	
	private KeySpinner keySpinner;

	@FXML
	private void initialize() {

		KeyCode[] key_arr = {KeyCode.S, KeyCode.E, KeyCode.C, KeyCode.T};
		Image[] image_arr = {new Image((new File("src/main/resources/images/default/camera.png")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/polaroid.jpg")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/configuration.png")).toURI().toString()),
				new Image((new File("src/main/resources/images/default/power.png")).toURI().toString())};
		keySpinner = new KeySpinner(new ArrayList<KeyCode>(Arrays.asList(key_arr)),
				new ArrayList<Image>(Arrays.asList(image_arr)));

		ObservableList<Snapshot> snapList = MainApp.getSnapshotData();
		
		targetRadio.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent ke)
			{
				System.out.println("Key typed: " + ke.getCode());
				if(ke.getCode() == KeyCode.ENTER)
				{
					handleTargetSelection();
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
					handleNonTargetSelection();
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
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		Snapshot snap = MainApp.getSnapshotData().get(index);
		
		main_controller.displaySnapshot(snap);
	}
	
	@FXML
	private void handleTargetSelection() {
		
		targetRadio.setSelected(true);
		nonTargetRadio.setSelected(false);
		targetBox.setVisible(true);
		priorityField.setVisible(true);
	}
	
	@FXML
	private void handleNonTargetSelection() {
		targetRadio.setSelected(false);
		nonTargetRadio.setSelected(true);
		targetBox.setVisible(false);
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

