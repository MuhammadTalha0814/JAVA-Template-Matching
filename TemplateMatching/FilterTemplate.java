import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

// Main class
public class FilterTemplate extends Frame implements ActionListener {
	BufferedImage input, search;
	CanvasImage source, template, target;
	double sigma = 1.0;
	int width, height;
	// Constructor
	public FilterTemplate(String name, String temp) {
		super("Template Matching");
		// load image
		try {
			input = ImageIO.read(new File(name));
			search = ImageIO.read(new File(temp));
		}
		catch ( Exception ex ) {
			ex.printStackTrace();
		}
		width = input.getWidth();
		height = input.getHeight();
		// prepare the panel for image canvas.
		Panel main = new Panel();
		source = new CanvasImage(input);
		target = new CanvasImage(width, height);
		main.setLayout(new GridLayout(1, 2, 10, 10));
		main.add(source);
		main.add(target);
		// prepare the panel for buttons.
		Panel controls = new Panel();
		template = new CanvasZoom(search, 2);
		controls.add(template);
		Button button = new Button("SAD");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("SSD");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("CC");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("NCC");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("ZNCC");
		button.addActionListener(this);
		controls.add(button);
		button = new Button("Non-max Suppression");
		button.addActionListener(this);
		controls.add(button);
		// add two panels
		add("Center", main);
		add("South", controls);
		addWindowListener(new ExitListener());
		pack();
		setVisible(true);
	}
	class ExitListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
	// Action listener for button click events
	public void actionPerformed(ActionEvent e) {
		// example -- change brightness
		if ( ((Button)e.getSource()).getLabel().equals("SAD") ) {
			for ( int y=0, i=0 ; y<height ; y++ )
				for ( int x=0 ; x<width ; x++, i++ ) {
					Color clr = new Color(source.image.getRGB(x, y));
					target.image.setRGB(x, y, (clr.brighter()).getRGB());
				}
			target.repaint();
		}
	}
	public static void main(String[] args) {
		new FilterTemplate(args.length==2 ? args[0] : "volcano_2.png", args.length==2 ? args[1] : "volcano_3f2.png");
	}
}

class CanvasZoom extends CanvasImage {
	double scale;

	public CanvasZoom(BufferedImage input, float s) {
		super(input);
		setPreferredSize(new Dimension((int)(image.getWidth()*s)+border*2, (int)(image.getHeight()*s)+border*2));
		scale = s;
	}
	// redraw the canvas
	public void paint(Graphics g) {
		// draw boundary
		g.setColor(Color.gray);
		g.drawRect(1, 1, getWidth()-2, getHeight()-2);
		// compute the offset of the image.
		int xoffset = (int)(getWidth()/scale - image.getWidth()) / 2;
		int yoffset = (int)(getHeight()/scale - image.getHeight()) / 2;
		((Graphics2D)g).scale(scale, scale);
		g.drawImage(image, xoffset, yoffset, this);
	}
}
