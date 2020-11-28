
import javax.swing.*;

// GREEN - The start node
// RED - The target node
// BLUE - Shows the program searching for the target node
// YELLOW - Displaying a path from the start node to the end node

public class Main {
	
	public static void main(String[] args) {
		
		// Initializing the GUI class and creating a JFrame
		GUI gui = new GUI();
		JFrame frame = new JFrame();
		frame.getContentPane().add(gui);
	}
}
