package VAMIX;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class Text extends SwingWorker<Void, Integer>  {

	private JFrame textFrame;
	private JPanel contentPane;

	private EmbeddedMediaPlayer preview;
	private Canvas videoSurface;
	private MediaPlayerFactory factory;

	private JTextField text;
	private JButton addText;
	private JCheckBox closing;
	private JTextField duration;
	private JLabel durationLabel;
	private JProgressBar loading;
	private JButton save;

	//inputs to edit
	private static String textField = null;
	private static String durationTime = null;
	private static String cwd;
	

	private static String fileOpen; 
	private static boolean saveable = false;
	private static boolean end = false;
	private static boolean ableToAdd = true;



	/**
	 * @wbp.parser.constructor
	 */ 
	public Text(String openFile) {
		//create frame
		textFrame = new JFrame("Text Editor");
		textFrame.getContentPane().setBackground(Color.BLACK);
		textFrame.setBounds(100, 100, 600, 570);
		textFrame.setVisible(true);

		//create panel to add everything to
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(4, 4, 4, 4));
		contentPane.setBackground(Color.BLACK);
		contentPane.setLayout(null);

		//create mediaPlayer with mediaFunctions
		createMedia();

		//create GUI for text editing
		createUI();

		fileOpen = openFile;

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
		addText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getInputs();

				if((textField == null) || (durationTime == null)){
					JOptionPane.showMessageDialog(null, "No text or length input","VAMIX", 
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					//create swingworker object
					

					//show progress activity
					loading.setIndeterminate(true);
					execute();
					
				;
				}
			}
		});

		closing = new JCheckBox("Closing");
		closing.setToolTipText("Check to add text to closing scene");
		closing.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				Object source = e.getSource();
				if(source == closing){
					end = true;
				}

			}

		});

		duration = new JTextField();
		duration.setColumns(3);
		duration.setToolTipText("Enter how long you want the text to appear on the video");

		durationLabel = new JLabel("Duration in Seconds");
		durationLabel .setForeground(Color.WHITE);

		loading = new JProgressBar();
		loading.setValue(0);

		save = new JButton("save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePreview();
			}
		});

	}

	private void layout(){
		JPanel textPanel = new JPanel();
		textPanel.setBounds(4, 4, 600, 50);
		textPanel.setBackground(Color.BLACK);
		textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		textPanel.add(text);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(4, 50, 600, 50);
		buttonPanel.setBackground(Color.BLACK);
		buttonPanel.setLayout(new FlowLayout());

		buttonPanel.add(save);
		buttonPanel.add(addText);
		buttonPanel.add(closing);
		buttonPanel.add(duration);
		buttonPanel.add(durationLabel);

		JPanel loadingPanel = new JPanel();
		loadingPanel.setBounds(4,520,600,50);
		loadingPanel.setBackground(Color.BLACK);
		loadingPanel.add(loading);

		contentPane.add(textPanel);
		contentPane.add(buttonPanel);
		contentPane.add(videoSurface);
		contentPane.add(loadingPanel);

		textFrame.getContentPane().add(contentPane);
	}

	private void getInputs(){
		textField = text.getText();
		durationTime = duration.getText();
	}

	private void addToFront() throws IOException, InterruptedException{
		//Store temp in working directory
		cwd = System.getProperty("user.dir");
		//add temp name
		cwd = cwd + "/temp1.mp4";
		
		

		String cmd = "avconv -y -i " + fileOpen + " -vf \"drawtext=fontfile='/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf':text='"
				+textField+"':x=text_w:y=50:fontsize=24:fontcolor=black: draw='lt(t," +durationTime+")'\"" +
				" -strict experimental " + cwd;
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
		saveable = true;


	}


	@Override
	protected Void doInBackground() throws Exception {

		if(end){
			this.addToEnd(); //add text  to closing scenes
		} else {
			//add to front
			this.addToFront();
		}
		return null;
	}



	protected void done(){
		//prepare temp file to be played

		JOptionPane.showMessageDialog(null, "Finished Adding text","VAMIX", 
				JOptionPane.INFORMATION_MESSAGE);


		//reset input to null
		textField = null;
		durationTime = null;
		
		loading.setIndeterminate(false);
		loading.setValue(0);
		preview.playMedia(cwd);


	}

	private void savePreview(){
		//check if text is added
		if(saveable){
			preview.stop();

			String savefile = fileSaver.savePath();
			String cmd = "mv " + cwd + " " + savefile;
			ProcessBuilder builder = new ProcessBuilder("bash", "-c", cmd);
			try {
				Process p = builder.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			saveable = false;
			textFrame.dispose();
			new Text(fileOpen);
		} else {
			JOptionPane.showMessageDialog(null, "No text added","VAMIX", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void addToEnd() throws IOException, InterruptedException{
		//get length of song
		int length = (int) preview.getLength();
		//turn length into seconds
		length = length /1000;

		//turn string duration into an int
		int d = Integer.parseInt(durationTime);

		//get time to start embeeding of message
		int timeWrite = length - 10;
		if(timeWrite < 0){
			d = 0;
			JOptionPane.showMessageDialog(null, "Duration to long. Will add text to whole video","VAMIX", 
					JOptionPane.INFORMATION_MESSAGE);
		} 

		//---------- ADD to video ---------
		
		//Store temp in working directory
		cwd = System.getProperty("user.dir");
		//add temp name
		cwd = cwd + "/temp1.mp4";
	


		String cmd = "avconv -y -i " + fileOpen + " -vf \"drawtext=fontfile='/usr/share/fonts/truetype/ttf-dejavu/DejaVuSans.ttf':text='"
				+textField+"':x=text_w:y=50:fontsize=24:fontcolor=black: draw='gt(t," +d+")'\"" +
				" -strict experimental " + cwd;
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		Process p = builder.start();
		p.waitFor();
		saveable = true;


	}
	

}