package tomatron.view;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

import tomatron.model.*;
import tomatron.model.PomodoroExecuter;

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
				break;
			case PAUSED:
				break;
			case STOPPED:
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
								"You can start a new pomodoro when ready.");
						break;
				}
				break;
		}
	}

	public void minorUpdate() {
		IPomodoro p = (Pomodoro)o.getUpdate(this);
		updatePomodoroInfo(p.getCurrentTime());
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
	private void updatePomodoroInfo(int secondsRemaining) {
		pomodoroCountItem.setEnabled(false);
		pomodoroCountItem.setLabel(String.format("Completed Pomodoros: %d", 
					pomExec.getCompletedPomodoros()));
		String shortTimeLeftString;
		if (secondsRemaining > 60) {
			shortTimeLeftString = Integer.toString(secondsRemaining / 60);
		} else {
			shortTimeLeftString = Integer.toString(secondsRemaining);
		}

		String timeLeftString = String.format("%02d:%02d",
				(secondsRemaining / 60), secondsRemaining % 60);

		switch (pomExec.type) {
			case work:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							shortTimeLeftString, 0.75, new Color(130, 30, 30)));
				trayIcon.setToolTip(String.format(
						"Pomodoro in progress\nRemaining: %s",
							timeLeftString)
						.toString());
				break;
			case shortBreak:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							shortTimeLeftString, 0.75, new Color(20, 100, 40)));
				trayIcon.setToolTip(String.format(
						"Short break in progress\nRemaining: %s",
							timeLeftString) .toString());
				break;
			case longBreak:
				trayIcon.setImage(TomatronUtils.createTrayIconImage(
							shortTimeLeftString, 0.75, new Color(10, 80, 150)));
				trayIcon.setToolTip(String.format(
						"Long break in progress\nRemaining: %s",
							timeLeftString) .toString());
				break;
			default:
				trayIcon.setImage(TomatronUtils.createTrayIconImage("P", 0.75, 
						new Color( 100, 100, 100)));
				trayIcon.setToolTip(String.format("Tomatron: inactive",
							timeLeftString).toString());
				break;
		}
	}

	/**
	 * Execute state transition and update UI/timers accordingly
	 * @param type new type to assume
	 */
	private void execByState(PomodoroExecuter.PomodoroType type) {
		pomodoroItem.setLabel("Start Pomodoro");
		shortBreakItem.setLabel("Start Short Break");
		longBreakItem.setLabel("Start Long Break");
		cancelItem.setLabel("Cancel");
		cancelItem.setEnabled(true); 

		if(type == null) {
			pomExec.stopPomodoro();
			cancelItem.setEnabled(false);
		}
		else {
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
			}
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
				execByState(PomodoroExecuter.PomodoroType.work);
			}
		});

		shortBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByState(PomodoroExecuter.PomodoroType.shortBreak);
			}
		});

		longBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByState(PomodoroExecuter.PomodoroType.longBreak);
			}
		});

		cancelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				execByState(null);
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
