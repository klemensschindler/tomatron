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
		if (getState() != STATE.RUNNING) {
			if(secondsRemaining == null)
				secondsRemaining = new Integer(minutes * 60);
			setState(STATE.RUNNING);
		}
	}

	public void pause() {  
		setState(STATE.PAUSED);
	}

	public void stop() {
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
		try {
			for (Observer o : obs) 
				o.update();
		}
		catch(ConcurrentModificationException e) {
			System.out.println("Execption on notify: " + e.toString());
		}
	}

	public void notifyMinorUpdate() {
		try {
			for (Observer o : obs) 
				o.minorUpdate();
		}
		catch(ConcurrentModificationException e) {
			System.out.println("Execption on notify: " + e.toString());
		}
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
						secondsRemaining--;
						notifyMinorUpdate();
					}
					break;
				case PAUSED:
					break;
				case STOPPED:
				case FINISHED:
					this.cancel();
					break;
			}
		}
	}

}
