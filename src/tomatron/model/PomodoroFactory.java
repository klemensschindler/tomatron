package tomatron.model;

public class PomodoroFactory implements IChronometerCreator{

	public IChronometer createPomodoro(){ 
		Pomodoro p = new Pomodoro();
		p.setMinutes(25);
		p.setLongPause(15);
		p.setShortPause(5);
		return p;
	}

	public IChronometer createChronometer() {
		return null;
	}
}
