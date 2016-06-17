import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class PictureFrame extends JFrame
{
	//navigation buttons
	private JButton next;
	private JButton prev;
	private JLabel pictureLabel;
	
	
	//autoplayPanel features
	private JCheckBox autoPlayCheckBox;
	private JSlider autoPlaySlider;
	private Timer autoplayTimer;
	private int timerdelay;
	private JPanel topPanel;
	private JLabel infoText = new JLabel();
	private JCheckBox showAllCheckBox;
	private JPanel autoplayPanel;
	
	
	
	//folder search 
	private JLabel folder;
	private JTextField input;
	private JButton load;
	
	//holds pictures 
	private int pictureCounter = 0;
	private ArrayList<File> pictures;
	
	
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;
	
	/**
	 * Constructor 
	 * @throws IOException
	 */
	public PictureFrame() throws IOException
	{
		
		createComponents();
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
	}
	
	/**NAVIGATION BUTTONS
	 * Action Listener for navigation buttons
	 *
	 */
	public class ClickListener implements ActionListener
	{
		
		public void actionPerformed(ActionEvent event) {
		
			
			if (pictures != null && !pictures.isEmpty())
			{
			//if user presses next button increment counter
			if(event.getSource() == next)
			{
				pictureCounter++;
				//go to first picture if at end
				if(pictureCounter == pictures.size())
				{
					pictureCounter = 0;
				}

			}
			//if user presses prev button decrement counter
			if(event.getSource() == prev)
			{
				pictureCounter--;
				//go to last picture if at beginning 
				if (pictureCounter == -1)
					pictureCounter = pictures.size() - 1;
			}
			try {
				
				//set label to picture at counter
				setLabelImage(pictures.get(pictureCounter),500,500, pictureLabel);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//set info text as "n of m pictures"
			infoText.setText((pictureCounter + 1) + "of "+pictures.size());

			
			}
		}

	}
	
	/**AUTOPLAY 
	 * Change Listener for slider of autoplay timer
	 *
	 */
	public class SliderListener implements ChangeListener {
		

		public void stateChanged(ChangeEvent event) {
			//get source value of slider
			JSlider source = (JSlider)event.getSource();
			timerdelay = (int)source.getValue() * 1000;
			
			//if autoplay has been checked, create timer based on slider reading
			if (autoPlayCheckBox.isSelected())
			{
				autoplayTimer.stop();
				ActionListener listener = new TimerListener();
				autoplayTimer = new Timer(timerdelay, listener);
				autoplayTimer.start();
			}
			
		}

	}
	
	/**TIMER
	 * Action Listener for timer event
	 *
	 */
	public class TimerListener implements ActionListener {
		
		//perform a click for each interval
		public void actionPerformed(ActionEvent arg0) {
			next.doClick();
			
		}
		
	}
	
	/**
	 * Action Listener for 'Show All' check box
	 *
	 */
	public class showAllListener implements ActionListener {

		public void actionPerformed(ActionEvent event){
			
				//if checkBox has been selected then create a new picture grid to display all pictures
				if (showAllCheckBox.isSelected())
				{
					//create new frame 
					JFrame shower = new JFrame();
					PictureGrid allPictures = null;
					try {
						allPictures = new PictureGrid(pictures);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//create scroll bars as necessary
					JScrollPane scrollPane = new JScrollPane(allPictures);
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					
					//add scroll pane to frame
					shower.add(scrollPane);
					shower.pack();
					shower.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					shower.setVisible(true);
					
				}
		}	
	}
	
	/**
	 * Action Listener for autoplay check box
	 * initiates automatic cycling through pictures
	 */
	public class autoplayListener implements ActionListener
	{

		public void actionPerformed(ActionEvent event) {
			//if it has been selected, start the event timer 
			if (autoPlayCheckBox.isSelected())
			{
					ActionListener listener = new TimerListener();
					autoplayTimer = new Timer(timerdelay, listener);
					autoplayTimer.start();

			}
			else
			{
				autoplayTimer.stop();
			}
			
		}
		
	}
	
	
	/**UPLOAD
	 * Action Listener for upload button for searching folder
	 *
	 */
	public class uploadListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			
			//ensure user has typed something into field
			pictures = new ArrayList<File>();
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc.showOpenDialog(PictureFrame.this);
			
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					File myFile = fc.getSelectedFile();

					for(int i = 0;i<myFile.listFiles().length;i++)
					{
						//check extension to see if it ends in .jpg and add to arrayList
						String fileName = myFile.listFiles()[i].getName();
						String fileExt = fileName.substring(fileName.length() - 4);
						if(fileExt.equals(".jpg"))
						{
							pictures.add(myFile.listFiles()[i]);
						}
					}
					
				}
				
			

			if (pictures.isEmpty())
			{
				input.setText("No Pictures Found");
			}
			else
			{
				try {
					//set pictureLabel to pictures, infotext to appropriate text
					//removeall, revalidate, repaint for if a new folder has been loaded
					pictureLabel.removeAll();
					setLabelImage(pictures.get(pictureCounter),500,500, pictureLabel);
					infoText.setText(((pictureCounter + 1) + "of "+pictures.size()));
					autoplayPanel.add(infoText);
					pictureLabel.revalidate();
					pictureLabel.repaint();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
		}
		
	}
	
	
	
	
	/**
	 * primary function for creating all components
	 * @throws IOException
	 */
	private void createComponents() throws IOException
	{
		//primary panel
		topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		
		//SIDE PANELS-NAVIGATION
		next = new JButton(">");
		prev = new JButton("<");
		next.addActionListener(new ClickListener());
		prev.addActionListener(new ClickListener());
		//add to major panel
		topPanel.add(next, BorderLayout.EAST);
		topPanel.add(prev, BorderLayout.WEST);
		
		
		//UPPER PANEL-FOLDER SEARCH
		JPanel searchPanel = new JPanel();
		folder = new JLabel("Folder: ");
		input = new JTextField(20);
		load = new JButton("Open");
		searchPanel.add(folder);
		searchPanel.add(input);
		searchPanel.add(load);
		load.addActionListener(new uploadListener());
		topPanel.add(searchPanel, BorderLayout.NORTH);
		
		
		
		
		
		//CENTER PANEL-IMAGE
		pictureLabel = new JLabel();
		//ensure it is always center aligned at center
		pictureLabel.setHorizontalAlignment(JLabel.CENTER);
		pictureLabel.setVerticalAlignment(JLabel.CENTER);
		JScrollPane pictureScroll = new JScrollPane(pictureLabel);
		//add scroll bars
		pictureScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		pictureScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		topPanel.add(pictureScroll, BorderLayout.CENTER);
		
		
		//BOTTOM PANEL-AUTOPLAY
		showAllCheckBox = new JCheckBox("Show All");
		showAllCheckBox.addActionListener(new showAllListener());
		autoPlayCheckBox = new JCheckBox("Autoplay");
		autoPlayCheckBox.addActionListener(new autoplayListener());

		//slider and ticks
		autoPlaySlider = new JSlider(0,10,0);
		autoPlaySlider.setMajorTickSpacing(5);
		autoPlaySlider.setMinorTickSpacing(1);
		autoPlaySlider.setPaintTicks(true);
		autoPlaySlider.setPaintLabels(true);
		autoPlaySlider.addChangeListener(new SliderListener());
		//labels 
		JLabel seconds = new JLabel("sec");
		//add to panel
		autoplayPanel = new JPanel();
		autoplayPanel.add(autoPlayCheckBox);
		autoplayPanel.add(autoPlaySlider);
		autoplayPanel.add(seconds);
		autoplayPanel.add(showAllCheckBox);

		//add to major panel
		topPanel.add(autoplayPanel, BorderLayout.SOUTH);
		

		

		
		add(topPanel);
		
		
	}
	
	//sets label to image
	private void setLabelImage(File imgFile, int width, int height, JLabel label) throws IOException
	{
		Image img = ImageIO.read(imgFile);
		label.setIcon(new ImageIcon(img.getScaledInstance(width,  height,  Image.SCALE_DEFAULT)));
		
		//label.setIcon(new ImageIcon(new ImageIcon("icon.png").getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));

		
	}

	
}