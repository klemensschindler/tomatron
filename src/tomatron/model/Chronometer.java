package tomatron.model;

public class Chronometer implements IChronometer {

	protected int minutes;

	public void start() { System.out.println("Started"); }
	public void pause() {  System.out.println("Paused"); }
	public void stop() {  System.out.println("Stopped"); }
	public void setMinutes(int minutes) { this.minutes = minutes; }
	public int getMinutes() {  return minutes; }
	public int getCurrentTime() {  return minutes; }
}
