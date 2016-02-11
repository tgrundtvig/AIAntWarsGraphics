/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.ILocationInfo;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Point2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;

/**
 *
 * @author Tobias Grundtvig
 */
public class LocationDrawer implements ILocationDrawer
{
    private final G2D.Polygon roomPolygon;
    private final Color clearColor;
    private final Color filledColor;
    private final Color rockColor;
    private final Point2D textPos;
    private final Color textColor;

    public LocationDrawer(G2D g2d, Color clearColor, Color filledColor, Color rockColor, Color textColor)
    {
        this.clearColor = clearColor;
        this.filledColor = filledColor;
        this.rockColor = rockColor;
        this.textColor = textColor;
        this.textPos = g2d.newPoint2D(50, 70);
        
        G2D.PolygonBuilder tmp = g2d.getPolygonBuilder();
        tmp.addPoint(g2d.newPoint2D(30, 0));
        tmp.addPoint(g2d.newPoint2D(70, 0));
        tmp.addPoint(g2d.newPoint2D(100, 30));
        tmp.addPoint(g2d.newPoint2D(100, 70));
        tmp.addPoint(g2d.newPoint2D(70, 100));
        tmp.addPoint(g2d.newPoint2D(30, 100));
        tmp.addPoint(g2d.newPoint2D(0, 70));
        tmp.addPoint(g2d.newPoint2D(0, 30));
        roomPolygon = tmp.build();
    }
    
    
    @Override
    public void draw(Canvas canvas, ILocationInfo loc)
    {
        if(loc.isRock())
        {
            canvas.setColor(rockColor);
            canvas.drawFilledPolygon(roomPolygon);
        }
        else if(loc.isFilled())
        {
            canvas.setColor(filledColor);
            canvas.drawFilledPolygon(roomPolygon);
        }
        else
        {
            canvas.setColor(clearColor);
            canvas.drawFilledPolygon(roomPolygon);
            canvas.setColor(textColor);
            canvas.drawText(textPos, Integer.toString(loc.getFoodCount()), 30, true, false);
        }
    }
    
}
