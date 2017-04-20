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
package app.model;

import java.io.Serializable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class TelemetryData implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final DoubleProperty air_temp = new SimpleDoubleProperty();

	public double getAirTemp() {
		return air_temp.doubleValue();
	}

	public void setAirTemp(double air_temp) {
		System.out.println("Setting air_temp to " + air_temp);
		this.air_temp.set(air_temp);
	}

	public DoubleProperty airTempProperty() {
		return air_temp;
	}
}
