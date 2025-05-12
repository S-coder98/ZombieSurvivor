import javax.swing.*;			// need this for GUI objects
import java.awt.*;			// need this for Layout Managers
import java.awt.event.*;		// need this to respond to GUI events
	
public class GameWindow extends JFrame 
				implements ActionListener,
					   KeyListener,
					   MouseListener
{
	// declare instance variables for user interface objects

	// declare labels 

	private JLabel statusBarL;
	private JLabel keyL;

	// declare text fields

	private JTextField statusBarTF;
	private JTextField keyTF;

	// declare buttons

	private JButton startB;
	private JButton pauseB;
	private JButton endB;
	private JButton exitB;

	private Container c;

	private JPanel mainPanel;
	private GamePanel gamePanel;

	private SoundManager soundManager;

	@SuppressWarnings({"unchecked"})
	public GameWindow() {
 
		setTitle ("Zombie Survivor");
		setSize (1000, 800);
		soundManager = SoundManager.getInstance();
		// create user interface objects

		// create labels

		statusBarL = new JLabel ("     Application Status: ");
		keyL = new JLabel("     Key Pressed: ");
		
		// create text fields and set their colour, etc.

		statusBarTF = new JTextField (25);
		keyTF = new JTextField (25);

		statusBarTF.setEditable(false);
		keyTF.setEditable(false);

		statusBarTF.setBackground(Color.gray);
		keyTF.setBackground(Color.gray);

		// create buttons

	        startB = new JButton ("Start Game");
	        pauseB = new JButton ("Pause Game");
	        endB = new JButton ("End Game");
			exitB = new JButton ("Exit");


		// add listener to each button (same as the current object)

		startB.addActionListener(this);
		pauseB.addActionListener(this);
		endB.addActionListener(this);
		exitB.addActionListener(this);
		
		// create mainPanel

		mainPanel = new JPanel();
		FlowLayout flowLayout = new FlowLayout();
		mainPanel.setLayout(flowLayout);

		GridLayout gridLayout;

		// create the gamePanel for game entities

		gamePanel = new GamePanel();
        gamePanel.setPreferredSize(new Dimension(800, 600));
	

		// create infoPanel

		JPanel infoPanel = new JPanel();
		gridLayout = new GridLayout(2, 2);
		infoPanel.setLayout(gridLayout);
		infoPanel.setBackground(Color.gray);

		// add user interface objects to infoPanel
	
		infoPanel.add (statusBarL);
		infoPanel.add (statusBarTF);

		infoPanel.add (keyL);
		infoPanel.add (keyTF);		

		
		// create buttonPanel

		JPanel buttonPanel = new JPanel();
		gridLayout = new GridLayout(1, 4);
		buttonPanel.setLayout(gridLayout);

		// add buttons to buttonPanel

		buttonPanel.add (startB);
		buttonPanel.add (pauseB);
		buttonPanel.add (endB);
		buttonPanel.add (exitB);

		// add sub-panels with GUI objects to mainPanel and set its colour

		mainPanel.add(infoPanel);
		mainPanel.add(gamePanel);
		mainPanel.add(buttonPanel);


		mainPanel.setBackground(Color.gray);

		// set up mainPanel to respond to keyboard and mouse

		gamePanel.addMouseListener(this);
		mainPanel.addKeyListener(this);

		// add mainPanel to window surface

		c = getContentPane();
		c.add(mainPanel);

		// set properties of window

		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setVisible(true);

		// set status bar message

		statusBarTF.setText("Application started.");
	}


	// implement single method in ActionListener interface

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();
		
		statusBarTF.setText(command + " button clicked.");

		if (command.equals(startB.getText())) {
			soundManager.playClip ("click", false);
			gamePanel.startGame();
			
		}

		if (command.equals(pauseB.getText())) {
			soundManager.playClip ("click", false);
			gamePanel.pauseGame();
			if (command.equals("Pause Game"))
				pauseB.setText ("Resume");
			else
				pauseB.setText ("Pause Game");

		}
		
		if (command.equals(endB.getText())) {
			soundManager.playClip ("click", false);
			gamePanel.endGame();
		}

		if (command.equals(exitB.getText()))
			System.exit(0);

		mainPanel.requestFocus();
	}


	// implement methods in KeyListener interface

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		String keyText = e.getKeyText(keyCode);
		keyTF.setText(keyText + " pressed.");

		if (keyCode == KeyEvent.VK_LEFT) {
			gamePanel.playerMove(1);
			gamePanel.updatePlayer (1);
			gamePanel.setLeftPressed(true);
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			gamePanel.playerMove (2);
			gamePanel.updatePlayer (2);
			gamePanel.setRightPressed(true);
		}

		if (keyCode == KeyEvent.VK_UP) {
			gamePanel.playerMove (3);
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			gamePanel.playerMove (4);
		}

		if (keyCode == KeyEvent.VK_SPACE) { // Spacebar to shoot
			gamePanel.playerShoot(); 
		}
	}

	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_LEFT) {
			gamePanel.setLeftPressed(false);
		}

		if (keyCode == KeyEvent.VK_RIGHT) {
			gamePanel.setRightPressed(false);
		}
	}

	public void keyTyped(KeyEvent e) {

	}


	// implement methods in MouseListener interface

	public void mouseClicked(MouseEvent e) {


	}


	public void mouseEntered(MouseEvent e) {
	
	}

	public void mouseExited(MouseEvent e) {
	
	}

	public void mousePressed(MouseEvent e) {
	
	}

	public void mouseReleased(MouseEvent e) {
	
	}

}