package org.ixnomad.ponggame.SoundFX;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;
import org.ixnomad.ponggame.MainComponent;
import java.io.File;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class MusicPlayer {
    
    MediaPlayer menuMusic;
    MediaPlayer gameMusic;
    Media menuMusicMedia;
    Media gameMusicMedia;
    
    public  MusicPlayer(MainComponent m) {
        new JFXPanel();
        
        this.menuMusicMedia   = new Media(new File(("sfx/sfx00")).toURI().toString());
        this.gameMusicMedia   = new Media(new File("sfx/sfx01").toURI().toString());
        try {
            menuMusic           = new MediaPlayer(menuMusicMedia);
            menuMusic.setVolume(0.5);
            gameMusic           = new MediaPlayer(gameMusicMedia);
            gameMusic.setVolume(0.5);
        } catch(Exception e) {
            System.out.println(e);
        }
        gameMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                gameMusic.seek(Duration.ZERO);
                gameMusic.play();
            }
        });
        menuMusic.setOnEndOfMedia(new Runnable() {
            public void run() {
                menuMusic.seek(Duration.ZERO);
                menuMusic.play();
            }
        });
    }
    
    public void playMusic(String musicName) {
        switch(musicName) {
            case "menu_music.wav":
                menuMusic.play();
                break;
            case "game_music.wav":
                gameMusic.play();
                break;
        }
    }
    
    public void stopMusic(String musicName) {
        switch(musicName) {
            case "menu_music.wav":
                menuMusic.stop();
                break;
            case "game_music.wav":
                gameMusic.stop();
                break;
        }
    }
    
    public void pauseMusic(String musicName) {
        switch(musicName) {
            case "menu_music.wav":
                menuMusic.pause();
                break;
            case "game_music.wav":
                gameMusic.pause();
                break;
        }
    }
}
