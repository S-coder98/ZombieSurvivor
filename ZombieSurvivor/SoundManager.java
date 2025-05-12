import javax.sound.sampled.AudioInputStream;		// for playing sound clips
import javax.sound.sampled.*;
import java.io.*;
import java.util.HashMap;				// for storing sound clips

public class SoundManager {				// a Singleton class
	HashMap<String, Clip> clips;

	private static SoundManager instance = null;	// keeps track of Singleton instance

	private float volume;

	private SoundManager () {
		clips = new HashMap<String, Clip>();
 
		Clip clip = loadClip("sounds/backgroundmusic.wav");	// played from start of the game
		clips.put("backgroundmusic", clip);
 
		clip = loadClip("sounds/winmusic.wav");	// played when player wins
		clips.put("winmusic", clip);

		clip = loadClip("sounds/playerhurt.wav");	
		clips.put("playerhurt", clip);

		clip = loadClip("sounds/gunshot.wav");	
		clips.put("gunshot", clip);

		clip = loadClip("sounds/zombieintro.wav");	
		clips.put("zombieintro", clip);

		clip = loadClip("sounds/monster.wav");	
		clips.put("monster", clip);

		clip = loadClip("sounds/click.wav");	
		clips.put("click", clip);

		clip = loadClip("sounds/zombieshot.wav");	
		clips.put("zombieshot", clip);

		clip = loadClip("sounds/countdown.wav");	
		clips.put("countdown", clip);

		clip = loadClip("sounds/gameover.wav");	
		clips.put("gameover", clip);

		clip = loadClip("sounds/healthpack.wav");	
		clips.put("healthpack", clip);

		clip = loadClip("sounds/youwin.wav");	
		clips.put("youwin", clip);

		volume = 1.0f;
	}


	public static SoundManager getInstance() {	// class method to retrieve instance of Singleton
		if (instance == null)
			instance = new SoundManager();
		
		return instance;
	}		


    	public Clip loadClip (String fileName) {	// gets clip from the specified file
 		AudioInputStream audioIn;
		Clip clip = null;

		try {
    			File file = new File(fileName);
    			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL()); 
    			clip = AudioSystem.getClip();
    			clip.open(audioIn);
		}
		catch (Exception e) {
 			System.out.println ("Error opening sound files: " + e);
		}
    		return clip;
    	}


		public Clip getClip (String title) {

			return clips.get(title);
		}


    	public void playClip(String title, boolean looping) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();
		}
    	}


    	public void stopClip(String title) {
		Clip clip = getClip(title);
		if (clip != null) {
			clip.stop();
		}
    	}

}