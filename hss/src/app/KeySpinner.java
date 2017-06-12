package app;

import java.util.ArrayList;

import javafx.scene.input.KeyCode;

public class KeySpinner {
	
	private ArrayList<KeyCode> keyList;
	private int index = 0;
	
	public KeySpinner(ArrayList<KeyCode> keyList) {
		this.keyList = keyList;
	}
	
	public void spin() {
		if(index++ == keyList.size()) {
			index = 0;
		}
	}
	
	public KeyCode getKey() {
		return keyList.get(index);
	}

}
