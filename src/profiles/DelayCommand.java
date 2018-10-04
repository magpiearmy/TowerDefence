package profiles;

public class DelayCommand extends Command
{
	private int _seconds;
	
	public DelayCommand(int seconds)
	{
		type = CommandType.COMMAND_DELAY;
		_seconds = seconds;
	}
	
	public int getSeconds() { return _seconds; }
}
