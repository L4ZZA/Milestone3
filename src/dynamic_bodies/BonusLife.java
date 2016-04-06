/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamic_bodies;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.Fixture;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.Walker;
import city.cs.engine.World;
import org.jbox2d.common.Vec2;

/**
 * A dynamic object 
 * @author Pietro
 */
public class BonusLife extends Walker{
    
    private static final Shape body = new BoxShape(0.5f, 0.5f);
    private static final BodyImage image =
        new BodyImage("data/life_bonus.png");
    
    /**
     * Creates the object in the world 
     * @param world the world within the character will be create
     */
    public BonusLife(World world) {
        super(world);
        addImage(image);
        Fixture fixture = new SolidFixture(this,body);
    }
    
    /**
     * Create the object triggering it immediately  
     * @param world the world within the character will be create
     * @param position sets the position of the  character
     */
    public BonusLife(World world, Vec2 position){
        this(world); // call the constructor of this class with this parameter
        setPosition(position.add(new Vec2(0,1)));
        jump(5);
    }

    /**
     * Triggers the bonus making the object jumping from a given position 
     * @param position sets the position of the  character
     */
    public void trigger(Vec2 position){
        setPosition(position.add(new Vec2(0,1)));
        jump(5);
    }
}
