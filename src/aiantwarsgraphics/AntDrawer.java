/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.IAntInfo;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class AntDrawer implements IAntDrawer
{
    private final Point2D hitPointPos;
    private final Point2D foodPos;
    private final Point2D actionPos;
    private final Point2D typePos;
    private final G2D.Polygon ant;
    private final G2D.Polygon team;
    private final G2D.Polygon info;
    

    private final Color[] teamColors;
    private final Color antColor;
    private final Color carrieColor;
    private final Color infoColor;
    private final Color hitPointColor;
    private final Color foodColor;
    private final Color actionColor;
    private final Color typeColor;
    private final Color damageColor;
    

    public AntDrawer(G2D g2d, ColorFactory cf)
    {
        teamColors = new Color[4];
        teamColors[0] = cf.getRed();
        teamColors[1] = cf.getBlue();
        teamColors[2] = cf.getGreen();
        teamColors[3] = cf.getYellow();
        this.antColor = cf.getBlack();
        this.carrieColor = cf.newColor(0.545f, 0.27f, 0.075f);
        this.infoColor = cf.newColor(0.3f, 0.3f, 0.2f);
        this.hitPointColor = cf.getGreen();
        this.foodColor = cf.getYellow();
        this.actionColor = cf.getBlue();
        this.typeColor = cf.getBlack();
        this.damageColor = cf.getRed();
        this.hitPointPos = g2d.newPoint2D(50, 42);
        this.foodPos = g2d.newPoint2D(50, 37);
        this.actionPos = g2d.newPoint2D(50, 65);
        this.typePos = g2d.newPoint2D(50, 47);
        
        G2D.PolygonBuilder tmp = g2d.getPolygonBuilder();
        tmp.addPoint(g2d.newPoint2D(45, 30));
        tmp.addPoint(g2d.newPoint2D(30, 25));
        tmp.addPoint(g2d.newPoint2D(42, 35));
        tmp.addPoint(g2d.newPoint2D(40, 42.5f));
        tmp.addPoint(g2d.newPoint2D(25, 45));
        tmp.addPoint(g2d.newPoint2D(40, 47.5f));
        
        //Leg
        tmp.addPoint(g2d.newPoint2D(45, 55));
        tmp.addPoint(g2d.newPoint2D(30, 65));
        
        
        tmp.addPoint(g2d.newPoint2D(45, 60));
        tmp.addPoint(g2d.newPoint2D(40, 70));
        tmp.addPoint(g2d.newPoint2D(45, 80));
        tmp.addPoint(g2d.newPoint2D(55, 80));
        tmp.addPoint(g2d.newPoint2D(60, 70));
        tmp.addPoint(g2d.newPoint2D(55, 60));
        //Leg
        tmp.addPoint(g2d.newPoint2D(70, 65));
        tmp.addPoint(g2d.newPoint2D(55, 55));
        
        tmp.addPoint(g2d.newPoint2D(60, 47.5f));
        tmp.addPoint(g2d.newPoint2D(75, 45));
        tmp.addPoint(g2d.newPoint2D(60, 42.5f));
        tmp.addPoint(g2d.newPoint2D(58, 35));
        tmp.addPoint(g2d.newPoint2D(70, 25));
        tmp.addPoint(g2d.newPoint2D(55, 30));
        ant = tmp.build();
        
        tmp = g2d.getPolygonBuilder();
        tmp.addPoint(g2d.newPoint2D(45f, 32.5f));
        tmp.addPoint(g2d.newPoint2D(42, 45));
        tmp.addPoint(g2d.newPoint2D(49, 57.5f));
        tmp.addPoint(g2d.newPoint2D(51, 57.5f));
        tmp.addPoint(g2d.newPoint2D(58, 45));
        tmp.addPoint(g2d.newPoint2D(55f, 32.5f));
        team = tmp.build();
        
        tmp = g2d.getPolygonBuilder();
        tmp.addPoint(g2d.newPoint2D(45, 37));
        tmp.addPoint(g2d.newPoint2D(55, 37));
        tmp.addPoint(g2d.newPoint2D(55, 47));
        tmp.addPoint(g2d.newPoint2D(45, 47));
        info = tmp.build();
    }
    
    @Override
    public Color getHitPointColor()
    {
        return hitPointColor;
    }
    
    @Override
    public Color getActionColor()
    {
        return actionColor;
    }
    
    @Override
    public Color getFoodColor()
    {
        return foodColor;
    }
    
    @Override
    public Color getDamageColor()
    {
        return damageColor;
    }
    
    
    
    @Override
    public void drawAnt(Canvas canvas, IAntInfo antInfo)
    {
        if(antInfo.hasHatched())
        {
            if(antInfo.carriesSoil())
            {
                canvas.setColor(carrieColor);
            }
            else
            {
                canvas.setColor(antColor);
            }
            canvas.drawFilledPolygon(ant);
            canvas.setColor(actionColor);
            canvas.drawText(actionPos, Integer.toString(antInfo.getActionPoints()), 10, true, false);
        }
        canvas.setColor(teamColors[antInfo.getTeamInfo().getTeamID()-1]);
        canvas.drawFilledPolygon(team);
        canvas.setColor(typeColor);
        canvas.drawText(typePos, antInfo.getAntType().toString().substring(0, 1), 9, true, false);
        canvas.setColor(infoColor);
        canvas.drawFilledPolygon(info);
        
        
        canvas.setColor(hitPointColor);
        canvas.drawText(hitPointPos, Integer.toString(antInfo.getHitPoints()), 5, true, false);
        canvas.setColor(foodColor);
        canvas.drawText(foodPos, Integer.toString(antInfo.getFoodLoad()), 5, true, false);
        
        
    }
    
}
