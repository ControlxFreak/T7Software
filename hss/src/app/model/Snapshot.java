package app.model;

import java.io.File;
import java.util.Date;

import javafx.scene.image.Image;

public class Snapshot {
	
	private static final Image pin = new Image((new File("/home/jarrett/T7Software/hss/src/main/resources/images/default/red_pin.png")).toURI().toString());

	private Image image;
	private int priority = 1;
	private String description;
	private String notes;
	private Date timestamp = new Date();

	public Snapshot(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPriority() {
		return priority;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public static Image getPin() {
		return pin;
	}
}
