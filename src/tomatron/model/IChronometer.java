package tomatron.model;

public interface IChronometer extends Observable{

	enum STATE { 
		STOPPED("stopped"),
		RUNNING("running"),
		PAUSED("paused"),
		FINISHED("finished");

		private final String text;

		private STATE(final String text) {
			this.text = text;
		}    

		@Override
		public String toString() {
			return text;
		}
	}

	void start();
	void pause();
	void stop();
	void setMinutes(int minutes);
	int getMinutes();
	int getCurrentTime();
	STATE getState();
}
