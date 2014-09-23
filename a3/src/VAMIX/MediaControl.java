package VAMIX;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;

import uk.co.caprica.vlcj.binding.LibVlcConst;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MediaControl extends JPanel {

	private static final float NORMAL_SPEED = 1;



	private final EmbeddedMediaPlayer mediaPlayer;

	private JButton rewindButton;
	private JButton pauseButton;
	private JButton playButton;
	private JButton fastForwardButton;

	private JSlider volumeSlider;
	
	private boolean mousePressedPlaying = false;
	private static boolean playButtonIsPressed;

	public MediaControl(EmbeddedMediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;

		createUI();


	}

	private void createUI() {
		createControls();
		layoutControls();

	}

	private void createControls() {


		rewindButton = new JButton("Rewind");
		rewindButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playButtonIsPressed = false;
				SkipWorker skipping = new SkipWorker(playButtonIsPressed,false,mediaPlayer);
				skipping.execute();
				


			}
		});



		pauseButton = new JButton("Pause");
		pauseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				playButtonIsPressed = false;
				mediaPlayer.pause();
			}
		});



		playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				//stop skip and play and normal speed
				playButtonIsPressed = true;
				mediaPlayer.setRate(NORMAL_SPEED);
				
				mediaPlayer.play();
				
				
			}
		});




		fastForwardButton = new JButton("FastFoward");
		fastForwardButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				playButtonIsPressed = false;
				SkipWorker skipping = new SkipWorker(playButtonIsPressed,true,mediaPlayer);
				skipping.execute();

			}

		});

		//add volume functionality
		volumeSlider = new JSlider();
		volumeSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource(); 
                mediaPlayer.setVolume(source.getValue());
                
              //add tip to specify volume as a %
                String v = currentVolume(source);
                volumeSlider.setToolTipText(v);
                
			}
		});
        volumeSlider.setOrientation(JSlider.HORIZONTAL);
        volumeSlider.setMinimum(LibVlcConst.MIN_VOLUME);
        volumeSlider.setMaximum(LibVlcConst.MAX_VOLUME);
        volumeSlider.setPreferredSize(new Dimension(100, 40));


	}

	private void layoutControls() {
		setBorder(new EmptyBorder(4, 4, 4, 4));

		setLayout(new BorderLayout());



		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(2,1));
		
		JPanel pushButtons = new JPanel();
		JPanel volumePanel = new JPanel();

		pushButtons.setLayout(new FlowLayout());


		pushButtons.add(rewindButton);
		pushButtons.add(pauseButton);
		pushButtons.add(playButton);
		pushButtons.add(fastForwardButton);
		
		volumePanel.add(volumeSlider);
		
		bottomPanel.add(pushButtons);
		bottomPanel.add(volumePanel);

		add(bottomPanel, BorderLayout.SOUTH);
	}


	private void updateUIState() {
		if(!mediaPlayer.isPlaying()) {
			// Resume play or play a few frames then pause to show current position in video
			mediaPlayer.play();
			if(!mousePressedPlaying) {
				try {
					// Half a second probably gets an iframe
					Thread.sleep(500);
				}
				catch(InterruptedException e) {
					// Don't care if unblocked early
				}
				mediaPlayer.pause();
			}
		}


	}


	public void skip(int skipTime) {
		// Only skip time if can handle time setting
		if(mediaPlayer.getLength() > 0) {
			mediaPlayer.skip(skipTime);
			updateUIState();
		}
	}

	//get isPressed Value;
	public static boolean getIsPressed(){
		return playButtonIsPressed;
	}
	
	public static String currentVolume(JSlider s){
		String per ="";
		int currentValue = s.getValue();
		
		//calculate as a %
		int percent = (int) (Math.floor(currentValue * 100) / LibVlcConst.MAX_VOLUME);
		//turn into a string
		per = per + percent + "%";
		return per;
	}


}




