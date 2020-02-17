package org.ixnomad.ponggame.TextureLoader;

public class Texture {
    
    private int sx1;
    private int sy1;
    private int sx2;
    private int sy2;
    private int width;
    private int height;
    
    private final String name;
    
    public Texture(String name) {
        this.name = name;
    }

    public void setSize() {
        this.width  = this.sx2 - this.sx1;
        this.height = this.sy2 - this.sy1;
    }
    
    //setters
    public void setSX1(int sx1) {
        this.sx1 = sx1;
    }
    public void setSY1(int sy1) {
        this.sy1 = sy1;
    }
    public void setSX2(int sx2) {
        this.sx2 = sx2;
    }
    public void setSY2(int sy2) {
        this.sy2 = sy2;
    }

    //getters
    public int getSX1() {
        return sx1;
    }
    public int getSY1() {
        return sy1;
    }
    public int getSX2() {
        return sx2;
    }
    public int getSY2() {
        return sy2;
    }
    public int getWidth() {
        return this.width;
    }
    public int getHeight() {
        return this.height;
    }
    public String getName() {
        return this.name;
    }
}
