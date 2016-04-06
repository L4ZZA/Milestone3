/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dynamic_bodies;

import city.cs.engine.BodyImage;
import city.cs.engine.BoxShape;
import city.cs.engine.CircleShape;
import city.cs.engine.CollisionEvent;
import city.cs.engine.CollisionListener;
import city.cs.engine.GhostlyFixture;
import city.cs.engine.PolygonShape;
import city.cs.engine.Shape;
import city.cs.engine.SolidFixture;
import city.cs.engine.Walker;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.swing.Timer;
import levels.Level;
import main.Frame;
import org.jbox2d.common.Vec2;
import static_bodies.Ground;
import static_bodies.Lifes;

/**
 *
 * @author Pietro
 */
public class SuperMario extends Walker implements ActionListener{

    private static final float JUMPING_SPEED = 10.5f;
    private static final float WALKING_SPEED = 3;
    private boolean released=true;
    
    // Remember:  using the keyword static below means the fields shape and image belong to the class, rather than any instance. 
    // That means there is a single shape and image for all instances of the class.
    /*
    private static final Shape[] animationShapes = {
        new PolygonShape(0.639f,0.993f, 0.646f,-1.004f, -0.468f,-1.0f, -0.461f,1.0f),//walking facingRight
        new PolygonShape(0.468f,1.0f, 0.464f,-1.004f, -0.643f,-0.996f, -0.639f,0.996f),//walking facingLeft
        new PolygonShape(0.548f,1.0f, 0.548f,-1.004f, -0.552f,-0.996f, -0.548f,0.993f),//jumping facingRight
        new PolygonShape(0.548f,0.996f, 0.545f,-1.0f, -0.548f,-1.007f, -0.552f,0.996f)//jumping facingLeft
    };*/
    private static final Shape shape = new PolygonShape(0.552f,0.989f, 0.545f,-1.0f, -0.548f,-0.996f, -0.548f,0.989f);
    
    private static BodyImage [][] spritesheet = {
        {new BodyImage("data/walk1.png", 2),new BodyImage("data/walk2.png", 2),new BodyImage("data/walk3.png", 2)},                 //walking rigth
        {new BodyImage("data/walk1_left.png", 2),new BodyImage("data/walk2_left.png", 2),new BodyImage("data/walk3_left.png", 2)},  //walking left
        {new BodyImage("data/jump1.png",2),new BodyImage("data/jump2.png",2)},                                                      //jumping rigth
        {new BodyImage("data/jump1_left.png",2),new BodyImage("data/jump2_left.png",2)},                                            //jumping left
        {new BodyImage("data/walk1.png",2), new BodyImage("data/walk1_left.png", 2)}
    };
    SolidFixture fixture;
    
    private boolean moving; //true = mario is moving , false = mario is not moving
    public boolean direction;//true = moving right  ,  false = moving left
    
    private Level world;
    
    private final int LIFE_MAX=5;
    private int currentLife=5;
    private Lifes[] heartsList = new Lifes[LIFE_MAX];
    
    public boolean goombaTouched =false;
    private final float gravityScale;
    
    private int rosesPicked;
    
    // animations
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames = {
            3, 2
    };
    private int currentFrame=0;
    private boolean falling =false, jumping=false;
    private boolean hitEnemy;
        
    //animation actions
    private static final int WALKING, JUMPING ;
    private static final int WALK_RIGHT = WALKING = 0;
    private static final int WALK_LEFT = JUMPING = 1;
    private static final int JUMP_RIGHT = 2;
    private static final int JUMP_LEFT = 3;
    private static final int IDLE = 4;
    private int currentState;
    Timer timer;
    private static final int DELAY=120;
    
    // fireball 
    private int damage;
    
    /**
     * 
     * @param world the world within the character will be create
     */
    public SuperMario(Level world) {
        super(world);
        this.world = world;
        currentState = IDLE;
        fixture = new SolidFixture(this,shape);
        addImage(spritesheet[IDLE][currentFrame]);
        
        released=true;
        rosesPicked=0;
        damage=5;
        gravityScale = getGravityScale();
        direction = true;
        
        for (int i = 0; i < LIFE_MAX; i++) {
            Lifes life = new Lifes(world,new Vec2(-15+i,11));
            heartsList[i]= life;
        }
        addCollisionListener(new Collision());
        
        timer = new Timer(DELAY,this);
        timer.setInitialDelay(DELAY);
        timer.start();
    }
    
    /**
     * 
     * @param world the world within the character will be create
     * @param pos sets the position of the  character
     */
    public SuperMario(Level world, Vec2 pos){
        this(world);
        setPosition(pos);
    }

    

    /**
     * 
     */
    public void decreaseLife(){
        currentLife--;
        if(currentLife>0){
            heartsList[currentLife].destroy();
            heartsList[currentLife] = null;
        }
        else{
            dead();
        }
    }
    
    /**
     * 
     */
    public void increaseLife(){
        if(currentLife < LIFE_MAX){
            currentLife++;
            if(currentLife == 0)
                heartsList[currentLife-1] = new Lifes(world,new Vec2(-15,11));
            else
                heartsList[currentLife-1] = new Lifes(world,heartsList[currentLife-2].getPosition().addLocal(1, 0));
        }
    }
    
    /**
     * 
     */
    public void touchable(){
        goombaTouched=false;
        getFixtureList().set(1, fixture);
        setGravityScale(gravityScale);
    }
    
    /**
     * This function doesn't work (doesn't change anything
     * @param prev needs the previous step's count 
     */
    public void untouchable(int prev){
        if(world.getStepCount() < prev+100){
            System.out.println("prev: "+prev +"\nstep: "+world.getStepCount());
            goombaTouched=true;
            setGravityScale(gravityScale);
        }
        else{
            touchable();
        }
    }
    
    /**
     * This function is called to let the character die (it destroys the object)
     */
    public void dead(){
        this.destroy();
        try {
            System.out.println("Dead");
            Thread.sleep(1000);
            Frame.restartLevel();
        } catch (InterruptedException ex) {
            Logger.getLogger(SuperMario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * @return the damage of each fireball
     */
    public int getDamage(){return damage;};
    /**
     * 
     * @return returns the number of roses picked
     */
    public int getRosesCount() { return rosesPicked; }
    /**
     * 
     * @return return true or false if the character is moving or not
     */
    public boolean isMoving() { return moving; }
    /**
     * 
     * @param isMoving change the moving status of the character
     */
    public void setMoving(boolean isMoving) { this.moving = isMoving; }   

    /**
     * 
     * @return <code>hitEnemy</code> - true or false if the actor is in the hit
     *         state or not
     */
    public boolean isHitEnemy() {
        return hitEnemy;
    }

    public void setHitEnemy(boolean hitEnemy) {
        this.hitEnemy = hitEnemy;
    }
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(!moving)
            currentState=IDLE;
        
        if((currentState==WALK_RIGHT)){
            currentFrame++;
            if(currentFrame == numFrames[WALKING])
                currentFrame=0;
        }
        if((currentState==WALK_LEFT)){
            currentFrame--;
            if(currentFrame == -1)
                currentFrame = 2;
        }
        else if(currentState==JUMP_RIGHT || currentState==JUMP_LEFT){
            if(direction)
                currentState=JUMP_RIGHT;
            else
                currentState=JUMP_LEFT;
            currentFrame=1;
            Vec2 v = this.getLinearVelocity();
            if (v.y == 0) {
                currentFrame=0;
                falling =false;
                jumping=false;
            }else if(v.y > 0){
                jumping=true;
                falling=false;
            }
            else if(v.y< 0){
                jumping=false;
                falling=true;
            }
            
        }
        else if(currentState == IDLE && !moving){
            if(direction)
                currentFrame=0;
            else
                currentFrame=1;
        }
        //System.out.println("Curr: "+currentState);
        removeAllImages();
        addImage(spritesheet[currentState][currentFrame]);
        
        
        if(getPosition().x < -world.HORIZONTAL_BOUNDS && !direction){
            setPosition(new Vec2(-world.HORIZONTAL_BOUNDS,getPosition().y));
        }
        else if(getPosition().x > world.HORIZONTAL_BOUNDS && direction){
            setPosition(new Vec2(world.HORIZONTAL_BOUNDS,getPosition().y));
        }
    }
    
    
    /**
     * 
     * @param k gets the int value for the key event
     */
    public void keyPressed(int k) {
 //       System.out.println("Fixtures: "+getFixtureList());
        switch(k){
            case KeyEvent.VK_Q: // Q = quit
                System.exit(0);
                break;
                
            case KeyEvent.VK_UP: // arrow up = jump
                //System.out.println(""+jumping+"\t" + falling);
                    //jumping=true;
                    moving=true;
                    Vec2 v = getLinearVelocity();// only jump if mario is not already jumping
                    
                    if (Math.abs(v.y) < 0.01f) {
                        if(direction)
                            currentState=JUMP_RIGHT;
                        else
                            currentState=JUMP_LEFT;
                        currentFrame=1;
                        jump(JUMPING_SPEED); 

                    }
                break;
                
            case KeyEvent.VK_LEFT: // walk left
                if(jumping || falling)
                    currentState=JUMP_LEFT;
                else
                    currentState=WALK_LEFT;
                startWalking(-WALKING_SPEED);
                moving=true; 
                direction=false;

                break;
                
            case KeyEvent.VK_RIGHT: // walk right
                if(jumping || falling)
                    currentState=JUMP_RIGHT;
                else
                    currentState=WALK_RIGHT;
                startWalking(WALKING_SPEED); 
                moving=true;
                direction=true;
                
                break;
            
            case KeyEvent.VK_SPACE:
                if(released){
                    FireBall fireBall;
                    //if the ball is created on top of the character is goin to disappear due to the collision
                    if(direction)
                        fireBall = new FireBall(world, getPosition().add(new Vec2(0.5f,0)), direction);
                    else
                        
                        fireBall = new FireBall(world, getPosition().add(new Vec2(-0.5f,0)), direction);
                    released=false;
                }
                break;
        }
    }

    /**
     * 
     * @param k gets the int value for the key event
     */
    public void keyReleased(int k) {
        switch(k){
            case KeyEvent.VK_LEFT:
                moving=false;
                stopWalking();
                break;
                
            case KeyEvent.VK_RIGHT:
                moving=false;
                stopWalking();
                break;
                
            case KeyEvent.VK_SPACE:
                released=true;
                break;
        }
    }
    /**
     * A inner class created to handle all the collisions between mario and the other objects
     */
    private class Collision implements CollisionListener{
        private int hitTimes;
        
        @Override
        public void collide(CollisionEvent e) {
            
            if(e.getOtherBody() instanceof Ground){
                if(hitEnemy)
                    hitEnemy=false;
            }
            if(e.getOtherBody().getClass() == BonusLife.class){
                increaseLife();
                e.getOtherBody().destroy();
            }
            if(e.getOtherBody() instanceof Plant){
                if(!hitEnemy){
                    hitEnemy=true;
                    decreaseLife();
                }
            }
            if(e.getOtherBody() instanceof Projectile){
                if(!hitEnemy){
                    hitTimes++;
                    hitEnemy=true;
                    System.out.println("hitTimes: "+hitTimes);
                    if(hitTimes > 5){
                        hitTimes=0;
                        decreaseLife();
                    }
                }
            }
            if(e.getOtherBody().getClass() == Goomba.class && !goombaTouched){
                //int prev =world.getStepCount();
                //untouchable(prev);
                if(direction){
                    move(new Vec2(-1,0));
                }
                else{
                    move(new Vec2(1,0));
                }
                for (int i = 0; i < 3; i++) {
                    decreaseLife();
                }
            }
            if(e.getOtherBody().getClass() == Rose.class){
                rosesPicked++;
                e.getOtherBody().destroy();
            }
            if(e.getOtherBody().getClass() == Princess.class && rosesPicked == world.getRoses()){
                Frame.nextLevel(world.currentLevel);
                rosesPicked=0;
            }
        }
        
    }

}
