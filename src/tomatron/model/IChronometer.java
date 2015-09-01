package tomatron.model;

public interface IChronometer {
	void start();
	void pause();
	void stop();
	void setMinutes(int minutes);
	int getMinutes();
	int getCurrentTime();
}
