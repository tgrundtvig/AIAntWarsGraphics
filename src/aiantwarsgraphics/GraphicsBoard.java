/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

import aiantwars.ILocationInfo;
import aiantwars.impl.Board;
import app2dapi.geometry.G2D;
import app2dapi.graphics.Canvas;
import app2dapi.graphics.ColorFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class GraphicsBoard
{

    private final G2D g2d;
    private final Board board;
    private final ILocationDrawer locationDrawer;

    public GraphicsBoard(G2D g2d, Board board, ColorFactory cf, ILocationDrawer locationDrawer)
    {
        this.g2d = g2d;
        this.board = board;
        this.locationDrawer = locationDrawer;
    }

    public void draw(Canvas canvas)
    {
        G2D.Transformation2D parent = canvas.getTransformation();
        int sizeX = board.getSizeX();
        int sizeY = board.getSizeY();

        for(int y = 0; y < sizeY; ++y)
        {
            for(int x = 0; x < sizeX; ++x)
            {
                ILocationInfo location = board.getLocation(x, y);
                G2D.Transformation2D t1 = g2d.translate(100 * x, 100 * y);
                canvas.setTransformation(g2d.combine(parent, t1));
                locationDrawer.draw(canvas, location);
            }
        }
        canvas.setTransformation(parent);
    }

}
