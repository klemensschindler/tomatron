package tomatron.model;

public class PomodoroFactory implements IChronometerCreator{

	public IChronometer createWorkPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(25);
		return p;
	}

	public IChronometer createLongBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(15);
		return p;
	}

	public IChronometer createShortBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(5);
		return p;
	}

	public IChronometer createChronometer() {
		return null;
	}
}
