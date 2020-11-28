
import java.awt.Color;
import java.util.*;

//ALGORITHM - Breadth First Search
//1. Add the start node to the queue
//2. While the queue is not empty
//		1. Check if the current position is equal to the end node
//			1. If it is, break out of the loop
//		2. Check all the possible directions from the current position
//   	3. Add the available directions to the queue
//   	4. Mark the available directions as visited
//	 	5. Remove the first element from the queue

public class BreadthFirstSearch {

	// Initializing needed variables
    private ArrayList<String> openPath = new ArrayList<String>();
    private ArrayList<String> paths = new ArrayList<String>();
    private Queue<Node> queue = new LinkedList<Node>();
    private Queue<String> allPaths = new LinkedList<String>();
    
    private int[][] maze;
    private boolean[][] visited;
    private int rows, columns;
    private int startX, startY;
    private int endPointX, endPointY;
    private int delay = 10;
    
    // Constructor
    public BreadthFirstSearch(int[][] maze, boolean[][] visited,  int rows, int cols) {
    	this.maze = maze;
    	this.visited = visited;
    	this.rows = rows;
    	this.columns = cols;
    	this.startX = 1;
		this.startY = 1;
		this.endPointX = rows - 2;
		this.endPointY = columns - 2;
    }
    
    // Updates the delay
    public void setDelay(int delay) {
		this.delay = delay;
	}

	// Sets the nodes visited status to false
    private void setVisited() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
            }
        }
    }
    
    // Checks if the North, East, South, and West directions are
    // valid paths - Not out of bounds, and has not been visited
 	private void isValidPath(int x, int y) {
 		
 		// Checks North
 		if (x - 1 >= 0 && x - 1 < rows && !(visited[x - 1][y]) && maze[x - 1][y] != 0)
 			openPath.add("U");
 		
 						
 		// Checks East
 		if (y + 1 >= 0 && y + 1 < columns && !(visited[x][y + 1]) && maze[x][y + 1] != 0)
 			openPath.add("R");
 						
 						
 		// Checks South
 		if (x + 1 >= 0 && x + 1 < rows && !(visited[x + 1][y]) && maze[x + 1][y] != 0)
 			openPath.add("D");
 						
 						
 		// Checks West
 		if (y - 1 >= 0 && y - 1 < columns && !(visited[x][y - 1]) && maze[x][y - 1] != 0) 
 			openPath.add("L");
 	}
 	
    // BREADTH FIRST SEARCH - Searches for a path from the start to the end node
 	public void findPath() {
 		 GUI.grid[startX][startY].setBackground(Color.green);     // Marks the start
 		 GUI.grid[endPointX][endPointY].setBackground(Color.red); // Marks the target
         setVisited(); // Sets the visited status to False

         // Adds the starting point to the queue
         Node start = new Node(startX, startY);
         queue.add(start);
         visited[startX][startY] = true;

         String currentPath = "";

         // Contains a history of all visited x and y positions
         ArrayList<Integer> Xs = new ArrayList<Integer>();
         ArrayList<Integer> Ys = new ArrayList<Integer>();

         // Executes while the queue is not empty
         while (!queue.isEmpty()) {
        	 try {
 				Thread.sleep(delay);
 			 } catch (InterruptedException e) {
 				e.printStackTrace();
 			 }
             isValidPath(queue.peek().getX(), queue.peek().getY());

             // North
             if (openPath.contains("U")) {
                 // Adds the node to the queue
                 int x = queue.peek().getX() - 1;
                 int y = queue.peek().getY();
                 visited[x][y] = true;
                 GUI.grid[x][y].setBackground(Color.cyan);

                 // Adds the current node to the queue
                 Node temp = new Node(x, y);
                 Xs.add(x);
                 Ys.add(y);
                 queue.add(temp);

                 // Keeps track of the directions selected - all the paths
                 String newPath = currentPath.concat("U");
                 allPaths.add(newPath);
                 paths.add(newPath);
             }

             // East
             if (openPath.contains("R")) {
                 // Adds the node to the queue
                 int x = queue.peek().getX();
                 int y = queue.peek().getY() + 1;
                 visited[x][y] = true;
                 GUI.grid[x][y].setBackground(Color.cyan);

                 // Adds the current node to the queue
                 Node temp = new Node(x, y);
                 Xs.add(x);
                 Ys.add(y);
                 queue.add(temp);

                 // Keeps track of the directions selected - all the paths
                 String newPath = currentPath.concat("R");
                 allPaths.add(newPath);
                 paths.add(newPath);
             }

             // South
             if (openPath.contains("D")) {
                 // Adds the node to the queue
                 int x = queue.peek().getX() + 1;
                 int y = queue.peek().getY();
                 visited[x][y] = true;
                 GUI.grid[x][y].setBackground(Color.cyan);

                 // Adds the current node to the queue
                 Node temp = new Node(x, y);
                 Xs.add(x);
                 Ys.add(y);
                 queue.add(temp);

                 // Keeps track of the directions selected - all the paths
                 String newPath = currentPath.concat("D");
                 allPaths.add(newPath);
                 paths.add(newPath);
             }

             // West
             if (openPath.contains("L")) {
                 // Adds the node to the queue
                 int x = queue.peek().getX();
                 int y = queue.peek().getY() - 1;
                 visited[x][y] = true;
                 GUI.grid[x][y].setBackground(Color.cyan);

                 // Adds the current node to the queue
                 Node temp = new Node(x, y);
                 Xs.add(x);
                 Ys.add(y);
                 queue.add(temp);

                 // Keeps track of the directions selected - all the paths
                 String newPath = currentPath.concat("L");
                 allPaths.add(newPath);
                 paths.add(newPath);
             }
             // Removes the first element in the queue
             Node temp = queue.poll();
             currentPath = allPaths.poll();
             
             // Checks if the current nodes are the end points
             if (Xs.get(Xs.size() - 1) == endPointX && Ys.get(Ys.size() - 1) == endPointY) {
				System.out.println("Path Found!");
				displayPath();
				break;
			}
            openPath.clear(); // Clears the array list
         }
         
 	}

 	// Displays the path to the end node
 	private void displayPath() {
 		 int currentX = startX;
         int currentY = startY;
         
         char[] dir = paths.get(paths.size() - 1).toCharArray();

         for (char direction : dir) {
        	 try {
 				Thread.sleep(delay);
 			} catch (InterruptedException e) {
 				e.printStackTrace();
 			}
        	 
             // Moves up
             if (direction == 'U') {
                 currentX -= 1;
                 GUI.grid[currentX][currentY].setBackground(Color.yellow);
             }
             // Moves right
             else if (direction == 'R') {
                 currentY += 1;
                 GUI.grid[currentX][currentY].setBackground(Color.yellow);
             }
             // Moves down
             else if (direction == 'D') {
                 currentX += 1;
                 GUI.grid[currentX][currentY].setBackground(Color.yellow);
             }
             // Moves left
             else if (direction == 'L') {
                 currentY -= 1;
                 GUI.grid[currentX][currentY].setBackground(Color.yellow);
             }
             GUI.grid[endPointX][endPointY].setBackground(Color.red);
         }
         // Clearing the array list and queues
         openPath.clear();
         paths.clear();
         queue.clear();
         allPaths.clear();
 	}
 	
}
