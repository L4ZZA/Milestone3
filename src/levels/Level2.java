/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package levels;

import city.cs.engine.StepEvent;
import city.cs.engine.StepListener;
import dynamic_bodies.Goomba;
import dynamic_bodies.Plant;
import dynamic_bodies.Princess;
import dynamic_bodies.Rose;
import dynamic_bodies.SuperMario;
import java.awt.event.KeyEvent;
import main.PlayPanel;
import org.jbox2d.common.Vec2;
import static_bodies.Ground;
import static_bodies.LifePlatform;
import static_bodies.Pipe;
import static_bodies.Platform;

/**
 *
 * @author Pietro
 */
public class Level2 extends Level{
    
    public Level2(int l){
        currentLevel = l;
        nRoses=2;
        Ground g = new Ground(this);
        //remember to put each walkable no more than 5 points above the closest one
        // the player might not be able to reach it
        mario = new SuperMario(this, new Vec2(g.getBound("left")+2, g.getBound("top")+1));
        
        Pipe pipe = new Pipe(this, new Vec2(-4f,g.getBound("top")+1));
        Plant plant = new Plant(this, new Vec2(pipe.getBound("left")+pipe.getWidth(),pipe.getBound("top")));
        LifePlatform lp = new LifePlatform(this, new Vec2(1,-6));
        Platform p = new Platform(this, new Vec2(8,-3));
        
        Princess princess = new Princess(this,new Vec2(p.getBound("right")-1, p.getBound("top")+1));
        Rose r = new Rose(this, new Vec2(pipe.getPosition().x-3, g.getBound("top")));
        Rose rr = new Rose(this, new Vec2(g.getPosition().x+3, g.getBound("top")));

        addStepListener(getStepListener());
    }

    @Override
    public void draw() {
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void setView(PlayPanel view) {
        super.view = view;
    }
}
