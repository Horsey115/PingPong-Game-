package org.ixnomad.ponggame.entity;

import org.ixnomad.ponggame.MainComponent;

public class AI {
    
    Player controllingPlayer;
    Ball ball;
    
    private double dy;
    private double difficulty = 1;
    private double difficulty_dy = 0.14d;
    private double hackerDifficulty = 0.6;
    private boolean hackerIsEnabled = false;
    private String difficultyName = "easy";
    
    public void updatePositionAI(Player player, Ball ball) {
        this.controllingPlayer = player;
        this.ball = ball;
        controllingPlayer.setY(controllingPlayer.getY() + dy);
        if(player.getY() < 16) {
            player.setY(16);
            dy = 0;
        }
        if(player.getY() > MainComponent.WINDOW_HEIGHT - Player.HEIGHT - 16) {
            player.setY(MainComponent.WINDOW_HEIGHT - Player.HEIGHT - 16);
            dy = 0;
        }
    }

    public void updateSpeed() {
        if(controllingPlayer.getY() + Player.HEIGHT / 2 > ball.getY()) {
            dy -= difficulty_dy * Math.sqrt(Math.abs(controllingPlayer.getY() - ball.getY()) / 2);
        } else if(controllingPlayer.getY() + Player.HEIGHT / 2 < ball.getY()) {
            dy += difficulty_dy * Math.sqrt(Math.abs(controllingPlayer.getY() - ball.getY()) / 2);
        }
        if (dy > Player.MOVESPEED * 0.3 * difficulty) {
            dy-= 0.3 * Math.sqrt(Math.abs(controllingPlayer.getY() - ball.getY()) / 2);
        } else if(dy < -(Player.MOVESPEED * 0.3 * difficulty)) {
            dy+= 0.3 * Math.sqrt(Math.abs(controllingPlayer.getY() - ball.getY()) / 2);
        }
        
        if(hackerIsEnabled) {
            if(ball.getXSpeed() > 0) {
                double distanceToBall = controllingPlayer.getX() - ball.getX();
                double ball_y0 = (distanceToBall / ball.getXSpeed()) * ball.getYSpeed() + ball.getY();
                if(ball_y0 < 0) {
                    ball_y0 = Math.abs(ball_y0);
                }
                if(ball_y0 > MainComponent.WINDOW_HEIGHT) {
                    ball_y0 = Math.abs(MainComponent.WINDOW_HEIGHT - (ball_y0 - MainComponent.WINDOW_HEIGHT));
                } 
                if(controllingPlayer.getY() < ball_y0 - Player.HEIGHT / 2) {
                    dy += 0.34d * Math.sqrt(Math.abs(controllingPlayer.getY() - ball_y0) / 2) * hackerDifficulty;
                } else if(controllingPlayer.getY() > ball_y0 - Player.HEIGHT / 2) {
                    dy -= 0.34d * Math.sqrt(Math.abs(controllingPlayer.getY() - ball_y0) / 2) * hackerDifficulty;
                }
            } else {
                if(controllingPlayer.getY() < (MainComponent.WINDOW_HEIGHT - Player.HEIGHT) / 2) {
                    dy += 0.34d * Math.sqrt(Math.abs(controllingPlayer.getY() - (MainComponent.WINDOW_HEIGHT - Player.HEIGHT) / 2) / 2) * hackerDifficulty;
                } else if(controllingPlayer.getY() > (MainComponent.WINDOW_HEIGHT - Player.HEIGHT)) {
                    dy -= 0.34d * Math.sqrt(Math.abs(controllingPlayer.getY() - (MainComponent.WINDOW_HEIGHT - Player.HEIGHT) / 2) / 2) * hackerDifficulty;
                }
            }
        }
    }
    
    public void setDifficulty(String easy_normal_hardocre_hacker) {
        this.difficultyName = easy_normal_hardocre_hacker;
        switch (difficultyName) {
            case "easy":
                difficulty = 1.4;
                difficulty_dy = 0.18d;
                hackerIsEnabled = false;
                break;
            case "normal":
                difficulty = 1.7;
                difficulty_dy = 0.18d;
                hackerIsEnabled = false;
                break;
            case "hardcore":
                difficulty = 3.2;
                difficulty_dy = 0.20d;
                hackerDifficulty = 1;
                hackerIsEnabled = false;
                break;
            case "hacker":
                difficulty = 0;
                difficulty_dy = 0;
                hackerDifficulty = 0.95;
                hackerIsEnabled = true;
                break;
        }
    }
    
    public String getDifficulty() {
        return this.difficultyName;
    }
}
