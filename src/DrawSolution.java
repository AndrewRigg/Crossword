import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class DrawSolution extends JComponent {
	
	private static final long serialVersionUID = 1L;
	int x,y;
	int frameSizeX, frameSizeY;
	private int squareSize = 20;
	private String[][] grid;
	JFrame frame;
	Font font;
	JTextArea area;
	
	public DrawSolution(String[][] grid,int x, int y){
		this.grid = grid;
		this.x = x;
		this.y = y;
		font = new Font("Times New Roman", Font.PLAIN, squareSize / 5 * 3);
		frameSizeX = 20*x+20;
		frameSizeY = 20*y+40;
		setOpaque(true);
		setBackground(Color.WHITE);
		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		frame = new JFrame("Auto-crossword solution!  (I bet you're cheating...) ");
		frame.setPreferredSize(new Dimension(frameSizeX, frameSizeY));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.pack();
	}
		
	protected void paintComponent(Graphics g){
		int width = x*squareSize;
		int height = y*squareSize;
		if(isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, width, height);
		}
		int ox = 0, oy = 0;
		for(int q = 1; q < x-1; q++){
			for(int qq = 1; qq < y-1; qq++){
				if (grid[q][qq].equals("_")) {
					g.setColor(Color.BLACK);
					new DrawRectangle(g, ox+q*squareSize, oy+qq*squareSize, squareSize);					
				}
				if (!grid[q][qq].equals("_")) {
					g.setColor(Color.WHITE);
					g.setFont(font);
					new DrawRectangle(g, ox+q*squareSize, oy+qq*squareSize, squareSize, grid[q][qq], squareSize);
				}	
			}					
		}
	}
}
