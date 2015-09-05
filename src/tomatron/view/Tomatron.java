package tomatron.view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import tomatron.model.*;
import tomatron.controller.PomodoroExecuter;

public class Tomatron implements Observer{	

	private TrayIcon trayIcon = new TrayIcon(
			TomatronUtils.createTrayIconImage("P", 0.75,
				new Color(100, 100, 100)));
	private final SystemTray tray = SystemTray.getSystemTray();

	MenuItem pomodoroCountItem = new MenuItem("Completed Pomodoros: 0");
	MenuItem pomodoroItem = new MenuItem("Start Pomodoro");
	MenuItem shortBreakItem = new MenuItem("Short Break");
	MenuItem longBreakItem = new MenuItem("Long Break");
	MenuItem cancelItem = new MenuItem("Cancel");
	MenuItem exitItem = new MenuItem("Exit");

	Observable o = null;
	PomodoroExecuter pomExec;

	public void setObservable(Observable o) {
		this.o = o;
	}

	public void update() {
		IPomodoro p = (Pomodoro)o.getUpdate(this);

		switch(p.getState()) {
			case RUNNING:
				switch(pomExec.type) {
					case work:
						displayDesktopNotification("Pomodoro is Running", 
								"Time to WORK!");
						break;
					case shortBreak:
					case longBreak:
						displayDesktopNotification("Break is Running", 
								"Time to RELAX!");
						break;
				}
				break;
			case PAUSED:
			case STOPPED:
				switch(pomExec.type) {
					case work:
						displayDesktopNotification("Pomodoro Interrupted", 
								"Start a new one when You are feeling ready!");
						break;
					case shortBreak:
					case longBreak:
						displayDesktopNotification("Break Interrupted", 
								"Start a new Pomodoro if You are feeling ready!");
						break;
				}
				p.detach(this);
				pomExec.stopPomodoro();
				execByPomodoroType(pomExec.type);
				break;
			case FINISHED:
				switch(pomExec.type) {
					case work:
						displayDesktopNotification("Pomodoro finished", 
								"Time for a break!");
						break;
					case shortBreak:
					case longBreak:
						displayDesktopNotification("Break finished", 
								"You can start a new pomodoro when ready!");
						break;
				}
				p.detach(this);
				pomExec.stopPomodoro();
				execByPomodoroType(pomExec.type);
				break;
		}
		updatePomodoroInfo();
	}

	public void minorUpdate() {
		updatePomodoroInfo();
	}

	public Tomatron() {
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			System.err.println("TrayIcon could not be added.");
			System.exit(1);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				populateMenu();
			}
		});

		pomExec = new PomodoroExecuter();
	}

	private void displayDesktopNotification(String summary, String message)
	{
		try {
			// Try notify-send. This is usually available on linux
			String[] cmd = {"notify-send", summary, message};
			Process notifySend = Runtime.getRuntime().exec(cmd);
			notifySend.waitFor();
		} catch (IOException | InterruptedException e) {
			// Java AWT tray icon notification.
			// Looks fine on Windows. Works on Linux as a fallback, but does not look so good
			trayIcon.displayMessage(summary, message, TrayIcon.MessageType.NONE);		
		}
	}

	/**
	 * Updates the tray icon and tooltip
	 */
	private void updatePomodoroInfo() {
		int secsLeft = 0;
		String timeLeftString = "";
		String timeLeft = "";

		// only retrieve the pomodoro current time if its a valid pomodoro
		if(pomExec.type != PomodoroExecuter.PomodoroType.inactive) {
			secsLeft = ((Pomodoro)o.getUpdate(this)).getCurrentTime();
			if (secsLeft > 60) {
				timeLeft = Integer.toString(secsLeft / 60);
			} else {
				timeLeft = Integer.toString(secsLeft);
			}
			timeLeftString = String.format("%02d:%02d",
					(secsLeft / 60), secsLeft % 60);
		}

		pomodoroCountItem.setEnabled(false);
		pomodoroCountItem.setLabel(String.format("Completed Pomodoros: %d", 
					pomExec.getCompletedPomodoros()));

		switch (pomExec.type) {
			case work:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							timeLeft, 0.75, new Color(130, 30, 30)));
				trayIcon.setToolTip(String.format(
							"Pomodoro in progress\nRemaining: %s",
							timeLeftString).toString());
				break;
			case shortBreak:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							timeLeft, 0.75, new Color(20, 100, 40)));
				trayIcon.setToolTip(String.format(
							"Short break in progress\nRemaining: %s",
							timeLeftString).toString());
				break;
			case longBreak:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							timeLeft, 0.75, new Color(10, 80, 150)));
				trayIcon.setToolTip(String.format(
							"Long break in progress\nRemaining: %s",
							timeLeftString).toString());
				break;
			case inactive:
				trayIcon.setImage(TomatronUtils.createTrayIconImage("P", 0.75, 
							new Color( 100, 100, 100)));
				trayIcon.setToolTip(String.format("Pomodoro inactive."));
				break;
		}
	}

	/**
	 * Execute state transition and update UI/timers accordingly
	 * @param type new type to assume
	 */
	private void execByPomodoroType(PomodoroExecuter.PomodoroType type) {
		pomodoroItem.setLabel("Start Pomodoro");
		shortBreakItem.setLabel("Start Short Break");
		longBreakItem.setLabel("Start Long Break");
		cancelItem.setLabel("Cancel");
		cancelItem.setEnabled(true); 
		// if its a restart, its unecessary to handle the stop update;
		// it can be the first execution (o with null value)
		if(o != null) {
			Pomodoro p = (Pomodoro)o.getUpdate(this);
			// check if the pomodoro it's not current stopped (already detached)
			if(p != null) {
				// it can be a cancel action (type inactive)
				if(type != PomodoroExecuter.PomodoroType.inactive) 
					p.detach(this);
			}
		}

		switch (type) {
			case work:
				pomodoroItem.setLabel("Restart Pomodoro");
				cancelItem.setLabel("Cancel Pomodoro");
				pomExec.initPomodoro(
						PomodoroExecuter.PomodoroType.work);
				break;
			case shortBreak:
				shortBreakItem.setLabel("Restart Short Break");
				cancelItem.setLabel("Cancel Break");
				pomExec.initPomodoro(
						PomodoroExecuter.PomodoroType.shortBreak);
				break;
			case longBreak:
				longBreakItem.setLabel("Restart Long Break");
				cancelItem.setLabel("Cancel Break");
				pomExec.initPomodoro(
						PomodoroExecuter.PomodoroType.longBreak);
				break;
			case inactive:
				pomExec.stopPomodoro();
				cancelItem.setEnabled(false);
				updatePomodoroInfo();
				break;

		}
		if(type != PomodoroExecuter.PomodoroType.inactive) {
			pomExec.getPomodoro().attach(this);
			setObservable(pomExec.getPomodoro());
			pomExec.startPomodoro();
		}
	}

	/**
	 * Add all items and listeners to the right click menu
	 */
	private void populateMenu() {
		final PopupMenu popup = new PopupMenu();
		popup.add(pomodoroCountItem);
		popup.addSeparator();
		popup.add(pomodoroItem);
		popup.add(shortBreakItem);
		popup.add(longBreakItem);
		popup.addSeparator();
		popup.add(cancelItem);
		popup.addSeparator();
		popup.add(exitItem);

		pomodoroItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByPomodoroType(PomodoroExecuter.PomodoroType.work);
			}
		});

		shortBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByPomodoroType(PomodoroExecuter.PomodoroType.shortBreak);
			}
		});

		longBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByPomodoroType(PomodoroExecuter.PomodoroType.longBreak);
			}
		});

		cancelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByPomodoroType(PomodoroExecuter.PomodoroType.inactive);
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pomExec.endExecution();
				tray.remove(trayIcon);
				System.exit(0);
			}
		});

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"Try right clicking on the system tray icon.");
			}
		});

		trayIcon.setPopupMenu(popup);
	}

	public static void main(String[] args) {
		if (!SystemTray.isSupported()) {
			System.err.println("System tray is not supported on this system");
			System.exit(1);
		}

		new Tomatron();
	}
}
