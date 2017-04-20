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
	public enum dataType {
		AIR_TEMP, ALTITUDE
	}

	@FXML
	private Label airTempLabel;
	@FXML
	private Label altitudeLabel;

	@FXML
	private void initialize() {
		System.out.println("initialize label = " + airTempLabel);
	}

	public void updateTelemetryDatum(double d, dataType type) {
		String newVal = telemetryDatumToLabelString(d);

		switch(type) {
		case AIR_TEMP:
			airTempLabel.setText(newVal);
			break;
		case ALTITUDE:
			altitudeLabel.setText(newVal);
			break;
		default:
			break;
		}
	}

	private String telemetryDatumToLabelString(double d) {
		String s = "";
		if(d != Double.MIN_VALUE) {
			s = Double.toString(d);
		}
		return s;
	}
}

