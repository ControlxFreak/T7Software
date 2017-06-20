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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

import T7.T7Messages.ConfigData.ToggleKeys;

public class DataConfigurationDialogController extends Tapper {

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
	@FXML
	private Button cancel;
	@FXML
	private Button ok;

	private boolean[] config_arr = MainApp.getConfigArr();
	private Stage dialogStage;
	private ArrayList<Node> focusList = new ArrayList<>();

	@FXML
	private void initialize() {
		focusList.add(acceleration);
		focusList.add(altitude);
		focusList.add(attitude);
		focusList.add(battery);
		focusList.add(gyroscope);
		focusList.add(heading);
		focusList.add(range);
		focusList.add(temperature);
		focusList.add(thermal);
		focusList.add(cancel);
		focusList.add(ok);
		
		acceleration.setSelected(config_arr[ToggleKeys.toggleAccel_VALUE]);
		gyroscope.setSelected(config_arr[ToggleKeys.toggleGyro_VALUE]);
		altitude.setSelected(config_arr[ToggleKeys.toggleAltitude_VALUE]);
		attitude.setSelected(config_arr[ToggleKeys.toggleAttitude_VALUE]);
		temperature.setSelected(config_arr[ToggleKeys.toggleTemp_VALUE]);
		battery.setSelected(config_arr[ToggleKeys.toggleBat_VALUE]);
		thermal.setSelected(config_arr[ToggleKeys.toggleArray_VALUE]);
		heading.setSelected(config_arr[ToggleKeys.toggleHead_VALUE]);
		range.setSelected(config_arr[ToggleKeys.toggleWifi_VALUE]);
		
		Tapper tapper = this;
		
		EventHandler<KeyEvent> boxHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				System.out.println("Key typed: " + event.getCode());
				CheckBox box = (CheckBox) dialogStage.getScene().getFocusOwner();
				if(event.getCode() == KeyCode.ENTER)
				{
					System.out.println("ENTER received, handling box selection.");
					
					if(box.isSelected()) {
						box.setSelected(false);
					} else {
						box.setSelected(true);
					}
				} else if(event.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");
						
						new Thread(new TapTimer(tapper, box)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
			
		};

		acceleration.setOnKeyPressed(boxHandler);
		altitude.setOnKeyPressed(boxHandler);
		attitude.setOnKeyPressed(boxHandler);
		battery.setOnKeyPressed(boxHandler);
		gyroscope.setOnKeyPressed(boxHandler);
		heading.setOnKeyPressed(boxHandler);
		temperature.setOnKeyPressed(boxHandler);
		thermal.setOnKeyPressed(boxHandler);
		range.setOnKeyPressed(boxHandler);
		
		EventHandler<KeyEvent> buttonHandler = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				System.out.println("Key typed: " + event.getCode());
				Button button = (Button) dialogStage.getScene().getFocusOwner();

				if(event.getCode() == KeyCode.ENTER)
				{
					System.out.println("ENTER received, handling box selection.");
					
					if(button == cancel) {
						handleCancel();
					} else if(button == ok) {
						handleOk();
					}
				} else if(event.getCode() == KeyCode.B)
				{
					if(!singleTap) {
						singleTap = true;
						System.out.println("singleTap = true");
						
						new Thread(new TapTimer(tapper, button)).start();
					} else {
						singleTap = false;
						System.out.println("singleTap = false");
					}
				}
			}
			
		};
		
		cancel.setOnKeyPressed(buttonHandler);
		ok.setOnKeyPressed(buttonHandler);
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
	
	protected void doubleTap(Node n) {
		KeyEvent artificialEvent = new KeyEvent(KeyEvent.KEY_PRESSED,
				KeyCode.ENTER.getName(), "Forwarded from foot switch.",
				KeyCode.ENTER, false, false, false, false);
		System.out.println("About to handle artificial ENTER.");
		n.getOnKeyPressed().handle(artificialEvent);
	}
	
	protected void singleTap(Node n) {
		singleTap = false;

		int index = focusList.indexOf(n);
		int new_index = index+1;

		if(new_index == focusList.size()) {
			new_index = 0;
		}

		Node target = focusList.get(new_index);

		target.requestFocus();
	}
}

