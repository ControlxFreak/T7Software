/*
 * ---------------------------------------------------------------------------------
 * Title: TelemetryDataOverviewController.java
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

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TelemetryDataOverviewController {
	@FXML
	private Label airTempLabel;

	@FXML
	private void initialize() {
		System.out.println("initialize label = " + airTempLabel);
	}

	public void updateAirTemp(double airTemp) {
		String newVal = "";
		if(airTemp != Double.MIN_VALUE) {
			newVal = Double.toString(airTemp);
		}
		airTempLabel.setText(newVal);
	}
}

