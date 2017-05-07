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

import java.util.Map;

import T7.T7Messages.GenericMessage.MsgType;

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
	private CheckBox temperature;

	private Map<MsgType, Boolean> configMap = MainApp.getConfigMap();
	private Stage dialogStage;

	@FXML
	private void initialize() {
		acceleration.setSelected(configMap.get(MsgType.ACCEL));
		altitude.setSelected(configMap.get(MsgType.ALTITUDE));
		attitude.setSelected(configMap.get(MsgType.ATTITUDE));
		battery.setSelected(configMap.get(MsgType.BAT));
		gyroscope.setSelected(configMap.get(MsgType.GYRO));
		temperature.setSelected(configMap.get(MsgType.TEMP));
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	@FXML
	private void handleOk() {
		configMap.put(MsgType.ACCEL, acceleration.isSelected());
		configMap.put(MsgType.ALTITUDE, altitude.isSelected());
		configMap.put(MsgType.ATTITUDE, attitude.isSelected());
		configMap.put(MsgType.BAT, battery.isSelected());
		configMap.put(MsgType.GYRO, gyroscope.isSelected());
		configMap.put(MsgType.TEMP, temperature.isSelected());

		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}

