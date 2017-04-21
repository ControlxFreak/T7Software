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
		AIR_TEMP, ALTITUDE, ACCELEROMETER, GYROSCOPE
	}

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

	public void updateVectorDatum(byte[] datum, dataType type) {
		String x = "";
		String y = "";
		String z = "";

		switch(type) {
		case ACCELEROMETER:
			xAccelLabel.setText(x);
			yAccelLabel.setText(y);
			zAccelLabel.setText(z);
			break;
		case GYROSCOPE:
			rollLabel.setText(x);
			pitchLabel.setText(y);
			yawLabel.setText(z);
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

