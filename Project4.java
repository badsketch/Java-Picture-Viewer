/**
 * Daniel Wang
 * Professor Jupe
 * CS 3B
 * Picture Viewer which allows user to select folder 
 * displays image and navigation buttons, autoplay features
 */

import java.io.IOException;

import javax.swing.JFrame;


public class Project4 {
	
	public static void main(String[] args) throws IOException
	{
		//create new Picture Frame
		JFrame myPicture = new PictureFrame();
		myPicture.setTitle("Photo Viewer");
		myPicture.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myPicture.setVisible(true);
	}
	
}
