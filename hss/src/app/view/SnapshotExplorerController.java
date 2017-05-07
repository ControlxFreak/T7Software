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

import app.MainApp;
import app.model.Snapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SnapshotExplorerController {

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

	}

	public void setMainApp(MainApp mainApp) {

	}
}

