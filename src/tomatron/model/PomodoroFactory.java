package tomatron.model;

public class PomodoroFactory implements IChronometerCreator{

	public int getDefaultWorkDuration() { 
		return 25; 
	}

	public int getDefaultLongBreakDuration() {
		return 15; 
	}

	public int getDefaultShortBreakDuration() { 
		return 5;
	}

	public IChronometer createWorkPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(getDefaultWorkDuration());
		return p;
	}

	public IChronometer createLongBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(getDefaultLongBreakDuration());
		return p;
	}

	public IChronometer createShortBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(getDefaultShortBreakDuration());
		return p;
	}

	public IChronometer createChronometer() {
		return null;
	}
}
