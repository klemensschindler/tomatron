package tomatron.model;

public interface IChronometerCreator{

	IChronometer createChronometer();

	IChronometer createWorkPomodoro();
	IChronometer createLongBreakPomodoro();
	IChronometer createShortBreakPomodoro();
}
