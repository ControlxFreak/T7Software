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
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.image.ImageView;

public class SnapshotExplorerController {

	private static Logger logger			= Logger.getLogger(SnapshotExplorerController.class.getName());

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
	private void initialize() {
		IntegerSpinnerValueFactory factory = new IntegerSpinnerValueFactory(1, 10);
		prioritySpinner.setValueFactory(factory);
		Snapshot snapshot = MainApp.getSnapshotData().get(0);
		System.out.println("snapshot image = " + snapshot.getImage().toString());
		showSnapshotDetails(snapshot);
	}

	@FXML
	private void handleDelete() {

	}

	@FXML
	private void handleExit() {

	}

	@FXML
	private void handleInfoUpdate() {

	}

	private void showSnapshotDetails(Snapshot snap) {
		if(snap == null) {
			logger.warning("Tried to show null snapshot.");
		}
		imageDisplay.setImage(snap.getImage());
		descriptionField.setText(snap.getDescription());
		prioritySpinner.getValueFactory().setValue(snap.getPriority());
		timestampField.setText(snap.getTimestamp().toString());
		notesArea.setText(snap.getNotes());
	}

	public void setMainApp(MainApp mainApp) {

	}
}

