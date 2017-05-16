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

import T7.T7Messages.GenericMessage.MsgType;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainDisplayController {

	/*
	public void updateDatum(double d, MsgType type) {
		String newVal = doubleDatumToLabelString(d);

		switch(type) {
		case TEMP:
			airTempLabel.setText(newVal);
			break;
		case ALTITUDE:
			altitudeLabel.setText(newVal);
			break;
		default:
			break;
		}
	}

	public void updateVectorDatum(double[] datum, MsgType type) {
		String x = "";
		String y = "";
		String z = "";

		switch(type) {
		case ACCEL:
			xAccelLabel.setText(x);
			yAccelLabel.setText(y);
			zAccelLabel.setText(z);
			break;
		case GYRO:
			rollLabel.setText(x);
			pitchLabel.setText(y);
			yawLabel.setText(z);
			break;
		default:
			break;
		}
	}
	*/

	private String doubleDatumToLabelString(double d) {
		String s = "";
		if(d != Double.MIN_VALUE) {
			s = Double.toString(d);
		}
		return s;
	}
}

