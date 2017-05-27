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

import java.util.logging.Logger;

import app.MainApp;
import app.model.Snapshot;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

public class SnapshotExplorerController {

	private static Logger logger					= Logger.getLogger(SnapshotExplorerController.class.getName());
	private MainDisplayController main_controller;

	@FXML
	private ImageView imageDisplay;
	@FXML
	private TextField descriptionField;
	@FXML
	private Spinner<Integer> prioritySpinner;
	@FXML
	private TextField timestampField;
	@FXML
	private TextArea notesArea;
	@FXML
	private ListView<Snapshot> thumbnails;

	@FXML
	private void initialize() {
		IntegerSpinnerValueFactory factory = new IntegerSpinnerValueFactory(1, 10);
		prioritySpinner.setValueFactory(factory);
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
			thumbnails.getSelectionModel().select(0);
			showSnapshotDetails(thumbnails.getItems().get(0));
		} else {
			showSnapshotDetails(null);
		}
		thumbnails.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showSnapshotDetails(newValue));
	}

	@FXML
	private void handleDelete() {
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		MainApp.getSnapshotData().remove(index);
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
		snap.setNotes(notesArea.getText());
		snap.setPriority(prioritySpinner.getValue());
	}

	@FXML
	private void handleDisplay() {
		int index = thumbnails.getSelectionModel().getSelectedIndex();
		Snapshot snap = MainApp.getSnapshotData().get(index);
		
		main_controller.displaySnapshot(snap);
	}

	private void showSnapshotDetails(Snapshot snap) {
		boolean imagePresent = false;
		if(snap == null) {
			logger.warning("Tried to show null snapshot.");
			imageDisplay.setImage(null);
			descriptionField.setText("");
			prioritySpinner.getValueFactory().setValue(1);
			timestampField.setText("");
			notesArea.setText("");
		} else {
			imagePresent = true;
			imageDisplay.setImage(snap.getImage());
			descriptionField.setText(snap.getDescription());
			prioritySpinner.getValueFactory().setValue(snap.getPriority());
			timestampField.setText(snap.getTimestamp().toString());
			notesArea.setText(snap.getNotes());
		}
		descriptionField.setEditable(imagePresent);
		prioritySpinner.setEditable(imagePresent);
		notesArea.setEditable(imagePresent);
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

