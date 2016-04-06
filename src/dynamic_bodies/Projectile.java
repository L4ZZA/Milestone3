/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamic_bodies;

import city.cs.engine.CircleShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import levels.Level;
import org.jbox2d.common.Vec2;

/**
 *
 * @author Pietro
 */
public class Projectile extends DynamicBody implements ActionListener{

    private Boss enemy;
    private SuperMario player;
    
    private Vec2 e,p,v;
    
    private Timer timer;
    private boolean yellow;
    private int hitTimes = 0;
    
    public Projectile(Level w) {
        super(w,new CircleShape(.25f));
        setFillColor(Color.yellow);
        this.setGravityScale(0);
        enemy=w.getBoss();
        player = w.getActor();
        e = enemy.getPosition();
        p = player.getPosition();
        setPosition(e.add(new Vec2(0,-3)));
        v = p.sub(e);
        v.normalize();
        applyImpulse(v.mul(1.01f));
        
        addCollisionListener(new Collision());
        
        yellow=true;
        
        timer = new Timer(50,this);
        timer.start();
    }
    
    public Projectile(Level w, float differentPos) {
        this(w);
        p = player.getPosition().addLocal(differentPos, 0);
        v = p.sub(e);
        v.normalize();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(yellow){
            setFillColor(Color.red);
            yellow=false;
        }
        else {
            setFillColor(Color.yellow);
            yellow=true;
        }
            
    }
    
    
    
    /**
     * A inner class created to handle all the collisions between the boss and the other objects
     */
    private class Collision implements CollisionListener{
        
        @Override
        public void collide(CollisionEvent e) {
            if(e.getOtherBody() instanceof Projectile){
                
            }
            else
                destroy();
        }
        
    }
    
}
