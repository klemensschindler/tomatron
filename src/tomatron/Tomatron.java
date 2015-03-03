package tomatron;

import java.util.Timer;	
import java.util.TimerTask;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class Tomatron {	
	enum PomodoroState {
		inactive, pomodoro, shortBreak, longBreak
	}

	private PomodoroState state = PomodoroState.inactive;
	private TrayIcon trayIcon = new TrayIcon(TomatronUtils.createTrayIconImage("P",
			0.75, new Color(100, 100, 100)));
	private final SystemTray tray = SystemTray.getSystemTray();
	private int completedPomodoros = 0;

	MenuItem pomodoroCountItem = new MenuItem("Completed Pomodoros: 0");
	MenuItem pomodoroItem = new MenuItem("Start Pomodoro");
	MenuItem shortBreakItem = new MenuItem("Short Break");
	MenuItem longBreakItem = new MenuItem("Long Break");
	MenuItem cancelItem = new MenuItem("Cancel");
	MenuItem exitItem = new MenuItem("Exit");

	int secondsRemaining = 0;
	Timer timer = new Timer();
	
	public Tomatron() {
		setState(PomodoroState.inactive);
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
	 * Task that is triggered every second.
	 * Automatically transitions the state upon timer expiration and updates information presented to the user.
	 */
	class EverySecond extends TimerTask {
		public void run() {
			if (state != PomodoroState.inactive) {
				secondsRemaining--;
				if (secondsRemaining <= 0) {
					// A break or pomodoro is finished
					switch (state) {
					case pomodoro:
						displayDesktopNotification("Pomodoro finished", "Time for a break!");
						completedPomodoros++;
						break;
					case longBreak:
					case shortBreak:
						displayDesktopNotification("Break finished", "You can start a new pomodoro when ready.");
						break;
					default:
						break;
					}
					setState(PomodoroState.inactive);
				}
			}
			updatePomodoroInfo();
		}
	}

	/**
	 * Updates the tray icon and tooltip
	 */
	private void updatePomodoroInfo() {
		pomodoroCountItem.setEnabled(false);
		pomodoroCountItem.setLabel(String.format("Completed Pomodoros: %d", completedPomodoros));
		String shortTimeLeftString;
		if (secondsRemaining > 60) {
			shortTimeLeftString = Integer.toString(secondsRemaining / 60);
		} else {
			shortTimeLeftString = Integer.toString(secondsRemaining);
		}

		String timeLeftString = String.format("%02d:%02d",
				(secondsRemaining / 60), secondsRemaining % 60);

		switch (state) {
		case pomodoro:
			trayIcon.setImage(TomatronUtils.createTrayIconImage(shortTimeLeftString,
					0.75, new Color(130, 30, 30)));
			trayIcon.setToolTip(String.format(
					"Pomodoro in progress\nRemaining: %s", timeLeftString)
					.toString());
			break;
		case shortBreak:
			trayIcon.setImage(TomatronUtils.createTrayIconImage(shortTimeLeftString,
					0.75, new Color(20, 100, 40)));
			trayIcon.setToolTip(String.format(
					"Short break in progress\nRemaining: %s", timeLeftString)
					.toString());
			break;
		case longBreak:
			trayIcon.setImage(TomatronUtils.createTrayIconImage(shortTimeLeftString,
					0.75, new Color(10, 80, 150)));
			trayIcon.setToolTip(String.format(
					"Long break in progress\nRemaining: %s", timeLeftString)
					.toString());
			break;
		case inactive:
			trayIcon.setImage(TomatronUtils.createTrayIconImage("P", 0.75, new Color(
					100, 100, 100)));
			trayIcon.setToolTip(String.format("Tomatron: inactive",
					timeLeftString).toString());
			break;
		}
	}

	/**
	 * Execute state transition and update UI/timers accordingly
	 * @param newState new state to assume
	 */
	private void setState(PomodoroState newState) {
		pomodoroItem.setLabel("Start Pomodoro");
		shortBreakItem.setLabel("Start Short Break");
		longBreakItem.setLabel("Start Long Break");
		cancelItem.setLabel("Cancel");
		cancelItem.setEnabled(true);

		timer.cancel();
		
		state = newState;
		switch (state) {
		case pomodoro:
			pomodoroItem.setLabel("Restart Pomodoro");
			cancelItem.setLabel("Void Pomodoro");
			secondsRemaining = 25 * 60;
			timer = new Timer();
			timer.scheduleAtFixedRate(new EverySecond(), 0, 1000L);
			break;
		case shortBreak:
			shortBreakItem.setLabel("Restart Short Break");
			cancelItem.setLabel("Cancel Break");
			secondsRemaining = 5 * 60;
			timer = new Timer();
			timer.scheduleAtFixedRate(new EverySecond(), 0, 1000L);
			break;
		case longBreak:
			longBreakItem.setLabel("Restart Long Break");
			cancelItem.setLabel("Cancel Break");
			secondsRemaining = 15 * 60;
			timer = new Timer();
			timer.scheduleAtFixedRate(new EverySecond(), 0, 1000L);
			break;
		case inactive:
			cancelItem.setEnabled(false);
			secondsRemaining = 0;
			break;
		}
		updatePomodoroInfo();
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
				setState(PomodoroState.pomodoro);
			}
		});

		shortBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(PomodoroState.shortBreak);
			}
		});

		longBreakItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(PomodoroState.longBreak);
			}
		});

		cancelItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setState(PomodoroState.inactive);
			}
		});

		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tray.remove(trayIcon);
				System.exit(0);
			}
		});

		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,
						"You can use Tomatron by right clicking on the system tray icon.");
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