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

public class SnapshotExplorerController {

	@FXML
	private Label airTempLabel;
	@FXML
	private Label altitudeLabel;
	@FXML
	private Label xAccelLabel;
	@FXML
	private Label yAccelLabel;
	@FXML
	private Label zAccelLabel;
	@FXML
	private Label rollLabel;
	@FXML
	private Label pitchLabel;
	@FXML
	private Label yawLabel;

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

