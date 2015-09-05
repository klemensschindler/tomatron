# Tomatron
Simple and extensible pomodoro timer in the system tray.

## Description 
Tomatron aims to be a simple pomodoro timer with the following features:
- Extensible in every way
- Timer functions operatable from the system tray
- Cross-platform (at least Linux and Windows)
- Completed pomodoro count display
- Desktop notifications on completed pomodoro/break
- Minimal build dependencies

The applications started from a fork of Tomatron

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

## INSTALL
###Ubuntu with Unity
From ```tomatron``` directory execute:
```bash
# install.sh
```
Run from Unity Dash

###Another Enviroment
- From ```tomatron/src``` directory execute:
```bash
 # javac -verbose -d ../bin/classes ./tomatron/model/*.java ./tomatron/controller/*.java  ./tomatron/view/*.java && jar vcmf Manifest.txt ../bin/tomatron -C ../bin/classes .;
```
Run from terminal
```bash
 $ java -jar ../bin/tomatron
```
## UNINSTALL
###Ubuntu with Unity
- From ```tomatron``` directory execute:
```bash
# uninstall.sh
```
 


