/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import app2dapi.animation.Drawable;
import app2dapi.geometry.G2D;
import app2dapi.geometry.G2D.Polygon;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.Color;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class MousePointer implements Drawable<Canvas>
{
    private final Polygon outer;
    private final Polygon inner;
    private final Color innerColor;
    private final Color outerColor;

    public MousePointer(G2D g2d, ColorFactory cf)
    {
        inner = g2d.createArrow(g2d.newPoint2D(15, -15), g2d.origo(), 6);
        outer = g2d.createArrow(g2d.newPoint2D(17, -17), g2d.newPoint2D(-2, 2), 10);
        innerColor = cf.getBlack();
        outerColor = cf.getWhite();
    }
    
    @Override
    public void draw(Canvas canvas)
    {
        canvas.setColor(outerColor);
        canvas.drawFilledPolygon(outer);
        canvas.setColor(innerColor);
        canvas.drawFilledPolygon(inner);
    }
    
}
