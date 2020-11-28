
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI extends JPanel {
	
    // Initializes these class
    MazeGenerator generator;
    BreadthFirstSearch breadthFirstSearch;
	DepthFirstSearch depthFirstSearch;
	AStar aStar;

	// Initializing needed variables
    private int rows = 53, cols = 53;
	private int delay = 10;
	private int startX = 1, startY = 1;
	private int endX = rows - 2, endY = cols - 2;
	private boolean solving = false;
	private int solvedCount = 0;
    private final Color WALL_COLOR = Color.gray;
    private final Color PATH_COLOR = Color.white;

    static JButton grid[][];
    private int[][] maze;
    private boolean[][] visited;
    
    private JPanel container = new JPanel();
	private JPanel controlsPanel = new JPanel();
	private JPanel mazePanel = new JPanel();
	
	private JButton bfs = new JButton("BFS");
	private JButton searchButton = new JButton("Find Path");
	static JButton generateMaze = new JButton("Generate Maze");
	private JSlider speed = new JSlider(0, 100, delay);
	
	private JLabel algosJLabel = new JLabel();
	private JLabel dfsText = new JLabel();
	private JLabel genMaze = new JLabel();
	private JLabel delayJLabel = new JLabel("Delay: ");
	private JLabel milliseconds = new JLabel(Integer.toString(delay) + "ms");
	private String[] algosStrings = {"Breadth First Search", "Depth First Search", "A-Star"};
	private JComboBox<String> algosComboBox = new JComboBox<String>(algosStrings);

	private String[] optionStrings = {"Start", "End", "Wall", "Path"};
	private JComboBox<String> optionsComboBox = new JComboBox<String>(optionStrings);
    
	private JFrame window = new JFrame("Maze Generator with PathFinder");
	
	// GUI constructor
    public GUI() {
		container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
		
		String title = "Controls";
		Border border = BorderFactory.createTitledBorder(title);
		controlsPanel.setBorder(border);
		
		controlsPanel.setLayout(null);
		mazePanel.setLayout(null);
		
		algosJLabel.setText("Algorithms: ");
		dfsText.setText("Depth First Seacrh: ");
		genMaze.setText("Generate Maze: ");
		
		algosJLabel.setBounds(40, 25, 180, 25);
		algosComboBox.setBounds(120, 25, 180, 25);

		optionsComboBox.setBounds(370, 25, 180, 25);
		
		generateMaze.setBounds(630, 25, 180, 25);
		delayJLabel.setBounds(600, 65, 180, 25);
		speed.setBounds(640, 65, 180, 25);
		milliseconds.setBounds(820, 65, 180, 25);
		
		searchButton.setBounds(80, 65, 180, 25);
		
		controlsPanel.add(algosJLabel);
		controlsPanel.add(bfs);
		
		controlsPanel.add(dfsText);
		controlsPanel.add(searchButton);
		
		controlsPanel.add(generateMaze);
		controlsPanel.add(delayJLabel);
		controlsPanel.add(speed);
		controlsPanel.add(milliseconds);

		controlsPanel.add(algosComboBox);
		controlsPanel.add(optionsComboBox);
		
		mazePanel.setLayout(new GridLayout(rows, cols));
		
        grid = new JButton[rows][cols];
		maze = new int[rows][cols];
		visited = new boolean[rows][cols];

		ActionListener btnListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent evt) {
				JButton selectedButton = (JButton) evt.getSource();
				int selectedOption = optionsComboBox.getSelectedIndex();
				
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						if (grid[i][j] == selectedButton && !solving) {
							switch (selectedOption) {
								// Start point
								case 0:
									grid[startX][startY].setBackground(PATH_COLOR);
									startX = i;
									startY = j;
									grid[i][j].setBackground(Color.green);
									break;

								// End point
								case 1:
									grid[endX][endY].setBackground(PATH_COLOR);
									endX = i;
									endY = j;
									grid[i][j].setBackground(Color.red);
									break;

								// Wall
								case 2:
									maze[i][j] = 0;
									grid[i][j].setBackground(WALL_COLOR);
									break;

								// Path
								case 3:
									maze[i][j] = 1;
									grid[i][j].setBackground(PATH_COLOR);
									break;
							}

							if (solvedCount >= 1) {
								// Gets the algorithm choose of the user
								resetGrid();
								int selectedAlgo = algosComboBox.getSelectedIndex();
								Node start = new Node(startX, startY);
								Node end = new Node(endX, endY);
								System.out.println(startX + " " + startY);
								System.out.println(endX + " " + endY);
								switch (selectedAlgo) {
									case 0:
										breadthFirstSearch.setStartAndEnd(start, end);
										breadthFirstSearch.setDelay(0);
										breadthFirstSearch.findPath();
										break;

									case 2:
										aStar.setStartAndEnd(start, end);
										aStar.setDelay(0);
										aStar.aStarSearch();
										break;
								}
							}
						}
					}
				}
			}
		};
		
        // Adding the grid to the JFrame
     	for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				grid[i][j] = new JButton();
				grid[i][j].addActionListener(btnListener);
				grid[i][j].setBackground(PATH_COLOR);
				mazePanel.add(grid[i][j]);
				maze[i][j] = 1;
			}
		}
     	
     	container.add(controlsPanel);
     	container.add(mazePanel);
     	
		window.add(container);
     	
        // JFrame components
		window.setSize(900, 770);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		
		generateMaze.addActionListener(new GenerateMazeListener());
		searchButton.addActionListener(new findPathListener());
		speed.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				delay = (int)speed.getValue();
				milliseconds.setText(Integer.toString(delay) + "ms");
				depthFirstSearch.setDelay(delay);
				breadthFirstSearch.setDelay(delay);
				aStar.setDelay(delay);
				generator.setDelay(delay);
			}
		});
		
		// Initializing needed classes
		depthFirstSearch = new DepthFirstSearch(maze, visited, rows, cols);
		breadthFirstSearch = new BreadthFirstSearch(maze, visited, rows, cols);
		generator = new MazeGenerator(maze, visited, rows, cols);
		aStar = new AStar(maze, visited, rows, cols);
    }
    
    // Button Listeners 
    private class GenerateMazeListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			createMaze();
		}
    }
    
    private class findPathListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			findPath();
		}
    }
    
    // Generates the maze
    private void createMaze() {
    	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				solving = true;
				generateMaze.setEnabled(false);
				bfs.setEnabled(false);
				searchButton.setEnabled(false);
				
				generator.createMaze();
				resetGrid();
				
				bfs.setEnabled(true);
				searchButton.setEnabled(true);
				generateMaze.setEnabled(true);
				solving = false;
				return null;
			}
		};
		worker.execute();
	}
    
    // Starts the Depth First Search
    private void findPath() {
    	SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				solvedCount = 1;
				solving = true;
				generateMaze.setEnabled(false);
				bfs.setEnabled(false);
				searchButton.setEnabled(false);
				
				resetGrid();
				
				// Start and end nodes
				Node start = new Node(startX, startY);
				Node end = new Node(endX, endY);

				// Gets the algorithm choose of the user
				int selectedAlgo = algosComboBox.getSelectedIndex();
				switch (selectedAlgo) {
					case 0:
						breadthFirstSearch.setStartAndEnd(start, end);
						breadthFirstSearch.findPath();
						break;

					case 1:
						depthFirstSearch.setStartAndEnd(start, end);
						depthFirstSearch.findPath();
						break;

					case 2:
						aStar.setStartAndEnd(start, end);
						aStar.aStarSearch();
						break;
				}
				
				generateMaze.setEnabled(true);
				bfs.setEnabled(true);
				searchButton.setEnabled(true);
				solving = false;
				return null;
			}
		};
		worker.execute();
	}
    
    // Resets the grid
    private void resetGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if (maze[i][j] == 0) {
					grid[i][j].setBackground(WALL_COLOR);
				
				} else {
					grid[i][j].setBackground(PATH_COLOR);
				}
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
