/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.EAction;
import aiantwars.EAntType;
import aiantwars.IAntAI;
import aiantwars.IAntInfo;
import aiantwars.IEgg;
import aiantwars.ILocationInfo;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.ColorFactory;
import java.util.List;

/**
 *
 * @author Tobias Grundtvig
 */
public class HumanGraphicsAI implements IAntAI
{
    private final G2D g2d;
    private final ColorFactory cf;
    private final SimpleMenu menu;

    public HumanGraphicsAI(G2D g2d, ColorFactory cf)
    {
        this.g2d = g2d;
        this.cf = cf;
        this.menu = new SimpleMenu(g2d, cf, 5, this);
    }
    
    @Override
    public void onHatch(IAntInfo thisAnt, ILocationInfo thisLocation, int worldSizeX, int worldSizeY)
    {
        
    }
    
    
    @Override
    public EAction chooseAction(IAntInfo thisAnt, ILocationInfo thisLocation,
                                List<ILocationInfo> visibleLocations,
                                List<EAction> possibleActions)
    {
        if(possibleActions.size() == 1)
        {
            return possibleActions.get(0);
        }
        EAntType type = thisAnt.getAntType();
        String[] choices = new String[possibleActions.size()];
        for(int i = 0; i < choices.length; ++i)
        {
            EAction action = possibleActions.get(i);
            choices[i] = action.toString() + " " + type.getActionCost(action);
        }
        int x = thisAnt.getLocation().getX();
        int y = thisAnt.getLocation().getY();
        Point2D menuPos = g2d.newPoint2D(x * 100 + 70, y * 100 + 70);
        int choice = menu.showMenu(menuPos, choices);
        return possibleActions.get(choice);
    }

    @Override
    public void onStartTurn(IAntInfo thisAnt, int turn)
    {
        
    }

    @Override
    public void onAttacked(IAntInfo thisAnt, int dir, IAntInfo attacker,
                           int damage)
    {
        
    }

    @Override
    public void onDeath(IAntInfo thisAnt)
    {
        
    }

    @Override
    public void onLayEgg(IAntInfo thisAnt, List<EAntType> types, IEgg egg)
    {
        String[] choices = new String[types.size()];
        for(int i = 0; i < choices.length; ++i)
        {
            choices[i] = types.get(i).toString();
        }
        int x = thisAnt.getLocation().getX();
        int y = thisAnt.getLocation().getY();
        Point2D menuPos = g2d.newPoint2D(x * 100 + 70, y * 100 + 70);
        int choice = menu.showMenu(menuPos, choices);
        egg.set(types.get(choice), this);
    }
    
    public void draw(Canvas canvas)
    {
        menu.draw(canvas);
    }
    
    public void onMouseMoved(Point2D mousePos)
    {
        menu.onMouseMoved(mousePos);
    }
    
    public void onMousePressed(Point2D mousePos)
    {
        menu.onMousePressed(mousePos);
    }
    
    public void onMouseReleased(Point2D mousePos)
    {
        menu.onMouseReleased(mousePos);
    }

    @Override
    public void onStartMatch(int worldSizeX, int worldSizeY)
    {
        
    }

    @Override
    public void onStartRound(int round)
    {
        
    }

    @Override
    public void onEndRound(int yourMajor, int yourMinor, int enemyMajor, int enemyMinor)
    {
        
    }

    @Override
    public void onEndMatch(int yourScore, int yourWins, int enemyScore, int enemyWins)
    {
        
    }

}
