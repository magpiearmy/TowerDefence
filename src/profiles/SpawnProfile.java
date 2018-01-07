package profiles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpawnProfile
{
	private List<CommandBase> _commands = new ArrayList<CommandBase>();
	private Iterator<CommandBase> _iterator;
	
	public SpawnProfile()
	{
	}
	
	public void addCommand(CommandBase command)
	{
		_commands.add(command);
	}
	
	public void finalize() {
		_iterator = _commands.iterator();
		_iterator.next();
	}
	
	public CommandBase getNextCommand()
	{
		if (_iterator.hasNext())
			return _iterator.next();
		else
			return null;
	}
}
