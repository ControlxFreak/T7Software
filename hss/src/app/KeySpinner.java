package app;

import java.util.ArrayList;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class KeySpinner {
	
	private ArrayList<KeyCode> keyList;
	private ArrayList<Image> iconList;
	private int index = 0;
	
	public KeySpinner(ArrayList<KeyCode> keyList, ArrayList<Image> iconList) {
		this.keyList = keyList;
		this.iconList = iconList;
	}
	
	public void spin() {
		if(++index == keyList.size()) {
			index = 0;
		}
	}
	
	public KeyCode getKey() {
		return keyList.get(index);
	}
	
	public Image getIcon() {
		return iconList.get(index);
	}

}
