
package org.ixnomad.ponggame.TextureLoader;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class TextureLoader {
    
    public List<Texture> texList = new ArrayList<>();
    public Image texMenu;
    
    public TextureLoader() {
        try {
            texMenu = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("org/ixnomad/ponggame/data/textures.png"));
        } catch (IOException ex) {
            Logger.getLogger(TextureLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadTextureParams(String filePath) {
        InputStream texFile = this.getClass().getClassLoader().getResourceAsStream(filePath); 
        try(Scanner scanner = new Scanner(texFile)){
            while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.equals("#")) {
                break;
            }
            String[] tokens = line.split("\\s+");
            Texture tex = new Texture(tokens[0]);
            System.out.print("\n \n || " + tokens[0] +  " || \n \n");
            tex.setSX1(Integer.parseInt(tokens[1]));
            tex.setSY1(Integer.parseInt(tokens[2]));
            tex.setSX2(Integer.parseInt(tokens[3]));
            tex.setSY2(Integer.parseInt(tokens[4]));
            System.out.println(tex);
            texList.add(tex);
            }
            
            scanner.close();
            System.out.print("\n");

            for(Texture tex : texList) {
                tex.setSize();
                System.out.print("\n" + tex.getName() + ": " + tex.getSX1() + " " + tex.getSY1() + " " + tex.getSX2() + " " + tex.getSY2() + " | " + tex.getWidth() + " " + tex.getHeight());
            }
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error: falied to load " + filePath + "/n" + e);
        }
    }
    
    public byte getID(String textureName) {
        byte id = 0;
        for(byte i = 0; i < texList.size(); i++) {
            Texture tex = texList.get(i);
            if(tex.getName().equals(textureName)) {
                id = i;
                System.out.println("ID of '" + textureName + "' equals: " + id);
                break;
            }
        }
        if(id == 0) {
            System.out.println("ERROR! Texture '" + textureName + "' has not been found!");
        }
        return id;
    }
}
