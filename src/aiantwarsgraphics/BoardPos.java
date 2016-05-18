/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aiantwarsgraphics;

/**
 *
 * @author Tobias Grundtvig
 */
public class BoardPos
{
    private final int x;
    private final int y;
    private final int direction;

    public BoardPos(int x, int y, int direction)
    {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getDirection()
    {
        return direction;
    }
}
