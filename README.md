# Tomatron
Simple pomodoro timer in the system tray.

## Description 
Tomatron aims to be a simple pomodoro timer with the following features:
- Timer functions operatable from the system tray
- Cross-platform (at least Linux and Windows)
- Completed pomodoro count display
- Desktop notifications on completed pomodoro/break
- Minimal build dependencies

The application is heavily inspired by tomighty: http://tomighty.org/.

## Usage
Run the jar file. A tray icon (``P`` on a grey background) is displayed.
Right click the tray icon to start pomodoros and breaks.

When a pomodoro or break is completed, a desktop notification is displayed.

The icon color and text is as follows
- Grey: Inactive and showing ``P`` displayed (stands for Pomodoro)
- Red: Pomodoro in progress, remaining time displayed
- Green: Short break in progress, remaining time displayed
- Blue: Long break in progress, remaining time displayed

Remaining time is given in minutes if more than a minute is left. If less then a minute is left, the remaining seconds are displayed.

## Build instructions
- Get Eclipse for Java developers from: https://www.eclipse.org/downloads/
- Import the project into the workspace
- Right click the project > Export > Java > Runnable JAR File

