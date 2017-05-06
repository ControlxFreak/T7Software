package app.model;

import java.awt.image.BufferedImage;

public class Snapshot {

	private BufferedImage image;
	private int priority;

	public Snapshot(BufferedImage image) {
		this.image = image;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
