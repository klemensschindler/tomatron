package tomatron.test;

import tomatron.model.*;

public class TestPomodoro implements Observer{
	Observable o = null;

	public void setObservable(Observable o) {
		this.o = o;
	}

	public void update() {
		System.out.println("Chronometer state changed to: " + 
				o.getUpdate(this).toString());
	}

	public static void main(String[] args) {
		IChronometerCreator icc = new PomodoroFactory();
		IPomodoro p = (IPomodoro) icc.createPomodoro();
		TestPomodoro t = new TestPomodoro();
		t.setObservable(p);
		TestPomodoro t1 = new TestPomodoro();
		t1.setObservable(p);
		TestPomodoro t2 = new TestPomodoro();
		t2.setObservable(p);
		System.out.println("Pomodoro state observer:OFF");
		p.start();
		p.pause();
		p.pause();
		p.stop();
		System.out.println("Pomodoro state observer:ON");
		p.attach(t);
		p.attach(t);
		p.attach(t1);
		p.attach(t2);
		p.start();
		p.start();
		p.pause();
		p.stop();
		System.out.println("Pomodoro state observer:OFF");
		p.detach(t);
		p.detach(t1);
		p.detach(t2);
		p.start();
		p.pause();
		p.stop();
		p.stop();

	}
}

