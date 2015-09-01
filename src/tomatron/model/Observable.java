package tomatron.model;

public interface Observable {
	void attach(Observer o);
	void detach(Observer o);
	void notify_update();
}
