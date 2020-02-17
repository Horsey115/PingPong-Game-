package org.ixnomad.ponggame;

import java.awt.Color;
import org.ixnomad.ponggame.TextureLoader.TextureLoader;
import org.ixnomad.ponggame.TextureLoader.Texture;

public class Menu {

    TextureLoader textureLoader;
    MainComponent m;
    
    private final int WINDOW_WIDTH;
    private final int WINDOW_HEIGHT;
    private final int iconX;
    private final int iconY;
    private final int selectionRegionSizeK = 6;
    public  final int MAX_ANIMATION_ALPHA  = 125;
    
    byte ID_icon, ID_logo, ID_start, ID_exit, ID_player1, ID_player2, ID_singleplayer, ID_multiplayer, 
            ID_controls, ID_ws, ID_arrows, ID_information, ID_back, ID_pause, ID_esc, ID_launchball, ID_esctopause, ID_gamebyixnomad;
    
    private boolean isMainMenuState         =   true;
    private byte    subState                =   0; //0 — information menu, 1 — chosing game type
    public  byte    selection               =   0;
    public  byte    selectionMin            =   0;
    public  byte    selectionsCount         =   3;
    private int     selectionTransparency   =   0;
    private double  animationMoment         =   0.0d;
    private int     selectorY;
    
    public Menu(MainComponent mainComponent) {
        this.m              =   mainComponent;
        this.textureLoader  =   mainComponent.textureLoader;
        textureLoader.loadTextureParams("org/ixnomad/ponggame/data/tex_loc.data");
        
        WINDOW_WIDTH        =   MainComponent.WINDOW_WIDTH;
        WINDOW_HEIGHT       =   MainComponent.WINDOW_HEIGHT;
        iconX               =   WINDOW_WIDTH  /  12;
        iconY               =   WINDOW_HEIGHT /  8;
        
        //setting textures ID's
        ID_icon             =   textureLoader.getID("icon");
        ID_logo             =   textureLoader.getID("logo");
        ID_start            =   textureLoader.getID("start");
        ID_exit             =   textureLoader.getID("exit");
        ID_player1          =   textureLoader.getID("player1");
        ID_player2          =   textureLoader.getID("player2");
        ID_singleplayer     =   textureLoader.getID("singleplayer");
        ID_multiplayer      =   textureLoader.getID("multiplayer");
        ID_controls         =   textureLoader.getID("controls");
        ID_ws               =   textureLoader.getID("ws");
        ID_arrows           =   textureLoader.getID("arrows");
        ID_information      =   textureLoader.getID("information");
        ID_back             =   textureLoader.getID("back");
        ID_pause            =   textureLoader.getID("pause");
        ID_esc              =   textureLoader.getID("esc");        
        ID_esctopause       =   textureLoader.getID("esctopause");
        ID_launchball       =   textureLoader.getID("launchball");
        ID_gamebyixnomad    =   textureLoader.getID("gamebyixnomad");
    }
       
    public void menuProcess() {
        
        selectionMin    = 0;
        
        if(isMainMenuState) {
           selectionsCount = 3;
        }
        
        if(subState == 2) {
            selection = 3;
            selectionMin = 2;
        }
        
        if(this.selection < 0) selection = 0;
        if(this.selection > (byte)(selectionsCount - 1)) selection = (byte)(selectionsCount - 1);
                
        switch(selection) {
            case(0) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (1 + (selectionRegionSizeK / 3)) - 6;
                break;
            case(1) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (2 + (selectionRegionSizeK / 3)) - 6;
                break;
            case(2) :
                selectorY = WINDOW_HEIGHT / selectionRegionSizeK * (3 + (selectionRegionSizeK / 3)) - 6;
                break;
        }
        
        animationMoment+=0.088d;
        if(animationMoment > 310000000000d) animationMoment = 0;
        this.selectionTransparency = Math.abs((int)(Math.sin(animationMoment) * MAX_ANIMATION_ALPHA));
    }
   
    public void drawMenu() {
        Texture tex;
        tex = textureLoader.texList.get(ID_icon);
        m.g.drawImage(textureLoader.texMenu, iconX, iconY, iconX + tex.getWidth(), iconY + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        tex = textureLoader.texList.get(ID_logo);
        m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / 6, (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / 6 + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        int position;
        if(isMainMenuState) {
            position = 1;
            tex = textureLoader.texList.get(ID_start);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 2;
            tex = textureLoader.texList.get(ID_information);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 3;
            tex = textureLoader.texList.get(ID_exit);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        if(subState == 2) {
            position = 1;
            tex = textureLoader.texList.get(ID_controls);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 2;
            tex = textureLoader.texList.get(ID_player1);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) - 48, (WINDOW_WIDTH) / 4 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight() - 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_player2);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH) / 4 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_pause);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH) / 4, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + 48, (WINDOW_WIDTH) / 4 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight() + 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_ws);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) - 48, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight() - 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_arrows);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_esc);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + 48, (WINDOW_WIDTH - tex.getWidth()) / 3 * 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight() + 48, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_launchball);
            m.g.drawImage(textureLoader.texMenu, 8, WINDOW_HEIGHT - tex.getHeight() - 8, 8 + tex.getWidth(), WINDOW_HEIGHT - 8, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_esctopause);
            m.g.drawImage(textureLoader.texMenu, 8, WINDOW_HEIGHT - tex.getHeight() * 2 - 16, 8 + tex.getWidth(), WINDOW_HEIGHT - tex.getHeight() - 16, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            tex = textureLoader.texList.get(ID_gamebyixnomad);
            m.g.drawImage(textureLoader.texMenu, WINDOW_WIDTH - tex.getWidth() - 8, WINDOW_HEIGHT - tex.getHeight() - 8, WINDOW_WIDTH - 8, WINDOW_HEIGHT - 8, tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 3;
            tex = textureLoader.texList.get(ID_back);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        if(subState == 1) {
            position = 1;
            tex = textureLoader.texList.get(ID_singleplayer);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 2;
            tex = textureLoader.texList.get(ID_multiplayer);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
            position = 3;
            tex = textureLoader.texList.get(ID_back);
            m.g.drawImage(textureLoader.texMenu, (WINDOW_WIDTH - tex.getWidth()) / 2, WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)), (WINDOW_WIDTH - tex.getWidth()) / 2 + tex.getWidth(), WINDOW_HEIGHT / selectionRegionSizeK * (position + (selectionRegionSizeK / 3)) + tex.getHeight(), tex.getSX1(), tex.getSY1(), tex.getSX2(), tex.getSY2(), m);
        }
        //m.g.drawImage(texMenu, MainComponent.WIDTH - , dy1, dx2, dy2, sx1, sy1, sx2, sy2, m);
        
        m.g.setColor(new Color(220, 0, 0, selectionTransparency));
        m.g.fillRect(0, selectorY, 1280, 44);
    }
    
    public void enter() {
        if(isMainMenuState) {
            if(selection == 0) {
                subState = 1;
                isMainMenuState = false;
            } else if(selection == 1) {
                subState = 2;
                isMainMenuState = false;
            } else if(selection == 2) {
                m.musicPlayer.stopMusic("menu_music.wav");
                System.exit(0);
            }
        } else {
            if(subState == 2) {
                subState = 0;
                isMainMenuState = true;
                selection = 1;
            }
            if(subState == 1) {
                if(selection == 2) {
                    subState = 0;
                    isMainMenuState = true;
                    selection = 0;
                } else if(selection == 0) {
                    m.musicPlayer.stopMusic("menu_music.wav");
                    m.game.multiplayer = false;
                    m.gameState = 1;
                    m.game.startGame();
                    m.musicPlayer.playMusic("game_music.wav");
                } else if(selection == 1) {
                    m.musicPlayer.stopMusic("menu_music.wav");
                    m.game.multiplayer = true;
                    m.gameState = 1;
                    m.game.startGame();
                    m.musicPlayer.playMusic("game_music.wav");
                }
            }
        }
    }
    
    public void setMainMenu() {
        subState = 0;
        isMainMenuState = true;
    }
    
    public boolean isMainMenuState() {
        return this.isMainMenuState;
    }
    
    public void setMainMenuState(boolean isMainMenuState) {
        this.isMainMenuState = isMainMenuState;
    }
    
    public void selectionUp() {
        this.selection--;
    }
    
    public void selectionDown() {
        this.selection++;
    }
}
