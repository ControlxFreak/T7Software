package app.model;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class Snapshot implements Comparable<Snapshot>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Image pin = new Image((new File("src/main/resources/images/default/red_pin.png")).toURI().toString());

	private transient Image image;
	private double priorityVal = -1;
	private int relativePriority = -1;
	private String description;
	private String notes;
	private Date timestamp = new Date();
	private boolean target = true;
	private Animal animal = Animal.UNKNOWN;
	private int animalQty = 1;
	private double maxThermal = Double.MIN_VALUE;

	public Snapshot(Image image) {
		this.image = image;
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
		s.defaultReadObject();
		image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
	}
	
	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
		ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
	}

	public Image getImage() {
		return image;
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
		refreshPriorityVal();
	}

	public int getAnimalQty() {
		return animalQty;
	}

	public void setAnimalQty(int animalQty) {
		this.animalQty = animalQty;
		refreshPriorityVal();
	}

	@Override
	public int compareTo(Snapshot o) {
		double d = this.priorityVal - o.priorityVal;
		
		if(d < 0) {
			return -1;
		} if (d == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	public int getRelativePriority() {
		return relativePriority;
	}

	public void setRelativePriority(int relativePriority) {
		this.relativePriority = relativePriority;
	}

	private void refreshPriorityVal() {
		priorityVal = animal.getPriority() * animalQty + .03*maxThermal;
	}

	public double getMaxThermal() {
		return maxThermal;
	}

	public void setMaxThermal(double maxThermal) {
		this.maxThermal = maxThermal;
		refreshPriorityVal();
	}
}
