package profiles;

import java.util.List;

import core.Enemy;

public class CommandBase
{
	protected enum CommandState
	{
		NOT_STARTED, IN_PROGRESS, FINISHED
	}
	
	protected CommandType _type;
	protected CommandState _state = CommandState.NOT_STARTED;
	
	public CommandType getType() { return _type; }
	
	public boolean isFinished() { return _state == CommandState.FINISHED; }
	public void start() { _state = CommandState.IN_PROGRESS; }
	public void finish() { _state = CommandState.FINISHED; }
	public List<Enemy> update(long elapsed) { return null; }
}
