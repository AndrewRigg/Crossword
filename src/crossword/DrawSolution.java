package crossword;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class DrawSolution extends JComponent {
	
	private static final long serialVersionUID = 1L;
	int x,y;
	int frameSizeX, frameSizeY;
	String puzzle, title;
	JFrame frame;
	Font font;
	JScrollPane area;
	JPanel main, body;
	Border border;
	Dimension screenSize;
	double width;
	double height;
	GridBagConstraints c;
	public DrawCrossword puz;
	JButton b;
	
	public DrawSolution(String[][] grid,int x, int y, int squareSize, String puzzle,  final DrawCrossword puz){
		this.x = x;
		this.y = y;
		this.puzzle = puzzle;
		this.puz = puz;
		frame = new JFrame(title);
		frame.setBackground(Color.WHITE);
		frame.setPreferredSize(new Dimension(frameSizeX, frameSizeY));
		frame.setMinimumSize(new Dimension(550,400));
		frame.setBackground(new Color(255, 255, 255, 255));
		
		font = new Font("Century Gothic", Font.PLAIN, 24);
		frameSizeX = (x) * squareSize+(squareSize/2);
		frameSizeY = (y) * squareSize;
		setOpaque(true);
		setBackground(Color.WHITE);
		if(puzzle.equals("Crossword")){
			title = "Auto Crossword Solution!";
		}else if (puzzle.equals("Word Search")){
			title = "Auto Word Search Solution!";
		}
		main = new JPanel(new GridLayout(x - 2, y - 2));
		main.setBounds(100, squareSize, (squareSize-5) * (x - 2), (squareSize-5) * (y - 2));
		main.setOpaque(false);
		main.setPreferredSize(new Dimension(squareSize * (x-2), squareSize * (y-2)));
		
		border = BorderFactory.createLineBorder(Color.BLACK);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = screenSize.getWidth();
		height = screenSize.getHeight();
		

		   final AbstractAction buttonPressed = new AbstractAction() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//   frame.dispose();
	            }
	        };
		
		
		for(int q = 1; q < x-1; q++){
			for(int qq = 1; qq < y-1; qq++){
				JLabel jl = new JLabel();
				jl.setBackground(Color.WHITE);
				if(q == 0){
					jl.setBackground(Color.WHITE);
				}
				else if (grid[qq][q].equals("_")) {
					//jl.setBounds(new Rectangle(20,20));
					jl.setBackground(Color.BLACK);
					jl.setText("");		
					jl.setBorder(null);
				}
				else if (!grid[qq][q].equals("_")) {
					jl.setBackground(Color.WHITE);
					jl.setText(grid[qq][q].toUpperCase());
					jl.setBorder(border);
				}
				jl.setFont(font);
				jl.setHorizontalAlignment(JTextField.CENTER);
				jl.setOpaque(true);
				main.add(jl);
			}					
		}
		
		body = new JPanel(new GridBagLayout());
		body.setBackground(Color.WHITE);
		//body.setBounds(0, 20, squareSize * x, squareSize * y);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.NONE;
		//c.insets = new Insets(0,0,0,100);
		body.setPreferredSize(new Dimension(squareSize * x, squareSize * y));
		body.add(main, c);
		
		
		area = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		area.setPreferredSize(new Dimension(squareSize * x, squareSize * y));
		area.getVerticalScrollBar().setUnitIncrement(10);
		area.getHorizontalScrollBar().setUnitIncrement(10);
		area.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		area.setBackground(new Color(255,255,255,255));
		area.setAlignmentX(SwingConstants.BOTTOM);
				
		frame = new JFrame("Auto Word Search");
		if(squareSize*(x+2)+squareSize/2 > width && squareSize*(y+2) > height-30){
			//frame.setPreferredSize(new Dimension((int)width,(int)height));
			frame.setPreferredSize(new Dimension((int)width,(int)height-30));
			//System.out.println("GOt here");
		}
		else if(squareSize*(x+2)+squareSize/2 > width){
			frame.setPreferredSize(new Dimension((int)width,squareSize*(y+2)));
		}else if(squareSize*(y+2) > height-30){
			frame.setPreferredSize(new Dimension(squareSize*(x+2)+squareSize/2, (int)height-30));
		}else{
			frame.setPreferredSize(new Dimension(squareSize*(x+2)+squareSize/2,squareSize*(y+2)));
		}

		frame.setContentPane(area);
		frame.pack();
		frame.setLocationRelativeTo(null);
		//This is to change the text on the puzzle GUI when the solution is closed
		//(needed to pass DrawCrossword into DrawSolution)
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	puz.hideSolution();
		    }
		});
	}
}
