package tomatron.model;

public interface Observer {
	void update();
	void setObservable(Observable o);
}
