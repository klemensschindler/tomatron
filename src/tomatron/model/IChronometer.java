package tomatron.model;

public interface IChronometer extends Observable{
	void start();
	void pause();
	void stop();
	void setMinutes(int minutes);
	int getMinutes();
	int getCurrentTime();

}
