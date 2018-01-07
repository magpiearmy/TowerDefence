package core;
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	
	public static final String FRAME_TITLE = "Defend the Empire!";
	public static final Dimension FRAME_SIZE = new Dimension(900,640);
	
	public GameFrame()
	{
		setTitle(FRAME_TITLE);
		setSize(FRAME_SIZE);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init()
	{	
		setLayout(new GridLayout(1,1,0,0));
		
		GameScreen screen = new GameScreen();
		add(screen);
		screen.init();
		
		setVisible(true);
	}
	
	public void update(Graphics g)
	{
		super.update(g);
	}
	
	public static void main(String[] args) {
		GameFrame frame = new GameFrame();
		frame.init();
	}
}
