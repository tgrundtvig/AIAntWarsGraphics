/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.IAntInfo;
import aiantwars.graphicsinterface.IGraphicsAnt;
import app2dapi.animation.Updatable;
import app2dapi.geometry.G2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.ColorFactory;
import java.util.ArrayList;
import java.util.List;
import aiantwars.graphicsinterface.IGraphicsAntWarsGUI;
import app2dapi.geometry.G2D.Dimension2D;
import app2dapi.geometry.G2D.Point2D;

/**
 *
 * @author Tobias Grundtvig
 */
public class GraphicsAntWarsGUI implements IGraphicsAntWarsGUI, Updatable
{
    
    private final G2D g2d;
    private final ColorFactory cf;
    private final IAntDrawer antDrawer;
    private int turn;
    private final Point2D turnPos;
    private final double turnSize;
    
    private final List<GraphicsAnt> ants;

    public GraphicsAntWarsGUI(G2D g2d, ColorFactory cf, Dimension2D sizeHUD)
    {
        this.cf = cf;
        this.g2d = g2d;
        ants = new ArrayList<>();
        antDrawer = new AntDrawer(g2d, cf);
        turn = 0;
        
        double margin = sizeHUD.height() * 0.02;
        turnPos = g2d.newPoint2D(margin, sizeHUD.height() - 2.0 * margin);
        turnSize = sizeHUD.height() * 0.02;
    }
    
    @Override
    public synchronized IGraphicsAnt createNewGraphicsAnt(IAntInfo antInfo)
    {
        GraphicsAnt newAnt = new GraphicsAnt(this, antInfo, antDrawer, g2d, cf);
        ants.add(newAnt);
        return newAnt;
    }

    @Override
    public synchronized void update(double time)
    {
        for(GraphicsAnt ant : ants)
        {
            ant.update(time);
        }
    }
    
    public synchronized void removeAnt(GraphicsAnt ant)
    {
        ants.remove(ant);
    }
    
    public synchronized void drawAnts(Canvas canvas)
    {
        for(GraphicsAnt ant : ants)
        {
            ant.draw(canvas);
        }
    }
    
    public synchronized void drawHUD(Canvas canvas)
    {
        canvas.setColor(cf.getWhite());
        canvas.drawText(turnPos, "Turn: " + turn, turnSize);
    }

    @Override
    public void onStartTurn(int turn)
    {
        this.turn = turn;
    }
    
}
