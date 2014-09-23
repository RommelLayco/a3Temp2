package VAMIX;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class Extract extends SwingWorker<Void, Integer>{

	private String openPath;
	private String savePath;
	private String videoSave;

	//Store file locations
	public Extract(String openPath, String savePath, String videoSave){
		this.openPath = openPath;
		this.savePath = savePath;
		this.videoSave = videoSave;
	}
	
	public void extract() throws IOException{
		//save audio in another file
		String cmd="avconv -i "+this.openPath+ " -vn -c:a libmp3lame -q:a 2 " +this.savePath;
		ProcessBuilder builder1 = new ProcessBuilder("/bin/bash", "-c", cmd);
		builder1.start();
		
		//save video without audio
		String cmd2 = "avconv -i " +this.openPath + " -an " + this.videoSave;
		ProcessBuilder builder2 = new ProcessBuilder("/bin/bash", "-c", cmd2);
		builder2.start();
	}
	
	//check for audio
	public int checkAudio(){
		EmbeddedMediaPlayerComponent video = new EmbeddedMediaPlayerComponent();
		EmbeddedMediaPlayer v = video.getMediaPlayer();
		int channel = v.getAudioChannel();	
		
		return channel;
	}
	
	
	protected Void doInBackground() throws Exception {
	
		this.extract();
		return null;
		
	}
	
	protected void done(){
		JFrame messageFrame = new JFrame();
		JOptionPane.showMessageDialog(messageFrame, "Finished extracting","VAMIX", JOptionPane.INFORMATION_MESSAGE);
		
	}

}
