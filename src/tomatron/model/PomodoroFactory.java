package tomatron.model;

public class PomodoroFactory implements IChronometerCreator{

	public IChronometer createWorkPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(Pomodoro.getDefaultWorkDuration());
		return p;
	}

	public IChronometer createLongBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(Pomodoro.getDefaultLongBreakDuration());
		return p;
	}

	public IChronometer createShortBreakPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(Pomodoro.getDefaultShortBreakDuration());
		return p;
	}

	public IChronometer createChronometer() {
		return null;
	}
}
