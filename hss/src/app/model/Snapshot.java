package app.model;

import java.awt.image.BufferedImage;
import java.util.Date;

public class Snapshot {

	private BufferedImage image;
	private int priority;
	private String description;
	private String notes;
	private Date timestamp;

	public Snapshot(BufferedImage image) {
		this.image = image;

	}

	public BufferedImage getImage() {
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

}
