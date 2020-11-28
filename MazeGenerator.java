
// ALGORITHM - Maze Generator
// 1.Choose the initial cell, mark it as visited and push it to the stack
// 2.While the stack is not empty
// 		1. Pop a cell from the stack and make it a current cell
// 		2. If the current cell has any neighbors which have not been visited
// 			1. Push the current cell to the stack
// 			2. Choose one of the unvisited neighbors
// 			3. Remove the wall between the current cell and the chosen cell
// 			4. Mark the chosen cell as visited and push it to the stack

import java.awt.Color;
import java.util.*;

public class MazeGenerator {

// Initializing needed variables
    private Stack<Node> stack = new Stack<Node>();
    private ArrayList<Integer> openDir = new ArrayList<Integer>();
    private Random rand = new Random();
    private int[][] maze;
    private boolean[][] visited;
    private int rows, columns;
    private int startX, startY;
    private int delay = 10;
    private final Color WALL_COLOR = Color.gray;

    // Constructor
    public MazeGenerator(int[][] maze, boolean[][] visited, int rows, int columns) {
        this.maze = maze;
        this.visited = visited;
        this.rows = rows;
        this.columns = columns;
        this.startX = (rows / 2) + 1;
        this.startY = (columns / 2) + 1;
    }
    
    // Updates the delay
    public void setDelay(int delay) {
		this.delay = delay;
	}
    
	// Sets the nodes visited status to false
    public void initialize() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
				maze[i][j] = 0;
				GUI.grid[i][j].setBackground(WALL_COLOR);
            }
        }
    }
    
    // Places the open direction into the arraylist - 'openDir'
	public void getRandomDirection(int x, int y) {
		
		// Checks North
		if (x - 2 >= 0 && x - 2 < this.rows && !(this.visited[x - 2][y]))
			this.openDir.add(0);
				
		// Checks East
		if (y + 2 >= 0 && y + 2 < this.columns && !(this.visited[x][y + 2]))
			this.openDir.add(1);
				
				
		// Checks South
		if (x + 2 >= 0 && x + 2 < this.rows && !(this.visited[x + 2][y]))
			this.openDir.add(2);
				
		
		// Checks West
		if (y - 2 >= 0 && y - 2 < this.columns && !(this.visited[x][y - 2])) 
			this.openDir.add(3);
			
	}
    
	// Random direction selected
	public int randomNum() {
		if (openDir.size() > 0) {
			int randomNum = rand.nextInt(openDir.size());
			return openDir.get(randomNum);
		}
		return -1;
	}

    // Generates the maze
    public void createMaze() {
       	Node start; // Start node
        int x, y;   // Stores the current x and y values
        int previousX, previousY; // Sets the color of the top back to white

		initialize();
        // Creates a start position and puts it in the stack
        start = new Node(startX, startY);
        stack.push(start);

        visited[startX][startY] = true;
        maze[startX][startY] = 1;
        
        // Executes till the stack is empty
        while (!stack.empty()) {
        	x = stack.peek().getX();
        	y = stack.peek().getY();
        	
        	maze[x][y] = 1;
        	GUI.grid[x][y].setBackground(Color.green);
        	
        	// Selects a random direction to move in
			getRandomDirection(x, y);
			int randomNum = randomNum();

			// North
			if (randomNum == 0) {
				maze[x - 1][y] = 1; // Turns the current position to a path
				
				x = stack.peek().getX() - 2;
				visited[x][y] = true; // Changes the visited status
				maze[x][y] = 1;
				
				Node temp = new Node(x, y);
				stack.push(temp); // Adds the current position to the stack
			}
			
			// East
			else if (randomNum == 1) {
				maze[x][y + 1] = 1; // Turns the current position to a path
				
				y = stack.peek().getY() + 2;
				visited[x][y] = true; // Changes the visited status
				maze[x][y] = 1;

				Node temp = new Node(x, y);
				stack.push(temp); // Adds the current position to the stack
			}

			// South
			else if (randomNum == 2) {
				maze[x + 1][y] = 1; // Turns the current position to a path

				x = stack.peek().getX() + 2;
				visited[x][y] = true; // Changes the visited status
				maze[x][y] = 1;

				Node temp = new Node(x, y);
				stack.push(temp); // Adds the current position to the stack
			}

			// West
			else if (randomNum == 3) {
				maze[x][y - 1] = 1; // Turns the current node to a path

				y = stack.peek().getY() - 2;
				visited[x][y] = true; // Changes the visited status
				maze[x][y] = 1;

				Node temp = new Node(x, y);
				stack.push(temp); // Adds the current position to the stack
			}

			// Backtracks
			else if (randomNum == -1) {
				stack.pop();
			}
			
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Painting the grid to show the maze generation
			for (int i = 0; i < rows; i++) {
    			for (int j = 0; j < columns; j++) {
    				if (maze[i][j] == 1) {
						GUI.grid[i][j].setBackground(Color.white);
					
    				} else {
						GUI.grid[i][j].setBackground(WALL_COLOR);
					}
    			}
    		}
			
			openDir.clear(); // Clears the array list
        }
		GUI.grid[startX][startY].setBackground(Color.white);
    }
    
}

