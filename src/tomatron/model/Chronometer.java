package tomatron.model;

import java.util.*;

public class Chronometer implements IChronometer {

	protected int minutes;
	protected Set<Observer> obs;

	public Chronometer() {
		obs = new Collections.synchronizedSet(HashSet<Observer>(5));
	}

	public void start() { System.out.println("Started"); }
	public void pause() {  System.out.println("Paused"); }
	public void stop() {  System.out.println("Stopped"); }
	public void setMinutes(int minutes) { this.minutes = minutes; }
	public int getMinutes() {  return minutes; }
	public int getCurrentTime() {  return minutes; }

	public void attach(Observer o) {
		if (o != null && !obs.Contains(o))
			obs.add(o);
	}

	public void detach(Observer o) { 
		if (o != null && obs.Contains(o))
			obs.remove(o);
	}

	public void notify_update() { }
}
