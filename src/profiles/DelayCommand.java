package profiles;

public class DelayCommand extends CommandBase
{
	private int _seconds;
	
	public DelayCommand(int seconds)
	{
		_type = CommandType.COMMAND_DELAY;
		_seconds = seconds;
	}
	
	public int getSeconds() { return _seconds; }
}
