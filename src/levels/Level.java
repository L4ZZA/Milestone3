/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package levels;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import city.cs.engine.World;
import dynamic_bodies.Boss;
import dynamic_bodies.Goomba;
import dynamic_bodies.SuperMario;
import main.PlayPanel;
import org.jbox2d.common.Vec2;
import static_bodies.Ground;
import static_bodies.Platform;

/**
 *
 * @author Pietro
 */
public abstract class Level extends World{ 
    //player object
    
    //list of characters (non-player)
    protected SuperMario mario;
    protected Goomba goomba;
    protected Boss boss;

    
    protected int nRoses=0;
    protected Ground g;
    
    //list of platforms
    Platform goombaPlatform;
    //ground
    public final float HORIZONTAL_BOUNDS= 15.2f;
    
    //level 
    public int currentLevel;
    protected PlayPanel view;
    protected boolean completed;
    protected Tracker stepListener =new Tracker();
    int stepCount= 0, previous= stepListener.stepCount;
    
    //view
    public abstract void setView(PlayPanel view);
    
    //steplistener
    protected void setStepListener(){
        addStepListener(stepListener);
    }
    
    public int getStepCount(){
        return stepListener.stepCount;
    }
    
    //boss
    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }
    
    /**
     * Return
     * @return the stepListener object
     */
    public Tracker getStepListener(){
        return stepListener;
    }
    
    //drawing
    public abstract void draw();
    
    //level complete
    public abstract boolean isCompleted();
    
    /**
     * Return the current level
     * @return the instance of the world 
     */
    public Level getWorld() {
        return this;
    }
    
    /**
     * Gives the instance of the playing actor
     * @return returns the current actor in the game
     */
    public SuperMario getActor() {
        return mario;
    }
    
    /**
     * Set the main character
     * @param mario return the main character
     */
    public void setPlayer(SuperMario mario){
        this.mario = mario;
    }
    
    /**
     * 
     * @return the number of roses needed to go to the next level
     */
    public int getRoses(){
        return nRoses;
    }
    
    /**
     * 
     * @return the ground inside the level
     */
    public Ground getGround(){ return g; }
    
    protected class Tracker implements StepListener{

        int stepCount=0;
        
        
        @Override
        public void preStep(StepEvent e) {
            
            
            stepCount++;
            //if exist work with goomba
            if(goomba != null){
                if(goomba.getPosition().x > goombaPlatform.getBound("left") && !goomba.direction){
                    goomba.startWalking(-goomba.SPEED);
                }
                else if(goomba.getPosition().x < goombaPlatform.getBound("right")&& goomba.direction){
                    goomba.startWalking(goomba.SPEED);
                }
                //System.out.println("goomba: "+goomba.getPosition().x);
                if(goomba.getPosition().x < goombaPlatform.getBound("left")+.5f && !goomba.direction){
                    goomba.direction=true;
                }
                else if(goomba.getPosition().x > goombaPlatform.getBound("right")-.5f && goomba.direction){
                    goomba.direction = false;
                }
            }
            /*
            if(mario.goombaTouched && mario.isMoving()){
                mario.removeAllImages();
                if(mario.direction)
                    mario.addImage(SuperMario.opaqueRight);
                else
                    mario.addImage(SuperMario.opaqueLeft);
            }
            */
        }

        @Override
        public void postStep(StepEvent e) {
            
        }
    }
}
