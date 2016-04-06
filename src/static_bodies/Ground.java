/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package static_bodies;

import city.cs.engine.BodyImage;
import city.cs.engine.World;
import java.awt.Color;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Pietro
 */
public class Ground extends Walkable{

    private static BodyImage image= new BodyImage("data/Platform.png");
    
    public Ground(World w) {
        super(w,16f, 0.5f,new Vec2(0,-11.5f));
        setFillColor(new Color(128, 64, 0));
        //addImage(image);
        
    }
    
}
