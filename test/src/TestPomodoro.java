package tomatron.test;

import tomatron.model.*;
import java.util.Timer;	

public class TestPomodoro implements Observer{
	Observable o = null;

	public void setObservable(Observable o) {
		this.o = o;
	}

	public void update() {
		IPomodoro p = (Pomodoro)o.getUpdate(this);
		System.out.println("Chronometer state changed to: " + 
				p.getState().toString());
	}
		
	public void minorUpdate() {
		IPomodoro p = (Pomodoro)o.getUpdate(this);
		System.out.println("tack");
		System.out.println(p.getCurrentTime() + "min");
	}

	public static void main(String[] args) throws InterruptedException {
		IChronometerCreator icc = new PomodoroFactory();
		Pomodoro p = (Pomodoro) icc.createPomodoro();
		TestPomodoro t = new TestPomodoro();
		t.setObservable(p);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(p, 0, 1000L);
		//TestPomodoro t1 = new TestPomodoro();
		//t1.setObservable(p);
		//TestPomodoro t2 = new TestPomodoro();
		//t2.setObservable(p);
		System.out.println("Pomodoro state observer:OFF");
		p.start();
		Thread.sleep(2000);
		p.pause();
		Thread.sleep(2000);
		p.stop();
		Thread.sleep(2000);
		timer.cancel();
		System.out.println("Pomodoro state observer:ON");
		p.attach(t);
		timer = new Timer();
		timer.scheduleAtFixedRate(p, 0, 1000L);
		p.start();
		Thread.sleep(3000);
		p.pause();
		Thread.sleep(3000);
		p.stop();
		Thread.sleep(3000);
		timer.cancel();
		System.out.println("Pomodoro state observer:OFF, no tick");
		p.detach(t);
		//p.detach(t1);
		//p.detach(t2);
		p.start();
		p.pause();
		p.stop();
		System.out.println("TEST END");
	}
}

