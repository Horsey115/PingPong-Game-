package org.ixnomad.ponggame.entity;

public class Ball {
    
    public  final int MAX_ANIMATION_ALPHA   =   255;
    public  final int MIN_ANIMATION_ALPHA   =   210;
    private final int size                  =   10;
    
    public double    ballAccelerationX      =   0.6;
    public double    ballAccelerationY      =   0.35;
    public double     transparency;
    public double     animationMoment       =   0.0d;
    private double x;
    private double y;
    private double dx;
    private double dy;
    
    public boolean isLaunched = false;
    
    public void updatePosition() {
        if(isLaunched) {
            this.x += this.dx;
            this.y += this.dy;
        }
    }
    
    public int getX() {
        return (int) this.x;
    }
    
    public int getY() {
        return (int) this.y;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public double getXSpeed() {
        return this.dx;
    }
    public double getYSpeed() {
        return this.dy;
    }
    public void setXSpeed(double speed) {
        this.dx = speed;
    }
    public void setYSpeed(double speed) {
        this.dy = speed;
    }
    
    public void bounceToLeft() {
        this.setXSpeed((this.getXSpeed() + ballAccelerationX * Math.random()) * -1);
        this.x -= 16;
        this.dy *= 1 + Math.random() * 0.05;
    }
    public void bounceToRight() {
        this.setXSpeed((this.getXSpeed() - ballAccelerationX * Math.random()) * -1);
        this.x += 16;
        this.dy *= 1 + Math.random() * 0.05;
    }
    
    public int getSize() {
        return this.size;
    }
}
