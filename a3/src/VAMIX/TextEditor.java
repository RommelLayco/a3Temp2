package VAMIX;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

public class TextEditor {

	JFrame textFrame;
	JPanel contentPane;
	
	private EmbeddedMediaPlayer preview;
	private Canvas videoSurface;
	private MediaPlayerFactory factory;
	
	private JTextField text;
	private JButton addText;
	private JCheckBox closing;
	private JTextField duration;
	private JLabel durationLabel;
	
	public TextEditor(String openFile) {
		//create frame
		textFrame = new JFrame("Text Editor");
		textFrame.setBackground(Color.BLACK);
		textFrame.setBounds(100, 100, 600, 520);
		
		//create panel to add everything to
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setBackground(Color.BLACK);
		contentPane.setLayout(null);
		
		//create mediaPlayer with mediaFunctions
		createMedia();
		
		//create GUI for text editing
		createUI();
		
		preview.prepareMedia(openFile);

	}
	
	private void createUI(){
		controls();
		layout();
	}
	
	private void createMedia(){
		//create canvas to put media player on
		FullScreenStrategy fullScreenStrategy = new DefaultFullScreenStrategy(textFrame);
		factory = new MediaPlayerFactory();
	
		videoSurface = new Canvas();
		videoSurface.setBounds(4, 100, 600, 300);
		videoSurface.setBackground(Color.black);
				
		preview = factory.newEmbeddedMediaPlayer(fullScreenStrategy);
		preview.setVideoSurface(factory.newVideoSurface(videoSurface));
		preview.setPlaySubItems(true);
		
		//create Panel to control mediaPlayer
		JPanel controlPanel = new MediaControl(preview);
		controlPanel.setBounds(4,400,600,100);
		contentPane.add(controlPanel);
		
		textFrame.getContentPane().add(contentPane);
	}
	
	private void controls(){
		text = new JTextField(20);
		
		addText = new JButton("Add Text");
		
		closing = new JCheckBox("Closing");
		closing.setToolTipText("Check to add text to closing scene");
		
		duration = new JTextField();
		duration.setColumns(3);
		duration.setToolTipText("Enter how long you want the text to appear on the video");
		
		durationLabel = new JLabel("Duration in Seconds");
		durationLabel .setForeground(Color.WHITE);
	}
	
	private void layout(){
		JPanel textPanel = new JPanel();
		textPanel.setBounds(4, 4, 600, 50);
		textPanel.setBackground(Color.BLACK);
		textPanel.add(text);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(4, 50, 600, 50);
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setLayout(new FlowLayout());
		
		buttonPanel.add(addText);
		buttonPanel.add(closing);
		buttonPanel.add(duration);
		buttonPanel.add(durationLabel);
		
		contentPane.add(textPanel);
		contentPane.add(buttonPanel);
		contentPane.add(videoSurface);
		
		textFrame.getContentPane().add(contentPane);
	}
}
