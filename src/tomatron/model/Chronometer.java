package tomatron.model;

import java.util.*;
import java.util.TimerTask;

public class Chronometer extends TimerTask implements IChronometer, Cloneable {

	protected int minutes;
	protected Set<Observer> obs;
	private STATE state = null;
	protected Integer secondsRemaining = null;

	public Chronometer() {
		this.obs = Collections.synchronizedSet(new HashSet<Observer>(5));
	}

	public void start() {
		System.out.println("Started"); 
		if (getState() != STATE.RUNNING) {
			if(secondsRemaining == null)
				secondsRemaining = new Integer(minutes * 60);
			setState(STATE.RUNNING);
		}
	}

	public void pause() {  
		System.out.println("Paused"); 
		setState(STATE.PAUSED);
	}

	public void stop() {
		System.out.println("Stopped");
		setState(STATE.STOPPED);
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes; 
	}

	public int getMinutes() { 
		return minutes;
	}

	public int getCurrentTime() {
		return secondsRemaining;
	}

	public STATE getState() {
		return this.state;
	}

	protected void setState(STATE state) {
		if (getState() != state) {
			this.state = state;
			notifyUpdate();
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

	public void notifyUpdate() {
		for (Observer o : obs) 
			o.update();
	}

	public void notifyMinorUpdate() {
		for (Observer o : obs) 
			o.minorUpdate();
	}

	public Chronometer getUpdate(Observer o) {
		return obs.contains(o) ? this : null;
	}

	/**
	 * Task that is triggered every second.
	 */
	@Override
	public void run() {
		// check if the state is ready (start methods was called).
		// Avoids error in case when the timer is trigged before the
		// start method
		if(getState() != null) {
			switch(getState()) {
				case RUNNING:
					if(secondsRemaining <= 0) {
						setState(STATE.FINISHED);
					}
					else {
						System.out.println("tick!");
						secondsRemaining--;
						notifyMinorUpdate();
					}
					break;
				case PAUSED:
					System.out.println("ignored tick!");
					break;
				case STOPPED:
				case FINISHED:
					System.out.println("Canceling.");
					this.cancel();
					break;
			}
			System.out.println(secondsRemaining + "s");
		}
	}

}
