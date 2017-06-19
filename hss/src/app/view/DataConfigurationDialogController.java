/*
 * ---------------------------------------------------------------------------------
 * Title: DataConfigurationDialogController.java
 * Description:
 * The controller class for the data configuration dialog.
 * ---------------------------------------------------------------------------------
 * Lockheed Martin
 * Engineering Leadership Development Program
 * Team 7
 * 6 May 2017
 * Jarrett Mead
 * ---------------------------------------------------------------------------------
 * Change Log
 * 	6 May 2017 - Jarrett Mead - Class Birthday
 * ---------------------------------------------------------------------------------
 */
package app.view;

import app.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import T7.T7Messages.ConfigData.ToggleKeys;

public class DataConfigurationDialogController {

	@FXML
	private CheckBox acceleration;
	@FXML
	private CheckBox altitude;
	@FXML
	private CheckBox attitude;
	@FXML
	private CheckBox battery;
	@FXML
	private CheckBox gyroscope;
	@FXML
	private CheckBox heading;
	@FXML
	private CheckBox temperature;
	@FXML
	private CheckBox thermal;
	@FXML
	private CheckBox range;

	private boolean[] config_arr = MainApp.getConfigArr();
	private Stage dialogStage;

	@FXML
	private void initialize() {
		acceleration.setSelected(config_arr[ToggleKeys.toggleAccel_VALUE]);
		gyroscope.setSelected(config_arr[ToggleKeys.toggleGyro_VALUE]);
		altitude.setSelected(config_arr[ToggleKeys.toggleAltitude_VALUE]);
		attitude.setSelected(config_arr[ToggleKeys.toggleAttitude_VALUE]);
		temperature.setSelected(config_arr[ToggleKeys.toggleTemp_VALUE]);
		battery.setSelected(config_arr[ToggleKeys.toggleBat_VALUE]);
		thermal.setSelected(config_arr[ToggleKeys.toggleArray_VALUE]);
		heading.setSelected(config_arr[ToggleKeys.toggleHead_VALUE]);
		range.setSelected(config_arr[ToggleKeys.toggleWifi_VALUE]);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleOk() {
		config_arr[ToggleKeys.toggleAccel_VALUE] = acceleration.isSelected();
		config_arr[ToggleKeys.toggleGyro_VALUE] = gyroscope.isSelected();
		config_arr[ToggleKeys.toggleAltitude_VALUE] = altitude.isSelected();
		config_arr[ToggleKeys.toggleAttitude_VALUE] = attitude.isSelected();
		config_arr[ToggleKeys.toggleTemp_VALUE] = temperature.isSelected();
		config_arr[ToggleKeys.toggleBat_VALUE] = battery.isSelected();
		config_arr[ToggleKeys.toggleArray_VALUE] = thermal.isSelected();
		config_arr[ToggleKeys.toggleHead_VALUE] = heading.isSelected();
		config_arr[ToggleKeys.toggleWifi_VALUE] = range.isSelected();

		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}

