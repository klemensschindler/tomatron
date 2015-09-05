package tomatron.model;

public class ChronometerFactory implements IChronometerCreator{

	public IChronometer createWorkPomodoro(){ 
		return null;
	}

	public IChronometer createLongBreakPomodoro(){ 
		return null;
	}

	public IChronometer createShortBreakPomodoro(){ 
		return null;
	}

	public IChronometer createChronometer() {
		Chronometer c = new Chronometer();
		c.setMinutes(1);
		return c;
	}
}
