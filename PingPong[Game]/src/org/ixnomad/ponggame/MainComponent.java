package org.ixnomad.ponggame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.Timer;
import org.ixnomad.ponggame.SoundFX.SoundFX;
import org.ixnomad.ponggame.SoundFX.MusicPlayer;

import org.ixnomad.ponggame.TextureLoader.TextureLoader;

public class MainComponent extends JPanel implements ActionListener, Runnable {
    
    public static final int WINDOW_WIDTH    =       1280;
    public static final int WINDOW_HEIGHT   =       640;
    public static final int GAME_SPEED      =       60;
    
    MusicPlayer musicPlayer = new MusicPlayer(this);
    TextureLoader textureLoader = new TextureLoader();
    HashMap<String, SoundFX> sfx;
    Menu menu = new Menu(this);
    Game game = new Game(false, this);
    Graphics g;
    Keyboard k = new Keyboard();
    Timer timer = new Timer(1000/GAME_SPEED, this);
    
    private static final String TITLE   =   "Pong Classic";
    /* 0 — menu
       1 — game*/
    public int gameState = 0;
    
    public MainComponent() {
        sfx = new HashMap<String, SoundFX>();
        sfx.put("bounce", new SoundFX("sfx/sfx02", this));
        sfx.put("player1_goal", new SoundFX("sfx/sfx03", this));
        sfx.put("player2_goal", new SoundFX("sfx/sfx04", this));
        sfx.put("outplayed", new SoundFX("sfx/sfx05", this));
        sfx.put("ownage", new SoundFX("sfx/sfx06", this));
        sfx.put("select", new SoundFX("sfx/sfx07", this));
        sfx.put("enter", new SoundFX("sfx/sfx08", this));
        sfx.put("comeback", new SoundFX("sfx/sfx09", this));
        addKeyListener(k);
        timer.start();
        setFocusable(true);
    }
    
    public void mainProcess() {
        k.updateKeys();
        if(gameState == 0) {
            menu.menuProcess();
        } else if(gameState == 1) {
            game.gameProcess();
            if(!game.isPaused) {
                if(!game.multiplayer) {
                    game.useAI();
                }
            }   
        }
    }
    
    @Override
    public void paint(Graphics g) {
        this.g = g;
        g.setColor(color(0, 0, 0));
        g.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT); 
        if(gameState == 0) {
            menu.drawMenu();
        } else if(gameState == 1) {
            game.drawGame();
        }
    }
    
    public Color color(int red, int green, int blue) {
        return new Color(red, green, blue);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainProcess();

        repaint();
    }

    @Override
    public void run() {
    }
    
    private class Keyboard extends KeyAdapter implements KeyListener {
        
        boolean key[] = new boolean[255];
        
        private boolean keyIsAlreadyPressed = false;
        
        @Override
        public void keyPressed(KeyEvent e) {
            key[e.getKeyCode()] = true;
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
            key[e.getKeyCode()] = false;
            keyIsAlreadyPressed = false;
        }
                
        public void updateKeys() {
            if(gameState == 0) {
                if(key[KeyEvent.VK_UP] & !keyIsAlreadyPressed) {
                    if(menu.selection > menu.selectionMin) {
                        System.out.println(menu.selection + " " + menu.selectionsCount);
                        sfx.get("select").play();
                    }
                    menu.selectionUp();
                    keyIsAlreadyPressed = true;
                } else if(key[KeyEvent.VK_DOWN] & !keyIsAlreadyPressed) {
                    if(menu.selection < menu.selectionsCount - 1) {
                        sfx.get("select").play();
                    }
                    menu.selectionDown();
                    keyIsAlreadyPressed = true;
                }
                if(key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                    sfx.get("enter").play();
                    keyIsAlreadyPressed = true;    
                    menu.enter();
                }
                if(key[KeyEvent.VK_ENTER] & !keyIsAlreadyPressed) {
                    sfx.get("enter").play();
                    keyIsAlreadyPressed = true;    
                    menu.enter();
                }
            } else if(gameState == 1) {
                
                if(key[KeyEvent.VK_ESCAPE] & !keyIsAlreadyPressed) {
                    keyIsAlreadyPressed = true;
                    game.isPaused = !game.isPaused;
                    game.subState = 0;
                    sfx.get("enter").play();
                    if(game.isPaused) {
                        game.selection = 0;
                        game.setAnimationMoment(0);
                    }
                }
                
                if(!game.isPaused) {
                    if(key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                        keyIsAlreadyPressed = true;
                        if(gameState == 1) game.launchBall();
                    } 
                    if(game.multiplayer) {
                        if(key[KeyEvent.VK_UP]) {
                            game.movePlayerUp(1);
                        }
                        if(key[KeyEvent.VK_DOWN]) {
                            game.movePlayerDown(1);
                        }
                        if(key[KeyEvent.VK_W]) {
                            game.movePlayerUp(0);
                        }
                        if(key[KeyEvent.VK_S]) {
                            game.movePlayerDown(0);
                        }
                    } else {
                        if(key[KeyEvent.VK_W] || key[KeyEvent.VK_UP]) {
                            game.movePlayerUp(0);
                        }
                    if(key[KeyEvent.VK_S] || key[KeyEvent.VK_DOWN]) {
                            game.movePlayerDown(0);
                        }
                    }
                } else {
                    if(key[KeyEvent.VK_UP] & !keyIsAlreadyPressed) {
                        if(game.selection > 0) {
                            sfx.get("select").play();
                        }
                        keyIsAlreadyPressed = true;
                        game.selection--;
                    }
                    if(key[KeyEvent.VK_DOWN] & !keyIsAlreadyPressed) {
                        if(game.selection < game.selectionsCount - 1) {
                            sfx.get("select").play();
                        }
                        keyIsAlreadyPressed = true;
                        game.selection++;
                    }
                    if(key[KeyEvent.VK_SPACE] & !keyIsAlreadyPressed) {
                        sfx.get("enter").play();
                        keyIsAlreadyPressed = true;
                        game.pauseEnter();
                    }
                    if(key[KeyEvent.VK_ENTER] & !keyIsAlreadyPressed) {
                        sfx.get("enter").play();
                        keyIsAlreadyPressed = true;
                        game.pauseEnter();
                    }
                }
            }
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
    
    public static void main(String[] args) {
        
        MainComponent mainComponent = new MainComponent();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        
        Dimension size = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setPreferredSize(size);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle(TITLE);
        
        frame.add(mainComponent);
        frame.setVisible(true);
        
        mainComponent.musicPlayer.playMusic("menu_music.wav");
    }
}