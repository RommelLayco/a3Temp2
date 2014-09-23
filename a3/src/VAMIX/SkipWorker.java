package VAMIX;

import java.util.List;

import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public class SkipWorker extends SwingWorker<Void,Void> {

	private static final int SKIP_TIME_MS = 10 * 1000;
	private static final float NORMAL_SPEED = 1;
	private static final float DOUBLE_SPEED = 2;
	
	
	private EmbeddedMediaPlayer mediaPlayer;
	private boolean isPressed;
	private boolean fastForward;

	public SkipWorker(boolean isPressed, boolean fastForward, EmbeddedMediaPlayer mediaPlayer){
		this.isPressed = isPressed;
		this.fastForward = fastForward;
		this.mediaPlayer = mediaPlayer;
	}
	

	protected Void doInBackground() throws Exception {

		while(!this.isPressed){
			
			
			if(this.fastForward){	
				this.mediaPlayer.skip(SKIP_TIME_MS);
				Thread.sleep(500);
				
			} else {
				this.mediaPlayer.skip(-SKIP_TIME_MS);
				Thread.sleep(500);
				
			}
			this.isPressed = MediaControl.getIsPressed();
			
		}
		return null;

	}
	
	


}
