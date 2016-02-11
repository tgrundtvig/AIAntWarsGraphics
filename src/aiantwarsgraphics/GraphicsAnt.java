/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.EAntType;
import aiantwars.IAntInfo;
import aiantwars.graphicsinterface.IGraphicsAnt;
import app2dapi.animation.Chaser;
import app2dapi.animation.LinearChaser;
import app2dapi.animation.Visual;
import app2dapi.animation.VisualManager;
import app2dapi.animation.impl.LinearChaserImpl;
import app2dapi.animation.impl.VisualManagerImpl;
import app2dapi.animation.impl.WaitForNotify;
import app2dapi.animation.impl.interpolators.DoubleCircularInterpolation;
import app2dapi.animation.impl.interpolators.DoubleInterpolator;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Transformation2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class GraphicsAnt implements IGraphicsAnt, Visual<Canvas>
{
    private final GraphicsAntWarsGUI factory;
    private final VisualManager<Canvas> textManager;
    private final G2D g2d;
    private final ColorFactory cf;
    private final IAntInfo antInfo;
    private final IAntDrawer antDrawer;
    private final LinearChaser<Double> directionCtrl;
    private final LinearChaser<Double> xCtrl;
    private final LinearChaser<Double> yCtrl;
    private final double turnDuration = 0.4f;
    private final double moveDuration = 0.5f;
    private final double attackDuration = 0.2f;
    private int textDirection = 0;
   

    public GraphicsAnt(GraphicsAntWarsGUI factory, IAntInfo antInfo, IAntDrawer antDrawer, G2D g2d, ColorFactory cf)
    {
        this.factory = factory;
        this.textManager = new VisualManagerImpl<>(true);
        this.antInfo = antInfo;
        this.antDrawer = antDrawer;
        this.g2d = g2d;
        this.cf = cf;
        DoubleInterpolator di = new DoubleInterpolator();
        xCtrl = new LinearChaserImpl<>(di,1.0);
        yCtrl = new LinearChaserImpl<>(di,1.0);
        directionCtrl = new LinearChaserImpl<>(new DoubleCircularInterpolation(4), 0.25);
     
        xCtrl.setCurrent((double)antInfo.getLocation().getX());
        yCtrl.setCurrent((double)antInfo.getLocation().getY());
        directionCtrl.setCurrent((double) antInfo.getDirection());
        
        xCtrl.setGoal((double)antInfo.getLocation().getX());
        yCtrl.setGoal((double)antInfo.getLocation().getY());
        directionCtrl.setGoal((double) antInfo.getDirection());
    }
    
    @Override
    public void onTakeAction()
    {
       
    }

    @Override
    public synchronized void playTurnLeftAnimation()
    {
        doTurn();
    }

    @Override
    public synchronized void playTurnRightAnimation()
    {
        doTurn();
    }
    
    private synchronized void doTurn()
    {
        directionCtrl.setGoal((double) antInfo.getDirection());
        directionCtrl.setDuration(turnDuration);
        waitFor(directionCtrl);
    }

    @Override
    public synchronized void playMoveForwardAnimation()
    {
        doMove();
    }

    @Override
    public synchronized void playMoveBackwardAnimation()
    {
        doMove();
    }

    private synchronized void doMove()
    {
        switch(antInfo.getDirection())
        {
            case 0:
            case 2:
            {
                yCtrl.setGoal((double) antInfo.getLocation().getY());
                yCtrl.setDuration(moveDuration);
                waitFor(yCtrl);
                break;
            }
            case 1:
            case 3:
            {
                xCtrl.setGoal((double) antInfo.getLocation().getX());
                xCtrl.setDuration(moveDuration);
                waitFor(xCtrl);
                break;
            }
        }
    }

    @Override
    public synchronized void playAttackAnimation(int damage)
    {
        switch(antInfo.getDirection())
        {
            case 0:
            {
                yCtrl.setGoal((double) antInfo.getLocation().getY() + 0.5f);
                yCtrl.setDuration(attackDuration);
                waitFor(yCtrl);
                
                yCtrl.setGoal((double) antInfo.getLocation().getY());
                yCtrl.setDuration(attackDuration);
                waitFor(yCtrl);
                break;
            }
            case 1:
            {
                xCtrl.setGoal((double) antInfo.getLocation().getX() + 0.5f);
                xCtrl.setDuration(attackDuration);
                waitFor(xCtrl);
                
                xCtrl.setGoal((double) antInfo.getLocation().getX());
                xCtrl.setDuration(attackDuration);
                waitFor(xCtrl);
                break;
            }
            case 2:
            {
                yCtrl.setGoal((double) antInfo.getLocation().getY() - 0.5f);
                yCtrl.setDuration(attackDuration);
                waitFor(yCtrl);
                
                yCtrl.setGoal((double) antInfo.getLocation().getY());
                yCtrl.setDuration(attackDuration);
                waitFor(yCtrl);
                break;
            }
            case 3:
                xCtrl.setGoal((double) antInfo.getLocation().getX() - 0.5f);
                xCtrl.setDuration(attackDuration);
                waitFor(xCtrl);
                
                xCtrl.setGoal((double) antInfo.getLocation().getX());
                xCtrl.setDuration(attackDuration);
                waitFor(xCtrl);
                break;
        }
    }

    @Override
    public synchronized void playOnDamage(int damage)
    {
        say("-" + Integer.toString(damage), antDrawer.getDamageColor());
        //shake();
    }

    @Override
    public synchronized void playPickUpFoodAnimation()
    {
        say("+1", antDrawer.getFoodColor());
        shake();
    }

    @Override
    public synchronized void playDropFoodAnimation()
    {
        say("-1", antDrawer.getFoodColor());
        shake();
    }

    @Override
    public synchronized void playEatFoodAnimation(int hitPointsGained)
    {
        say("+" + hitPointsGained, antDrawer.getHitPointColor());
        shake();
    }

    @Override
    public synchronized void playDigOutAnimation()
    {
        say("Digout", antDrawer.getActionColor());
        shake();
    }

    @Override
    public synchronized void playDropSoilAnimation()
    {
        say("Drop soil", antDrawer.getActionColor());
        shake();
    }

    @Override
    public synchronized void playLayEggAnimation(EAntType eggType)
    {
        say("Layed egg: " + eggType, antDrawer.getActionColor());
        shake();
    }

    @Override
    public synchronized void playPassAnimation()
    {
        say("Pass", antDrawer.getActionColor());
        shake();
    }

    @Override
    public synchronized void playHatchAnimation()
    {
        say("Hatced!", antDrawer.getActionColor());
        shake();
    }

    @Override
    public synchronized void playDeathAnimation()
    {
        for(int i = 0; i < 5; ++i)
        {
            shake();
        }
        factory.removeAnt(this);
    }

    @Override
    public synchronized void update(double time)
    {
        xCtrl.update(time);
        yCtrl.update(time);
        directionCtrl.update(time);
        textManager.update(time);
    }

    public synchronized void draw(Canvas canvas)
    {
        Transformation2D parent = canvas.getTransformation();
        Transformation2D t = g2d.translate(xCtrl.getCurrent() * 100.0, yCtrl.getCurrent() * 100.0);
        Transformation2D r = g2d.rotate(directionCtrl.getCurrent() * Math.PI * -0.5, g2d.newPoint2D(50, 50));
        Transformation2D pt = g2d.combine(parent, t);
        Transformation2D ptr = g2d.combine(pt, r);
      
        
        canvas.setTransformation(ptr);
        antDrawer.drawAnt(canvas, antInfo);
        canvas.setTransformation(pt);
        textManager.draw(canvas);
        canvas.setTransformation(parent);
    }

    private synchronized void shake()
    {
        directionCtrl.setGoal(antInfo.getDirection() + 0.1);
        directionCtrl.setDuration(turnDuration*0.1);
        waitFor(directionCtrl);
        
        if(antInfo.getDirection() > 0)
        {
            directionCtrl.setGoal(antInfo.getDirection() - 0.1);
        }
        else
        {
            directionCtrl.setGoal(3.9);
        }
        directionCtrl.setDuration(turnDuration*0.1);
        waitFor(directionCtrl);
        
        directionCtrl.setGoal((double) antInfo.getDirection());
        directionCtrl.setDuration(turnDuration*0.1);
        waitFor(directionCtrl);
    }

    private void say(String text, Color color)
    {
        Point2D startPos = g2d.newPoint2D(50, 50);
        Point2D endPos;
        switch(textDirection)
        {
            case 0:
                endPos = g2d.newPoint2D(80, 80);
                textDirection = 1;
                break;
            case 1:
                endPos = g2d.newPoint2D(80, 20);
                textDirection = 2;
                break;
            case 2:
                endPos = g2d.newPoint2D(20, 20);
                textDirection = 3;
                break;
            default:
                endPos = g2d.newPoint2D(20, 80);
                textDirection = 0;
                break;
                
        }
        TextVisual tv = new TextVisual(g2d, text, color, startPos, endPos, 10.0, 30.0, 2.0);
        textManager.addVisual(tv);
    }

    @Override
    public synchronized void start(double time)
    {
        //Do nothing
    }

    @Override
    public synchronized boolean isFinished()
    {
        return antInfo.isDead();
    }
    
    private synchronized <E> void waitFor(Chaser<E> chaser)
    {
        WaitForNotify wfn = new WaitForNotify(this);
        chaser.setGoalListener(wfn);
        wfn.waitForNotify();
    }
}
