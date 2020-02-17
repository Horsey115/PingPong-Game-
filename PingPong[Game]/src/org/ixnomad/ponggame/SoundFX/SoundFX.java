package org.ixnomad.ponggame.SoundFX;

import java.io.File;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;
import org.ixnomad.ponggame.MainComponent;

public class SoundFX {
    
    private Clip clip;
    
    public SoundFX(String filePath, MainComponent mainComponent) {
        try {
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            AudioFormat baseFormat = audioInputStream.getFormat();
            AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, 
                    baseFormat.getChannels(), baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(), false
            );
            AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodeFormat, audioInputStream);
            clip = AudioSystem.getClip();
            clip.open(decodedAudioInputStream);
            
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainComponent, e);
        }
    }
    
    public void play() {
        if(clip == null) return;
        stop();
        clip.setFramePosition(0);
        clip.start();
    }
    
    public void stop() {
        if(clip.isRunning()) clip.stop();
    }
    
    public void close() {
        
    }
} 
