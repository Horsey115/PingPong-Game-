package org.ixnomad.ponggame;

import java.awt.Color;
import org.ixnomad.ponggame.SoundFX.SoundFX;
import org.ixnomad.ponggame.TextureLoader.Texture;
import org.ixnomad.ponggame.TextureLoader.TextureLoader;
import org.ixnomad.ponggame.entity.AI;
import org.ixnomad.ponggame.entity.Ball;
import org.ixnomad.ponggame.entity.Player;

public class Game {
    
    MainComponent m;
    TextureLoader textureLoader;
    Menu menu;
    
    byte ID_gameIsPaused, ID_continue, ID_exit, ID_difficulty, ID_easy, ID_normal, ID_hardcore, ID_hacker, 
            ID_outplay12, ID_outplay21, ID_own12, ID_own21, ID_goal1, ID_goal2, ID_comeback;
    
    private Player player1;
    private Player player2;
    private Ball ball;
    private AI AI;
    private SoundFX sfx;
    
    public boolean multiplayer;
    public boolean isPaused;
    
    private final int selectionRegionSizeK  =   7;
    
    public byte     selection               =   0; //0 — continue, 1 — exit
    public byte     selectionsCount         =   2;
    public byte     subState                =   0; //0 — main pause menu, 1 — difficulty meny
    private int     selectionTransparency   =   0;
    private double  animationMoment         =   0.0d;
    private int     selectorY;
    private int     player1_streak          =   0;
    private int     player2_streak          =   0;
    private int     streakOutplayed         =   4;
    private int     streakOwned             =   7;
    private int     comebackStreak          =   4;
    
    private static final double appearDelay                 =   1.46d;    
    private static final double messageBrightShowTime       =   2.8d;
    private static final double messageTransparencyChange   =   1.5d;
    private static final double comebackMessageShowTime     =   2.2d;
        
    private boolean isOnGoal = false;
    private boolean streakInformed = false;
    private boolean streakMessaged = false;
    double pauseReducingTransparency = 1;
    private double messageTransparency = 255;
    private double transparencyTime = 0;
    private double comebackCurrentTime = 0;
    private int currentTextureID;
    
    public Game(boolean multiplayer, MainComponent m) {
        this.m = m;
        this.multiplayer = multiplayer;
        this.ball = new Ball();
        this.textureLoader = m.textureLoader;
        this.menu = m.menu;
        this.AI = new AI();
        
        ID_gameIsPaused     =   textureLoader.getID("gameispaused");
        ID_continue         =   textureLoader.getID("continue");
        ID_exit             =   textureLoader.getID("exit");
        ID_difficulty       =   textureLoader.getID("difficulty");
        ID_easy             =   textureLoader.getID("easy");
        ID_normal           =   textureLoader.getID("normal");
        ID_hardcore         =   textureLoader.getID("hardcore");
        ID_hacker           =   textureLoader.getID("hacker");
        ID_outplay12        =   textureLoader.getID("playeroneoutplaysplayertwo");
        ID_outplay21        =   textureLoader.getID("playertwooutplaysplayerone");
        ID_own12            =   textureLoader.getID("playertwohasbeenowned");
        ID_own21            =   textureLoader.getID("playeronehasbeenowned");
        ID_goal1            =   textureLoader.getID("playeronescoredagoal");
        ID_goal2            =   textureLoader.getID("playertwoscoredagoal");
        ID_comeback         =   textureLoader.getID("comeback");
        
        player1 = new Player(0);
        player2 = new Player(1);
    }
    
    public void startGame() {
        this.setStartBallPosition((MainComponent.WINDOW_WIDTH - this.ball.getSize()) / 2,(MainComponent.WINDOW_HEIGHT - this.ball.getSize()) / 2);
        player1.setStartPosition(0);
        player2.setStartPosition(1);
    }
    
    public void gameProcess() {
        
        if(!isPaused) {
            if(transparencyTime <= 0) {
                transparencyTime = 0;
                messageTransparency += messageTransparencyChange;
            }
            transparencyTime--;
            if(comebackCurrentTime > 0) comebackCurrentTime--;
            
            ball.updatePosition();
            this.ball.animationMoment+=0.188d;
            if(this.ball.animationMoment > 310000000000d) this.ball.animationMoment = 0;
            this.ball.transparency = Math.abs((int)(Math.sin(this.ball.animationMoment) * (this.ball.MAX_ANIMATION_ALPHA - this.ball.MIN_ANIMATION_ALPHA) +  this.ball.MIN_ANIMATION_ALPHA));
        
            if(this.ball.getY() < 16) {
                this.ball.setY(16);
                this.ball.setYSpeed((this.ball.getYSpeed() - ball.ballAccelerationY * Math.random()) * -1);
            } else if(this.ball.getY() > MainComponent.WINDOW_HEIGHT - 16) {
                this.ball.setY(MainComponent.WINDOW_HEIGHT - 16);
                this.ball.setYSpeed((this.ball.getYSpeed() + ball.ballAccelerationY * Math.random()) * -1);
            }
        
            if(((ball.getX() < Player.INDENT + Player.WIDTH + ball.getSize() / 2) & (ball.getY() > (player1.getY() - ball.getSize())) & (ball.getY() < player1.getY() + Player.HEIGHT + ball.getSize())) & ball.getX() > Player.INDENT) {
                m.sfx.get("bounce").play();
                ball.bounceToRight();
            } 
            if(((ball.getX() > MainComponent.WINDOW_WIDTH - Player.INDENT - Player.WIDTH - ball.getSize() / 2) & (ball.getY() > (player2.getY() - ball.getSize())) & (ball.getY() < player2.getY() + Player.HEIGHT + ball.getSize())) & ball.getX() < MainComponent.WINDOW_WIDTH - Player.INDENT) {
                m.sfx.get("bounce").play();
                ball.bounceToLeft();
            }
            if(ball.getX() < 0 - appearDelay * Math.abs(ball.getXSpeed()) * MainComponent.GAME_SPEED) {
                ball.setX((MainComponent.WINDOW_WIDTH - this.ball.getSize()) / 2); 
                ball.setY((MainComponent.WINDOW_HEIGHT - this.ball.getSize()) / 2);
                ball.isLaunched = false;
                isOnGoal = false;
                if(player1_streak >= comebackStreak) {
                    comebackCurrentTime = comebackMessageShowTime * MainComponent.GAME_SPEED;
                    m.sfx.get("comeback").play();
                }
                player1_streak = 0;
                player2_streak++;
                streakInformed = false;
                messageTransparency = 0;
                transparencyTime = MainComponent.GAME_SPEED * messageBrightShowTime;
            }
            if(ball.getX() > MainComponent.WINDOW_WIDTH + appearDelay * Math.abs(ball.getXSpeed()) * MainComponent.GAME_SPEED) {
                ball.setX((MainComponent.WINDOW_WIDTH - this.ball.getSize()) / 2); 
                ball.setY((MainComponent.WINDOW_HEIGHT - this.ball.getSize()) / 2);
                ball.isLaunched = false;
                isOnGoal = false;
                if(player2_streak >= comebackStreak) {
                    comebackCurrentTime = comebackMessageShowTime * MainComponent.GAME_SPEED;
                    m.sfx.get("comeback").play();
                }
                player2_streak = 0;
                player1_streak++;
                streakInformed = false;
                streakMessaged = false;
                messageTransparency = 0;
                transparencyTime = MainComponent.GAME_SPEED * messageBrightShowTime;
            }
            
            if(!streakInformed & ((player1_streak >= streakOutplayed & player1_streak < streakOwned) || (player2_streak >= streakOutplayed & player2_streak < streakOwned))) {
                m.sfx.get("outplayed").play();
                streakInformed = true;
            } else if(!streakInformed & (player1_streak >= streakOwned || player2_streak >= streakOwned)) {
                m.sfx.get("ownage").play();
                streakInformed = true;
            }
                        
            if(ball.getX() < Player.INDENT & !isOnGoal) {
                m.sfx.get("player2_goal").play();
                isOnGoal = true;
            } else if(ball.getX() > MainComponent.WINDOW_WIDTH - Player.INDENT & !isOnGoal) {
                m.sfx.get("player1_goal").play();
                isOnGoal = true;
            }
            if(player1_streak > 0) {
                if(player1_streak < streakOutplayed) {
                    currentTextureID = ID_goal1;
                } else if(player1_streak >= streakOutplayed & player1_streak < streakOwned) {
                    currentTextureID = ID_outplay12;
                } else {
                    currentTextureID = ID_own12;
                } 
            } else if(player2_streak > 0) {
                if(player2_streak < streakOutplayed) {
                    currentTextureID = ID_goal2;
                } else if(player2_streak >= streakOutplayed & player2_streak < streakOwned) {
                    currentTextureID = ID_outplay21;
                } else {
                    currentTextureID = ID_own21;
                }
            }
        } else {
            if(this.selection < 0) selection = 0;
            if(this.selection > (byte)(selectionsCount - 1)) selection = (byte)(selectionsCount - 1);
                
             switch(selection) {
                case(0) :
                    selectorY = MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (selectionRegionSizeK / 3) - 6;
                    break;
                case(1) :
                    selectorY = MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (1 + (selectionRegionSizeK / 3)) - 6;
                    break;
                case(2) :
                    selectorY = MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (2 + (selectionRegionSizeK / 3)) - 6;
                    break;
                case(3) :
                    selectorY = MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (3 + (selectionRegionSizeK / 3)) - 6;
                    break;
        }
        
        animationMoment+=0.088d;
        if(animationMoment > 310000000000d) animationMoment = 0;
            this.selectionTransparency = Math.abs((int)(Math.sin(animationMoment) * menu.MAX_ANIMATION_ALPHA));
        }
    }
    
    public void launchBall() {
        if(!ball.isLaunched) {
            ball.isLaunched = true;
            double dir = Math.random() * 4;
            if(dir < 1) {
                ball.setXSpeed(Math.random() * 8 + 6);
                ball.setYSpeed(Math.random() * 7 + 3);
            } else if (dir >= 1 & dir < 2) {
                ball.setXSpeed(-1 * (Math.random() * 8 + 6));
                ball.setYSpeed(Math.random() * 7 + 3);
            } else if (dir >= 2 & dir < 3) {
                ball.setXSpeed(Math.random() * 8 + 6);
                ball.setYSpeed(-1 * (Math.random() * 7 + 3));                
            } else {
                ball.setXSpeed(-1 * (Math.random() * 8 + 6));
                ball.setYSpeed(-1 * (Math.random() * 7 + 3));                
            }
            m.sfx.get("bounce").play();
        }
    }
    
    private void setStartBallPosition(int x, int y) {
        this.ball.setX(x);
        this.ball.setY(y);
    }
    
    public void movePlayerUp(int ID) {
        if(ID == 0) {
            this.player1.moveUp();
        } else if(ID == 1) {
            this.player2.moveUp();
        }
    }
    public void movePlayerDown(int ID) {
        if(ID == 0) {
            this.player1.moveDown();
        } else if(ID == 1) {
            this.player2.moveDown();
        }        
    }
    
    public void setAnimationMoment(double x) {
        this.animationMoment = x;
    }
    
    public void drawGame() {
        if(isPaused) {
            pauseReducingTransparency = 0.25d;
        } else {
            pauseReducingTransparency = 1.0d;
        }
        m.g.setColor(new Color(255, 255, 255, (int)(this.ball.transparency * pauseReducingTransparency)));
        m.g.fillRect(ball.getX() - ball.getSize() / 2, ball.getY() - ball.getSize() / 2, ball.getSize(), ball.getSize());
        m.g.setColor(new Color(255, 255, 255, (int)(240 * pauseReducingTransparency)));
        m.g.fillRect(player1.getX(), player1.getY(), Player.WIDTH, Player.HEIGHT);
        
        Texture tex;
        
        drawMessage(currentTextureID);
        if(comebackCurrentTime > 0) {
            tex = textureLoader.texList.get(ID_comeback);
            m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, (MainComponent.WINDOW_HEIGHT - tex.getHeight()) / 3, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), (MainComponent.WINDOW_HEIGHT - tex.getHeight()) / 3 + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        
        if(!multiplayer) {
            switch(AI.getDifficulty()) {
                case "easy":
                    m.g.setColor(new Color(100, 255, 255, (int)(240 * pauseReducingTransparency)));
                    break;
                case "normal":
                    m.g.setColor(new Color(255, 255, 255, (int)(240 * pauseReducingTransparency)));
                    break;
                case "hardcore":
                    m.g.setColor(new Color(255, 200, 50, (int)(240 * pauseReducingTransparency)));
                    break;
                case "hacker":
                    m.g.setColor(new Color(255, 25, 0, (int)(240 * pauseReducingTransparency)));
                    break;
            }
        }
        m.g.fillRect(player2.getX(), player2.getY(), Player.WIDTH, Player.HEIGHT);
        
        if(isPaused) {
            tex = textureLoader.texList.get(ID_gameIsPaused);
            m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT - tex.getHeight() - 32, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT - 32, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);        
            m.g.setColor(new Color(220, 0, 0, selectionTransparency));
            m.g.fillRect(0, selectorY, 1280, 44);
            int position = 0;
            if(subState == 0) {
                if(multiplayer) {
                    selectionsCount = 2;   
                } else {
                    selectionsCount = 3;
                }
                tex = textureLoader.texList.get(ID_continue);
                m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                position = 1; 
                if(!multiplayer) {
                    tex = textureLoader.texList.get(ID_difficulty);
                    m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                    position = 2;
                    tex = textureLoader.texList.get(ID_exit);
                    m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                } else {
                    tex = textureLoader.texList.get(ID_exit);  
                    m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                }
            } else if(subState == 1) {
                selectionsCount = 4;
                position = 0;
                tex = textureLoader.texList.get(ID_easy);
                m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                position = 1;
                tex = textureLoader.texList.get(ID_normal);
                m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                position = 2;
                tex = textureLoader.texList.get(ID_hardcore);
                m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
                position = 3;
                tex = textureLoader.texList.get(ID_hacker);
                m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), MainComponent.WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            }
        }
    }
    
    public void pauseEnter() {
        if(subState == 0) {
            if(selection == 0) {
                this.isPaused = !this.isPaused;
            }
            if(!multiplayer) {
                selectionsCount = 3;
                if(selection == 1) {
                    subState = 1;
                } else if(selection == 2) {
                    m.musicPlayer.stopMusic("game_music.wav");
                    m.musicPlayer.playMusic("menu_music.wav");
                    exitToMainMenu();
                }
            } else {
                selectionsCount = 1;
                if(selection == 1) {
                    m.musicPlayer.stopMusic("game_music.wav");
                    m.musicPlayer.playMusic("menu_music.wav");
                    exitToMainMenu();
                }
            }
            selection = 0;
        } else if(subState == 1) {
            if(selection == 0) {
                AI.setDifficulty("easy");
            } else if(selection == 1) {
                AI.setDifficulty("normal");
            } else if(selection == 2) {
                AI.setDifficulty("hardcore");
            } else if(selection == 3) {
                AI.setDifficulty("hacker");
            }
            selection = 0;
            subState = 0;
        }
    }

    private void exitToMainMenu() {
        player1.setStartPosition(0);
        player2.setStartPosition(1);
        setStartBallPosition((MainComponent.WINDOW_WIDTH - this.ball.getSize()) / 2,(MainComponent.WINDOW_HEIGHT - this.ball.getSize()) / 2);
        this.isPaused = false;
        ball.isLaunched = false;
        player1_streak = 0;
        player2_streak = 0;
        m.gameState = 0;
        menu.setMainMenu();
        menu.selectionUp();
    }
    
    public void useAI() {
        AI.updatePositionAI(this.player2, this.ball);
        AI.updateSpeed();
    }
    
    private void drawMessage(int ID) {
        Texture tex = textureLoader.texList.get(ID);
        m.g.drawImage(textureLoader.texMenu, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, 16, (MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), 16 + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        double finalMessageTransparency = messageTransparency + (255 - (255 - messageTransparency) * ((pauseReducingTransparency + 1) / 2));
        if(finalMessageTransparency > 255) finalMessageTransparency = 255;
        m.g.setColor(new Color(0, 0, 0, (int)finalMessageTransparency));
        System.out.println(pauseReducingTransparency + " " + messageTransparency + " " + finalMessageTransparency);
        m.g.fillRect((MainComponent.WINDOW_WIDTH - tex.getWidth()) / 2, 16, tex.getWidth(), tex.getHeight());
    }
}
