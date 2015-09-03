package tomatron.model;

public interface Observer {
	void update();
	void minorUpdate();
	void setObservable(Observable o);
}
