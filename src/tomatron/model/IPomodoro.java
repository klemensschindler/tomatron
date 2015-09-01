package tomatron.model;

public interface IPomodoro extends IChronometer{

	public void setShortPause(int minutes);
	public void setLongPause(int minutes);
}
