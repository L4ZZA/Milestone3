/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamic_bodies;

import city.cs.engine.Body;
import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.DestructionEvent;
import city.cs.engine.DestructionListener;
import city.cs.engine.DynamicBody;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.StaticBody;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import levels.Level;
import main.Frame;
import org.jbox2d.common.Vec2;
import static_bodies.Ground;

/**
 *
 * @author Pietro
 */
public class Boss extends StaticBody implements ActionListener{

    private static final Shape shape = new BoxShape(1.5f,2f);
    private static BodyImage [] spritesheet = { 
        new BodyImage("data/Boss1.png", 4), new BodyImage("data/Boss2.png", 4), new BodyImage("data/Boss3.png", 4) 
    };
    SolidFixture fixture = new SolidFixture(this,shape);
    
    private final int numFrames = 3;
    
    //enemy
    private int health;
    private int maxHealth;
    
    // animation
    private Timer timer;
    private Timer wait;
    private Timer waitLife;
    private final int DELAY= 20;
    
    // animation states
    private static final int SLEEPY = 0;
    private static final int ANGER = 1;
    private static final int RAGE = 2;
    private int currentState=0;
    private int angerTimes;
    private boolean anger;
    private boolean sleepy;
    
    // player
    private SuperMario mario;
    private final double RANGE= 4.5f;
    
    private Level world;
    
    //movements
    private boolean direction;
    private float x, y, currentx, currenty;
    private float dx,dy;
    private float moveSpeed;
    private float fallSpeed; // a type of attack
    private boolean inside;
    private boolean falling;
    private boolean movingUp;
    private boolean attacking;
    private boolean hit;
    
    /**
     * 
     * @param w the world within the character will be create
     */
    public Boss(Level w){
        super(w);
        world = w;
        y = 7;
        x=0;
        setPosition(new Vec2(x,y));
        addImage(spritesheet[SLEEPY]);
        mario = w.getActor();
        
        health = maxHealth = 100;
        
        direction = true;
        dx=1;
        dy=1;
        moveSpeed=.15f;
        fallSpeed=0;
        
        angerTimes=0;
        
        sleepy = true;
        anger=false;
        
        inside=false;
        falling=false;
        movingUp = false;
        attacking = false;
        hit=false;
        
        timer = new Timer(DELAY,this);
        timer.setInitialDelay(0);
        timer.start();
        
        wait = new Timer(1200,this);
        wait.setRepeats(false);
        
        addCollisionListener(new Collision());
        addDestructionListener(new DestructionListener(){

            @Override
            public void destroy(DestructionEvent e) {
                Frame.nextLevel(w.currentLevel);
            }
            
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(mario.getPosition().x == getPosition().x);
        if(e.getSource() == timer){
            if(inRangeLeft() || inRangeRight()){
                //System.out.println(angerTimes);
                if(!inside){
                    angerTimes++;
                    inside=true;
                }
                if(angerTimes < 2){
                    currentState=ANGER;
                }
                else{
                    currentState=RAGE;
                }
            }
            else{
                mario.setHitEnemy(false);
                attacking = false;
                currentState=SLEEPY;
                if(inside)
                    inside=false;
            }

            currentx=getPosition().x;
            currenty= getPosition().y;

            if(getPosition().x > -world.HORIZONTAL_BOUNDS && !direction){
                move(new Vec2(-dx*moveSpeed,dy*fallSpeed));
            }
            else if(getPosition().x < world.HORIZONTAL_BOUNDS && direction){
                move(new Vec2(dx*moveSpeed,dy*fallSpeed));
            }

            if(getPosition().x < -world.HORIZONTAL_BOUNDS+.5f && !direction){
                direction=true;
            }
            else if(getPosition().x > world.HORIZONTAL_BOUNDS-.5f && direction){
                direction = false;
            }
        }
            
        if(e.getSource() == wait){
            falling=false;
        }
        
        update();
    }

    /**
     * 
     * @return return true or false if the main character is in the range (left)
     */
    public boolean inRangeLeft() {
        Body a = mario;
        float gap = getPosition().x - a.getPosition().x;
        return gap < RANGE && gap > 0;
    }
    
    /**
     * 
     * @return return true or false if the main character is in the range (right)
     */
    public boolean inRangeRight() {
        Body a = mario;
        float gap = getPosition().x - a.getPosition().x;
        return gap > -RANGE && gap < 0;
    }
    
    /**
     * updates the status of the object 
     */
    private void update(){
        if(currentState==SLEEPY){
            moveSpeed=.15f;
            removeAllImages();
            addImage(spritesheet[SLEEPY]);
        }
        else if(currentState==ANGER){
            addImage(spritesheet[ANGER]);
            moveSpeed=.15f;
            shootAttack();
        }
        else if(currentState==RAGE){
            fallAttack();
            moveSpeed=.08f;
            addImage(spritesheet[RAGE]);
        }
    }
    
    /**
     * makes the boss falling until he reaches a certain point and after that it 
     * brings it back 
     */
    private void fallAttack(){
        //System.out.println(falling+"\t"+movingUp);
        if(currenty == y){
            if(!falling && !movingUp){
                moveSpeed=0;
                fallSpeed=-1.5f;
                falling=true;
            }else if(movingUp && !falling){
                setPosition(new Vec2(currentx,y));
                moveSpeed=.15f;
                fallSpeed=0;
                movingUp=false;
                angerTimes=0;
                hit=false;
            }
        }
        if(currenty <= -5.5f){
            if(falling && !movingUp){
                moveSpeed=0;
                fallSpeed=0;
                if(wait.isRunning())
                    wait.start();
                else 
                    wait.restart();
            }else if(!falling && !movingUp){
                moveSpeed=0;
                fallSpeed=1.5f;
                movingUp=true;
            }
        }
        
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    
    /**
     * 
     */
    private void shootAttack(){
        Projectile p1,p2,p3;
        p1 = new Projectile(world);
        p2 = new Projectile(world,1f);
        p3 = new Projectile(world,-1f);
    }
    
    /**
     * A inner class created to handle all the collisions between the boss and the other objects
     */
    private class Collision implements CollisionListener{
        
        SuperMario mario = world.getActor();
        
        @Override
        public void collide(CollisionEvent e) {
            if(e.getOtherBody() == mario && !hit){
                hit= true;
                //mario.decreaseLife();
                //mario.decreaseLife();
            }
            
            if(e.getOtherBody() instanceof FireBall){
                if(health>0)
                    health -= 15;
                else
                    destroy();
            }
        }
        
    }
}
