/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.geometry.G2D.Transformation2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.ColorFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Grundtvig
 */
public class SimpleMenu implements MenuListener
{
    private final G2D g2d;
    private final Object lock;
    private final MenuDrawer menuDrawer;
    private Point2D position;
    private Transformation2D toWorld;
    private int choice;
    private boolean active;

    public SimpleMenu(G2D g2d, ColorFactory cf, double menuItemHeight, Object lock)
    {
        this.g2d = g2d;
        this.lock = lock;
        this.menuDrawer = new MenuDrawer(g2d, cf, menuItemHeight, this);
        this.active = false;
    }
    
    
    public int showMenu(Point2D pos, String[] choices)
    {
        synchronized(lock)
        {
            active = true;
            choice = - 1;
            menuDrawer.setChoices(choices);
            position = pos;
            toWorld = g2d.translate(pos.x(), pos.y());
            while(choice < 0)
            {
                try
                {
                    lock.wait();
                }
                catch(InterruptedException ex)
                {
                    Logger.getLogger(SimpleMenu.class.getName()).
                      log(Level.SEVERE, null, ex);
                }
            }
            return choice;
        }
    }
    
    public void onMouseMoved(Point2D mouseWorldPos)
    {
        synchronized(lock)
        {
            if(active)
            {
                menuDrawer.onMouseMoved(toMenu(mouseWorldPos));
            }
        }
    }
    
    public void onMousePressed(Point2D mouseWorldPos)
    {
        synchronized(lock)
        {
            if(active)
            {
                menuDrawer.onMousePressed(toMenu(mouseWorldPos));
            }
        }
    }
    
    public void onMouseReleased(Point2D mouseWorldPos)
    {
        synchronized(lock)
        {
            if(active)
            {
                menuDrawer.onMouseReleased(toMenu(mouseWorldPos));
            }
        }
    }
    
    public void draw(Canvas canvas)
    {
        synchronized(lock)
        {
            if(active)
            {
                Transformation2D parent = canvas.getTransformation();
                Transformation2D tmp = g2d.combine(parent, toWorld);
                canvas.setTransformation(tmp);
                menuDrawer.draw(canvas);
                canvas.setTransformation(parent);
            }
        }
    }
    
    private Point2D toMenu(Point2D p)
    {
        return g2d.newPoint2D(p.x()-position.x(), p.y()-position.y());
    }
    
    @Override
    public void onChoice(int choice)
    {
        synchronized(lock)
        {
            this.choice = choice;
            active = false;
            lock.notifyAll();
        }
    }    
}

    
