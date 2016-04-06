import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * JPanel class that displays grid of pictures
 *
 */
public class PictureGrid extends JPanel{
	
	//stores picture
	private ArrayList<File> pictures;
	
	//constructor
	public PictureGrid(ArrayList<File> pictures) throws IOException
	{
		this.pictures = pictures;
		//set rows always to be multiple of 2
		int rows = pictures.size() / 2;
		setLayout(new GridLayout(rows,2));
		//add images to grid 
		for(int i = 0;i < pictures.size();i++)
		{
			JLabel picture = new JLabel();
			setLabelImage(pictures.get(i), 500, 500, picture);
			add(picture);
		}
	}
	
	//sets label to image
	private void setLabelImage(File imgFile, int width, int height, JLabel label) throws IOException
	{

		Image img = ImageIO.read(imgFile);

		label.setIcon(new ImageIcon(img.getScaledInstance(width,  height,  Image.SCALE_DEFAULT)));
	}
	
	
	
}
