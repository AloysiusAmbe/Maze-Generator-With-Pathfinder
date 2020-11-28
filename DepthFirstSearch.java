
import java.awt.Color;
import java.util.*;

public class DepthFirstSearch {

	// Initializing needed variables
    private Stack<Node> stack = new Stack<Node>();
    private ArrayList<Integer> paths = new ArrayList<Integer>();
    private Random rand = new Random();
    private boolean[][] visited;
    private int[][] maze;
    private int rows, columns;
    private int startX, startY;
    private int endPointX, endPointY;
    private int delay = 10;
    private final Color COLOR = Color.yellow;

    // Constructor
    public DepthFirstSearch(int[][] maze, boolean[][] visited,  int rows, int cols) {
        this.maze = maze;
    	this.visited = visited;
    	this.rows = rows;
    	this.columns = cols;
    	this.startX = 1;
    	this.startY = 1;
    	this.endPointX = rows - 2;
    	this.endPointY = cols - 2;
    }
    
    // Updates the delay
    public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setStartAndEnd (Node start, Node end) {
        this.startX = start.getX();
    	this.startY = start.getY();
    	this.endPointX = end.getX();
        this.endPointY = end.getY();
    }

    // Sets the nodes visited status to false
    public void setVisited() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
            }
        }
    }

    // Gets possible paths
    public void getAvailablePaths(int x, int y) {
    	
    	// Checks North
 		if (x - 1 >= 0 && x - 1 < rows && !(visited[x - 1][y]) && maze[x - 1][y] == 1)
 			paths.add(0);
                     
	    // Checks East
 		if (y + 1 >= 0 && y + 1 < columns && !(visited[x][y + 1]) && maze[x][y + 1] == 1)
 			paths.add(1);
	                     
	                     
	    // Checks South
	    if (x + 1 >= 0 && x + 1 < rows && !(visited[x + 1][y]) && maze[x + 1][y] == 1)
	        paths.add(2);
	                     
	                     
	    // Checks West
	    if (y - 1 >= 0 && y - 1 < columns && !(visited[x][y - 1]) && maze[x][y - 1] == 1) 
	        paths.add(3);
	}
  
	// Random direction selected
	public int randomNum() {
		if (paths.size() > 0) {
			int randomNum = rand.nextInt(paths.size());
			return paths.get(randomNum);
		}
		return -1;
	}

	// Finds a path and displays it on the GUI
    public void findPath() {
    	GUI.grid[startX][startY].setBackground(Color.green);     // Marks start
    	GUI.grid[endPointX][endPointY].setBackground(Color.red); // Marks end
        setVisited(); // Sets the visited status to False
        
        // Adds the starting point to the stack
        Node start = new Node(startX, startY);
        stack.push(start);
        visited[startX][startY] = true;
        
        while (!stack.empty()) {
        	try {
				Thread.sleep(delay);
			} catch (Exception e) {
				// TODO: handle exception
			}
        	int x = stack.peek().getX();
        	int y = stack.peek().getY();
        	
        	// Checks if the path has been found
        	if (x == endPointX && y == endPointY) {
        		System.out.println("Path Found!");
        		break;
        	}
        	
			getAvailablePaths(x, y); // Gets the available paths
			int randDir = randomNum();
			
			// North
			if (randDir == 0) {
				x -= 1;
				visited[x][y] = true;
				GUI.grid[x][y].setBackground(COLOR);
				Node temp = new Node(x, y);
				stack.push(temp);
			}
			
			// East
			else if (randDir == 1) {
				y += 1;
				visited[x][y] = true;
				GUI.grid[x][y].setBackground(COLOR);
				Node temp = new Node(x, y);
				stack.push(temp);
			}
			
			// South
			else if (randDir == 2) {
				x += 1;
				visited[x][y] = true;
				GUI.grid[x][y].setBackground(COLOR);
				Node temp = new Node(x, y);
				stack.push(temp);
			}
			
			// West
			else if (randDir == 3) {
				y -= 1;
				visited[x][y] = true;
				GUI.grid[x][y].setBackground(COLOR);
				Node temp = new Node(x, y);
				stack.push(temp);
			}
			
			// Backtracks - No paths
			else {
				Node temp = stack.pop();
				x = temp.getX();
				y = temp.getY();
				temp.setX(x);
				temp.setY(y);
				GUI.grid[x][y].setBackground(Color.white);
			}
			GUI.grid[endPointX][endPointY].setBackground(Color.red);
			paths.clear();
		}
        // CLearing the array list and the stack
        paths.clear();
        stack.clear();
    	
	}

}
