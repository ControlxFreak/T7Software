package app.view;

abstract class Tapper {

	protected volatile boolean singleTap = false;
	
	public boolean isSingleTap() {
		return singleTap;
	}
	
}
