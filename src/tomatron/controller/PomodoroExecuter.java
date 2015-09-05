package tomatron.controller;

import java.util.Timer;	
import tomatron.model.*;

public class PomodoroExecuter implements Observer{
	public enum PomodoroType {
		inactive, work, shortBreak, longBreak
	}

	public PomodoroType type;
	private Observable o = null;
	private Timer timer = new Timer();
	private int completedPomodoros = 0;
	private Pomodoro p;

	public void setObservable(Observable o) {
		this.o = o;
	}

	public boolean startPomodoro() {
		if(p != null){
			p.start();
			timer.scheduleAtFixedRate(p, 0, 1000L);
		}
		return p != null;
	}

	public void stopPomodoro() {
		if(p != null) {
			p.stop();
			p = null;
			type = PomodoroType.inactive;
		}
		timer.purge();
	}

	public void endExecution() { 
		stopPomodoro();
		timer.cancel();
	}

	public Pomodoro getPomodoro() {
		return p;
	}

	public int getCompletedPomodoros() {
		return completedPomodoros;
	}


	public void initPomodoro(PomodoroType type) {
		IChronometerCreator icc = new PomodoroFactory();
		// A pomodoro can be executing
		stopPomodoro();
		this.type = type;
		switch(this.type) {
			case work:
				p = (Pomodoro) icc.createWorkPomodoro();
				break;
			case shortBreak:
				p = (Pomodoro) icc.createShortBreakPomodoro();
				break;
			case longBreak:
				p = (Pomodoro) icc.createLongBreakPomodoro();
				break;
		}
		setObservable(p);
		p.attach(this);
	}

	public void update() {
		IPomodoro p = (Pomodoro)o.getUpdate(this);
		switch(p.getState()) {
			case RUNNING:
				break;
			case PAUSED:
				break;
			case STOPPED:
				break;
			case FINISHED:
				switch(type) {
					case work:
						completedPomodoros++;
						break;
					case shortBreak:
					case longBreak:
						break;
				}
				break;
		}
	}

	public void minorUpdate() {
		// not interessed in minor updates
	}
}
