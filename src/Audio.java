import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
public class Audio {
    private static final String DIRECTORY = "audio/",
                                EXTENSION = ".wav";
    private static final List<String> LIST = Arrays.asList("BGM", "Button", "ButtonRollOver", "GarbageTimeUp", "Place", "Ranking",  "SetPortal", "Shield", "Title", "Transport", "WhenPortalIsOpen");
    private Clip clip;
  
    public Audio(final String filename) {
        if (LIST.contains(filename)){
            try {
                clip = AudioSystem.getClip(null);
                clip.open(AudioSystem.getAudioInputStream(new File((Audio.class.getResource("").getPath() + "../" + DIRECTORY).replaceAll("\\\\", "/") + filename + EXTENSION)));
                clip.addLineListener(new LineListener() {
                    @Override
                    public void update(final LineEvent event) {
                        if (event.getType().equals(LineEvent.Type.STOP)){
                            clip.stop();
                            clip.close();
                        }
                    }
                });
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e){
                e.printStackTrace();
            } catch (UnsupportedAudioFileException e){
                e.printStackTrace();
            }
        }
    }
      
    public void play() {
       try {
            clip.setFramePosition(0);
            clip.start();
       } catch (Exception e) {
            e.printStackTrace();
       }
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.close();
    }
  }