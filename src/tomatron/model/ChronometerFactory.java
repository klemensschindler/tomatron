package tomatron.model;

public class ChronometerFactory implements IChronometerCreator{

	public IChronometer createPomodoro(){ 
		return null;
	}

	public IChronometer createChronometer() {
		Chronometer c = new Chronometer();
		c.setMinutes(25);
		return c;
	}
}
