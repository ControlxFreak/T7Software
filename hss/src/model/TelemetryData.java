/*
 * ---------------------------------------------------------------------------------
 * Title: TelemetryData.java
 * Description:
 * The model class for the telemetry data overview GUI.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 09 April 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	09 April 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class TelemetryData {

	private final DoubleProperty air_temp;

	public TelemetryData() {
		air_temp = new SimpleDoubleProperty(0.0);
	}

	public Double getAirTemp() {
		return air_temp.get();
	}

	public DoubleProperty getAirTempProperty() {
		return air_temp;
	}

	public void setAirTemp(Double air_temp) {
		this.air_temp.set(air_temp);
	}
}
