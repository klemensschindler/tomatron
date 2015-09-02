package tomatron.model;

import java.util.*;

public class Chronometer implements IChronometer {

	protected int minutes;
	protected Set<Observer> obs;
	private STATE state;

	public Chronometer() {
		this.obs = Collections.synchronizedSet(new HashSet<Observer>(5));
	}

	public void start() {
		System.out.println("Started"); 
		setState(STATE.RUNNING);
	}

	public void pause() {  
		System.out.println("Paused"); 
		setState(STATE.PAUSED);
	}

	public void stop() {
		System.out.println("Stopped");
		setState(STATE.STOPPED);
	}

	public void setMinutes(int minutes) { this.minutes = minutes; }
	public int getMinutes() {  return minutes; }
	public int getCurrentTime() {  return minutes; }

	public STATE getState() {
		return this.state;
	}

	protected void setState(STATE state) {
		if (this.state != state) {
			this.state = state;
			notify_update();
		}
	}

	public void attach(Observer o) {
		if (o != null)
			obs.add(o);
	}

	public void detach(Observer o) { 
		if (o != null && obs.contains(o))
			obs.remove(o);
	}

	public void notify_update() {
		for (Observer o : obs) 
			o.update();
	}

	public IChronometer.STATE getUpdate(Observer o) {
		return obs.contains(o) ? this.state : null;
	}
}
