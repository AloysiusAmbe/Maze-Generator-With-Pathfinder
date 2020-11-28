
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar {

    // Initializing needed variable
    private int[][] maze;
    private boolean[][] visited;
    private Point[][] pointOnGrid;
    private int rows, columns;
    private int startX, startY;
    private int endPointX, endPointY;
    private int delay = 10;
    private ArrayList<Character> neighbors = new ArrayList<Character>();
    private PriorityQueue<Point> priorityQueue = new PriorityQueue<Point>(new PointComparator());
    private final Color PATH_COLOR = Color.yellow;

    // Constructor
    public AStar(int[][] maze, boolean[][] visited, int rows, int cols) {
        this.maze = maze;
        this.visited = visited;
    	this.rows = rows;
    	this.columns = cols;
        pointOnGrid = new Point[rows][cols];
    }

    // Updates the delay
    public void setDelay(int delay) {
		this.delay = delay;
    }
    
    public void setStartAndEnd(Node start, Node end) {
    	this.startX = start.getX();
    	this.startY = start.getY();
    	this.endPointX = end.getX();
        this.endPointY = end.getY();
    }

    // Sets every nodes' visited status to false and sets
    // the f_score, g_score and h_score to infinity
    private void initialize() {
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
                pointOnGrid[i][j] = new Point();
            }
        }
    }

    // Gets the neighbors of a node
    private void getNeighbors(int x, int y) {
    	
    	// Checks North
 		if (x - 1 >= 0 && x - 1 < rows && !(visited[x - 1][y]) && maze[x - 1][y] == 1)
             neighbors.add('N');
             

	    // Checks East
 		if (y + 1 >= 0 && y + 1 < columns && !(visited[x][y + 1]) && maze[x][y + 1] == 1)
             neighbors.add('E');
             

	    // Checks South
	    if (x + 1 >= 0 && x + 1 < rows && !(visited[x + 1][y]) && maze[x + 1][y] == 1)
            neighbors.add('S');
            

	    // Checks West
	    if (y - 1 >= 0 && y - 1 < columns && !(visited[x][y - 1]) && maze[x][y - 1] == 1) 
	        neighbors.add('W');
    }
    
    // Checks if we have reached the endpoint/destination
    private boolean isDestination(int cur_x, int cur_y) {
        if (cur_x == endPointX && cur_y == endPointY)
            return true;
        return false;
    }

    // Backtracks to the start point after finding the endpoint
    private void backtrackToStart(Point destination) {
        Node node = destination.getNode();
        GUI.grid[node.getX()][node.getY()].setBackground(Color.red);
        Point point = destination.getParent();
        
        while (point.getParent() != null) {
            delay(delay);
            node = point.getNode();
            GUI.grid[node.getX()][node.getY()].setBackground(PATH_COLOR);
            point = point.getParent();
        }
        priorityQueue.clear();
    }

    // Calculates the heuristic value (h_score) - Manhattan Distance
    private int heuristic(int cur_x, int cur_y) {
        int h_score = Math.abs(cur_x - endPointX) + Math.abs(cur_y - endPointY);
        return h_score;
    }

    // A-Star search algorithm implementation
    public void aStarSearch() {
        // Marks the start and end node
        GUI.grid[startX][startY].setBackground(Color.green);
 		GUI.grid[endPointX][endPointY].setBackground(Color.red);
        initialize();

        // Initializing the start point and add it to the priority queue
        Node startNode = new Node(startX, startY);
        pointOnGrid[startX][startY] = new Point(startNode, null, 0, 0, 0);
        priorityQueue.add(pointOnGrid[startX][startY]);
        
        while (!priorityQueue.isEmpty()) {
            delay(delay);
            Point currentPoint = priorityQueue.poll();
            int x = currentPoint.getNode().getX();
            int y = currentPoint.getNode().getY();
            visited[x][y] = true;

            getNeighbors(x, y);

            // North
            if (neighbors.contains('N')) {
                int cur_x = x - 1;
                int cur_y = y;
                GUI.grid[cur_x][cur_y].setBackground(Color.cyan);

                Node node = new Node(cur_x, cur_y);
                int g_score = currentPoint.get_gscore() + 1;
                int h_score = heuristic(cur_x, cur_y);
                int f_score = g_score + h_score;
                Point newPoint = new Point(node, currentPoint, g_score, h_score, f_score);

                // Checks if a connection has been found -
                // if the current node being checked is the endpoint
                if (isDestination(cur_x, cur_y)) {
                    backtrackToStart(newPoint);
                    return;
                }

                if (pointOnGrid[cur_x][cur_y].get_fScore() > f_score || pointOnGrid[cur_x][cur_y].get_fScore() == Integer.MAX_VALUE) {
                    priorityQueue.add(newPoint);
                }
            }

            // South
            if (neighbors.contains('S')) {
                int cur_x = x + 1;
                int cur_y = y;
                GUI.grid[cur_x][cur_y].setBackground(Color.cyan);

                Node node = new Node(cur_x, cur_y);
                int g_score = currentPoint.get_gscore() + 1;
                int h_score = heuristic(cur_x, cur_y);
                int f_score = g_score + h_score;
                Point newPoint = new Point(node, currentPoint, g_score, h_score, f_score);

                // Checks if a connection has been found -
                // if the current node being checked is the endpoint
                if (isDestination(cur_x, cur_y)) {
                    backtrackToStart(newPoint);
                    return;
                }

                if (pointOnGrid[cur_x][cur_y].get_fScore() > f_score || pointOnGrid[cur_x][cur_y].get_fScore() == Integer.MAX_VALUE) {
                    priorityQueue.add(newPoint);
                }
            }

            // East
            if (neighbors.contains('E')) {
                int cur_x = x;
                int cur_y = y + 1;
                GUI.grid[cur_x][cur_y].setBackground(Color.cyan);

                Node node = new Node(cur_x, cur_y);
                int g_score = currentPoint.get_gscore() + 1;
                int h_score = heuristic(cur_x, cur_y);
                int f_score = g_score + h_score;
                Point newPoint = new Point(node, currentPoint, g_score, h_score, f_score);

                // Checks if a connection has been found -
                // if the current node being checked is the endpoint
                if (isDestination(cur_x, cur_y)) {
                    backtrackToStart(newPoint);
                    return;
                }

                if (pointOnGrid[cur_x][cur_y].get_fScore() > f_score || pointOnGrid[cur_x][cur_y].get_fScore() == Integer.MAX_VALUE) {
                    priorityQueue.add(newPoint);
                }
            }

            // West
            if (neighbors.contains('W')) {
                int cur_x = x;
                int cur_y = y - 1;
                GUI.grid[cur_x][cur_y].setBackground(Color.cyan);

                Node node = new Node(cur_x, cur_y);
                int g_score = currentPoint.get_gscore() + 1;
                int h_score = heuristic(cur_x, cur_y);
                int f_score = g_score + h_score;
                Point newPoint = new Point(node, currentPoint, g_score, h_score, f_score);

                // Checks if a connection has been found -
                // if the current node being checked is the endpoint
                if (isDestination(cur_x, cur_y)) {
                    backtrackToStart(newPoint);
                    return;
                }

                if (pointOnGrid[cur_x][cur_y].get_fScore() > f_score || pointOnGrid[cur_x][cur_y].get_fScore() == Integer.MAX_VALUE) {
                    priorityQueue.add(newPoint);
                }
            }
            neighbors.clear();
        }
    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}

// Stores a point on the grid along with its parent,
// heuristic score (h), global score (f) and the movement cost
// to the next point/node (g)
class Point {
    private Node node;
    private Point parent;
    private int f_score;
    private int g_score;
    private int h_score;

    // Used when the points on the grid are being initialized
    // so we can set the g_score, h_score, f_score to infinity
    public Point() {
        final int MAX = Integer.MAX_VALUE;
        this.g_score = MAX;
        this.h_score = MAX;
        this.f_score = MAX;
    }

    public Point(Node node, Point parent, int g_score, int h_score, int f_score) {
        this.node = node;
        this.parent = parent;
        this.g_score = g_score;
        this.h_score = h_score;
        this.f_score = f_score;
    }

    public Node getNode() {
        return node;
    }

    public Point getParent() {
        return parent;
    }

    public int get_fScore() {
        return f_score;
    }

    public int get_gscore() {
        return g_score;
    }

    public int get_hscore() {
        return h_score;
    }
}

class PointComparator implements Comparator<Point> {
    public int compare(Point p1, Point p2) {
        if (p1.get_fScore() < p2.get_fScore())
            return -1;

        else if (p1.get_fScore() > p2.get_fScore())
            return 1;

        return 0;
    }
}
