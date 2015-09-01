package tomatron.test;

import tomatron.model.*;

public class TestPomodoro {
	public static void main(String[] args) {
		IChronometerCreator icc = PomodoroFactory();
		IPomodoro p = (IPomodoro) icc.createPomodoro();
		p.start();
		p.pause();
		p.stop();
	}
}

