package app.model;

import java.io.File;
import java.util.Date;

import javafx.scene.image.Image;

public class Snapshot {
	
	private static final Image pin = new Image((new File("/home/jarrett/T7Software/hss/src/main/resources/images/default/red_pin.png")).toURI().toString());

	private Image image;
	private int priority = -1;
	private String description;
	private String notes;
	private Date timestamp = new Date();
	private boolean target = true;
	private Animal animal = Animal.UNKNOWN;
	private int animalQty = 0;

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

	public boolean isTarget() {
		return target;
	}

	public void setTarget(boolean target) {
		this.target = target;
	}

	public Animal getAnimal() {
		return animal;
	}

	public void setAnimal(Animal animal) {
		this.animal = animal;
	}

	public int getAnimalQty() {
		return animalQty;
	}

	public void setAnimalQty(int animalQty) {
		this.animalQty = animalQty;
	}
}
