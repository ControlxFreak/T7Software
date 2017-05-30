package app.model;

public enum Animal {
	UNKNOWN(2),
	ELEPHANT(4),
	GORILLA(4),
	RHINO(4),
	TIGER(3),
	PANDA(3),
	GIRAFFE(2),
	LION(2),
	CHEETAH(2),
	ZEBRA(1),
	MOOSE(1),
	WARTHOG(1),
	HIPPO(1);
	
	private final int priority;
	Animal(int priority) {
		this.priority = priority;
	}
	
	int priority() {
		return priority;
	}
}
