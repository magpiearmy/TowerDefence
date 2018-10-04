package profiles;

import java.util.List;

import core.Enemy;

public class Command
{
	protected enum CommandState
	{
		NOT_STARTED, IN_PROGRESS, FINISHED
	}
	
	protected CommandType type;
	protected CommandState state = CommandState.NOT_STARTED;
	
	public CommandType getType() { return type; }
	
	public boolean isFinished() { return state == CommandState.FINISHED; }
	public void start() { state = CommandState.IN_PROGRESS; }
	public void finish() { state = CommandState.FINISHED; }
	public List<Enemy> update(long elapsed) { return null; }
}
